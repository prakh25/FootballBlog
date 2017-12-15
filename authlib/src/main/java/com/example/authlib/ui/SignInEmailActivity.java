package com.example.authlib.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.example.authlib.IdpResponse;
import com.example.authlib.R;
import com.example.authlib.User;
import com.example.authlib.ui.fieldvalidators.EmailFieldValidator;
import com.example.authlib.ui.fieldvalidators.RequiredFieldValidator;
import com.example.authlib.utils.ImeHelper;
import com.example.corelib.SharedPreferenceManager;
import com.example.corelib.model.auth.signinwithemail.UserInfo;
import com.example.corelib.network.DataManager;
import com.example.corelib.ui.authui.SignInEmailContract;
import com.example.corelib.ui.authui.SignInEmailPresenter;
import com.google.firebase.auth.EmailAuthProvider;

/**
 * Created by prakh on 04-12-2017.
 */

public class SignInEmailActivity extends HelperActivityBase implements
        View.OnFocusChangeListener, ImeHelper.DonePressedListener,
        SignInEmailContract.SignInEmailView {

    private static final String TAG = "SignInEmailActivity";

    private static final int RC_SIGN_IN = 18;

    private TextInputLayout emailFieldLayout;
    private EditText emailField;
    private TextInputLayout passwordFieldLayout;
    private EditText passwordField;

    private EmailFieldValidator emailFieldValidator;
    private RequiredFieldValidator emailValidator;
    private RequiredFieldValidator passwordValidator;
    private SignInEmailPresenter presenter;

    public static Intent createIntent(Context context, FlowParameters flowParams) {
        return HelperActivityBase.createBaseIntent(context, SignInEmailActivity.class, flowParams);
    }

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        presenter = new SignInEmailPresenter(DataManager.getInstance(),
                SharedPreferenceManager.getInstance());
        setContentView(R.layout.auth_activity_sign_in_activity);
        presenter.attachView(this);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        emailFieldLayout = findViewById(R.id.email_field_layout);
        emailField = findViewById(R.id.email_field);
        passwordFieldLayout = findViewById(R.id.password_field_layout);
        passwordField = findViewById(R.id.password_field);

        ImeHelper.setImeOnDoneListener(passwordField, this);

        emailFieldValidator = new EmailFieldValidator(emailFieldLayout);
        emailValidator = new RequiredFieldValidator(emailFieldLayout);
        passwordValidator = new RequiredFieldValidator(passwordFieldLayout);

        emailField.setOnFocusChangeListener(this);
        passwordField.setOnFocusChangeListener(this);

        findViewById(R.id.button_login).setOnClickListener(view -> validateAndSignIn());

    }

    @Override
    public void onFocusChange(View view, boolean b) {
        if (b) return; // Only consider fields losing focus

        int id = view.getId();
        if (id == R.id.email_field) {
            emailValidator.validate(emailField.getText());
            emailFieldValidator.validate(emailField.getText());
        } else if (id == R.id.password_field) {
            passwordValidator.validate(passwordField.getText());
        }
    }

    @Override
    public void onDonePressed() {
        validateAndSignIn();
    }

    private void validateAndSignIn() {
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();

        boolean emailValid = emailValidator.validate(email);
        boolean validEmail = emailFieldValidator.validate(email);
        boolean passwordValid = passwordValidator.validate(password);

        if (emailValid && passwordValid && validEmail) {
            trySignInUsingWordpress(email, password);
        }
    }

    private void trySignInUsingWordpress(String email, String password) {
        getDialogHolder().showLoadingDialog(R.string.auth_sign_in);

//        IdpResponse response = new IdpResponse.Builder(
//                new User.Builder(EmailAuthProvider.PROVIDER_ID, email).build())
//                .build();

        presenter.loginUsingWordpress(email, password);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RC_SIGN_IN:
                finish(resultCode, data);
        }
    }

    @Override
    public void onSignInSuccessful(UserInfo info) {
        String password = passwordField.getText().toString();
        String email = info.getEmail();
        String username = info.getUsername();
        String name = info.getDisplayname();
        String photoUri = null;
        if (!TextUtils.isEmpty(info.getWslCurrentUserImage())) {
            photoUri = info.getWslCurrentUserImage();
        }

        IdpResponse response = new IdpResponse.Builder(
                new User.Builder(EmailAuthProvider.PROVIDER_ID, email)
                        .setUsername(username)
                        .setName(name)
                        .setPhotoUri(photoUri)
                        .build())
                .build();

        setResultAndFinish(password, response);
//        registerInFirebase(email, password, username, name, photoUri);

    }


//    private void registerInFirebase(String email, String password, String username,
//                                    String name, Uri photoUri) {
//
//        IdpResponse response = new IdpResponse.Builder(
//                new User.Builder(EmailAuthProvider.PROVIDER_ID, email)
//                        .setUsername(username)
//                        .setName(name)
//                        .setPhotoUri(photoUri)
//                        .build())
//                .build();
//
//        getAuthHelper().getFirebaseAuth()
//                .createUserWithEmailAndPassword(email, password)
//                .continueWithTask(new ProfileMerger(response))
//                .addOnFailureListener(new TaskFailureLogger(TAG, "Error creating user"))
//                .addOnSuccessListener(this, authResult ->
//                        setResultAndFinish(authResult.getUser(),
//                                password, response))
//                .addOnFailureListener(this, e -> {
//                    if (e instanceof FirebaseAuthWeakPasswordException) {
//                        // Password too weak
//                        passwordFieldLayout.setError(getResources().getQuantityString(
//                                R.plurals.auth_error_weak_password, R.integer.auth_min_password_length));
//                    } else {
//                        Toast.makeText(this, "Registration Failed", Toast.LENGTH_SHORT)
//                                .show();
//                    }
//                    getDialogHolder().dismissDialog();
//                });
//    }

    @Override
    public void invalidEmailError(String error) {
        passwordFieldLayout.setError(error);
        getDialogHolder().dismissDialog();
    }

    @Override
    public void emailDoesNotExistError(String error) {
        passwordFieldLayout.setError(error);
        getDialogHolder().dismissDialog();
    }
}
