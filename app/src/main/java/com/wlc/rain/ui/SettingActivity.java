package com.rainbowloveapp.app.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.rainbowloveapp.app.MyApplication;
import com.rainbowloveapp.app.R;
import com.rainbowloveapp.app.network.ApiClient;
import com.rainbowloveapp.app.utils.AnalyticsHelper;
import com.rainbowloveapp.app.utils.Utils;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 *  on 10/5/17.
 */

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView img_setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_activity);

        findViewById(R.id.header_frame_left_1).setOnClickListener(this);

        img_setting = (ImageView) findViewById(R.id.img_setting);

        Utils.loadImageGlide(this, R.drawable.bg_categories_view, img_setting);

        findViewById(R.id.tv_mainmenu).setOnClickListener(this);
        findViewById(R.id.tv_design_tip).setOnClickListener(this);
        findViewById(R.id.tv_faq).setOnClickListener(this);
        findViewById(R.id.tv_tech).setOnClickListener(this);
        findViewById(R.id.tv_edit_of_week).setOnClickListener(this);
        findViewById(R.id.tv_share_app).setOnClickListener(this);
        findViewById(R.id.tv_review).setOnClickListener(this);
        findViewById(R.id.tv_follow_on_insta).setOnClickListener(this);
        findViewById(R.id.tv_privacy_policy).setOnClickListener(this);
        findViewById(R.id.tv_terms_of_service).setOnClickListener(this);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.getFirebaseAnalytics().setCurrentScreen(this, AnalyticsHelper.Screen_Settings, null);
    }

    @Override
    public void onClick(View view) {
        if(view == findViewById(R.id.header_frame_left_1)){
            onBackPressed();
        }else if(view == findViewById(R.id.tv_mainmenu)){

        }else if(view == findViewById(R.id.tv_design_tip)){
            startActivity(new Intent(this, WebActivity.class).putExtra(Utils.WebActivity_Key, Utils.DesignTips));
            MyApplication.getFirebaseAnalytics().setCurrentScreen(this, AnalyticsHelper.Screen_More_Content, null);
        }else if(view == findViewById(R.id.tv_faq)){
            startActivity(new Intent(this, WebActivity.class).putExtra(Utils.WebActivity_Key, Utils.FAQ));
            MyApplication.getFirebaseAnalytics().setCurrentScreen(this, AnalyticsHelper.Screen_More_Content, null);
        }else if(view == findViewById(R.id.tv_tech)){
            composeEmail(getResources().getStringArray(R.array.rainbowlove_email), "Rainbow Love Android", ApiClient.EmailContent);
            MyApplication.getFirebaseAnalytics().setCurrentScreen(this, AnalyticsHelper.Screen_More_Content, null);
        }else if(view == findViewById(R.id.tv_edit_of_week)){
            startActivity(new Intent(this, WebActivity.class).putExtra(Utils.WebActivity_Key, Utils.EditOfWeek));
            MyApplication.getFirebaseAnalytics().setCurrentScreen(this, AnalyticsHelper.Screen_More_Content, null);
        }else if(view == findViewById(R.id.tv_share_app)){
            //String msg = getString(R.string.share_msg) + " http://play.google.com/store/apps/details?id=" + getPackageName();
            String msg = "Here’s an app I use for my photo edits.   Try Rainbow Love App for colorful and fun photo editing!  \n" +
                    "Now available for Android! \n" +
                    "\n" +
                    "iTunes:  https://itunes.apple.com/us/app/rainbowlove-greetings/id922406189?mt=8\n" +
                    "\n" +
                    "Play Store:  http://play.google.com/store/apps/details?id=com.rainbowloveapp.app\n" +
                    "\n" +
                    "Need some inspo?  Visit Rainbow Love App on Instagram: https://www.instagram.com/rainbowloveapp/";
            shareMessage(msg);
            MyApplication.getFirebaseAnalytics().setCurrentScreen(this, AnalyticsHelper.Screen_More_Content, null);
        }else if(view == findViewById(R.id.tv_review)){
            Utils.openAppOnPlayStore(this);
            MyApplication.getFirebaseAnalytics().setCurrentScreen(this, AnalyticsHelper.Screen_More_Content, null);
        }else if(view == findViewById(R.id.tv_follow_on_insta)){
            startActivity(new Intent(this, WebActivity.class).putExtra(Utils.WebActivity_Key, Utils.FollowInsta));
            MyApplication.getFirebaseAnalytics().setCurrentScreen(this, AnalyticsHelper.Screen_More_Content, null);
        }else if(view == findViewById(R.id.tv_privacy_policy)){
            startActivity(new Intent(this, WebActivity.class).putExtra(Utils.WebActivity_Key, Utils.PrivacyPolicy));
            MyApplication.getFirebaseAnalytics().setCurrentScreen(this, AnalyticsHelper.Screen_More_Content, null);
        }else if(view == findViewById(R.id.tv_terms_of_service)){
            startActivity(new Intent(this, WebActivity.class).putExtra(Utils.WebActivity_Key, Utils.TermService));
            MyApplication.getFirebaseAnalytics().setCurrentScreen(this, AnalyticsHelper.Screen_More_Content, null);
        }
    }



    public void composeEmail(String[] addresses, String subject, String content) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        //intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.setType("text/html");
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(content));
        /*intent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(new StringBuilder()
                .append("<p><b>Some Content</b></p>")
                .append("<img src='http://luvisallaroundme.com/admin/images/techsupport-logo.png'/>")
                .toString()));*/

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, getString(R.string.no_suitable_app_found), Toast.LENGTH_LONG).show();
        }
    }

    public void shareMessage(String message) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Get Creative, Colorful and Happy!");
        intent.putExtra(Intent.EXTRA_TEXT, message);
        //intent.putExtra(Intent.EXTRA_STREAM, attachment);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, getString(R.string.no_suitable_app_found), Toast.LENGTH_LONG).show();
        }
    }


}
