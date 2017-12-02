package com.example.prakh.footballblog;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.example.corelib.SharedPreferenceManager;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.GoogleAuthProvider;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by prakh on 27-11-2017.
 */

public class LoginActivity extends BaseActivity {

    public static final String TAG = LoginActivity.class.getSimpleName();

    public static final String GOOGLE_PROVIDER = GoogleAuthProvider.PROVIDER_ID;

    public static final String EMAIL_PROVIDER = EmailAuthProvider.PROVIDER_ID;

    public static final String SKIP_PROVIDER = "Skip";
//
//    @BindView(R.id.button_skip)
//    SupportVectorDrawablesButton skipButton;
    @BindView(R.id.btn_holder)
    ViewGroup buttonHolder;

    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        unbinder = ButterKnife.bind(this);

        SharedPreferenceManager preferenceManager = SharedPreferenceManager.getInstance();

        View loginButton = getLayoutInflater()
                .inflate(R.layout.button_email_sign_in, buttonHolder, false);
        buttonHolder.addView(loginButton);

//        if(preferenceManager.isSecondLaunch()) {
//            startActivity(HomeActivity.createNewIntent(this));
//            finish();
//        }

//        skipButton.setOnClickListener(view -> {
//            preferenceManager.setFirstLaunch();
//            startActivity(HomeActivity.createNewIntent(this));
//            finish();
//        });
    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }
}
