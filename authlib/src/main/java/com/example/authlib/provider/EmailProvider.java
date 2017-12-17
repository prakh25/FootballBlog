package com.example.authlib.provider;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;

import com.example.authlib.R;
import com.example.authlib.ui.FlowParameters;
import com.example.authlib.ui.RegisterUserActivity;

/**
 * Created by prakh on 02-12-2017.
 */

public class EmailProvider implements Provider {

    private static final int RC_EMAIL_FLOW = 2;

    private Activity mActivity;
    private FlowParameters mFlowParameters;

    public EmailProvider(Activity activity, FlowParameters flowParameters) {
        mActivity = activity;
        mFlowParameters = flowParameters;
    }

    @Override
    public String getName(Context context) {
        return context.getString(R.string.auth_provider_name_email);
    }

    @Override
    @LayoutRes
    public int getButtonLayout() {
        return R.layout.auth_button_email_sign_in;
    }

    @Override
    public void startLogin(Activity activity) {
        activity.startActivityForResult(RegisterUserActivity.createIntent(activity, mFlowParameters),
                RC_EMAIL_FLOW);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_EMAIL_FLOW && resultCode == Activity.RESULT_OK) {
            mActivity.setResult(Activity.RESULT_OK, data);
            mActivity.finish();
        }
    }
}
