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

public class ArtEditingActivity extends AppCompatActivity {

    @BindView(R.id.img_art_editing)
    ImageView imgArtEditing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.art_editing_activity);
        ButterKnife.bind(this);

        Utils.loadImageGlide(this, R.drawable.quicktips_art_editing, imgArtEditing);

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    @OnClick({R.id.view_close_top_art_editing, R.id.view_close_bottom_art_editing})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.view_close_top_art_editing:
                onBackPressed();
                break;
            case R.id.view_close_bottom_art_editing:
                onBackPressed();
                break;
        }
    }
}
