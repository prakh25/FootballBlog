package com.example.corelib.model.splash.notification;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by prakh on 18-12-2017.
 */

public class DeviceInfo implements Serializable {
    @Expose
    public String regid;
    @Expose
    public String serial;
    @Expose
    public String device_name;
    @Expose
    public String os_version;
}
