package com.example.authlib.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.authlib.IdpResponse;
import com.example.authlib.R;
import com.example.authlib.User;
import com.example.authlib.provider.AuthProviderId;
import com.example.authlib.ui.fieldvalidators.EmailFieldValidator;
import com.example.authlib.ui.fieldvalidators.PasswordFieldValidator;
import com.example.authlib.ui.fieldvalidators.RequiredFieldValidator;
import com.example.authlib.utils.ImeHelper;
import com.example.corelib.SharedPreferenceManager;
import com.example.corelib.model.auth.UserData;
import com.example.corelib.network.DataManager;
import com.example.corelib.ui.authui.RegisterEmailContract;
import com.example.corelib.ui.authui.RegisterEmailPresenter;

/**
 * Created by prakh on 03-12-2017.
 */

public class RegisterEmailFragment extends FragmentBase implements
        RegisterEmailContract.RegisterEmailView, View.OnClickListener,
        View.OnFocusChangeListener, ImeHelper.DonePressedListener {

    public static final String TAG = "RegisterEmailFragment";

    private HelperActivityBase activityBase;

    private EditText emailField;
    private EditText usernameField;
    private EditText fullNameField;
    private EditText passwordField;
    private TextInputLayout emailFieldLayout;
    private TextInputLayout userNameFieldLayout;
    private TextInputLayout fullNameFieldLayout;
    private TextInputLayout passwordLayout;

    private EmailFieldValidator emailFieldValidator;
    private RequiredFieldValidator usernameFieldValidator;
    private RequiredFieldValidator fullNameFieldValidator;
    private PasswordFieldValidator passwordFieldValidator;

    private User user;

    private RegisterEmailPresenter presenter;

    public static RegisterEmailFragment newInstance(FlowParameters flowParameters,
                                                    User user) {

        RegisterEmailFragment fragment = new RegisterEmailFragment();

        Bundle args = new Bundle();
        args.putParcelable(ExtraConstants.EXTRA_FLOW_PARAMS, flowParameters);
        args.putParcelable(ExtraConstants.EXTRA_USER, user);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            user = User.getUser(getArguments());
        } else {
            user = User.getUser(savedInstanceState);
        }
        presenter = new RegisterEmailPresenter(DataManager.getInstance(),
                SharedPreferenceManager.getInstance());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.auth_fragment_new_user_registration,
                container, false);
        presenter.attachView(this);
        init(view);
        if (savedInstanceState != null) {
            return view;
        }
        return view;
    }

    private void init(View view) {
        emailField = view.findViewById(R.id.email_field);
        usernameField = view.findViewById(R.id.username_field);
        fullNameField = view.findViewById(R.id.full_name_field);
        passwordField = view.findViewById(R.id.password_field);

        emailFieldLayout = view.findViewById(R.id.email_field_layout);
        userNameFieldLayout = view.findViewById(R.id.username_field_layout);
        fullNameFieldLayout = view.findViewById(R.id.full_name_field_layout);
        passwordLayout = view.findViewById(R.id.password_field_layout);

        emailFieldValidator = new EmailFieldValidator(emailFieldLayout);
        passwordFieldValidator = new PasswordFieldValidator(passwordLayout,
                getResources().getInteger(R.integer.auth_min_password_length));
        usernameFieldValidator = new RequiredFieldValidator(userNameFieldLayout);
        fullNameFieldValidator = new RequiredFieldValidator(fullNameFieldLayout);

        ImeHelper.setImeOnDoneListener(passwordField, this);

        emailField.setOnFocusChangeListener(this);
        usernameField.setOnFocusChangeListener(this);
        fullNameField.setOnFocusChangeListener(this);
        passwordField.setOnFocusChangeListener(this);

        String email = user.getEmail();
        if(!TextUtils.isEmpty(email)) {
            emailField.setText(email);
        }

        String username = user.getmUsername();
        if (!TextUtils.isEmpty(username)) {
            usernameField.setText(username);
        }

        String name = user.getName();
        if (!TextUtils.isEmpty(name)) {
            fullNameField.setText(name);
        }

        view.findViewById(R.id.button_save).setOnClickListener(this);

        safeRequestFocus(usernameField);

    }

    private void safeRequestFocus(final View v) {
        v.post(v::requestFocus);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!(getActivity() instanceof HelperActivityBase)) {
            throw new RuntimeException("Must be attached to a HelperActivityBase.");
        }
        activityBase = (HelperActivityBase) getActivity();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(ExtraConstants.EXTRA_USER,
                new User.Builder(AuthProviderId.EMAIL_PROVIDER_ID, user.getEmail())
                        .setUsername(usernameField.getText().toString())
                        .setName(fullNameField.getText().toString())
                        .setPhotoUri(user.getPhotoUri())
                        .build());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        if (b) return; // Only consider fields losing focus

        int id = view.getId();
        if(id == R.id.email_field) {
            emailFieldValidator.validate(emailField.getText());
        }else if (id == R.id.username_field) {
            usernameFieldValidator.validate(usernameField.getText());
        } else if (id == R.id.full_name_field) {
            fullNameFieldValidator.validate(fullNameField.getText());
        } else if (id == R.id.password_field) {
            passwordFieldValidator.validate(passwordField.getText());
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.button_save) {
            validateAndRegisterUser();
        }
    }

    @Override
    public void onDonePressed() {
        validateAndRegisterUser();
    }

    private void validateAndRegisterUser() {
        String email = emailField.getText().toString();
        String username = usernameField.getText().toString();
        String fullName = fullNameField.getText().toString();
        String password = passwordField.getText().toString();

        boolean emailValid = emailFieldValidator.validate(email);
        boolean usernameValid = usernameFieldValidator.validate(username);
        boolean fullNameValid = fullNameFieldValidator.validate(fullName);
        boolean passwordValid = passwordFieldValidator.validate(password);

        if (emailValid && usernameValid && fullNameValid && passwordValid) {
            presenter.checkUsername(username);
        }
    }

    @Override
    public void isUsernameExists(boolean usernameExists) {
        if (!usernameExists) {
            presenter.acquireNonce();
        } else {
            userNameFieldLayout.setError(getString(R.string.auth_username_exists));
        }
    }

    @Override
    public void nonceAcquired(String nonce) {
        registerUser(nonce);
    }

    private void registerUser(String nonce) {

        getDialogHolder().showLoadingDialog(R.string.auth_sign_up);

        String email = user.getEmail();
        String username = usernameField.getText().toString();
        String fullName = fullNameField.getText().toString();
        String password = passwordField.getText().toString();

        String firstName;
        String lastName = "";

        if (fullName.split("\\w+").length > 1) {

            lastName = fullName.substring(fullName.lastIndexOf(" ") + 1);
            firstName = fullName.substring(0, fullName.lastIndexOf(' '));
        } else {
            firstName = fullName;
        }

        presenter.registerNewUser(email, username, nonce, password, firstName, lastName,
                fullName, AuthProviderId.EMAIL_PROVIDER_ID);

    }

    @Override
    public void userRegistered(UserData userData) {

            String email = userData.getEmail();
            String username = userData.getUsername();
            String fullName = userData.getDisplayname();
            String photoUri = "";

            if(!TextUtils.isEmpty(userData.getAvatar())) {
                photoUri = userData.getAvatar();
            }

            IdpResponse response = new IdpResponse.Builder(
                    new User.Builder(AuthProviderId.EMAIL_PROVIDER_ID, email)
                            .setUsername(username)
                            .setName(fullName)
                            .setPhotoUri(photoUri)
                            .build())
                    .build();

            activityBase.setResultAndFinish(response);

    }

    @Override
    public void onDestroyView() {
        presenter.detachView();
        super.onDestroyView();
    }
}
