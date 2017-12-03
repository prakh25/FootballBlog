package com.example.authlib.ui.fieldvalidators;

import android.support.design.widget.TextInputLayout;

/**
 * Created by prakh on 02-12-2017.
 */

public class BaseValidator {
    protected TextInputLayout mErrorContainer;
    protected String mErrorMessage = "";
    protected String mEmptyMessage;

    public BaseValidator(TextInputLayout errorContainer) {
        mErrorContainer = errorContainer;
    }

    protected boolean isValid(CharSequence charSequence) {
        return true;
    }

    public boolean validate(CharSequence charSequence) {
        if (mEmptyMessage != null && (charSequence == null || charSequence.length() == 0)) {
            mErrorContainer.setError(mEmptyMessage);
            return false;
        } else if (isValid(charSequence)) {
            mErrorContainer.setError("");
            return true;
        } else {
            mErrorContainer.setError(mErrorMessage);
            return false;
        }
    }
}
