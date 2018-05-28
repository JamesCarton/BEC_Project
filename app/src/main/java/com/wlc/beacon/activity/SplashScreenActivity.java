package com.wlc.beacon.activity;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.wlc.beacon.R;
import com.wlc.beacon.service.MyService;
import com.wlc.beacon.utils.Utils;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.internal.Util;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;


public class SplashScreenActivity extends AppCompatActivity {

    @BindView(R.id.imageView)
    ImageView imageView;

    public static Activity splashActivity;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);
        ButterKnife.bind(this);

        splashActivity = this;

        setupLanguage();

        if (Utils.getStringPreference(this, Utils.IsFirstTime_key, "true").equals("true")) {

            Utils.setStringPreference(this, Utils.AccessToken_Key, "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjE4IiwiaWF0IjoxNTIwNDg4MDAwLCJleHAiOjE1MjA1NzQ0MDB9.9n-kdu0wQNj1eSP0loDWX8schH-YYw6a9080q-vvWeg");

            Utils.setStringPreference(this, Utils.IsFirstTime_key, "false");
        }

        Glide.with(this).load(R.drawable.splash_logo_animated).into(imageView);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(SplashScreenActivity.this, imageView, ViewCompat.getTransitionName(imageView));
                    if(Utils.isContainsPreference(SplashScreenActivity.this, Utils.UserDetail_Key)){
                        startActivity(new  Intent(SplashScreenActivity.this,MainActivity.class),options.toBundle());
                        finish();
                    }else {
                        startActivity(new  Intent(SplashScreenActivity.this,LoginActivity.class),options.toBundle());
                    }

                    //finish();
                }else {
                    if(Utils.isContainsPreference(SplashScreenActivity.this, Utils.UserDetail_Key)){
                        startActivity(new  Intent(SplashScreenActivity.this,MainActivity.class));
                        finish();
                    }else {
                        startActivity(new  Intent(SplashScreenActivity.this,LoginActivity.class));
                    }
                    //finish();
                }
            }
        }, 3500);

        startService(new Intent(this, MyService.class));


    }

    private void setupLanguage() {

        String selectedLanguage = Utils.getStringPreference(this, Utils.SelectedLang_Key, "en");

        String languageToLoad  = selectedLanguage; // your language en, es
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        //Utils.Language_Device = selectedLanguage; // en - English,  ar- Arabic
        //Log.d( "Language_Device", "..." + Utils.Language_Device );

        // en - English,  ar- Arabic, fr - French, es - Spanish
    }



}
