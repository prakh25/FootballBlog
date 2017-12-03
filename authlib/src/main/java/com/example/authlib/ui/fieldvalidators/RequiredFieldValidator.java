package com.example.authlib.ui.fieldvalidators;

import android.support.design.widget.TextInputLayout;

import com.example.authlib.R;

/**
 * Created by prakh on 03-12-2017.
 */

public class RequiredFieldValidator extends BaseValidator {
    public RequiredFieldValidator(TextInputLayout errorContainer) {
        super(errorContainer);
        mErrorMessage = mErrorContainer.getResources().getString(R.string.auth_required_field);
    }

    @Override
    protected boolean isValid(CharSequence charSequence) {
        return charSequence != null && charSequence.length() > 0;
    }
}
