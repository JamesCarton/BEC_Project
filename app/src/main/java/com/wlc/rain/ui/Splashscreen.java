package com.rainbowloveapp.app.ui;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.ImageView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.rainbowloveapp.app.R;
import com.rainbowloveapp.app.database.DatabaseHandler;
import com.rainbowloveapp.app.model.ArtInAppLocalModel;
import com.rainbowloveapp.app.utils.Utils;
import com.localytics.android.Localytics;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *  on 19/5/17.
 */

public class Splashscreen extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 2000;

    @BindView(R.id.img_spalsh)
    ImageView imgSpalsh;

    Handler handler;

    Runnable runnable;

    DatabaseHandler databaseHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);
        ButterKnife.bind(this);

        databaseHandler = new DatabaseHandler(this);

        Utils.loadImageGlide(this, R.drawable.splash_img, imgSpalsh);

        handler = new Handler();

        runnable = new Runnable() {
            @Override
            public void run() {
                openMainActivity();
                handler.removeCallbacks(runnable);
            }
        };

        handler.postDelayed(runnable, SPLASH_DISPLAY_LENGTH);

        // Get token
        Log.v("FCM","..Token.." + FirebaseInstanceId.getInstance().getToken());


    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);

        Localytics.onNewIntent(this, intent);
    }


    public void openMainActivity() {

        Intent intent = null;

        if(Utils.getStringPreference(Splashscreen.this, Utils.IsFirstTime_key, "true").equalsIgnoreCase("true")){
            intent = new Intent(Splashscreen.this, RegisterActivity.class);

            // set filter InApp preference
            Utils.setStringPreference(Splashscreen.this, Utils.EclipseFilterInApp_key, Utils.Purchased);
            Utils.setStringPreference(Splashscreen.this, Utils.LiteFilterInApp_key, Utils.Purchased);
            Utils.setStringPreference(Splashscreen.this, Utils.RainbowFilterInApp_key, Utils.Purchased);
            Utils.setStringPreference(Splashscreen.this, Utils.MoonFilterInApp_key, "");
            Utils.setStringPreference(Splashscreen.this, Utils.DreamyFilterInApp_key, "");
            Utils.setStringPreference(Splashscreen.this, Utils.FloralFilterInApp_key, "");
            Utils.setStringPreference(Splashscreen.this, Utils.DuskFilterInApp_key, "");

            Utils.setStringPreference(Splashscreen.this, Utils.UnlockallFilter_key, "");
            Utils.setStringPreference(Splashscreen.this, Utils.UnlockallArt_key, "");

            Utils.setStringPreference(Splashscreen.this, Utils.AddLayerInApp_key, "");
            Utils.setStringPreference(Splashscreen.this, Utils.LogoInApp_key, "");

            // blank_transparent_id
            Utils.setStringPreference(Splashscreen.this, getResources().getStringArray(R.array.filter_inapp_id_array)[0], Utils.Purchased);


            // ------ add Art InAppTable 2 free -------
            ArtInAppLocalModel artInAppLocalModel_1 = new ArtInAppLocalModel();
            artInAppLocalModel_1.setCatId("40");

            ArtInAppLocalModel artInAppLocalModel_2 = new ArtInAppLocalModel();
            artInAppLocalModel_2.setCatId("77");

            databaseHandler.insertArtInAppTable(artInAppLocalModel_1);
            databaseHandler.insertArtInAppTable(artInAppLocalModel_2);

        }else{
            intent = new Intent(Splashscreen.this, HomeActivity.class);
        }

        startActivity(intent);
        Splashscreen.this.finish();

    }
}
