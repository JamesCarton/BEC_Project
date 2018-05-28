package com.rainbowloveapp.app.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.rainbowloveapp.app.R;
import com.rainbowloveapp.app.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 *  on 10/5/17.
 */

public class FiltersTipsActivity extends AppCompatActivity {


    @BindView(R.id.img_fiterstips)
    ImageView imgFiterstips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filters_tips_activity);
        ButterKnife.bind(this);

        Utils.loadImageGlide(this, R.drawable.quicktips_filters, imgFiterstips);

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    @OnClick({R.id.view_close_top_fiterstips, R.id.view_close_bottom_fiterstips, R.id.view_full_guide_fiterstips})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.view_close_top_fiterstips:
                onBackPressed();
                break;
            case R.id.view_close_bottom_fiterstips:
                onBackPressed();
                break;
            case R.id.view_full_guide_fiterstips:
                startActivity(new Intent(this, WebActivity.class).putExtra(Utils.WebActivity_Key, Utils.FilterTips));
                break;
        }
    }
}
