package com.example.authlib.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.example.authlib.IdpResponse;
import com.example.authlib.R;
import com.example.authlib.User;
import com.example.authlib.provider.AuthProviderId;
import com.example.authlib.utils.ImeHelper;
import com.example.corelib.SharedPreferenceManager;
import com.example.corelib.model.auth.UserData;
import com.example.corelib.network.DataManager;
import com.example.corelib.ui.authui.WelcomeBackEmailContract;
import com.example.corelib.ui.authui.WelcomeBackEmailPresenter;

/**
 * Created by prakh on 16-12-2017.
 */

public class WelcomeBackEmailActivity extends HelperActivityBase
        implements ImeHelper.DonePressedListener, WelcomeBackEmailContract.SignInEmailView {

    public static final String TAG = "WelcomeBackEmail";

    private String email;
    private TextInputLayout passwordLayout;
    private EditText passwordField;
    private WelcomeBackEmailPresenter presenter;

    public static Intent createNewIntent(Context context, String email) {
        return new Intent(context, WelcomeBackEmailActivity.class)
                .putExtra(ExtraConstants.EXTRA_EMAIL, email);
    }

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        presenter = new WelcomeBackEmailPresenter(DataManager.getInstance(),
                SharedPreferenceManager.getInstance());
        setContentView(R.layout.auth_activity_welcome_back_email);
        presenter.attachView(this);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        email = getIntent().getStringExtra(ExtraConstants.EXTRA_EMAIL);

        passwordField = findViewById(R.id.password_field);
        passwordLayout = findViewById(R.id.password_field_layout);

        ImeHelper.setImeOnDoneListener(passwordField, this);

        String bodyText = getString(R.string.auth_welcome_back_password_prompt_body, email);

        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(bodyText);
        int emailStart = bodyText.indexOf(email);
        spannableStringBuilder.setSpan(new StyleSpan(Typeface.BOLD),
                emailStart, emailStart + email.length(),
                Spannable.SPAN_INCLUSIVE_INCLUSIVE);

        TextView bodyTextView = findViewById(R.id.welcome_back_email_body);
        bodyTextView.setText(spannableStringBuilder);

        findViewById(R.id.button_login).setOnClickListener(view -> validateAndLogin());
    }

    @Override
    public void onDonePressed() {
        validateAndLogin();
    }

    private void validateAndLogin() {
        validateAndLogin(email, passwordField.getText().toString());
    }

    private void validateAndLogin(String email, String password) {
        if(TextUtils.isEmpty(password)) {
            passwordLayout.setError(getString(R.string.auth_required_field));
        } else {
            passwordLayout.setError(null);
        }
        getDialogHolder().showLoadingDialog(R.string.auth_sign_in);

        presenter.loginUsingWordpress(email, password);
    }


    @Override
    public void onSignInSuccessful(UserData info) {

        String email = info.getEmail();
        String username = info.getUsername();
        String name = info.getDisplayname();
        String photoUri = null;

        if (!TextUtils.isEmpty(info.getWslCurrentUserImage())) {
            photoUri = info.getWslCurrentUserImage();
        }

        IdpResponse response = new IdpResponse.Builder(
                new User.Builder(AuthProviderId.EMAIL_PROVIDER_ID, email)
                        .setUsername(username)
                        .setName(name)
                        .setPhotoUri(photoUri)
                        .build())
                .build();

        setResultAndFinish(response);
    }

    @Override
    public void onSignInFailed(String error) {
        getDialogHolder().dismissDialog();
        passwordLayout.setError(error);
    }

    @Override
    protected void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }
}
