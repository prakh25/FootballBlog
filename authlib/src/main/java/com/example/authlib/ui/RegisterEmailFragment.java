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
import android.widget.Toast;

import com.example.authlib.IdpResponse;
import com.example.authlib.R;
import com.example.authlib.User;
import com.example.authlib.ui.fieldvalidators.PasswordFieldValidator;
import com.example.authlib.ui.fieldvalidators.RequiredFieldValidator;
import com.example.authlib.utils.ImeHelper;
import com.example.authlib.utils.ProfileMerger;
import com.example.authlib.utils.TaskFailureLogger;
import com.example.corelib.SharedPreferenceManager;
import com.example.corelib.network.DataManager;
import com.example.corelib.ui.authui.RegisterEmailContract;
import com.example.corelib.ui.authui.RegisterEmailPresenter;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

/**
 * Created by prakh on 03-12-2017.
 */

public class RegisterEmailFragment extends FragmentBase implements
        RegisterEmailContract.RegisterEmailView, View.OnClickListener,
        View.OnFocusChangeListener, ImeHelper.DonePressedListener {

    public static final String TAG = "RegisterEmailFragment";

    private HelperActivityBase activityBase;

    private EditText usernameField;
    private EditText fullNameField;
    private TextInputLayout userNameFieldLayout;
    private TextInputLayout fullNameFieldLayout;
    private TextInputLayout passwordLayout;
    private EditText passwordField;

    private RequiredFieldValidator usernameFieldValidator;
    private RequiredFieldValidator fullNameFieldValidator;
    private PasswordFieldValidator passwordFieldValidator;

    private User user;

    private RegisterEmailPresenter presenter;

    private String username;
    private String fullName;
    private String password;

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
        View view = inflater.inflate(R.layout.auth_fragment_user_username_and_fullname,
                container, false);
        presenter.attachView(this);
        init(view);
        presenter.onIntialisedRequest();
        if (savedInstanceState != null) {
            return view;
        }
        return view;
    }

    private void init(View view) {
        usernameField = view.findViewById(R.id.username_field);
        fullNameField = view.findViewById(R.id.full_name_field);
        userNameFieldLayout = view.findViewById(R.id.username_field_layout);
        fullNameFieldLayout = view.findViewById(R.id.full_name_field_layout);
        passwordLayout = view.findViewById(R.id.password_field_layout);
        passwordField = view.findViewById(R.id.password_field);

        passwordFieldValidator = new PasswordFieldValidator(passwordLayout,
                getResources().getInteger(R.integer.auth_min_password_length));
        usernameFieldValidator = new RequiredFieldValidator(userNameFieldLayout);
        fullNameFieldValidator = new RequiredFieldValidator(fullNameFieldLayout);

        ImeHelper.setImeOnDoneListener(fullNameField, this);

        usernameField.setOnFocusChangeListener(this);
        fullNameField.setOnFocusChangeListener(this);
        passwordField.setOnFocusChangeListener(this);

        usernameField.setOnClickListener(this);
        userNameFieldLayout.setOnClickListener(this);
        fullNameField.setOnClickListener(this);
        fullNameFieldLayout.setOnClickListener(this);
        passwordField.setOnClickListener(this);
        passwordLayout.setOnClickListener(this);

        String username = user.getmUsername();
        if (!TextUtils.isEmpty(username)) {
            usernameField.setText(username);
        }

        String name = user.getName();
        if (!TextUtils.isEmpty(name)) {
            fullNameField.setText(name);
        }

        safeRequestFocus(usernameField);

        view.findViewById(R.id.button_save).setOnClickListener(this);

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
                new User.Builder(EmailAuthProvider.PROVIDER_ID, user.getEmail())
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
        if (id == R.id.username_field) {
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
        } else if (id == R.id.username_field_layout || id == R.id.username_field) {
            userNameFieldLayout.setError(null);
        } else if (id == R.id.full_name_field_layout || id == R.id.full_name_field) {
            fullNameFieldLayout.setError(null);
        } else if (id == R.id.password_field_layout || id == R.id.password_field) {
            passwordLayout.setError(null);
        }
    }

    @Override
    public void onDonePressed() {
        validateAndRegisterUser();
    }

    private void validateAndRegisterUser() {
        username = usernameField.getText().toString();
        fullName = fullNameField.getText().toString();
        password = passwordField.getText().toString();

        boolean usernameValid = usernameFieldValidator.validate(username);
        boolean fullNameValid = fullNameFieldValidator.validate(fullName);
        boolean passwordValid = passwordFieldValidator.validate(password);

        if (usernameValid && fullNameValid && passwordValid) {
            presenter.checkUsername(username);
        }
    }

    @Override
    public void isUsernameExists(boolean usernameExists) {
        if (!usernameExists) {
            String email = user.getEmail();
            registerUser(email);
        }
        userNameFieldLayout.setError(getString(R.string.auth_username_exists));
    }

    private void registerUser(final String email) {
        getDialogHolder().showLoadingDialog(R.string.auth_sign_up);

        registerUserWordpress();

        IdpResponse response = new IdpResponse.Builder(
                new User.Builder(EmailAuthProvider.PROVIDER_ID, user.getEmail())
                        .setUsername(username)
                        .setName(fullName)
                        .setPhotoUri(user.getPhotoUri())
                        .build())
                .build();
        getAuthHelper().getFirebaseAuth()
                .createUserWithEmailAndPassword(email, password)
                .continueWithTask(new ProfileMerger(response))
                .addOnFailureListener(new TaskFailureLogger(TAG, "Error creating user"))
                .addOnSuccessListener(getActivity(), authResult -> {
                    activityBase.setResultAndFinish(authResult.getUser(),
                            password, response);
                })
                .addOnFailureListener(getActivity(), e -> {
                    if (e instanceof FirebaseAuthWeakPasswordException) {
                        // Password too weak
                        passwordLayout.setError(getResources().getQuantityString(
                                R.plurals.auth_error_weak_password, R.integer.auth_min_password_length));
                    } else {
                        Toast.makeText(getActivity(), "Registration Failed", Toast.LENGTH_SHORT)
                                .show();
                    }
                    getDialogHolder().dismissDialog();
                });

    }

    private void registerUserWordpress() {
        String email = user.getEmail();

        String firstName;
        String lastName = "";

        if (fullName.split("\\w+").length > 1) {

            lastName = fullName.substring(fullName.lastIndexOf(" ") + 1);
            firstName = fullName.substring(0, fullName.lastIndexOf(' '));
        } else {
            firstName = fullName;
        }

        presenter.registerNewUser(username, email, password, fullName, firstName, lastName,
                EmailAuthProvider.PROVIDER_ID);
    }

    @Override
    public void onDestroyView() {
        presenter.detachView();
        super.onDestroyView();
    }
}
