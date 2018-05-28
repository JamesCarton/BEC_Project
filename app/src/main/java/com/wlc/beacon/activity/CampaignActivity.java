package com.wlc.beacon.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wlc.beacon.R;
import com.wlc.beacon.adapter.MyTagCampaignAdapter;
import com.wlc.beacon.adapter.SearchAdapter;
import com.wlc.beacon.fragment.SearchFragment;
import com.wlc.beacon.model.CampaignModel;
import com.wlc.beacon.model.PopularListModel;
import com.wlc.beacon.utils.Utils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;



public class CampaignActivity extends AppCompatActivity {

    @BindView(R.id.header_img_left_1)
    ImageView headerImgLeft1;
    @BindView(R.id.header_text_left_2)
    TextView headerTextLeft2;

    RecyclerView recyclerview_campaign;

    MyTagCampaignAdapter myTagCampaignAdapter;

    List<CampaignModel.Root> tmpCampaignRootList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.campaign_activity);
        ButterKnife.bind(this);

        if (getIntent().getExtras() != null) {
            if (getIntent().getExtras().containsKey(Utils.CampaignRootList_Key)) {
                String str = getIntent().getExtras().getString(Utils.CampaignTitle_Key);

                String rootList = getIntent().getExtras().getString(Utils.CampaignRootList_Key);

                headerTextLeft2.setText(str);

                Type listType = new TypeToken<List<CampaignModel.Root>>() {}.getType();
                tmpCampaignRootList = new ArrayList<>();
                tmpCampaignRootList = new Gson().fromJson(rootList, listType);

            }
        }


        headerImgLeft1.setImageResource(R.drawable.left_arrow_ic);
        headerImgLeft1.setColorFilter(ContextCompat.getColor(this, R.color.blue_app));

        recyclerview_campaign = findViewById(R.id.recyclerview_campaign);
        recyclerview_campaign.setLayoutManager(new LinearLayoutManager(this));
        myTagCampaignAdapter = new MyTagCampaignAdapter(this, tmpCampaignRootList);
        recyclerview_campaign.setAdapter(myTagCampaignAdapter);

    }

    @OnClick(R.id.header_frame_left_1)
    public void onViewClicked() {
        onBackPressed();
    }
}
