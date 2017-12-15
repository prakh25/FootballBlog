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
import com.example.authlib.provider.AuthProviderId;
import com.example.authlib.ui.fieldvalidators.EmailFieldValidator;
import com.example.authlib.utils.GoogleApiHelper;
import com.example.authlib.utils.ImeHelper;
import com.example.corelib.network.DataManager;
import com.example.corelib.ui.authui.CheckEmailContract;
import com.example.corelib.ui.authui.CheckEmailPresenter;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.CredentialPickerConfig;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by prakh on 03-12-2017.
 */
public class CheckEmailFragment extends FragmentBase implements View.OnClickListener,
        View.OnFocusChangeListener, ImeHelper.DonePressedListener,
        CheckEmailContract.CheckEmailView {

    public static final String TAG = "CheckEmailFragment";

    interface CheckEmailListener {
        void newUser(User user);
    }

    private static final int RC_HINT = 13;
    private static final int RC_SIGN_IN = 16;

    private TextInputLayout emailLayout;
    private EditText emailField;

    private Credential lastCredentials;

    private EmailFieldValidator emailFieldValidator;

    private CheckEmailListener listener;

    private CheckEmailPresenter presenter;

    public static CheckEmailFragment newInstance(FlowParameters flowParams) {

        Bundle args = new Bundle();
        args.putParcelable(ExtraConstants.EXTRA_FLOW_PARAMS, flowParams);
        CheckEmailFragment fragment = new CheckEmailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new CheckEmailPresenter(DataManager.getInstance());
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

        emailFieldValidator = new EmailFieldValidator(emailLayout);

        ImeHelper.setImeOnDoneListener(emailField, this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && getFlowParams().enableHints) {
            emailField.setImportantForAutofill(View.IMPORTANT_FOR_AUTOFILL_NO);
        }

        emailLayout.setOnClickListener(this);
        emailField.setOnClickListener(this);

        emailField.setOnFocusChangeListener(this);

        view.findViewById(R.id.button_next).setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (!(getActivity() instanceof CheckEmailListener)) {
            throw new IllegalStateException("Activity must implement CheckEmailListener");
        }
        listener = (CheckEmailListener) getActivity();

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
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.button_next) {
            validateAndProceed();
        } else if (id == R.id.email_field_layout || id == R.id.email_field) {
            emailLayout.setError(null);
        }
    }

    @Override
    public void onDonePressed() {
        validateAndProceed();
    }

    private void validateAndProceed() {
        String email = emailField.getText().toString();

        boolean emailValid = emailFieldValidator.validate(email);

        if (emailValid) {
            presenter.checkEmail(email);
        }
    }

    @Override
    public void isEmailPresent(boolean emailExists) {
        if(!emailExists) {
            createNewUser();
        }
        emailLayout.setError(getString(R.string.auth_email_already_exists));
    }

    private void createNewUser() {
        String email = emailField.getText().toString();

        String name = null;
        String photoUri = null;
        if(lastCredentials != null && lastCredentials.getId().equals(email)) {
            name = lastCredentials.getName();
            photoUri = String.valueOf(lastCredentials.getProfilePictureUri());
        }

        listener.newUser(new User.Builder(AuthProviderId.EMAIL_PROVIDER_ID, email)
                .setName(name)
                .setPhotoUri(photoUri)
                .build());
    }

    @Override
    public void onDestroyView() {
        presenter.detachView();
        super.onDestroyView();
    }
}
