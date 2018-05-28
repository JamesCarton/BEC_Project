package com.rainbowloveapp.app;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.bumptech.glide.request.target.ViewTarget;
import com.crashlytics.android.Crashlytics;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.localytics.android.Localytics;

import io.fabric.sdk.android.Fabric;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 *  on 27/1/17.
 */

public class MyApplication extends Application {

    private static FirebaseAnalytics mFirebaseAnalytics;

    @Override
    public void onCreate() {
        super.onCreate();

        Fabric.with(this, new Crashlytics());

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Quicksand_Book.otf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        ViewTarget.setTagId(R.id.glide_tag);

        Localytics.autoIntegrate(this);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static FirebaseAnalytics getFirebaseAnalytics() {
        return mFirebaseAnalytics;
    }
}
