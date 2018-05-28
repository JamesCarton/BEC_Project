package com.wlc.beacon.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.wlc.beacon.BuildConfig;
import com.wlc.beacon.R;
import com.wlc.beacon.network.ApiClient;
import com.wlc.beacon.network.ApiInterface;
import com.wlc.beacon.utils.Utils;

import org.json.JSONObject;

import java.io.File;

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
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;
import retrofit2.Response;



@RuntimePermissions
public class SignupActivity extends BaseActivity {

    private static final int CAMERA_REQUEST = 100;


    @BindView(R.id.header_img_left_1)
    ImageView headerImgLeft1;
    @BindView(R.id.header_text_left_2)
    TextView headerTextLeft2;
    @BindView(R.id.txt_edit_name_signup)
    TextInputEditText txtEditNameSignup;
    @BindView(R.id.txt_edit_email_signup)
    TextInputEditText txtEditEmailSignup;
    @BindView(R.id.txt_edit_password_signup)
    TextInputEditText txtEditPasswordSignup;
    @BindView(R.id.txt_edit_conf_pass_signup)
    TextInputEditText txtEditConfPassSignup;
    @BindView(R.id.card_register_signup)
    CardView cardRegisterSignup;
    @BindView(R.id.img_profile_pic_signup)
    ImageView imgProfilePicSignup;

    String profilePicFilePath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);
        ButterKnife.bind(this);

        headerTextLeft2.setText(getString(R.string.signup));

        headerImgLeft1.setImageResource(R.drawable.left_arrow_ic);
        headerImgLeft1.setColorFilter(ContextCompat.getColor(this, R.color.blue_app));


    }


    @OnClick({R.id.header_frame_left_1, R.id.card_register_signup, R.id.img_profile_pic_signup})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.header_frame_left_1:
                onBackPressed();
                break;
            case R.id.card_register_signup:
                validateUserDetails();
                break;
            case R.id.img_profile_pic_signup:
                selectImage();
                break;
        }
    }

    public void validateUserDetails() {

        Utils.hideKeyboardFrom(this, cardRegisterSignup);

        if (txtEditNameSignup.getText().toString().isEmpty()) {
            txtEditNameSignup.setError(getString(R.string.pls_enter_filed));
        } else if (txtEditEmailSignup.getText().toString().isEmpty()) {
            txtEditEmailSignup.setError(getString(R.string.pls_enter_filed));
        } else if (!Utils.isEmailValid(txtEditEmailSignup.getText().toString())) {
            txtEditEmailSignup.setError(getString(R.string.pls_enter_filed));
        } else if (txtEditPasswordSignup.getText().toString().isEmpty()) {
            txtEditPasswordSignup.setError(getString(R.string.pls_enter_filed));
        } else if (txtEditConfPassSignup.getText().toString().isEmpty()) {
            txtEditConfPassSignup.setError(getString(R.string.pls_enter_filed));
        } else if (!txtEditPasswordSignup.getText().toString().equalsIgnoreCase(txtEditConfPassSignup.getText().toString())) {
            txtEditPasswordSignup.setError(getString(R.string.pass_not_match));
        } else {
            signupUserApi(txtEditNameSignup.getText().toString(), txtEditEmailSignup.getText().toString().trim(), txtEditPasswordSignup.getText().toString());
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

                    SignupActivityPermissionsDispatcher.showCameraWithPermissionCheck(SignupActivity.this);

                } else if (options[item].equals(getString(R.string.album))) {

                    CropImage.activity()
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .setFixAspectRatio(true)
                            .start(SignupActivity.this);

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
    public void showCamera(){

        profilePicFilePath = android.os.Environment.getExternalStorageDirectory() + File.separator + "img_temp.jpg";
        File f = new File(profilePicFilePath);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(SignupActivity.this, BuildConfig.APPLICATION_ID + ".provider",f));
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
                imgProfilePicSignup.setImageURI(resultUri);

                profilePicFilePath = Utils.getRealPathFromUri(this, resultUri);

                Log.v("profilePicFilePath","...." + profilePicFilePath + "...uri.." + resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }else if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            //beginCrop(Uri.fromFile(new File(img_path)));

            CropImage.activity(FileProvider.getUriForFile(SignupActivity.this, BuildConfig.APPLICATION_ID + ".provider",new File(profilePicFilePath)))
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setFixAspectRatio(true)
                    .start(SignupActivity.this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        SignupActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    public void signupUserApi(String name, String email, String password) {

        Log.v("signupUserApi","..name.." + name + "..emial.." + email);

        final ProgressDialog progressDialog = new ProgressDialog(this);
        Utils.showProgressDialog(progressDialog, getString(R.string.loading));

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        File imageFile = null;
        MultipartBody.Part bodyFile;
        if(profilePicFilePath != null && !profilePicFilePath.isEmpty()){
            imageFile = new File(profilePicFilePath);

            RequestBody reqFile = RequestBody.create(MediaType.parse("image"), imageFile);

            bodyFile = MultipartBody.Part.createFormData("user_avatar", "user_avatar.jpg", reqFile); //key,file name,file

        }else {
            bodyFile = null;
        }

        RequestBody nameBody = RequestBody.create(MediaType.parse("text/plain"), name);
        RequestBody emailBody = RequestBody.create(MediaType.parse("text/plain"), email);
        RequestBody passwordBody = RequestBody.create(MediaType.parse("text/plain"), password);

        apiService.signupUser(nameBody, emailBody, passwordBody, bodyFile)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<ResponseBody> responseBodyResponse) {

                        try {
                            if (responseBodyResponse.isSuccessful()) {

                                String response = responseBodyResponse.body().string();
                                Log.v("response","...." + response);
                                JSONObject jsonObject = new JSONObject(response);

                                String message = jsonObject.getString(ApiClient.MESSAGE);

                                showDialog(message);

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

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

    public void showDialog(final String msg) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this);
        builder.title(getString(R.string.app_name));
        builder.titleGravity(GravityEnum.CENTER);
        builder.content(msg);
        builder.cancelable(false);
        builder.positiveText(getString(R.string.ok));
        builder.positiveColor(Color.parseColor("#303F9F"));
        builder.onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(MaterialDialog dialog, DialogAction which) {
                if (msg.contains("successfully")) {
                    dialog.dismiss();
                    SignupActivity.this.finish();
                } else {
                    dialog.dismiss();
                }

            }
        });
        builder.show();
    }

}
