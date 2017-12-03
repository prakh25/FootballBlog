package com.example.authlib.ui.fieldvalidators;

import android.support.design.widget.TextInputLayout;
import android.util.Patterns;

import com.example.authlib.R;

/**
 * Created by prakh on 02-12-2017.
 */

public class EmailFieldValidator extends BaseValidator {

    public EmailFieldValidator(TextInputLayout errorContainer) {
        super(errorContainer);
        mErrorMessage = mErrorContainer.getResources().getString(R.string.auth_invalid_email_address);
        mEmptyMessage = mErrorContainer.getResources().getString(R.string.auth_missing_email_address);
    }

    @Override
    protected boolean isValid(CharSequence charSequence) {
        return Patterns.EMAIL_ADDRESS.matcher(charSequence).matches();
    }


}
