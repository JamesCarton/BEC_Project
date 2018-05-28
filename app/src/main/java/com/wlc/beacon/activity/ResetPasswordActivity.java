package com.wlc.beacon.activity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wlc.beacon.R;
import com.wlc.beacon.model.PopularListModel;
import com.wlc.beacon.model.UserLoginModel;
import com.wlc.beacon.network.ApiClient;
import com.wlc.beacon.network.ApiInterface;
import com.wlc.beacon.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
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
 * Created by qae on 14/3/18.
 */

public class ResetPasswordActivity extends BaseActivity {


    @BindView(R.id.header_img_left_1)
    ImageView headerImgLeft1;
    @BindView(R.id.header_text_left_2)
    TextView headerTextLeft2;
    @BindView(R.id.edt_current_pass_reset)
    TextInputEditText edtCurrentPassReset;
    @BindView(R.id.edt_new_pass_reset)
    TextInputEditText edtNewPassReset;
    @BindView(R.id.edt_confirm_pass_reset)
    TextInputEditText edtConfirmPassReset;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_password_activity);
        ButterKnife.bind(this);

        headerTextLeft2.setText(getString(R.string.reset_password));

        headerImgLeft1.setImageResource(R.drawable.left_arrow_ic);
        headerImgLeft1.setColorFilter(ContextCompat.getColor(this, R.color.blue_app));

        //Utils.hideKeyboardFrom(this, edtCurrentPassReset);


    }


    @OnClick({R.id.header_frame_left_1, R.id.card_submit_reset})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.header_frame_left_1:
                onBackPressed();
                break;
            case R.id.card_submit_reset:
                validateResetPass();
                break;
        }
    }

    public void validateResetPass(){

        String currentPass = edtCurrentPassReset.getText().toString().trim();
        String newPass = edtNewPassReset.getText().toString().trim();
        String confirmPass = edtConfirmPassReset.getText().toString().trim();

        if(currentPass.isEmpty()){
            Utils.showDialog(this, getString(R.string.pls_enter_current_pass));
        }else if(newPass.isEmpty()){
            Utils.showDialog(this, getString(R.string.pls_enter_new_pass));
        }else if(confirmPass.isEmpty()){
            Utils.showDialog(this, getString(R.string.pls_enter_confirm_pass));
        }else if(!newPass.equals(confirmPass)){
            Utils.showDialog(this, getString(R.string.pass_not_match));
        }else{
            resetPassword(currentPass, newPass);
        }
    }

    public void resetPassword(String oldPass, String newPass) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        Utils.showProgressDialog(progressDialog, getString(R.string.loading));

        UserLoginModel userLoginModel = new Gson().fromJson(Utils.getStringPreference(this, Utils.UserDetail_Key, ""), UserLoginModel.class);

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        apiInterface.changePassword(userLoginModel.getRoot().get(0).getUserId(), oldPass, newPass, Utils.getStringPreference(this, Utils.AccessToken_Key, ""))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<ResponseBody> responseBodyResponse) {

                        if (responseBodyResponse.isSuccessful()) {

                            try {

                                String str = responseBodyResponse.body().string();

                                JSONObject jsonObject = new JSONObject(str);
                                if (jsonObject.getString(ApiClient.STATUS).equalsIgnoreCase(ApiClient.SUCCESS)) {

                                    MaterialDialog.Builder builder = new MaterialDialog.Builder(ResetPasswordActivity.this);
                                    builder.title(getString(R.string.app_name));
                                    builder.titleGravity(GravityEnum.CENTER);
                                    builder.content(jsonObject.getString(ApiClient.MESSAGE));
                                    builder.cancelable(false);
                                    builder.positiveText(getString(R.string.ok));
                                    builder.positiveColor(Color.parseColor("#303F9F"));
                                    builder.onPositive(new MaterialDialog.SingleButtonCallback() {
                                        @Override
                                        public void onClick(MaterialDialog dialog, DialogAction which) {
                                            dialog.dismiss();
                                            ResetPasswordActivity.this.finish();
                                        }
                                    });
                                    builder.show();

                                }else{
                                    Utils.showDialog(ResetPasswordActivity.this, jsonObject.getString(ApiClient.MESSAGE));
                                }


                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Utils.dismissProgressDialog(progressDialog);
                    }

                    @Override
                    public void onComplete() {
                        Utils.dismissProgressDialog(progressDialog);
                    }
                });

    }


}
