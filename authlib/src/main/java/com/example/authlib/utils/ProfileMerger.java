package com.example.authlib.utils;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.example.authlib.IdpResponse;
import com.example.authlib.User;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

/**
 * Created by prakh on 04-12-2017.
 */

public class ProfileMerger implements Continuation<AuthResult, Task<AuthResult>> {
    private static final String TAG = "ProfileMerger";

    private final IdpResponse mIdpResponse;

    public ProfileMerger(IdpResponse response) {
        mIdpResponse = response;
    }

    @Override
    public Task<AuthResult> then(@NonNull Task<AuthResult> task) throws Exception {
        final AuthResult authResult = task.getResult();
        FirebaseUser firebaseUser = authResult.getUser();

        String name = firebaseUser.getDisplayName();
        Uri photoUri = firebaseUser.getPhotoUrl();
        if (!TextUtils.isEmpty(name) && photoUri != null) {
            return Tasks.forResult(authResult);
        }

        User user = mIdpResponse.getUser();
        if (TextUtils.isEmpty(name)) { name = user.getName(); }
        if (photoUri == null) { photoUri = user.getPhotoUri(); }

        return firebaseUser.updateProfile(
                new UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .setPhotoUri(photoUri)
                        .build())
                .addOnFailureListener(new TaskFailureLogger(TAG, "Error updating profile"))
                .continueWithTask(task1 -> Tasks.forResult(authResult));
    }
}
