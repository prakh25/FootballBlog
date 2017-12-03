package com.example.authlib.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.example.authlib.R;
import com.example.authlib.User;

/**
 * Created by prakh on 02-12-2017.
 */

public class RegisterUserActivity extends HelperActivityBase
        implements CheckEmailFragment.CheckEmailListener {

    private static final int RC_SIGN_IN = 17;

    public static Intent createIntent(Context context, FlowParameters flowParams) {
        return HelperActivityBase.createBaseIntent(context, RegisterUserActivity.class, flowParams);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth_activity_new_user_registration);

        if (savedInstanceState != null) {
            return;
        }

        Fragment fragment = CheckEmailFragment.newInstance(getFlowParams());

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_new_user, fragment, CheckEmailFragment.TAG)
                .disallowAddToBackStack()
                .commit();
    }

    @Override
    public void newUser(User user) {
        if(getFlowParams().allowNewEmailAccounts) {
            RegisterEmailFragment fragment = RegisterEmailFragment.newInstance(getFlowParams(),
                    user);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_new_user, fragment, RegisterEmailFragment.TAG)
                    .disallowAddToBackStack()
                    .commit();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RC_SIGN_IN:
                finish(resultCode, data);
        }
    }
}
