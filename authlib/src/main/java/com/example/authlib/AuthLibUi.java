package com.example.authlib;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.v4.app.FragmentActivity;

import com.example.authlib.provider.AuthProviderId;
import com.example.authlib.ui.FlowParameters;
import com.example.authlib.utils.GoogleSignInHelper;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by prakh on 02-12-2017.
 */

public class AuthLibUi {

    @StringDef({
            AuthProviderId.EMAIL_PROVIDER_ID, EMAIL_PROVIDER,
            AuthProviderId.GOOGLE_PROVIDER_ID, GOOGLE_PROVIDER,
            AuthProviderId.FACEBOOK_PROVIDER_ID, FACEBOOK_PROVIDER,
            AuthProviderId.SKIP_PROVIDER_ID, SKIP_PROVIDER
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface SupportedProviders {}

    public static final String EMAIL_PROVIDER = AuthProviderId.EMAIL_PROVIDER_ID;
    public static final String GOOGLE_PROVIDER = AuthProviderId.GOOGLE_PROVIDER_ID;
    public static final String FACEBOOK_PROVIDER = AuthProviderId.FACEBOOK_PROVIDER_ID;
    public static final String SKIP_PROVIDER = AuthProviderId.SKIP_PROVIDER_ID;

    public static final Set<String> SUPPORTED_PROVIDERS =
            Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
                    EMAIL_PROVIDER,
                    GOOGLE_PROVIDER,
                    FACEBOOK_PROVIDER,
                    SKIP_PROVIDER
            )));

    public static final IdentityHashMap<FirebaseApp, AuthLibUi> INSTANCES = new IdentityHashMap<>();

    private final FirebaseApp firebaseApp;
    private final FirebaseAuth firebaseAuth;

    private AuthLibUi(FirebaseApp app) {
        firebaseApp = app;
        firebaseAuth = FirebaseAuth.getInstance(firebaseApp);
        firebaseAuth.useAppLanguage();
    }

    public static AuthLibUi getInstance() {
        return getInstance(FirebaseApp.getInstance());
    }

    public static AuthLibUi getInstance(FirebaseApp app) {
        AuthLibUi authLibUi;
        synchronized (INSTANCES) {
            authLibUi = INSTANCES.get(app);
            if (authLibUi == null) {
                authLibUi = new AuthLibUi(app);
                INSTANCES.put(app, authLibUi);
            }
        }
        return authLibUi;
    }

    public Task<Void> signOut(@NonNull FragmentActivity activity) {
        // Get Credentials Helper
        GoogleSignInHelper signInHelper = GoogleSignInHelper.getInstance(activity);

        // Firebase Sign out
        firebaseAuth.signOut();

        // Disable credentials auto sign-in
        Task<Status> disableCredentialsTask = signInHelper.disableAutoSignIn();

        // Google sign out
        Task<Status> signOutTask = signInHelper.signOut();

        // Facebook sign out
//        try {
//            LoginManager.getInstance().logOut();
//        } catch (NoClassDefFoundError e) {
//            // do nothing
//        }

        // Wait for all tasks to complete
        return Tasks.whenAll(disableCredentialsTask, signOutTask);
    }

    public SignInIntentBuilder createSignInIntentBuilder() {
        return new SignInIntentBuilder();
    }

    public static class IdpConfig implements Parcelable {
        private final String mProviderId;
        private final List<String> mScopes;
        private final Bundle mParams;

        private IdpConfig(
                @SupportedProviders @NonNull String providerId,
                List<String> scopes,
                Bundle params) {
            mProviderId = providerId;
            mScopes = Collections.unmodifiableList(scopes);
            mParams = params;
        }

        private IdpConfig(Parcel in) {
            mProviderId = in.readString();
            mScopes = Collections.unmodifiableList(in.createStringArrayList());
            mParams = in.readBundle(getClass().getClassLoader());
        }

        @SupportedProviders
        public String getProviderId() {
            return mProviderId;
        }

        public List<String> getScopes() {
            return mScopes;
        }

        public Bundle getParams() {
            return mParams;
        }

        public static final Creator<IdpConfig> CREATOR = new Creator<IdpConfig>() {
            @Override
            public IdpConfig createFromParcel(Parcel in) {
                return new IdpConfig(in);
            }

            @Override
            public IdpConfig[] newArray(int size) {
                return new IdpConfig[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(mProviderId);
            parcel.writeStringList(mScopes);
            parcel.writeBundle(mParams);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            IdpConfig config = (IdpConfig) o;

            return mProviderId.equals(config.mProviderId);
        }

        @Override
        public int hashCode() {
            return mProviderId.hashCode();
        }

        @Override
        public String toString() {
            return "IdpConfig{" +
                    "mProviderId='" + mProviderId + '\'' +
                    ", mScopes=" + mScopes +
                    ", mParams=" + mParams +
                    '}';
        }

        public static class Builder {
            @SupportedProviders private String mProviderId;
            private List<String> mScopes = new ArrayList<>();
            private Bundle mParams = new Bundle();

            /**
             * Builds the configuration parameters for an identity provider.
             *
             * @param providerId An ID of one of the supported identity providers. e.g. {@link
             *                   AuthLibUi#GOOGLE_PROVIDER}. See {@link AuthLibUi#SUPPORTED_PROVIDERS} for
             *                   the complete list of supported Identity providers
             */
            public Builder(@SupportedProviders @NonNull String providerId) {
                if (!SUPPORTED_PROVIDERS.contains(providerId)) {
                    throw new IllegalArgumentException("Unkown provider: " + providerId);
                }
                mProviderId = providerId;
            }

            /**
             * Specifies the additional permissions that the application will request for this
             * identity provider.
             * <p>
             * For Facebook permissions see:
             * https://developers.facebook.com/docs/facebook-login/android
             * https://developers.facebook.com/docs/facebook-login/permissions
             * <p>
             * For Google permissions see:
             * https://developers.google.com/identity/protocols/googlescopes
             * <p>
             * Twitter permissions are only configurable through the
             * <a href="https://apps.twitter.com/">Twitter developer console</a>.
             */
            public Builder setPermissions(List<String> permissions) {
                mScopes = permissions;
                return this;
            }

            public Builder setParams(Bundle params) {
                mParams = params;
                return this;
            }

            public IdpConfig build() {
                return new IdpConfig(mProviderId, mScopes, mParams);
            }
        }
    }

    private abstract class AuthIntentBuilder<T extends AuthIntentBuilder> {
        List<IdpConfig> mProviders = new ArrayList<>();
        String mTosUrl;
        String mPrivacyPolicyUrl;
        boolean mEnableCredentials = true;
        boolean mEnableHints = true;

        private AuthIntentBuilder() {}


        /**
         * Specifies the terms-of-service URL for the application.
         */
        public T setTosUrl(@Nullable String tosUrl) {
            mTosUrl = tosUrl;
            return (T) this;
        }

        /**
         * Specifies the privacy policy URL for the application.
         */
        public T setPrivacyPolicyUrl(@Nullable String privacyPolicyUrl) {
            mPrivacyPolicyUrl = privacyPolicyUrl;
            return (T) this;
        }

        /**
         * Specified the set of supported authentication providers. At least one provider must be
         * specified. There may only be one instance of each provider.
         * <p>
         * <p>If no providers are explicitly specified by calling this method, then the email
         * provider is the default supported provider.
         *
         * @param idpConfigs a list of {@link IdpConfig}s, where each {@link IdpConfig} contains the
         *                   configuration parameters for the IDP.
         * @see IdpConfig
         */
        public T setAvailableProviders(@NonNull List<IdpConfig> idpConfigs) {
            mProviders.clear();

            for (IdpConfig config : idpConfigs) {
                if (mProviders.contains(config)) {
                    throw new IllegalArgumentException("Each provider can only be set once. "
                            + config.getProviderId()
                            + " was set twice.");
                } else {
                    mProviders.add(config);
                }

//                if (config.getProviderId().equals(FACEBOOK_PROVIDER)) {
//                    try {
//                        Class c = com.facebook.FacebookSdk.class;
//                    } catch (NoClassDefFoundError e) {
//                        throw new RuntimeException(
//                                "Facebook provider cannot be configured " +
//                                        "without dependency. Did you forget to add " +
//                                        "'com.facebook.android:facebook-android-sdk:VERSION' dependency?");
//                    }
//                }

//                if (config.getProviderId().equals(TWITTER_PROVIDER)) {
//                    try {
//                        Class c = com.twitter.sdk.android.core.TwitterCore.class;
//                    } catch (NoClassDefFoundError e) {
//                        throw new RuntimeException(
//                                "Twitter provider cannot be configured " +
//                                        "without dependency. Did you forget to add " +
//                                        "'com.twitter.sdk.android:twitter-core:VERSION' dependency?");
//                    }
//                }
            }

            return (T) this;
        }

        /**
         * Specified the set of supported authentication providers. At least one provider must be
         * specified. There may only be one instance of each provider.
         * <p>
         * <p>If no providers are explicitly specified by calling this method, then the email
         * provider is the default supported provider.
         *
         * @param idpConfigs a list of {@link IdpConfig}s, where each {@link IdpConfig} contains the
         *                   configuration parameters for the IDP.
         * @see IdpConfig
         * @deprecated because the order in which providers were displayed was the inverse of the
         * order in which they were supplied. Use {@link #setAvailableProviders(List)} to display
         * the providers in the order in which they were supplied.
         */
        @Deprecated
        public T setProviders(@NonNull List<IdpConfig> idpConfigs) {
            setAvailableProviders(idpConfigs);

            // Ensure email provider is at the bottom to keep backwards compatibility
            int emailProviderIndex = mProviders.indexOf(new IdpConfig.Builder(EMAIL_PROVIDER).build());
            if (emailProviderIndex != -1) {
                mProviders.add(0, mProviders.remove(emailProviderIndex));
            }
            Collections.reverse(mProviders);

            return (T) this;
        }

        /**
         * Enables or disables the use of Smart Lock for Passwords credential selector and hint
         * selector.
         * <p>
         * <p>Both selectors are enabled by default.
         *
         * @param enableHints       enable hint selector in respective signup screens
         */
        public T setIsSmartLockEnabled(boolean enableHints) {
            mEnableHints = enableHints;
            return (T) this;
        }

        @CallSuper
        public Intent build() {
            if (mProviders.isEmpty()) {
                mProviders.add(new IdpConfig.Builder(EMAIL_PROVIDER).build());
            }

            return KickoffActivity.createIntent(firebaseApp.getApplicationContext(),
                    getFlowParams());
        }

        protected abstract FlowParameters getFlowParams();
    }

    /**
     * Builder for the intent to start the user authentication flow.
     */
    public final class SignInIntentBuilder extends AuthIntentBuilder<SignInIntentBuilder> {
        private boolean mAllowNewEmailAccounts = true;

        private SignInIntentBuilder() {
            super();
        }

        /**
         * Enables or disables creating new accounts in the email sign in flow.
         * <p>
         * <p>Account creation is enabled by default.
         */
        public SignInIntentBuilder setAllowNewEmailAccounts(boolean enabled) {
            mAllowNewEmailAccounts = enabled;
            return this;
        }

        @Override
        protected FlowParameters getFlowParams() {
            return new FlowParameters(
                    firebaseApp.getName(),
                    mProviders,
                    mTosUrl,
                    mPrivacyPolicyUrl,
                    mEnableHints,
                    mAllowNewEmailAccounts);
        }
    }
}
