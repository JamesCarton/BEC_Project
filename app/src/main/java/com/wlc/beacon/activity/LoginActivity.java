package com.wlc.beacon.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.CardView;
import android.transition.ChangeBounds;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.estimote.mustard.rx_goodness.rx_requirements_wizard.Requirement;
import com.estimote.mustard.rx_goodness.rx_requirements_wizard.RequirementsWizardFactory;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.wlc.beacon.MyApplication;
import com.wlc.beacon.R;
import com.wlc.beacon.model.UserLoginModel;
import com.wlc.beacon.network.ApiClient;
import com.wlc.beacon.network.ApiInterface;
import com.wlc.beacon.utils.Utils;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Created by wde on 15/3/18.
 */

public class LoginActivity extends BaseActivity {

    private static final int RC_SIGN_IN = 9001;


    @BindView(R.id.img_icon_login)
    ImageView imgIconLogin;
    @BindView(R.id.txt_username)
    TextInputEditText txtUsername;
    @BindView(R.id.txt_password)
    TextInputEditText txtPassword;
    @BindView(R.id.txt_username_login)
    TextInputLayout txtUsernameLogin;
    @BindView(R.id.txt_password_login)
    TextInputLayout txtPasswordLogin;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.img_facebook_signin)
    ImageView imgFacebookSignin;
    @BindView(R.id.img_gplus_signin)
    ImageView imgGplusSignin;

    private GoogleSignInClient mGoogleSignInClient;

    CallbackManager callbackManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            getWindow().setSharedElementEnterTransition(new ChangeBounds().setDuration(1000));
        }
        setContentView(R.layout.login_activity);
        ButterKnife.bind(this);

        Glide.with(this).load(R.drawable.splash_logo_animated).into(imgIconLogin);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                imgIconLogin.setImageResource(0);
                Glide.with(LoginActivity.this).load(R.drawable.splash_logo_animated).into(imgIconLogin);
                SplashScreenActivity.splashActivity.finish();
            }
        }, 2000);//


        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        callbackManager = CallbackManager.Factory.create();

        checkBeaconPermission();


        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        // -------------------------------



    }

    @OnClick({R.id.btn_login, R.id.tv_create_acc_login, R.id.img_facebook_signin, R.id.img_gplus_signin, R.id.tv_forgot_pass_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login:

                validateEmailPassword();

                break;
            case R.id.tv_create_acc_login:
                startActivity(new Intent(this, SignupActivity.class));
                break;
            case R.id.img_facebook_signin:
                facebookLogin();
                break;
            case R.id.img_gplus_signin:
                Glide.with(this).load(R.drawable.aaa).into(imgGplusSignin);
                googleSignIn();
                break;
            case R.id.tv_forgot_pass_login:
                forgotPasswordDialog();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition();
        } else {
            finish();
        }*/

    }

    public void validateEmailPassword() {

        Utils.hideKeyboardFrom(this, btnLogin);

        if (txtUsername.getText().toString().isEmpty()) {
            txtUsername.setError(getString(R.string.pls_enter_filed));
        } else if (txtPassword.getText().toString().isEmpty()) {
            txtPassword.setError(getString(R.string.pls_enter_filed));
        } else if (!Utils.isEmailValid(txtUsername.getText().toString())) {
            txtUsername.setError(getString(R.string.enter_valid_email));
        } else {
            loginUserApi(txtUsername.getText().toString(), txtPassword.getText().toString());
        }

    }

    public void loginUserApi(String email, String password) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        Utils.showProgressDialog(progressDialog, getString(R.string.loading));

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        apiService.loginUser(email, password, Utils.getStringPreference(this, Utils.AccessToken_Key, ""))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<UserLoginModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<UserLoginModel> userLoginModelResponse) {
                        Log.v("response..", "..code.." + userLoginModelResponse.code());
                        if (userLoginModelResponse.isSuccessful()) {
                            Log.v("response..", "..success.." + userLoginModelResponse.body().getMessage());

                            if (userLoginModelResponse.body().getStatus().equalsIgnoreCase(ApiClient.SUCCESS)) {

                                onLoginSuccess(userLoginModelResponse.body());

                            } else {
                                Utils.showDialog(LoginActivity.this, userLoginModelResponse.body().getMessage());
                            }
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        Utils.dismissProgressDialog(progressDialog);
                        e.printStackTrace();
                        Log.v("response..", "..onError.." + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Utils.dismissProgressDialog(progressDialog);
                    }
                });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                handleGoogleSignInResult(task);
            } catch (Exception e) {
                // Google Sign In failed, update UI appropriately
                Log.w("TAG", "Google sign in failed", e);
                Glide.with(this).load(R.drawable.gplus_ic).into(imgGplusSignin);
            }
        }

        // for Facebook
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    private void handleGoogleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount acct = completedTask.getResult(ApiException.class);

            if (acct != null) {
                String personName = acct.getDisplayName();
                String personGivenName = acct.getGivenName();
                String personFamilyName = acct.getFamilyName();
                String personEmail = acct.getEmail();
                String personId = acct.getId();
                Uri personPhoto = acct.getPhotoUrl();

                String photoURL = "";
                if(personPhoto != null){
                    photoURL = personPhoto.toString();
                }
                socialMediaLoginUserApi(personEmail, personName, personId, Utils.SocialTypeGmail, photoURL);

                //Log.w("TAG", "signInResult:success = " + personName + "..." + personEmail + "..." + personId + "..." + personPhoto.toString());
            }




        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("TAG", "signInResult:failed code=" + e.getStatusCode());
            Glide.with(this).load(R.drawable.gplus_ic).into(imgGplusSignin);
            //updateUI(null);
        }
    }

    private void googleSignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void socialMediaLoginUserApi(String email, String name, String socialID, String socialType, String user_avatar) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        Utils.showProgressDialog(progressDialog, getString(R.string.loading));

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        apiService.socialMediaLoginUser(email, name, socialID, socialType, user_avatar)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<UserLoginModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<UserLoginModel> userLoginModelResponse) {
                        Log.v("response..", "..code.." + userLoginModelResponse.code() + userLoginModelResponse.isSuccessful());
                        if (userLoginModelResponse.isSuccessful()) {
                            Log.v("response..", "..success.." + userLoginModelResponse.body().getMessage());

                            if (userLoginModelResponse.body().getStatus().equalsIgnoreCase(ApiClient.SUCCESS)) {

                                onLoginSuccess(userLoginModelResponse.body());

                            } else {
                                Utils.showDialog(LoginActivity.this, userLoginModelResponse.body().getMessage());
                            }
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        Utils.dismissProgressDialog(progressDialog);
                        e.printStackTrace();
                        Log.v("response..", "..onError.." + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Utils.dismissProgressDialog(progressDialog);
                    }
                });

    }

    public void facebookLogin() {

        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "public_profile"));

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                // Insert your code here

                                if (response.getError() != null) {
                                    // handle error
                                    System.out.println("ERROR");

                                } else {

                                    try {
                                        String jsonresult = String.valueOf(object);
                                        System.out.println("JSON Result" + jsonresult);

                                        String str_email_fb = object.getString("email");
                                        String str_id_fb = object.getString("id");
                                        String str_firstname_fb = object.getString("first_name");
                                        String str_lastname_fb = object.getString("last_name");
                                        String str_picture_fb = object.getJSONObject("picture").getJSONObject("data").getString("url");

                                        String displayName = str_firstname_fb + " " + str_lastname_fb;


                                        socialMediaLoginUserApi(str_email_fb, displayName.trim(), str_id_fb, Utils.SocialTypeFacebook, str_picture_fb);

                                        //Log.v("fb..","..." + str_email_fb + ".." + str_id_fb + ".." + str_firstname_fb + ".." + str_lastname_fb + str_picture_fb);


                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }
                            }
                        });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,first_name,last_name,link,email,hometown,location,picture.type(large)"); // picture OR picture.type(large)
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

    }


    public void forgotPasswordDialog() {

        View view = getLayoutInflater().inflate(R.layout.forgot_password_layout, null);
        final Dialog dialog_forgot_password = new Dialog(this);
        int width = (int) (Utils.getScreenWidth(this) * 0.8);
        dialog_forgot_password.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_forgot_password.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog_forgot_password.setContentView(view);
        dialog_forgot_password.getWindow().setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog_forgot_password.setCancelable(true);

        final EditText txt_email_forgot_pass = view.findViewById(R.id.txt_email_forgot_pass);
        CardView card_submit_forgot_pass = view.findViewById(R.id.card_submit_forgot_pass);

        card_submit_forgot_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Utils.isEmailValid(txt_email_forgot_pass.getText().toString().trim())){
                    forgotPassword(txt_email_forgot_pass.getText().toString());
                    dialog_forgot_password.dismiss();
                }else {
                    Utils.showDialog(LoginActivity.this, getString(R.string.enter_valid_email));
                }
            }
        });

        dialog_forgot_password.show();

    }

    public void onLoginSuccess(UserLoginModel userLoginModel){

        Utils.setStringPreference(LoginActivity.this, Utils.AccessToken_Key, userLoginModel.getRoot().get(0).getXAccessToken());

        Gson gson = new Gson();
        String userDetail = gson.toJson(userLoginModel);

        Utils.setStringPreference(LoginActivity.this, Utils.UserDetail_Key, userDetail);

        startActivity(new Intent(this, MainActivity.class));

        finish();

    }

    public void forgotPassword(String email){

        final ProgressDialog progressDialog = new ProgressDialog(this);
        Utils.showProgressDialog(progressDialog, getString(R.string.loading));

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

       // UserLoginModel userLoginModel = new Gson().fromJson(Utils.getStringPreference(this, Utils.UserDetail_Key, ""), UserLoginModel.class);

        apiInterface.forgotPassword(email, Utils.getStringPreference(this, Utils.AccessToken_Key, ""))
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
                                    // onBackPressed();
                                }

                                Utils.showDialog(LoginActivity.this, message);

                            }
                        }catch (Exception e){
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

    public void checkBeaconPermission(){

        RequirementsWizardFactory.createEstimoteRequirementsWizard().fulfillRequirements(
                this,
                new Function0<Unit>() {
                    @Override
                    public Unit invoke() {
                        //proximityObserver.addProximityZone(venueZone).start();
                        return null;
                    }
                },

                new Function1<List<? extends Requirement>, Unit>() {
                    @Override
                    public Unit invoke(List<? extends Requirement> requirements) {
          /* scanning won't work, handle this case in your app */
                        return null;
                    }
                },

                new Function1<Throwable, Unit>() {
                    @Override
                    public Unit invoke(Throwable throwable) {
          /* Oops, some error occurred, handle it here! */
                        return null;
                    }

        });

    }


}
