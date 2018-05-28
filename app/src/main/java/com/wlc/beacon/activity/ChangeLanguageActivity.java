package com.wlc.beacon.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.wlc.beacon.R;
import com.wlc.beacon.adapter.InterestAdapter;
import com.wlc.beacon.model.InterestModel;
import com.wlc.beacon.model.UserLoginModel;
import com.wlc.beacon.network.ApiClient;
import com.wlc.beacon.network.ApiInterface;
import com.wlc.beacon.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;



public class ChangeLanguageActivity extends BaseActivity {

    @BindView(R.id.header_img_left_1)
    ImageView headerImgLeft1;
    @BindView(R.id.header_text_left_2)
    TextView headerTextLeft2;
    @BindView(R.id.radiogroup_language_preference)
    RadioGroup radiogroup_language_preference;

    @BindView(R.id.rb_english_preference)
    RadioButton rb_english_preference;
    @BindView(R.id.rb_spanish_preference)
    RadioButton rb_spanish_preference;

    String language_preference;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_language_layout);
        ButterKnife.bind(this);

        headerTextLeft2.setText(R.string.save_preference);

        headerImgLeft1.setImageResource(R.drawable.left_arrow_ic);
        headerImgLeft1.setColorFilter(ContextCompat.getColor(this, R.color.blue_app));


        if(Utils.getStringPreference(this, Utils.SelectedLang_Key, "en").equalsIgnoreCase("en")){
            rb_english_preference.setChecked(true);
        }else{
            rb_spanish_preference.setChecked(true);
        }


    }


    @OnClick({R.id.header_frame_left_1})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.header_frame_left_1:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        saveLanguage();

        super.onBackPressed();
    }

    public void saveLanguage(){

        String savedLan = Utils.getStringPreference(this, Utils.SelectedLang_Key, "en");

        if(rb_english_preference.isChecked() && !savedLan.equalsIgnoreCase("en")){
            setupLanguage("en");
        }else if(rb_spanish_preference.isChecked() && !savedLan.equalsIgnoreCase("es")){
            setupLanguage("es");
        }

        //onBackPressed();
    }

    private void setupLanguage(String selectedLanguage) {

        Utils.setStringPreference(this, Utils.SelectedLang_Key, selectedLanguage);

        //String selectedLanguage = Utils.getStringPreference(this, Utils.SelectedLang_Key, "en");

        String languageToLoad  = selectedLanguage; // your language en, es
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());


        Intent intent = new Intent(this, SplashScreenActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

        //Utils.Language_Device = selectedLanguage; // en - English,  ar- Arabic
        //Log.d( "Language_Device", "..." + Utils.Language_Device );

        // en - English,  ar- Arabic, fr - French, es - Spanish
    }

}
