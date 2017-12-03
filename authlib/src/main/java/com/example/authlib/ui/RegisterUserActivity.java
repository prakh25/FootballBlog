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
        implements RegisterEmailAndPasswordFragment.EmailAndPasswordListener {

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

        Fragment fragment = RegisterEmailAndPasswordFragment.newInstance(getFlowParams());

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_new_user, fragment, RegisterEmailAndPasswordFragment.TAG)
                .disallowAddToBackStack()
                .commit();
    }

    @Override
    public void newUser(User user) {

    }
}
