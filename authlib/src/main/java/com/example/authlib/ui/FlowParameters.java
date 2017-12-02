package com.example.authlib.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;

import com.example.authlib.AuthLibUi.IdpConfig;
import com.example.authlib.utils.Precondition;

import java.util.Collections;
import java.util.List;

/**
 * Created by prakh on 02-12-2017.
 */

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public class FlowParameters implements Parcelable {
    @NonNull
    public final String appName;

    @NonNull
    public final List<IdpConfig> providerInfo;

    @Nullable
    public final String termsOfServiceUrl;

    @Nullable
    public final String privacyPolicyUrl;

    public final boolean allowNewEmailAccounts;

    public final boolean enableHints;

    public FlowParameters(
            @NonNull String appName,
            @NonNull List<IdpConfig> providerInfo,
            @Nullable String termsOfServiceUrl,
            @Nullable String privacyPolicyUrl,
            boolean enableHints,
            boolean allowNewEmailAccounts) {
        this.appName = Precondition.checkNotNull(appName, "appName cannot be null");
        this.providerInfo = Collections.unmodifiableList(
                Precondition.checkNotNull(providerInfo, "providerInfo cannot be null"));

        this.termsOfServiceUrl = termsOfServiceUrl;
        this.privacyPolicyUrl = privacyPolicyUrl;

        this.enableHints = enableHints;
        this.allowNewEmailAccounts = allowNewEmailAccounts;
    }

    /**
     * Extract FlowParameters from an Intent.
     */
    public static FlowParameters fromIntent(Intent intent) {
        return intent.getParcelableExtra(ExtraConstants.EXTRA_FLOW_PARAMS);
    }

    /**
     * Extract FlowParameters from a Bundle.
     */
    public static FlowParameters fromBundle(Bundle bundle) {
        return bundle.getParcelable(ExtraConstants.EXTRA_FLOW_PARAMS);
    }

    /**
     * Create a bundle containing this FlowParameters object as {@link
     * ExtraConstants#EXTRA_FLOW_PARAMS}.
     */
    public Bundle toBundle() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(ExtraConstants.EXTRA_FLOW_PARAMS, this);
        return bundle;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(appName);
        dest.writeTypedList(providerInfo);
        dest.writeString(termsOfServiceUrl);
        dest.writeString(privacyPolicyUrl);
        dest.writeInt(enableHints ? 1 : 0);
        dest.writeInt(allowNewEmailAccounts ? 1 : 0);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FlowParameters> CREATOR = new Creator<FlowParameters>() {
        @Override
        public FlowParameters createFromParcel(Parcel in) {
            String appName = in.readString();
            List<IdpConfig> providerInfo = in.createTypedArrayList(IdpConfig.CREATOR);
            String termsOfServiceUrl = in.readString();
            String privacyPolicyUrl = in.readString();
            boolean enableHints = in.readInt() != 0;
            boolean allowNewEmailAccounts = in.readInt() != 0;

            return new FlowParameters(
                    appName,
                    providerInfo,
                    termsOfServiceUrl,
                    privacyPolicyUrl,
                    enableHints,
                    allowNewEmailAccounts);
        }

        @Override
        public FlowParameters[] newArray(int size) {
            return new FlowParameters[size];
        }
    };
}
