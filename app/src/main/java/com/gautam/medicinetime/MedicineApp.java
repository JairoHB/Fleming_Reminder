package com.gautam.medicinetime;

import android.app.Application;
import android.content.Context;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

/**
 * Created by gautam on 12/07/17.
 */

public class MedicineApp extends Application {

    private static Context mInstance;


    @Override
    public void onCreate() {
        super.onCreate();
        AppEventsLogger.activateApp(this);

        if (mInstance == null) {
            mInstance = getApplicationContext();
        }
    }

    public static Context getInstance() {
        return mInstance;
    }
}
