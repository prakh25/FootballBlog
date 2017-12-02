package com.example.authlib.utils;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;

/**
 * Created by prakh on 02-12-2017.
 */

public class TaskFailureLogger implements OnFailureListener {
    private String mTag;
    private String mMessage;

    public TaskFailureLogger(@NonNull String tag, @NonNull String message) {
        mTag = tag;
        mMessage = message;
    }

    @Override
    public void onFailure(@NonNull Exception e) {
        Log.w(mTag, mMessage, e);
    }
}
