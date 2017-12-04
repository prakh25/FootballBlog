package com.example.authlib;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.RestrictTo;
import android.util.Log;

import com.example.authlib.ui.AuthMethodPickerActivity;
import com.example.authlib.ui.ExtraConstants;
import com.example.authlib.ui.FlowParameters;
import com.example.authlib.ui.HelperActivityBase;
import com.example.authlib.utils.PlayServicesHelper;

import java.util.List;

/**
 * Created by prakh on 02-12-2017.
 */

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public class KickoffActivity extends HelperActivityBase {
    private static final String TAG = "KickoffActivity";
    private static final String IS_WAITING_FOR_PLAY_SERVICES = "is_waiting_for_play_services";
    private static final int RC_PLAY_SERVICES = 1;
    private static final int RC_AUTH_METHOD_PICKER = 4;

    private boolean mIsWaitingForPlayServices = false;

    public static Intent createIntent(Context context, FlowParameters flowParams) {
        return HelperActivityBase.createBaseIntent(context, KickoffActivity.class, flowParams);
    }

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        if (savedInstance == null || savedInstance.getBoolean(IS_WAITING_FOR_PLAY_SERVICES)) {
            if (isOffline()) {
                Log.d(TAG, "No network connection");
                finish(RESULT_CANCELED,
                        IdpResponse.getErrorCodeIntent(ErrorCodes.NO_NETWORK));
                return;
            }

            boolean isPlayServicesAvailable = PlayServicesHelper.makePlayServicesAvailable(
                    this,
                    RC_PLAY_SERVICES,
                    dialog -> finish(RESULT_CANCELED,
                            IdpResponse.getErrorCodeIntent(
                                    ErrorCodes.UNKNOWN_ERROR)));

            if (isPlayServicesAvailable) {
                start();
            } else {
                mIsWaitingForPlayServices = true;
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // It doesn't matter what we put here, we just don't want outState to be empty
        outState.putBoolean(ExtraConstants.HAS_EXISTING_INSTANCE, true);
        outState.putBoolean(IS_WAITING_FOR_PLAY_SERVICES, mIsWaitingForPlayServices);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RC_PLAY_SERVICES:
                if (resultCode == RESULT_OK) {
                    start();
                } else {
                    finish(RESULT_CANCELED,
                            IdpResponse.getErrorCodeIntent(ErrorCodes.UNKNOWN_ERROR));
                }
                break;
            case RC_AUTH_METHOD_PICKER:
                finish(resultCode, data);
                break;
        }
    }

    private void start() {
        FlowParameters flowParams = getFlowParams();
        List<AuthLibUi.IdpConfig> idpConfigs = flowParams.providerInfo;

        startActivityForResult(
                AuthMethodPickerActivity.createIntent(
                        this,
                        flowParams),
                RC_AUTH_METHOD_PICKER);

        getDialogHolder().dismissDialog();
    }

    /**
     * Check if there is an active or soon-to-be-active network connection.
     *
     * @return true if there is no network connection, false otherwise.
     */
    private boolean isOffline() {
        ConnectivityManager manager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return !(manager != null
                && manager.getActiveNetworkInfo() != null
                && manager.getActiveNetworkInfo().isConnectedOrConnecting());
    }
}

