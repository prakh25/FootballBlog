package com.example.authlib.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.RestrictTo;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.example.authlib.R;
import com.example.authlib.User;

/**
 * Created by prakh on 02-12-2017.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public class RegisterUserActivity extends HelperActivityBase
        implements CheckEmailFragment.CheckEmailListener {

    private static final int RC_SIGN_IN = 17;
    public static final int RC_WELCOME_BACK_IDP = 18;

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RC_SIGN_IN:
            case RC_WELCOME_BACK_IDP:
                finish(resultCode, data);
        }
    }

    @Override
    public void onExistingEmailUser(User user) {
        startActivityForResult(WelcomeBackEmailActivity.createNewIntent(
                this, user.getEmail()),
                RC_SIGN_IN);

        setSlideAnimation();
    }

    @Override
    public void onExistingIdpUser(User user) {
        startActivityForResult(
                WelcomeBackIdpActivity.createIntent(this, getFlowParams(), user.getEmail(),
                        user.getProviderId()),
                RC_WELCOME_BACK_IDP);
        setSlideAnimation();
    }

    @Override
    public void onNewUser(User user) {

        if(getFlowParams().allowNewEmailAccounts) {
            RegisterEmailFragment fragment = RegisterEmailFragment.newInstance(getFlowParams(),
                    user);

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_new_user, fragment, RegisterEmailFragment.TAG);

            ft.disallowAddToBackStack().commit();
        }
    }

    private void setSlideAnimation() {
        overridePendingTransition(R.anim.auth_slide_in_right, R.anim.auth_slide_out_left);
    }
}
