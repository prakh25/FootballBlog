package com.example.prakh.footballblog.provider;

import android.content.Intent;
import android.support.annotation.LayoutRes;

/**
 * Created by prakh on 02-12-2017.
 */

public interface Provider {

    @LayoutRes
    int getButtonLayout();

    void startLoginOrSkip();

    void onActivityResult(int requestCode, int resultCode, Intent data);
}
