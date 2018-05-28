package com.wlc.beacon.activity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.wlc.beacon.BuildConfig;
import com.wlc.beacon.R;
import com.wlc.beacon.model.UserLoginModel;
import com.wlc.beacon.network.ApiClient;
import com.wlc.beacon.network.ApiInterface;
import com.wlc.beacon.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;
import retrofit2.Response;

/**
 * Created by  on 14/3/18.
 */

@RuntimePermissions
public class EditProfileActivity extends BaseActivity {

    private static final int CAMERA_REQUEST = 100;


    @BindView(R.id.header_img_left_1)
    ImageView headerImgLeft1;
    @BindView(R.id.header_text_left_2)
    TextView headerTextLeft2;
    @BindView(R.id.img_profile_photo)
    ImageView imgProfilePhoto;

    String profilePicFilePath;
    @BindView(R.id.edt_name_editprofile)
    TextInputEditText edtNameEditprofile;
    @BindView(R.id.edt_email_editprofile)
    TextInputEditText edtEmailEditprofile;
    @BindView(R.id.edt_phone_editprofile)
    TextInputEditText edtPhoneEditprofile;
    @BindView(R.id.edt_gender_editprofile)
    EditText edtGenderEditprofile;
    @BindView(R.id.edt_birthdate_editprofile)
    TextInputEditText edtBirthdateEditprofile;

    String[] genderArray;

    int genderDefaultSelected = 0; // 0=Male 1=Female

    Calendar mDefaultDate;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile_activity);
        ButterKnife.bind(this);

        genderArray = new String[]{getString(R.string.male), getString(R.string.female)};

        headerTextLeft2.setText(getString(R.string.update_profile));

        headerImgLeft1.setImageResource(R.drawable.left_arrow_ic);
        headerImgLeft1.setColorFilter(ContextCompat.getColor(this, R.color.blue_app));

        mDefaultDate = Calendar.getInstance();

       /* findViewById(R.id.frame_gender_editprofile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("gender..",".directtt..click...");
                openGenderDialog();
            }
        });*/

        setView();

    }


    @OnClick({R.id.header_frame_left_1, R.id.card_update_editprofile, R.id.card_profile_photo, R.id.view_gender_editprofile,
            R.id.view_birthdate_editprofile, R.id.card_change_pass_editprofile})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.header_frame_left_1:
                onBackPressed();
                break;
            case R.id.card_update_editprofile:
                validateSaveProfile();
                break;
            case R.id.card_profile_photo:
                selectImage();
                break;
            case R.id.view_gender_editprofile:
                openGenderDialog();
                break;
            case R.id.view_birthdate_editprofile:
                openDatePicker();
                break;
            case R.id.card_change_pass_editprofile:
                startActivity(new Intent(this, ResetPasswordActivity.class));
                break;
        }
    }

    public void selectImage() {

        final CharSequence[] options = {getString(R.string.take_photo), getString(R.string.album), getString(R.string.cancel)};


        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(getString(R.string.select_photo));

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals(getString(R.string.take_photo))) {

                    EditProfileActivityPermissionsDispatcher.showCameraWithPermissionCheck(EditProfileActivity.this);

                } else if (options[item].equals(getString(R.string.album))) {

                    CropImage.activity()
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .setFixAspectRatio(true)
                            .start(EditProfileActivity.this);

                    //Crop.pickImage(AddGoal.this);
//                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                    startActivityForResult(intent, RESULT_LOAD_IMAGE);
                } else if (options[item].equals(getString(R.string.cancel))) {

                    dialog.dismiss();

                }

            }

        });

        builder.show();

    }

    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void showCamera() {

        profilePicFilePath = Environment.getExternalStorageDirectory() + File.separator + "img_temp.jpg";
        File f = new File(profilePicFilePath);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(EditProfileActivity.this, BuildConfig.APPLICATION_ID + ".provider", f));
        startActivityForResult(intent, CAMERA_REQUEST);

    }

    @OnPermissionDenied({Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void showDeniedForPermission() {
        Toast.makeText(this, getString(R.string.allow_permission), Toast.LENGTH_LONG).show();

        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                imgProfilePhoto.setImageURI(resultUri);

                profilePicFilePath = Utils.getRealPathFromUri(this, resultUri);

                Log.v("profilePicFilePath", "...." + profilePicFilePath + "...uri.." + resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        } else if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            //beginCrop(Uri.fromFile(new File(img_path)));

            CropImage.activity(FileProvider.getUriForFile(EditProfileActivity.this, BuildConfig.APPLICATION_ID + ".provider", new File(profilePicFilePath)))
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setFixAspectRatio(true)
                    .start(EditProfileActivity.this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        EditProfileActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    public void openGenderDialog() {

        new MaterialDialog.Builder(this)
                .title(R.string.select_gender)
                .items(genderArray)
                .itemsCallbackSingleChoice(genderDefaultSelected, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        genderDefaultSelected = which;
                        edtGenderEditprofile.setText(genderArray[which]);

                        /**
                         * If you use alwaysCallSingleChoiceCallback(), which is discussed below,
                         * returning false here won't allow the newly selected radio button to actually be selected.
                         **/
                        return true;
                    }
                })
                //.widgetColor(getResources().getColor(R.color.blue_app))
                .positiveText(R.string.choose)
                .show();

    }

    public void openDatePicker() {

        //Calendar mcurrentDate = Calendar.getInstance();
        int mYear = mDefaultDate.get(Calendar.YEAR);
        int mMonth = mDefaultDate.get(Calendar.MONTH);
        int mDay = mDefaultDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog pickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int selectedyear, int selectedmonth, int selectedday) {

                Calendar mselectedDate = Calendar.getInstance();
                mselectedDate.set(selectedyear, selectedmonth, selectedday);

                mDefaultDate = mselectedDate;

                Date date = new Date(mselectedDate.getTimeInMillis());

                edtBirthdateEditprofile.setText(Utils.convertDateToStringUS(date, getString(R.string.date_format_type_1)));

            }
        }, mYear, mMonth, mDay);

        //pickerDialog.getDatePicker().setMinDate(new Date().getTime());
        pickerDialog.show();

    }


    public void setView() {

        if (Utils.isContainsPreference(this, Utils.UserDetail_Key)) {

            UserLoginModel userLoginModel = new Gson().fromJson(Utils.getStringPreference(EditProfileActivity.this, Utils.UserDetail_Key, ""), UserLoginModel.class);

            UserLoginModel.Root root_tmp = userLoginModel.getRoot().get(0);

            if (!root_tmp.getUserAvatar().isEmpty()) {
                Utils.loadImageGlide(this, root_tmp.getUserAvatar(), imgProfilePhoto);
            } else {
                imgProfilePhoto.setImageResource(0);
            }

            edtNameEditprofile.setText(root_tmp.getName());
            edtNameEditprofile.setSelection(edtNameEditprofile.getText().length());
            edtEmailEditprofile.setText(root_tmp.getEmail());
            edtPhoneEditprofile.setText(root_tmp.getPhone());
            edtGenderEditprofile.setText(root_tmp.getGender());
            edtBirthdateEditprofile.setText(root_tmp.getBirthdate());

        }


    }

    public void validateSaveProfile(){

        String name = edtNameEditprofile.getText().toString().trim();
        String email = edtEmailEditprofile.getText().toString();
        String phone = edtPhoneEditprofile.getText().toString().trim();
        String gender = edtGenderEditprofile.getText().toString().trim();
        String birthdate = edtBirthdateEditprofile.getText().toString().trim();

        if(name.isEmpty()){
            Utils.showDialog(this, getString(R.string.pls_enter_name));
        }else if(profilePicFilePath != null && !profilePicFilePath.isEmpty()){
            // with photo
            updateProfileWithImage(name, email, phone, gender, birthdate);
        }else{
            // without photo
            updateProfileWithoutImage(name, email, phone, gender, birthdate);
        }

    }

    public void updateProfileWithoutImage(final String name, String email, final String phone, final String gender, final String birthdate){

        final ProgressDialog progressDialog = new ProgressDialog(this);
        Utils.showProgressDialog(progressDialog, getString(R.string.loading));

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        UserLoginModel userLoginModel = new Gson().fromJson(Utils.getStringPreference(this, Utils.UserDetail_Key, ""), UserLoginModel.class);

        apiInterface.profileUpdateWithoutImage(userLoginModel.getRoot().get(0).getUserId(), name, email, phone, gender, birthdate, Utils.getStringPreference(this, Utils.AccessToken_Key, ""))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<ResponseBody> responseBodyResponse) {
                        try {
                            if(responseBodyResponse.isSuccessful()){
                                String response = responseBodyResponse.body().string();
                                Log.v("response","..readPing.." + response);
                                JSONObject jsonObject = new JSONObject(response);

                                String status = jsonObject.getString(ApiClient.STATUS);
                                String message = jsonObject.getString(ApiClient.MESSAGE);
                                if(status.equalsIgnoreCase(ApiClient.SUCCESS)){

                                    MaterialDialog.Builder builder = new MaterialDialog.Builder(EditProfileActivity.this);
                                    builder.title(getString(R.string.app_name));
                                    builder.titleGravity(GravityEnum.CENTER);
                                    builder.content(message);
                                    builder.cancelable(false);
                                    builder.positiveText(getString(R.string.ok));
                                    builder.positiveColor(Color.parseColor("#303F9F"));
                                    builder.onPositive(new MaterialDialog.SingleButtonCallback() {
                                        @Override
                                        public void onClick(MaterialDialog dialog, DialogAction which) {
                                            saveUserModel(name, gender, phone, birthdate, "");
                                            dialog.dismiss();
                                            EditProfileActivity.this.finish();
                                        }
                                    });
                                    builder.show();

                                }else{
                                    Utils.showDialog(EditProfileActivity.this, message);
                                }



                            }
                        }catch (Exception e){
                            e.printStackTrace();
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

    public void updateProfileWithImage(final String name, String email, final String phone, final String gender, final String birthdate){

        final ProgressDialog progressDialog = new ProgressDialog(this);
        Utils.showProgressDialog(progressDialog, getString(R.string.loading));

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        File imageFile = null;
        MultipartBody.Part bodyFile;
        if(profilePicFilePath != null && !profilePicFilePath.isEmpty()){
            imageFile = new File(profilePicFilePath);

            RequestBody reqFile = RequestBody.create(MediaType.parse("image"), imageFile);

            bodyFile = MultipartBody.Part.createFormData("user_avatar", "user_avatar.jpg", reqFile); //key,file name,file

        }else {
            bodyFile = null;
        }

        UserLoginModel userLoginModel = new Gson().fromJson(Utils.getStringPreference(this, Utils.UserDetail_Key, ""), UserLoginModel.class);

        RequestBody userIdBody = RequestBody.create(MediaType.parse("text/plain"), userLoginModel.getRoot().get(0).getUserId());
        RequestBody nameBody = RequestBody.create(MediaType.parse("text/plain"), name);
        RequestBody emailBody = RequestBody.create(MediaType.parse("text/plain"), email);
        RequestBody phoneBody = RequestBody.create(MediaType.parse("text/plain"), phone);
        RequestBody genderBody = RequestBody.create(MediaType.parse("text/plain"), gender);
        RequestBody birthdateBody = RequestBody.create(MediaType.parse("text/plain"), birthdate);

        apiInterface.profileUpdateWithImage(userIdBody, nameBody, emailBody, phoneBody, genderBody, birthdateBody, Utils.getStringPreference(this, Utils.AccessToken_Key, ""), bodyFile)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<ResponseBody> responseBodyResponse) {
                        try {
                            if(responseBodyResponse.isSuccessful()){
                                String response = responseBodyResponse.body().string();
                                Log.v("response","..readPing.." + response);
                                JSONObject jsonObject = new JSONObject(response);

                                String status = jsonObject.getString(ApiClient.STATUS);
                                String message = jsonObject.getString(ApiClient.MESSAGE);
                                if(status.equalsIgnoreCase(ApiClient.SUCCESS)){

                                    JSONArray jsonArray = jsonObject.getJSONArray(ApiClient.ROOT);
                                    jsonObject = jsonArray.getJSONObject(0);
                                    final String user_avatar = jsonObject.getString(ApiClient.USER_AVATAR);

                                    MaterialDialog.Builder builder = new MaterialDialog.Builder(EditProfileActivity.this);
                                    builder.title(getString(R.string.app_name));
                                    builder.titleGravity(GravityEnum.CENTER);
                                    builder.content(message);
                                    builder.cancelable(false);
                                    builder.positiveText(getString(R.string.ok));
                                    builder.positiveColor(Color.parseColor("#303F9F"));
                                    builder.onPositive(new MaterialDialog.SingleButtonCallback() {
                                        @Override
                                        public void onClick(MaterialDialog dialog, DialogAction which) {
                                            saveUserModel(name, gender, phone, birthdate, user_avatar);
                                            dialog.dismiss();
                                            EditProfileActivity.this.finish();
                                        }
                                    });
                                    builder.show();

                                }else{
                                    Utils.showDialog(EditProfileActivity.this, message);
                                }



                            }
                        }catch (Exception e){
                            e.printStackTrace();
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

    public void saveUserModel(String name, String gender, String phone, String birthdate, String image){

        UserLoginModel userLogin = new Gson().fromJson(Utils.getStringPreference(getApplicationContext(), Utils.UserDetail_Key, ""), UserLoginModel.class);
        UserLoginModel.Root root = userLogin.getRoot().get(0);
        root.setName(name);
        root.setGender(gender);
        root.setPhone(phone);
        root.setBirthdate(birthdate);
        if(!image.isEmpty()){
            root.setUserAvatar(image);
        }

        Gson gson = new Gson();
        String userDetail = gson.toJson(userLogin);

        Utils.setStringPreference(getApplicationContext(), Utils.UserDetail_Key, userDetail);

    }

}
