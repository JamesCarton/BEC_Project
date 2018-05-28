package com.rainbowloveapp.app.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.rainbowloveapp.app.MyApplication;
import com.rainbowloveapp.app.R;
import com.rainbowloveapp.app.database.DatabaseHandler;
import com.rainbowloveapp.app.model.LoginLocalModel;
import com.rainbowloveapp.app.model.ProfileLocalModel;
import com.rainbowloveapp.app.network.ApiClient;
import com.rainbowloveapp.app.network.ApiInterface;
import com.rainbowloveapp.app.utils.AnalyticsHelper;
import com.rainbowloveapp.app.utils.LetterSpacingTextView;
import com.rainbowloveapp.app.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 *  on 10/5/17.
 */

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private ScrollView scroll_register;
    private ImageView img_register;
    private LetterSpacingTextView tv_email;
    private EditText txt_email;
    private LetterSpacingTextView tv_username;
    private EditText txt_username;
    private LetterSpacingTextView tv_register;
    private LetterSpacingTextView tv_skip;
    private LetterSpacingTextView tv_privacy_statement;

    DatabaseHandler databaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        databaseHandler = new DatabaseHandler(this);
        databaseHandler.getWritableDatabase();

        setUpViews();

        /*findViewById(R.id.tv_email).setOnClickListener(this);
        findViewById(R.id.tv_design_tip).setOnClickListener(this);
        findViewById(R.id.tv_faq).setOnClickListener(this);
        findViewById(R.id.tv_tech).setOnClickListener(this);
        findViewById(R.id.tv_edit_of_week).setOnClickListener(this);
        findViewById(R.id.tv_share_app).setOnClickListener(this);
        findViewById(R.id.tv_review).setOnClickListener(this);
        findViewById(R.id.tv_follow_on_insta).setOnClickListener(this);
        findViewById(R.id.tv_privacy_policy).setOnClickListener(this);
        findViewById(R.id.tv_terms_of_service).setOnClickListener(this);*/
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.getFirebaseAnalytics().setCurrentScreen(this, AnalyticsHelper.Screen_Register, null);
    }

    @Override
    public void onClick(View view) {
        if(view == tv_register){
            String email = txt_email.getText().toString();
            String name = txt_username.getText().toString();

            if(!Utils.isEmailValid(email)){
                Utils.showDialog(this, getString(R.string.email_not_valid));
            }else if(name.isEmpty()){
                Utils.showDialog(this, getString(R.string.pls_ent_name));
            }else{
                registerUser(name, email);
            }
        }else if(view == tv_skip){

            LoginLocalModel loginLocalModel = new LoginLocalModel();

            loginLocalModel.setUserId("0");
            loginLocalModel.setUserName("");
            loginLocalModel.setUserPass("");
            loginLocalModel.setLogType("");


            ProfileLocalModel profileLocalModel = new ProfileLocalModel();

            profileLocalModel.setUserId("0");
            profileLocalModel.setUserFirstname("");
            profileLocalModel.setUserLastname("");
            profileLocalModel.setUserEmail("");
            profileLocalModel.setUserDob("");
            profileLocalModel.setPurchaseArt("0");
            profileLocalModel.setPurchaseLogo("0");
            profileLocalModel.setPurchaseAddlayer("0");
            profileLocalModel.setPurchaseHd("0");

            databaseHandler.insertLoginTable(loginLocalModel);
            databaseHandler.insertProfileTable(profileLocalModel);

            openHomeActivity();

        }else if(view == tv_privacy_statement){
            startActivity(new Intent(this, WebActivity.class).putExtra(Utils.WebActivity_Key, Utils.PrivacyPolicy));
        }else if(view == findViewById(R.id.tv_term_of_use)){
            startActivity(new Intent(this, WebActivity.class).putExtra(Utils.WebActivity_Key, Utils.TermService));
        }
    }



    private void setUpViews() {
        scroll_register = (ScrollView) findViewById(R.id.scroll_register);
        img_register = (ImageView) findViewById( R.id.img_register );
        tv_email = (LetterSpacingTextView)findViewById( R.id.tv_email );
        txt_email = (EditText)findViewById( R.id.txt_email );
        tv_username = (LetterSpacingTextView)findViewById( R.id.tv_username );
        txt_username = (EditText)findViewById( R.id.txt_username );
        tv_register = (LetterSpacingTextView)findViewById( R.id.tv_register );
        tv_skip = (LetterSpacingTextView)findViewById( R.id.tv_skip );
        tv_privacy_statement = (LetterSpacingTextView)findViewById( R.id.tv_privacy_statement );

        tv_register.setOnClickListener(this);
        tv_skip.setOnClickListener(this);
        tv_privacy_statement.setOnClickListener(this);

        findViewById(R.id.tv_term_of_use).setOnClickListener(this);

        /*int deviceWidth = Utils.getScreenWidth(this);
        int height = deviceWidth - Utils.getSizefromDp(this, 24); // total - statusBar*/

        //Glide.with(this).load(R.drawable.bg_register).fitCenter().into(img_register);
        Utils.loadImageGlide(this, R.drawable.bg_register, img_register);


        txt_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() > 0){
                    //txt_email.setText(Utils.applyKerning(charSequence, 0));
                    tv_email.setText(charSequence);
                }else{
                    tv_email.setText(getString(R.string.email));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
               /* if(editable.length() > 0){
                    txt_email.setText(Utils.applyKerning(editable, 1));
                }*/
            }
        });

        txt_username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() > 0){
                    //Utils.applyKerning(charSequence, 0);
                    tv_username.setText(charSequence);
                }else{
                    tv_username.setText(getString(R.string.user_name));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        txt_email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){

                }else{
                    //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
                }
            }
        });

        txt_username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){

                }else{
                    //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
                }
            }
        });
    }

    public void registerUser(String name, String email){

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        final ProgressDialog progressDialog = new ProgressDialog(this);
        Utils.showProgressDialog(progressDialog, getString(R.string.loading));

        Call<ResponseBody> call = apiService.registerUser(name, email, Utils.DeviceType);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    try {
                        String resultStr = response.body().string();
                        JSONObject jsonObject = new JSONObject(resultStr);

                        //Log.v("register...","..jsonObject.." + jsonObject);

                        if(jsonObject.getString(ApiClient.STATUS).equalsIgnoreCase(ApiClient.SUCCESS)){

                            JSONArray jsonArray = jsonObject.getJSONArray(ApiClient.ROOT);

                            JSONObject jObj = jsonArray.getJSONObject(0);

                            jObj = jObj.getJSONObject(ApiClient.RECORD);

                            LoginLocalModel loginLocalModel = new LoginLocalModel();

                            loginLocalModel.setUserId(jObj.getString(ApiClient.USER_ID));
                            loginLocalModel.setUserName(jObj.getString(ApiClient.NAME));
                            loginLocalModel.setUserPass("");
                            loginLocalModel.setLogType("");


                            ProfileLocalModel profileLocalModel = new ProfileLocalModel();

                            profileLocalModel.setUserId(jObj.getString(ApiClient.USER_ID));
                            profileLocalModel.setUserFirstname(jObj.getString(ApiClient.NAME));
                            profileLocalModel.setUserLastname("");
                            profileLocalModel.setUserEmail(jObj.getString(ApiClient.EMAIL));
                            profileLocalModel.setUserDob(jObj.getString(ApiClient.BIRTHDATE));
                            profileLocalModel.setPurchaseArt("0");
                            profileLocalModel.setPurchaseLogo("0");
                            profileLocalModel.setPurchaseAddlayer("0");
                            profileLocalModel.setPurchaseHd("0");

                            databaseHandler.insertLoginTable(loginLocalModel);
                            databaseHandler.insertProfileTable(profileLocalModel);

                            openHomeActivity();

                        }

                    }catch (Exception e){
                        e.printStackTrace();
                        //Log.v("Exception...","...." + e.getMessage());

                    }

                    Utils.dismissProgressDialog(progressDialog);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Utils.dismissProgressDialog(progressDialog);
                //Log.v("onFailure...","...." + t.getMessage());
            }
        });
    }

    public void openHomeActivity(){
        Utils.setStringPreference(this, Utils.IsFirstTime_key, "false");
        startActivity(new Intent(this, HomeActivity.class));
        this.finish();
    }


}
