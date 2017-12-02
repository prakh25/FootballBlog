package com.example.authlib.ui;

import android.content.Context;
import android.content.Intent;

import com.example.authlib.FlowParameters;
import com.example.authlib.IdpResponse;
import com.example.authlib.provider.IdpProvider;

/**
 * Created by prakh on 02-12-2017.
 */

public class AuthMethodPickerActivity extends HelperActivityBase implements IdpProvider.IdpCallback {

    public static Intent createIntent(Context context, FlowParameters flowParams) {
        return HelperActivityBase.createBaseIntent(
                context, AuthMethodPickerActivity.class, flowParams);
    }

    @Override
    public void onSuccess(IdpResponse idpResponse) {

    }

    @Override
    public void onFailure() {

    }
}
