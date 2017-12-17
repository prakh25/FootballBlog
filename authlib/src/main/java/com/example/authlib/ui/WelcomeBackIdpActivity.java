package com.example.authlib.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.example.authlib.AuthLibUi;
import com.example.authlib.ErrorCodes;
import com.example.authlib.IdpResponse;
import com.example.authlib.R;
import com.example.authlib.User;
import com.example.authlib.provider.AuthProviderId;
import com.example.authlib.provider.GoogleProvider;
import com.example.authlib.provider.IdpProvider;
import com.example.corelib.SharedPreferenceManager;
import com.example.corelib.model.auth.UserData;
import com.example.corelib.network.DataManager;
import com.example.corelib.ui.authui.WelcomeBackIdpContract;
import com.example.corelib.ui.authui.WelcomeBackIdpPresenter;

/**
 * Created by prakh on 16-12-2017.
 */

public class WelcomeBackIdpActivity extends HelperActivityBase implements IdpProvider.IdpCallback,
        WelcomeBackIdpContract.WelcomeBackIdpView {

    private IdpProvider mIdpProvider;
    private WelcomeBackIdpPresenter presenter;

    public static Intent createIntent(Context context, FlowParameters flowParameters,
                                      String email, String provideId) {
        return HelperActivityBase.createBaseIntent(context, WelcomeBackIdpActivity.class, flowParameters)
                .putExtra(ExtraConstants.EXTRA_EMAIL, email)
                .putExtra(ExtraConstants.EXTRA_PROVIDER_ID, provideId);
    }

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        presenter = new WelcomeBackIdpPresenter(DataManager.getInstance(),
                SharedPreferenceManager.getInstance());
        setContentView(R.layout.auth_activity_welcome_back_idp);
        presenter.attachView(this);

        String email = getIntent().getStringExtra(ExtraConstants.EXTRA_EMAIL);
        String providerId = getIntent().getStringExtra(ExtraConstants.EXTRA_PROVIDER_ID);

        for (AuthLibUi.IdpConfig idpConfig : getFlowParams().providerInfo) {
            if (providerId.equals(idpConfig.getProviderId())) {
                switch (providerId) {
                    case AuthProviderId.GOOGLE_PROVIDER_ID:
                        mIdpProvider = new GoogleProvider(this, idpConfig, email);
                        break;
                    default:
                        finish(RESULT_CANCELED, IdpResponse.getErrorCodeIntent(ErrorCodes.UNKNOWN_ERROR));
                        return;
                }
            }
        }

        if (mIdpProvider == null) {
            finish(RESULT_CANCELED, IdpResponse.getErrorCodeIntent(ErrorCodes.UNKNOWN_ERROR));
            return;
        }

        mIdpProvider.setAuthenticationCallback(this);
        TextView welcomeBackBody = findViewById(R.id.welcome_back_idp_body);
        welcomeBackBody.setText(getIdpPromptString(email));

        findViewById(R.id.button_login).setOnClickListener(view -> {
            getDialogHolder().showLoadingDialog(R.string.auth_sign_in);
            mIdpProvider.startLogin(WelcomeBackIdpActivity.this);
        });
    }

    private String getIdpPromptString(String email) {
        return getString(R.string.auth_welcome_back_idp_prompt_body, email);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mIdpProvider.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSuccess(IdpResponse idpResponse) {
        String email = idpResponse.getEmail();
        if(TextUtils.isEmpty(email)) {
            finish(RESULT_CANCELED, IdpResponse.getErrorCodeIntent(ErrorCodes.UNKNOWN_ERROR));
            return;
        }

        presenter.loginUsingIdpProvider(email);
    }

    @Override
    public void onFailure() {
        finishWithError();
    }

    private void finishWithError() {
        Toast.makeText(this, R.string.auth_general_error, Toast.LENGTH_LONG).show();
        finish(RESULT_CANCELED, IdpResponse.getErrorCodeIntent(ErrorCodes.UNKNOWN_ERROR));
    }

    @Override
    public void onSignInSuccessful(UserData info) {
        String email = info.getEmail();
        String username = info.getUsername();
        String name = info.getDisplayname();
        String photoUri = "";

        if (!TextUtils.isEmpty(info.getWslCurrentUserImage())) {
            photoUri = info.getWslCurrentUserImage();
        }

        IdpResponse response = new IdpResponse.Builder(
                new User.Builder(AuthProviderId.GOOGLE_PROVIDER_ID, email)
                        .setUsername(username)
                        .setName(name)
                        .setPhotoUri(photoUri)
                        .build())
                .build();

        setResultAndFinish(null, response);
    }

    @Override
    protected void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }
}
