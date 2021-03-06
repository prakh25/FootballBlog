package com.example.authlib;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;

import com.example.authlib.ui.ExtraConstants;


/**
 * Created by prakh on 02-12-2017.
 */

public class IdpResponse implements Parcelable {
    private final User mUser;
    private final String mToken;
    private final String mSecret;

    private final int mErrorCode;

    private IdpResponse(int errorCode) {
        this(null, null, null, errorCode);
    }

    private IdpResponse(User user, String token, String secret, int errorCode) {
        mUser = user;
        mToken = token;
        mSecret = secret;
        mErrorCode = errorCode;
    }

    /**
     * Extract the {@link IdpResponse} from the flow's result intent.
     *
     * @param resultIntent The intent which {@code onActivityResult} was called with.
     * @return The IdpResponse containing the token(s) from signing in with the Idp
     */
    @Nullable
    public static IdpResponse fromResultIntent(Intent resultIntent) {
        if (resultIntent != null) {
            return resultIntent.getParcelableExtra(ExtraConstants.EXTRA_IDP_RESPONSE);
        } else {
            return null;
        }
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    public static Intent getErrorCodeIntent(int errorCode) {
        return new IdpResponse(errorCode).toIntent();
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    public Intent toIntent() {
        return new Intent().putExtra(ExtraConstants.EXTRA_IDP_RESPONSE, this);
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    public User getUser() {
        return mUser;
    }

    /**
     * Get the type of provider. e.g. {@link AuthLibUi#GOOGLE_PROVIDER}
     */
    @NonNull
    @AuthLibUi.SupportedProviders
    public String getProviderType() {
        return mUser.getProviderId();
    }

    /**
     * Get the email used to sign in.
     */
    @Nullable
    public String getEmail() {
        return mUser.getEmail();
    }

    /**
     * Get the phone number used to sign in.
     */
    @Nullable
    public String getPhoneNumber() {
        return mUser.getPhoneNumber();
    }

    /**
     * Get the token received as a result of logging in with the specified IDP
     */
    @Nullable
    public String getIdpToken() {
        return mToken;
    }

    /**
     * Twitter only. Return the token secret received as a result of logging in with Twitter.
     */
    @Nullable
    public String getIdpSecret() {
        return mSecret;
    }

    /**
     * Get the error code for a failed sign in
     */
    public int getErrorCode() {
        return mErrorCode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(mUser, flags);
        dest.writeString(mToken);
        dest.writeString(mSecret);
        dest.writeInt(mErrorCode);
    }

    public static final Creator<IdpResponse> CREATOR = new Creator<IdpResponse>() {
        @Override
        public IdpResponse createFromParcel(Parcel in) {
            return new IdpResponse(
                    in.<User>readParcelable(User.class.getClassLoader()),
                    in.readString(),
                    in.readString(),
                    in.readInt()
            );
        }

        @Override
        public IdpResponse[] newArray(int size) {
            return new IdpResponse[size];
        }
    };

    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    public static class Builder {
        private User mUser;
        private String mToken;
        private String mSecret;

        public Builder(@NonNull User user) {
            mUser = user;
        }

        public Builder setToken(String token) {
            mToken = token;
            return this;
        }

        public Builder setSecret(String secret) {
            mSecret = secret;
            return this;
        }

        public IdpResponse build() {
//            String providerId = mUser.getProviderId();
//            if ((providerId.equalsIgnoreCase(AuthLibUi.GOOGLE_PROVIDER)
//                    || providerId.equalsIgnoreCase(AuthLibUi.FACEBOOK_PROVIDER))
//                    && TextUtils.isEmpty(mToken)) {
//                throw new IllegalStateException(
//                        "Token cannot be null when using a non-email provider.");
//            }
//            if (providerId.equalsIgnoreCase(TwitterAuthProvider.PROVIDER_ID)
//                    && TextUtils.isEmpty(mSecret)) {
//                throw new IllegalStateException(
//                        "Secret cannot be null when using the Twitter provider.");
//            }

            return new IdpResponse(mUser, mToken, mSecret, Activity.RESULT_OK);
        }
    }
}

