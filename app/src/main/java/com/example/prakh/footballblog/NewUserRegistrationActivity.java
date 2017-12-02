package com.example.prakh.footballblog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by prakh on 30-11-2017.
 */

public class NewUserRegistrationActivity extends BaseActivity {

    public static Intent createNewIntent(Context context) {
        return new Intent(context, NewUserRegistrationActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user_registration);
    }
}
