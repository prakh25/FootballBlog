package com.example.authlib.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.example.authlib.AuthLibUi;
import com.example.authlib.AuthLibUi.IdpConfig;
import com.example.authlib.IdpResponse;
import com.example.authlib.R;
import com.example.authlib.provider.EmailProvider;
import com.example.authlib.provider.GoogleProvider;
import com.example.authlib.provider.IdpProvider;
import com.example.authlib.provider.Provider;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prakh on 02-12-2017.
 */

public class AuthMethodPickerActivity extends HelperActivityBase implements IdpProvider.IdpCallback {

    private static final String TAG = "AuthMethodPicker";

    private static final int RC_ACCOUNT_LINK = 3;

    private List<Provider> mProviders;

    public static Intent createIntent(Context context, FlowParameters flowParams) {
        return HelperActivityBase.createBaseIntent(
                context, AuthMethodPickerActivity.class, flowParams);
    }

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.auth_activity_method_picker);

        populateIdpList(getFlowParams().providerInfo);
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
        if (requestCode == RC_ACCOUNT_LINK) {
            finish(resultCode, data);
        } else {
            for (Provider provider : mProviders) {
                provider.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    @Override
    public void onSuccess(IdpResponse idpResponse) {

    }

    @Override
    public void onFailure() {

    }
}
