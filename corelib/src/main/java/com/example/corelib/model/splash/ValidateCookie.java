package com.example.corelib.model.splash;

import com.google.gson.annotations.Expose;

/**
 * Created by prakh on 18-12-2017.
 */

public class ValidateCookie {

    @Expose
    private boolean valid;

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }
}
