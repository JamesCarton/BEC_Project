package com.rainbowloveapp.app.ui;

import android.content.Context;
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

public class ShortcutsActivity extends AppCompatActivity {


    @BindView(R.id.img_shortcuts)
    ImageView imgShortcuts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shortcuts_activity);
        ButterKnife.bind(this);

        Utils.loadImageGlide(this, R.drawable.quicktips_shortcuts, imgShortcuts);

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    @OnClick({R.id.view_close_top_shortcuts, R.id.view_close_bottom_shortcuts})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.view_close_top_shortcuts:
                onBackPressed();
                break;
            case R.id.view_close_bottom_shortcuts:
                onBackPressed();
                break;
        }
    }
}
