package com.wlc.beacon.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Created by tgbh on 14/3/18.
 */

public class SavePreferenceActivity extends BaseActivity {

    @BindView(R.id.header_img_left_1)
    ImageView headerImgLeft1;
    @BindView(R.id.header_text_left_2)
    TextView headerTextLeft2;
    @BindView(R.id.radiogroup_gender_preference)
    RadioGroup radiogroupGenderPreference;
    @BindView(R.id.radiogroup_age_preference)
    RadioGroup radiogroupAgePreference;
    @BindView(R.id.rb_male_preference)
    RadioButton rbMalePreference;
    @BindView(R.id.rb_female_preference)
    RadioButton rbFemalePreference;
    @BindView(R.id.rb_18_preference)
    RadioButton rb18Preference;
    @BindView(R.id.rb_25_preference)
    RadioButton rb25Preference;
    @BindView(R.id.rb_35_preference)
    RadioButton rb35Preference;
    @BindView(R.id.rb_45_preference)
    RadioButton rb45Preference;
    @BindView(R.id.rb_55_preference)
    RadioButton rb55Preference;
    @BindView(R.id.recycler_interest)
    RecyclerView recyclerInterest;


    String gender_preference;
    String age_preference;

    List<String> interest_preference;

    List<InterestModel.Root> interestRootList;

    InterestAdapter interestAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.save_preference_layout);
        ButterKnife.bind(this);

        headerTextLeft2.setText(R.string.save_preference);

        headerImgLeft1.setImageResource(R.drawable.left_arrow_ic);
        headerImgLeft1.setColorFilter(ContextCompat.getColor(this, R.color.blue_app));

        interestRootList = new ArrayList<>();
        recyclerInterest.setLayoutManager(new LinearLayoutManager(this));
        interestAdapter = new InterestAdapter(this, interestRootList);
        recyclerInterest.setAdapter(interestAdapter);

        getInterestList();

        setDefaultPreference();


        radiogroupGenderPreference.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (rbMalePreference.isChecked()) {
                    gender_preference = "Male";
                } else {
                    gender_preference = "Female";
                }
            }
        });

        radiogroupAgePreference.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (rb18Preference.isChecked()) {
                    age_preference = "18-24";
                } else if (rb25Preference.isChecked()) {
                    age_preference = "25-34";
                } else if (rb35Preference.isChecked()) {
                    age_preference = "35-44";
                } else if (rb45Preference.isChecked()) {
                    age_preference = "45-54";
                } else if (rb55Preference.isChecked()) {
                    age_preference = "55 or more";
                }
            }
        });


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

        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < interestRootList.size(); i++){
            if(interestRootList.get(i).isChecked()) {
                //Log.v("onBackPressed", "..interest_preference..i = " + interestRootList.get(i).getCategoryId() + "..name.." + interestRootList.get(i).getCategoryName());
                stringBuilder.append(interestRootList.get(i).getCategoryId());
                stringBuilder.append(",");
            }
        }
        String interest_str = stringBuilder.toString();
        if(interest_str.isEmpty()){
            interest_str = "";
        }else{
            interest_str = interest_str.substring(0, interest_str.length()-1);
        }
        //Log.v("onBackPressed", "..gender_preference.." + gender_preference + "..age_preference.." + age_preference + "..stringBuilder.." + interest_str);

        savePreference(gender_preference, age_preference, interest_str);

        super.onBackPressed();
    }

    public void setDefaultPreference() {

        interest_preference = new ArrayList<>();

        UserLoginModel userLoginModel = new Gson().fromJson(Utils.getStringPreference(this, Utils.UserDetail_Key, ""), UserLoginModel.class);

        UserLoginModel.Root root = userLoginModel.getRoot().get(0);

        if (root.getGenderPreference().equalsIgnoreCase("male")) {
            rbMalePreference.setChecked(true);
            gender_preference = "Male";
        } else {
            rbFemalePreference.setChecked(true);
            gender_preference = "Female";
        }

        if (root.getAgePreference().contains("18")) {
            rb18Preference.setChecked(true);
            age_preference = "18-24";
        } else if (root.getAgePreference().contains("25")) {
            rb25Preference.setChecked(true);
            age_preference = "25-34";
        } else if (root.getAgePreference().contains("35")) {
            rb35Preference.setChecked(true);
            age_preference = "35-44";
        } else if (root.getAgePreference().contains("45")) {
            rb45Preference.setChecked(true);
            age_preference = "45-54";
        } else if (root.getAgePreference().contains("55")) {
            rb55Preference.setChecked(true);
            age_preference = "55 or more";
        }


    }


    public void getInterestList() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        Utils.showProgressDialog(progressDialog, getString(R.string.loading));

        UserLoginModel userLoginModel = new Gson().fromJson(Utils.getStringPreference(this, Utils.UserDetail_Key, ""), UserLoginModel.class);

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        apiInterface.interestList(userLoginModel.getRoot().get(0).getUserId(), Utils.getStringPreference(this, Utils.AccessToken_Key, ""))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<InterestModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<InterestModel> interestModelResponse) {

                        interestRootList.clear();

                        if(interestModelResponse.isSuccessful()){
                            if(interestModelResponse.body().getStatus().equalsIgnoreCase(ApiClient.SUCCESS)){
                                interestRootList.addAll(interestModelResponse.body().getRoot());
                            }
                        }

                        setInterest();

                    }

                    @Override
                    public void onError(Throwable e) {
                        Utils.dismissProgressDialog(progressDialog);
                    }

                    @Override
                    public void onComplete() {
                        Utils.dismissProgressDialog(progressDialog);
                    }
                });

    }

    public void setInterest(){

        UserLoginModel userLoginModel = new Gson().fromJson(Utils.getStringPreference(SavePreferenceActivity.this, Utils.UserDetail_Key, ""), UserLoginModel.class);

        UserLoginModel.Root root_tmp = userLoginModel.getRoot().get(0);

        String str = root_tmp.getInterestPreference();
        List<String> items_interest = new ArrayList<>();
        if(!str.isEmpty()){
            items_interest = Arrays.asList(str.split("\\s*,\\s*"));
        }

        for (InterestModel.Root root : interestRootList){
            if(items_interest.contains(String.valueOf(root.getCategoryId()))){
                root.setChecked(true);
            }
        }

        interestAdapter.notifyDataSetChanged();

    }


    public void savePreference(final String gender, final String age, final String interest) {

        //final ProgressDialog progressDialog = new ProgressDialog(this);
        //Utils.showProgressDialog(progressDialog, getString(R.string.loading));

        UserLoginModel userLoginModel = new Gson().fromJson(Utils.getStringPreference(this, Utils.UserDetail_Key, ""), UserLoginModel.class);

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        apiInterface.savePreference(userLoginModel.getRoot().get(0).getUserId(), gender, age, interest, Utils.getStringPreference(this, Utils.AccessToken_Key, ""))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<ResponseBody> responseBodyResponse) {

                        if(responseBodyResponse.isSuccessful()){

                            try{

                            String str = responseBodyResponse.body().string();
                            JSONObject jsonObject = new JSONObject(str);

                                Log.v("save", ".._preference. " + jsonObject);

                            if(jsonObject.getString(ApiClient.STATUS).equalsIgnoreCase(ApiClient.SUCCESS)){
                                JSONArray jsonArray = jsonObject.getJSONArray(ApiClient.ROOT);
                                jsonObject = jsonArray.getJSONObject(0);

                                UserLoginModel userLogin = new Gson().fromJson(Utils.getStringPreference(getApplicationContext(), Utils.UserDetail_Key, ""), UserLoginModel.class);
                                UserLoginModel.Root root = userLogin.getRoot().get(0);
                                root.setGenderPreference(gender);
                                root.setAgePreference(age);
                                root.setInterestPreference(interest);

                                Gson gson = new Gson();
                                String userDetail = gson.toJson(userLogin);

                                Utils.setStringPreference(getApplicationContext(), Utils.UserDetail_Key, userDetail);
                            }

                            }catch (Exception e){
                                e.printStackTrace();
                            }

                        }



                    }

                    @Override
                    public void onError(Throwable e) {
                        //Utils.dismissProgressDialog(progressDialog);
                    }

                    @Override
                    public void onComplete() {
                        //Utils.dismissProgressDialog(progressDialog);
                    }
                });

    }

}
