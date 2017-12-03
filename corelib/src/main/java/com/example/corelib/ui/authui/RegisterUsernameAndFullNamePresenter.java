package com.example.corelib.ui.authui;

import com.example.corelib.model.auth.username_validator.UsernameExists;
import com.example.corelib.network.DataManager;
import com.example.corelib.network.RemoteCallback;
import com.example.corelib.ui.BasePresenter;

/**
 * Created by prakh on 03-12-2017.
 */

public class RegisterUsernameAndFullNamePresenter extends
        BasePresenter<RegisterUsernameAndFullNameContract.RegisterUsernameAndFullNameView>
        implements RegisterUsernameAndFullNameContract.ViewActions {

    private static final String INSECURE = "cool";

    private final DataManager dataManager;

    public RegisterUsernameAndFullNamePresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void checkUsername(String username) {
        checkIfUsernameExists(username);
    }

    private void checkIfUsernameExists(String username) {
        dataManager.ifUsernameExists(INSECURE, username, new RemoteCallback<UsernameExists>() {
            @Override
            public void onSuccess(UsernameExists response) {
                if(response.getStatus().equalsIgnoreCase("ok")) {
                    mView.isUsernameExists(response.isUsername_exists());
                }
            }

            @Override
            public void onFailed(Throwable throwable) {

            }
        });
    }
}
