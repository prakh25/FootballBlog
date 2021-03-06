package com.example.corelib;

import android.os.Build;

import java.util.List;

/**
 * Created by prakh on 20-11-2017.
 */

public class Utils {

    public static String convertListToString(List<Integer> integers) {
        StringBuilder commaSepValueBuilder = new StringBuilder();
        //Looping through the list
        for ( int i = 0; i< integers.size(); i++){
            //append the value into the builder
            commaSepValueBuilder.append(integers.get(i));

            //if the value is not the last element of the list
            //then append the comma(,) as well
            if ( i != integers.size()-1){
                commaSepValueBuilder.append(", ");
            }
        }

        return commaSepValueBuilder.toString();
    }

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return model;
        } else {
            return manufacturer + " " + model;
        }
    }

    public static String getAndroidVersion() {
        return Build.VERSION.RELEASE + "";
    }
}
