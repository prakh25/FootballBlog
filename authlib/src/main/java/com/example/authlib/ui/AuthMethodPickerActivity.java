package com.example.authlib.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.example.authlib.AuthLibUi;
import com.example.authlib.AuthLibUi.IdpConfig;
import com.example.authlib.IdpResponse;
import com.example.authlib.R;
import com.example.authlib.User;
import com.example.authlib.provider.EmailProvider;
import com.example.authlib.provider.GoogleProvider;
import com.example.authlib.provider.IdpProvider;
import com.example.authlib.provider.Provider;
import com.example.corelib.SharedPreferenceManager;
import com.example.corelib.model.auth.signinwithemail.UserInfo;
import com.example.corelib.network.DataManager;
import com.example.corelib.ui.authui.AuthMethodContract;
import com.example.corelib.ui.authui.AuthMethodPresenter;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * Created by prakh on 02-12-2017.
 */

public class AuthMethodPickerActivity extends HelperActivityBase implements IdpProvider.IdpCallback,
        AuthMethodContract.AuthMethodView {

    private static final String TAG = "AuthMethodPicker";

    private static final int RC_ACCOUNT_LINK = 3;
    private static final int RC_EMAIL_FLOW = 5;
    private static final int RC_SIGN_IN = 12;

    private List<Provider> mProviders;
    private AuthMethodPresenter presenter;

    public static Intent createIntent(Context context, FlowParameters flowParams) {
        return HelperActivityBase.createBaseIntent(
                context, AuthMethodPickerActivity.class, flowParams);
    }

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        presenter = new AuthMethodPresenter(DataManager.getInstance(),
                SharedPreferenceManager.getInstance());

        setContentView(R.layout.auth_activity_method_picker);

        presenter.attachView(this);
        populateIdpList(getFlowParams().providerInfo);

        SupportVectorDrawablesButton newUserButton = findViewById(R.id.new_user_button);

        newUserButton.setOnClickListener(view ->
                startActivityForResult(RegisterUserActivity.createIntent(this, getFlowParams()),
                        RC_EMAIL_FLOW));
    }

    @SuppressLint("WrongConstant")
    private void populateIdpList(List<IdpConfig> providers) {
        mProviders = new ArrayList<>();
        for (IdpConfig idpConfig : providers) {
            switch (idpConfig.getProviderId()) {
                case AuthLibUi.EMAIL_PROVIDER:
                    mProviders.add(new EmailProvider(this, getFlowParams()));
                    break;
                case AuthLibUi.GOOGLE_PROVIDER:
                    mProviders.add(new GoogleProvider(this, idpConfig));
                    break;
                default:
                    Log.e(TAG, "Encountered unknown provider parcel with type: "
                            + idpConfig.getProviderId());
            }
        }

        ViewGroup btnHolder = findViewById(R.id.btn_holder);
        for (final Provider provider : mProviders) {
            View loginButton = getLayoutInflater()
                    .inflate(provider.getButtonLayout(), btnHolder, false);

            loginButton.setOnClickListener(view -> {
                if (provider instanceof IdpProvider) {
                    getDialogHolder().showLoadingDialog(R.string.auth_progress_dialog_loading);
                }
                provider.startLogin(AuthMethodPickerActivity.this);
            });
            if (provider instanceof IdpProvider) {
                ((IdpProvider) provider).setAuthenticationCallback(this);
            }
            btnHolder.addView(loginButton);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_ACCOUNT_LINK || requestCode == RC_EMAIL_FLOW ||
                requestCode == RC_SIGN_IN) {
            finish(resultCode, data);
        } else {
            for (Provider provider : mProviders) {
                provider.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    @Override
    public void onSuccess(IdpResponse idpResponse) {
//        AuthCredential credential = ProviderUtils.getAuthCredential(idpResponse);
//        getAuthHelper().getFirebaseAuth()
//                .signInWithCredential(credential)
//                .addOnCompleteListener(new CredentialSignInHandler(this,
//                        idpResponse, RC_ACCOUNT_LINK))
//                .addOnFailureListener(
//                        new TaskFailureLogger(TAG, "Firebase signIn with credential unsuccessful")
//                );
        String email = idpResponse.getUser().getEmail();
        Log.d(TAG, " email " + email);
        String name = idpResponse.getUser().getName();
        Log.d(TAG, " name " + name);
        String photoUri = idpResponse.getUser().getPhotoUri();

        String firstName;
        String lastName = "";

        if (name.split("\\w+").length > 1) {

            lastName = name.substring(name.lastIndexOf(" ") + 1);
            firstName = name.substring(0, name.lastIndexOf(' '));
        } else {
            firstName = name;
        }

        Random r = new Random();
        int randomNumber = r.nextInt(99999 - 100) + 100;

        String userName = String.format(Locale.US, "%s_%d", firstName, randomNumber);

        presenter.getNonce(email, userName, firstName, lastName, name,
                GoogleAuthProvider.PROVIDER_ID, photoUri);
    }

    @Override
    public void onFailure() {
        getDialogHolder().dismissDialog();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mProviders != null) {
            for (Provider provider : mProviders) {
                if (provider instanceof GoogleProvider) {
                    ((GoogleProvider) provider).disconnect();
                }
            }
        }
    }

    @Override
    public void nonceAcquired(String nonce, String email,
                              String username, String fistName, String lastName,
                              String displayName, String providerId, String customAvatar) {
        presenter.loginUsingIdpProvider(email, username, nonce, fistName,
                lastName, displayName, providerId, customAvatar);
    }

    @Override
    public void onSignInSuccessful(UserInfo info) {
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

        setResultAndFinish(null, response);
    }
}
