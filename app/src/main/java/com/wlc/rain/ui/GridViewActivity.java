package com.rainbowloveapp.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rainbowloveapp.app.MyApplication;
import com.rainbowloveapp.app.R;
import com.rainbowloveapp.app.adapter.GridListAdapter;
import com.rainbowloveapp.app.database.DatabaseHandler;
import com.rainbowloveapp.app.model.CardImageLocalModel;
import com.rainbowloveapp.app.network.ApiClient;
import com.rainbowloveapp.app.network.ApiInterface;
import com.rainbowloveapp.app.network.DownloadImage;
import com.rainbowloveapp.app.utils.AnalyticsHelper;
import com.rainbowloveapp.app.utils.LetterSpacingTextView;
import com.rainbowloveapp.app.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *  on 13/5/17.
 */

public class GridViewActivity extends AppCompatActivity {

    @BindView(R.id.header_img_right_1)
    ImageView headerImgRight1;
    @BindView(R.id.tv_title_grid)
    LetterSpacingTextView tvTitleGrid;
    @BindView(R.id.recyclerview_grid)
    RecyclerView recyclerviewGrid;

    GridListAdapter gridListAdapter;

    DatabaseHandler databaseHandler;

    Gson gson;

    public static boolean From_Grid_BG_Change;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gridview_activity);
        ButterKnife.bind(this);

        databaseHandler = new DatabaseHandler(this);
        databaseHandler.getWritableDatabase();

        From_Grid_BG_Change = false;

        headerImgRight1.setImageResource(R.drawable.btn_settings);

        recyclerviewGrid.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerviewGrid.setHasFixedSize(true);
        recyclerviewGrid.setItemViewCacheSize(20);
        recyclerviewGrid.setDrawingCacheEnabled(true);

        if(getIntent().getExtras().containsKey(Utils.CardID_Key)){
            String cardID = getIntent().getExtras().getString(Utils.CardID_Key);
            String cardCategory = getIntent().getExtras().getString(Utils.CardCategory_Key);

            tvTitleGrid.setText(cardCategory);
            getCardCatogriesImages(cardID);
        }



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.getFirebaseAnalytics().setCurrentScreen(this, AnalyticsHelper.Screen_Categories_Card, null);
    }

    @OnClick({R.id.header_frame_left_1, R.id.header_frame_right_1})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.header_frame_left_1:
                onBackPressed();
                break;
            case R.id.header_frame_right_1:
                startActivity(new Intent(this, SettingActivity.class));
                break;
        }
    }


    public void getCardCatogriesImages(final String catID){

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<ResponseBody> call = apiService.getCardImages(catID);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    try {
                        String resultStr = response.body().string();
                        JSONObject jsonObject = new JSONObject(resultStr);
                        //Log.v("jsonObject...","..jsonObject.." + jsonObject);

                        if(jsonObject.getString(ApiClient.STATUS).equalsIgnoreCase(ApiClient.SUCCESS)){

                            gson = new Gson();
                            String jsonOutput = Utils.cur2JsonArray(databaseHandler.getCardImage(catID)).toString();
                            Type listType = new TypeToken<List<CardImageLocalModel>>(){}.getType();
                            List<CardImageLocalModel> lists = gson.fromJson(jsonOutput, listType);

                            List<String> imageIDList = new ArrayList();

                            for (CardImageLocalModel model : lists){
                                imageIDList.add(model.getImageId());
                            }

                            JSONArray jsonArray = jsonObject.getJSONArray(ApiClient.ROOT);

                            for(int i = 0; i < jsonArray.length(); i++){

                                JSONObject jObj = jsonArray.getJSONObject(i);
                                jObj = jObj.getJSONObject(ApiClient.RECORD);

                                String cat_ID = jObj.getString(ApiClient.CAT_ID);
                                String imageID = jObj.getString(ApiClient.IMAGE_ID);
                                String imageURL = jObj.getString(ApiClient.IMAGE);

                                CardImageLocalModel cardImageLocalModel = new CardImageLocalModel();
                                cardImageLocalModel.setCatId(cat_ID);
                                cardImageLocalModel.setCardImage(imageURL);
                                cardImageLocalModel.setStatus("0");
                                cardImageLocalModel.setImageId(imageID);
                                cardImageLocalModel.setImageUrl(imageURL);

                                if(imageIDList.contains(imageID)){
                                    imageIDList.remove(imageID);
                                }else{
                                    databaseHandler.insertCardImageTable(cardImageLocalModel);    // insert query
                                    //Log.v("insert..","..i.." + i + "..." );
                                }

                            }

                            if(!imageIDList.isEmpty()){
                                for(int i = 0; i < imageIDList.size(); i++){
                                    //Log.v("delete..","..i.." + i + "..." );
                                    databaseHandler.deleteFromCardImageTableOnImageId(imageIDList.get(i));
                                }
                            }

                            setUpAdapter(catID);

                        }


                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                setUpAdapter(catID);
            }
        });

    }

    public void setUpAdapter(String cat_id){

        gson = new Gson();
        String jsonOutput = Utils.cur2JsonArray(databaseHandler.getCardImage(cat_id)).toString();
        Type listType = new TypeToken<List<CardImageLocalModel>>(){}.getType();
        List<CardImageLocalModel> lists = gson.fromJson(jsonOutput, listType);

        gridListAdapter = new GridListAdapter(this, lists);
        recyclerviewGrid.setAdapter(gridListAdapter);

        //DownloadImage.cardCategoriesImage(getApplicationContext());

        new Thread(new Runnable() {
            @Override
            public void run() {
                DownloadImage.cardCategoriesImage(getApplicationContext());
            }
        }).start();

    }



}
