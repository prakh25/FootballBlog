package com.example.authlib.ui.fieldvalidators;

import android.support.design.widget.TextInputLayout;

import com.example.authlib.R;

/**
 * Created by prakh on 03-12-2017.
 */

public class PasswordFieldValidator extends BaseValidator {
    private int mMinLength;

    public PasswordFieldValidator(TextInputLayout errorContainer, int minLength) {
        super(errorContainer);
        mMinLength = minLength;
        mErrorMessage = mErrorContainer.getResources()
                .getQuantityString(R.plurals.auth_error_weak_password, mMinLength, mMinLength);
    }

    @Override
    protected boolean isValid(CharSequence charSequence) {
        return charSequence.length() >= mMinLength;
    }
}
