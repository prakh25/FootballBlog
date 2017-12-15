package com.example.authlib;

import android.support.annotation.NonNull;

import com.example.authlib.ui.HelperActivityBase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

/**
 * Created by prakh on 05-12-2017.
 */

public class CredentialSignInHandler implements OnCompleteListener<User> {
    private static final String TAG = "CredentialSignInHandler";

    private HelperActivityBase mActivity;
    private IdpResponse mResponse;
    private int mAccountLinkRequestCode;

    public CredentialSignInHandler(HelperActivityBase mActivity,
                                   IdpResponse mResponse, int mAccountLinkRequestCode) {
        this.mActivity = mActivity;
        this.mResponse = mResponse;
        this.mAccountLinkRequestCode = mAccountLinkRequestCode;
    }

    @Override
    public void onComplete(@NonNull Task<User> task) {
        if(task.isSuccessful()) {
            User user = task.getResult();
            mActivity.setResultAndFinish(mResponse);
        }
    }
}
