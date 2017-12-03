package com.example.authlib;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;

import com.example.authlib.ui.ExtraConstants;

/**
 * Created by prakh on 02-12-2017.
 */

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public class User implements Parcelable {
    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(
                    in.readString(),
                    in.readString(),
                    in.readString(),
                    in.readString(),
                    in.readString(),
                    in.<Uri>readParcelable(Uri.class.getClassLoader()));
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    private final String mProviderId;
    private final String mEmail;
    private final String mUsername;
    private final String mPhoneNumber;
    private final String mName;
    private final Uri mPhotoUri;

    private User(String providerId, String email, String username,
                 String phoneNumber, String name, Uri photoUri) {
        mProviderId = providerId;
        mEmail = email;
        mUsername = username;
        mPhoneNumber = phoneNumber;
        mName = name;
        mPhotoUri = photoUri;
    }

    public static User getUser(Intent intent) {
        return intent.getParcelableExtra(ExtraConstants.EXTRA_USER);
    }

    public static User getUser(Bundle arguments) {
        return arguments.getParcelable(ExtraConstants.EXTRA_USER);
    }

    @NonNull
    @AuthLibUi.SupportedProviders
    public String getProviderId() {
        return mProviderId;
    }

    @Nullable
    public String getEmail() {
        return mEmail;
    }

    @Nullable
    public String getmUsername() {
        return mUsername;
    }

    @Nullable
    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    @Nullable
    public String getName() {
        return mName;
    }

    @Nullable
    public Uri getPhotoUri() {
        return mPhotoUri;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return mProviderId.equals(user.mProviderId)
                && (mEmail == null ? user.mEmail == null : mEmail.equals(user.mEmail))
                && (mName == null ? user.mName == null : mName.equals(user.mName))
                && (mPhotoUri == null ? user.mPhotoUri == null : mPhotoUri.equals(user.mPhotoUri));
    }

    @Override
    public int hashCode() {
        int result = mProviderId.hashCode();
        result = 31 * result + (mEmail == null ? 0 : mEmail.hashCode());
        result = 31 * result + (mName == null ? 0 : mName.hashCode());
        result = 31 * result + (mPhotoUri == null ? 0 : mPhotoUri.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "mProviderId='" + mProviderId + '\'' +
                ", mEmail='" + mEmail + '\'' +
                ", mName='" + mName + '\'' +
                ", mPhotoUri=" + mPhotoUri +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(mProviderId);
        dest.writeString(mEmail);
        dest.writeString(mPhoneNumber);
        dest.writeString(mName);
        dest.writeParcelable(mPhotoUri, flags);
    }

    public static class Builder {
        private String mProviderId;
        private String mEmail;
        private String mUsername;
        private String mPhoneNumber;
        private String mName;
        private Uri mPhotoUri;

        public Builder(@AuthLibUi.SupportedProviders @NonNull String providerId,
                       @Nullable String email) {
            mProviderId = providerId;
            mEmail = email;
        }

        public Builder setUsername(String username) {
            mUsername = username;
            return this;
        }

        public Builder setPhoneNumber(String phoneNumber) {
            mPhoneNumber = phoneNumber;
            return this;
        }

        public Builder setName(String name) {
            mName = name;
            return this;
        }

        public Builder setPhotoUri(Uri photoUri) {
            mPhotoUri = photoUri;
            return this;
        }

        public User build() {
            return new User(mProviderId, mEmail, mUsername, mPhoneNumber, mName,
                    mPhotoUri);
        }
    }
}