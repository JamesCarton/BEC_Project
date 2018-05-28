package com.rainbowloveapp.app.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rainbowloveapp.app.MyApplication;
import com.rainbowloveapp.app.R;
import com.rainbowloveapp.app.adapter.CardCategoriesListAdapter;
import com.rainbowloveapp.app.database.DatabaseHandler;
import com.rainbowloveapp.app.model.CardLocalModel;
import com.rainbowloveapp.app.network.ApiClient;
import com.rainbowloveapp.app.network.ApiInterface;
import com.rainbowloveapp.app.utils.AnalyticsHelper;
import com.rainbowloveapp.app.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 *  on 10/5/17.
 */

public class CategoriesActivity extends AppCompatActivity {

    @BindView(R.id.img_categories)
    ImageView imgCategories;
    @BindView(R.id.header_img_right_1)
    ImageView headerImgRight1;
    @BindView(R.id.recycler_card_categories)
    RecyclerView recyclerCardCategories;

    DatabaseHandler databaseHandler;

    CardCategoriesListAdapter cardCategoriesListAdapter;

    Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.categories_activity);
        ButterKnife.bind(this);

        databaseHandler = new DatabaseHandler(this);
        databaseHandler.getWritableDatabase();

        headerImgRight1.setImageResource(R.drawable.btn_settings);

        Utils.loadImageGlide(this, R.drawable.bg_categories_view, imgCategories);

        recyclerCardCategories.setLayoutManager(new LinearLayoutManager(this));

        getCardCatogries();

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(GridViewActivity.From_Grid_BG_Change){
            GridViewActivity.From_Grid_BG_Change = false;
            onBackPressed();
        }

        MyApplication.getFirebaseAnalytics().setCurrentScreen(this, AnalyticsHelper.Screen_Categories_List, null);
    }

    @OnClick({R.id.header_frame_left_1, R.id.header_frame_right_1})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.header_frame_left_1:
                MainActivity.From_BG_Change = false;
                onBackPressed();
                break;
            case R.id.header_frame_right_1:
                startActivity(new Intent(this, SettingActivity.class));
                break;
        }
    }

    public void getCardCatogries(){

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        final ProgressDialog progressDialog = new ProgressDialog(this);
        Utils.showProgressDialog(progressDialog, getString(R.string.loading));

        Call<ResponseBody> call = apiService.getCardCategory();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    try {
                        String resultStr = response.body().string();
                        JSONObject jsonObject = new JSONObject(resultStr);

                        if(jsonObject.getString(ApiClient.STATUS).equalsIgnoreCase(ApiClient.SUCCESS)){

                            gson = new Gson();
                            String jsonOutput = Utils.cur2JsonArray(databaseHandler.getAllDataFromTable(DatabaseHandler.CARD_TABLE)).toString();
                            Type listType = new TypeToken<List<CardLocalModel>>(){}.getType();
                            List<CardLocalModel> lists = gson.fromJson(jsonOutput, listType);

                            List<String> catIDList = new ArrayList();

                            for (CardLocalModel model : lists){
                                catIDList.add(model.getCatId());
                            }

                            JSONArray jsonArray = jsonObject.getJSONArray(ApiClient.ROOT);

                            for(int i = 0; i < jsonArray.length(); i++){

                                JSONObject jObj = jsonArray.getJSONObject(i);
                                jObj = jObj.getJSONObject(ApiClient.RECORD);
                                //Log.v("name...","..i.." + i + "..." + jObj.getString(ApiClient.CATE_NAME));

                                String catID = jObj.getString(ApiClient.CAT_ID);
                                String cateName = jObj.getString(ApiClient.CATE_NAME);
                                String possion = String.valueOf(i);

                                CardLocalModel cardLocalModel = new CardLocalModel();
                                cardLocalModel.setCatId(catID);
                                cardLocalModel.setCardName(cateName);
                                cardLocalModel.setCatPossion(possion);

                                if(catIDList.contains(catID)){
                                    databaseHandler.updateCardTable(cardLocalModel);    // update query
                                    //Log.v("update..","..i.." + i + "..." );
                                }else{
                                    databaseHandler.insertCardTable(cardLocalModel);    // insert query
                                    //Log.v("insert..","..i.." + i + "..." );
                                }

                                catIDList.remove(catID);

                            }

                            if(!catIDList.isEmpty()){
                                for(int i = 0; i < catIDList.size(); i++){
                                    //Log.v("delete..","..i.." + i + "..." );
                                    databaseHandler.deleteFromCardTable(catIDList.get(i));
                                    databaseHandler.deleteFromCardImageTable(catIDList.get(i));
                                }
                            }

                            setUpAdapter();


                        }


                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Utils.dismissProgressDialog(progressDialog);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //Log.v("onFailure..","..onFailure.." +t.getMessage() );

                setUpAdapter();

                Utils.dismissProgressDialog(progressDialog);
            }
        });

    }

    public void setUpAdapter(){

        gson = new Gson();
        String jsonOutput = Utils.cur2JsonArray(databaseHandler.getAllDataFromTable(DatabaseHandler.CARD_TABLE)).toString();
        Type listType = new TypeToken<List<CardLocalModel>>(){}.getType();
        List<CardLocalModel> lists = gson.fromJson(jsonOutput, listType);

        Collections.sort(lists, new Comparator<CardLocalModel>() {
            @Override
            public int compare(CardLocalModel cardLocalModel, CardLocalModel t1) {
                return cardLocalModel.getCatPossion().compareTo(t1.getCatPossion());
            }
        });

        cardCategoriesListAdapter = new CardCategoriesListAdapter(this, lists);
        recyclerCardCategories.setAdapter(cardCategoriesListAdapter);

    }


}
