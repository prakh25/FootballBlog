package com.example.authlib.ui;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.authlib.R;
import com.example.authlib.User;
import com.example.authlib.ui.fieldvalidators.EmailFieldValidator;
import com.example.authlib.ui.fieldvalidators.PasswordFieldValidator;
import com.example.authlib.utils.GoogleApiHelper;
import com.example.authlib.utils.ImeHelper;
import com.example.corelib.network.DataManager;
import com.example.corelib.ui.authui.RegisterEmailAndPasswordContract;
import com.example.corelib.ui.authui.RegisterEmailAndPasswordPresenter;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.CredentialPickerConfig;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.EmailAuthProvider;

/**
 * Created by prakh on 03-12-2017.
 */
public class RegisterEmailAndPasswordFragment extends FragmentBase implements View.OnClickListener,
        View.OnFocusChangeListener, ImeHelper.DonePressedListener,
        RegisterEmailAndPasswordContract.RegisterEmailAndPasswordView {

    public static final String TAG = "EmailAndPasswordFrag";

    interface EmailAndPasswordListener {
        void newUser(User user);
    }

    private static final int RC_HINT = 13;
    private static final int RC_SIGN_IN = 16;

    private TextInputLayout emailLayout;
    private EditText emailField;

    private TextInputLayout passwordLayout;
    private EditText passwordField;

    private Credential lastCredentials;

    private EmailFieldValidator emailFieldValidator;
    private PasswordFieldValidator passwordFieldValidator;

    private EmailAndPasswordListener listener;

    private RegisterEmailAndPasswordPresenter presenter;

    public static RegisterEmailAndPasswordFragment newInstance(FlowParameters flowParams) {

        Bundle args = new Bundle();
        args.putParcelable(ExtraConstants.EXTRA_FLOW_PARAMS, flowParams);
        RegisterEmailAndPasswordFragment fragment = new RegisterEmailAndPasswordFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new RegisterEmailAndPasswordPresenter(DataManager.getInstance());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.auth_fragment_user_email_and_password,
                container, false);
        presenter.attachView(this);
        init(view);
        return view;
    }

    private void init(View view) {
        emailLayout = view.findViewById(R.id.email_field_layout);
        emailField = view.findViewById(R.id.email_field);
        passwordLayout = view.findViewById(R.id.password_field_layout);
        passwordField = view.findViewById(R.id.password_field);

        emailFieldValidator = new EmailFieldValidator(emailLayout);
        passwordFieldValidator = new PasswordFieldValidator(passwordLayout,
                getResources().getInteger(R.integer.auth_min_password_length));

        ImeHelper.setImeOnDoneListener(passwordField, this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && getFlowParams().enableHints) {
            emailField.setImportantForAutofill(View.IMPORTANT_FOR_AUTOFILL_NO);
        }

        emailLayout.setOnClickListener(this);
        emailField.setOnClickListener(this);
        passwordField.setOnClickListener(this);
        passwordLayout.setOnClickListener(this);

        emailField.setOnFocusChangeListener(this);
        passwordField.setOnFocusChangeListener(this);

        view.findViewById(R.id.button_next).setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (!(getActivity() instanceof EmailAndPasswordListener)) {
            throw new IllegalStateException("Activity must implement EmailAndPasswordListener");
        }
        listener = (EmailAndPasswordListener) getActivity();

        if (savedInstanceState != null) {
            return;
        }

        if (getFlowParams().enableHints) {
            showEmailAutoCompleteHint();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(ExtraConstants.HAS_EXISTING_INSTANCE, true);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RC_HINT:
                if (data != null) {
                    lastCredentials = data.getParcelableExtra(Credential.EXTRA_KEY);
                    if (lastCredentials != null) {
                        // Get the email from the credential
                        emailField.setText(lastCredentials.getId());

                        // Attempt to proceed
                        validateAndProceed();
                    }
                }
                break;
            case RC_SIGN_IN:
                finish(resultCode, data);
                break;
        }
    }

    private void showEmailAutoCompleteHint() {
        try {
            startIntentSenderForResult(getEmailHintIntent().getIntentSender(), RC_HINT);
        } catch (IntentSender.SendIntentException e) {
            Log.e(TAG, "Unable to start hint intent", e);
        }
    }

    private PendingIntent getEmailHintIntent() {
        GoogleApiClient client = new GoogleApiClient.Builder(getContext())
                .addApi(Auth.CREDENTIALS_API)
                .enableAutoManage(getActivity(), GoogleApiHelper.getSafeAutoManageId(),
                        connectionResult -> Log.e(TAG,
                                "Client connection failed: " + connectionResult.getErrorMessage()))
                .build();

        HintRequest hintRequest = new HintRequest.Builder()
                .setHintPickerConfig(new CredentialPickerConfig.Builder()
                        .setShowCancelButton(true)
                        .build())
                .setEmailAddressIdentifierSupported(true)
                .build();

        return Auth.CredentialsApi.getHintPickerIntent(client, hintRequest);
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        if(b) return;

        int id = view.getId();
        if(id == R.id.email_field) {
            emailFieldValidator.validate(emailField.getText());
            presenter.checkEmail(emailField.getText().toString());
        } else if(id == R.id.password_field) {
            passwordFieldValidator.validate(passwordField.getText());
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.button_next) {
            validateAndProceed();
        } else if (id == R.id.email_field_layout || id == R.id.email_field) {
            emailLayout.setError(null);
        } else if (id == R.id.password_field_layout || id == R.id.password_field) {
            passwordLayout.setError(null);
        }
    }

    @Override
    public void onDonePressed() {
        validateAndProceed();
    }

    private void validateAndProceed() {
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();

        boolean emailValid = emailFieldValidator.validate(email);
        boolean passwordValid = passwordFieldValidator.validate(password);

        if (emailValid && passwordValid) {
            presenter.checkEmail(email);
        }
    }

    @Override
    public void isEmailPresent(boolean emailExists) {
        if(!emailExists) {
            String email = emailField.getText().toString();
            String password = passwordField.getText().toString();
            listener.newUser(new User.Builder(EmailAuthProvider.PROVIDER_ID,
                    email, password).build());
        }
        emailLayout.setError(getString(R.string.auth_email_already_exists));
    }
}
