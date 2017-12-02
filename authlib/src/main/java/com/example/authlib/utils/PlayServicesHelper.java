package com.example.authlib.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;

import com.google.android.gms.common.GoogleApiAvailability;

/**
 * Created by prakh on 02-12-2017.
 */

public class PlayServicesHelper {
    private static GoogleApiAvailability mApiAvailability;

    public static GoogleApiAvailability getGoogleApiAvailability() {
        if (mApiAvailability == null) {
            mApiAvailability = GoogleApiAvailability.getInstance();
        }
        return mApiAvailability;
    }

    /**
     * @param activity       The Activity that will host necessary dialogs.
     * @param requestCode    A request code to be used to return results to the Activity.
     * @param cancelListener A Dialog listener if the user cancels the recommended action.
     * @return true if play services is available, false otherwise.
     */
    public static boolean makePlayServicesAvailable(Activity activity,
                                                    int requestCode,
                                                    DialogInterface.OnCancelListener cancelListener) {
        Dialog errorDialog = getGoogleApiAvailability().getErrorDialog(
                activity,
                getGoogleApiAvailability().isGooglePlayServicesAvailable(activity),
                requestCode,
                cancelListener);

        // The error dialog will be null if isGooglePlayServicesAvailable returned SUCCESS
        if (errorDialog == null) {
            return true;
        } else {
            errorDialog.show();
            return false;
        }
    }
}
