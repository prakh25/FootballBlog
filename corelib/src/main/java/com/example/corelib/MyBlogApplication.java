package com.example.corelib;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;

import com.facebook.stetho.Stetho;
import com.squareup.leakcanary.LeakCanary;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

import java.io.File;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by prakh on 16-11-2017.
 */

public class MyBlogApplication extends Application{

    private static MyBlogApplication app = null;
    private ConnectivityManager manager;

    @Override
    public void onCreate() {
        super.onCreate();
        if(LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);

        app = this;

        Realm.init(getApplicationContext());

        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .name("wordpress.realm")
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);

        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
                        .build());
    }

    public static MyBlogApplication getApp() {
        return app;
    }

    public static File getCache() {
        return app.getCacheDir();
    }

    public static boolean hasNetwork() {
        return app.checkNetwork();
    }

    public static boolean isWifiConnected() {
        return app.checkNetworkType();
    }

    private boolean checkNetwork() {
        manager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        return info != null && info.isConnected();
    }

    private boolean checkNetworkType() {
        manager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        return (info.getType() == ConnectivityManager.TYPE_WIFI);
    }

    public static SharedPreferences getCustomPreferences() {
        return app.getSharedPreferences("MAIN_PREF", MODE_PRIVATE);
    }

    public static SharedPreferences getDefaultPreference() {
        return PreferenceManager.getDefaultSharedPreferences(app);
    }
}
