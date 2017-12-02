package com.example.authlib.provider;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.example.authlib.IdpResponse;
import com.example.authlib.utils.TaskFailureLogger;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.List;

/**
 * Created by prakh on 02-12-2017.
 */

public final class ProviderUtils {
    private static final String TAG = "ProviderUtils";

    private ProviderUtils() {
        throw new AssertionError("No instance for you!");
    }

    @Nullable
    public static AuthCredential getAuthCredential(IdpResponse idpResponse) {
        switch (idpResponse.getProviderType()) {
            case GoogleAuthProvider.PROVIDER_ID:
                return GoogleProvider.createAuthCredential(idpResponse);
            default:
                return null;
        }
    }

    public static Task<String> fetchTopProvider(FirebaseAuth auth, @NonNull String email) {
        if (TextUtils.isEmpty(email)) {
            return Tasks.forException(new NullPointerException("Email cannot be empty"));
        }

        return auth.fetchProvidersForEmail(email)
                .addOnFailureListener(
                        new TaskFailureLogger(TAG, "Error fetching providers for email"))
                .continueWith(task -> {
                    if (!task.isSuccessful()) return null;

                    List<String> providers = task.getResult().getProviders();
                    return providers == null || providers.isEmpty()
                            ? null : providers.get(providers.size() - 1);
                });
    }
}

