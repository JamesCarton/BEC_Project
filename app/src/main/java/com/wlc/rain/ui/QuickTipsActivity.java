package com.rainbowloveapp.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.rainbowloveapp.app.R;
import com.rainbowloveapp.app.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;




public class QuickTipsActivity extends AppCompatActivity {

    @BindView(R.id.img_quicktips)
    ImageView imgQuicktips;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quicktips_activity);
        ButterKnife.bind(this);


        Utils.loadImageGlide(this, R.drawable.quicktips_mainmenu, imgQuicktips);
        //Glide.with(this).load(R.drawable.quicktips_mainmenu).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imgquicktips);

    }


    @OnClick({R.id.view_close_quicktips, R.id.view_shortcuts_quicktips, R.id.view_arttextedit_quicktips, R.id.view_filters_quicktips, R.id.view_visitblog_quicktips})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.view_close_quicktips:
                onBackPressed();
                break;
            case R.id.view_shortcuts_quicktips:
                startActivity(new Intent(this, ShortcutsActivity.class));
                break;
            case R.id.view_arttextedit_quicktips:
                startActivity(new Intent(this, ArtEditingActivity.class));
                break;
            case R.id.view_filters_quicktips:
                startActivity(new Intent(this, FiltersTipsActivity.class));
                break;
            case R.id.view_visitblog_quicktips:
                startActivity(new Intent(this, WebActivity.class).putExtra(Utils.WebActivity_Key, Utils.DesignTips));
                break;
        }
    }
}
