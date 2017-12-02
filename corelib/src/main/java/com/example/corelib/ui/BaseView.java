package com.example.corelib.ui;

/**
 * Created by prakh on 16-11-2017.
 */

public interface BaseView {
    void showProgress();

    void hideProgress();

    void showEmpty();

    void showError(String errorMessage);

    void showMessageLayout(boolean show);
}
