package com.example.authlib.ui;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.v4.app.Fragment;
import android.view.ContextThemeWrapper;

import com.example.authlib.R;
import com.example.authlib.utils.AuthHelper;

/**
 * Created by prakh on 03-12-2017.
 */

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public class FragmentBase extends Fragment {

    private FlowParameters mFlowParameters;
    private AuthHelper mAuthHelper;
    private ProgressDialogHolder mProgressDialogHolder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuthHelper = new AuthHelper();
        ContextThemeWrapper context = new ContextThemeWrapper(getContext(),
                R.style.NewUserActivityTheme);
        mProgressDialogHolder = new ProgressDialogHolder(context);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mProgressDialogHolder.dismissDialog();
    }

    public FlowParameters getFlowParams() {
        if (mFlowParameters == null) {
            mFlowParameters = FlowParameters.fromBundle(getArguments());
        }

        return mFlowParameters;
    }

    public AuthHelper getAuthHelper() {
        return mAuthHelper;
    }

    public ProgressDialogHolder getDialogHolder() {
        return mProgressDialogHolder;
    }

    public void finish(int resultCode, Intent resultIntent) {
        getActivity().setResult(resultCode, resultIntent);
        getActivity().finish();
    }

    public void startIntentSenderForResult(IntentSender sender, int requestCode)
            throws IntentSender.SendIntentException {
        startIntentSenderForResult(sender, requestCode, null, 0, 0, 0, null);
    }
}
