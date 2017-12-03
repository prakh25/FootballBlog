package com.example.corelib.ui.authui;

import com.example.corelib.model.auth.emailvalidator.EmailExists;
import com.example.corelib.network.DataManager;
import com.example.corelib.network.RemoteCallback;
import com.example.corelib.ui.BasePresenter;

/**
 * Created by prakh on 03-12-2017.
 */

public class CheckEmailPresenter extends
        BasePresenter<CheckEmailContract.CheckEmailView>
        implements CheckEmailContract.viewActions {

    private static final String INSECURE = "cool";

    private final DataManager dataManager;

    public CheckEmailPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void checkEmail(String email) {
        checkIfEmailExists(email);
    }

    private void checkIfEmailExists(String email) {

        dataManager.ifEmailExists(INSECURE, email, new RemoteCallback<EmailExists>() {
            @Override
            public void onSuccess(EmailExists response) {
                if(response.getStatus().equalsIgnoreCase("ok")) {
                    mView.isEmailPresent(response.isEmail_exists());
                }
            }

            @Override
            public void onFailed(Throwable throwable) {

            }
        });
    }
}
