package com.wlc.beacon;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.wlc.beacon.utils.BeaconProximityUtils;

import java.util.List;
import java.util.UUID;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;


/**
 *  on 27/1/17.
 */

public class MyApplication extends MultiDexApplication {

    //public static BeaconUtils beaconUtils;
    public static BeaconProximityUtils beaconProximityUtils;

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseApp.initializeApp(this);

        //beaconUtils = new BeaconUtils(getApplicationContext());
        beaconProximityUtils = new BeaconProximityUtils(getApplicationContext());

        //Fabric.with(this, new Crashlytics());

        /*CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Quicksand_Book.otf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );*/


    }

    public static BeaconProximityUtils getBeaconUtils(){
        return  beaconProximityUtils;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


}
