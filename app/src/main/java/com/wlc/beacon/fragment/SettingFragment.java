package com.wlc.beacon.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.wlc.beacon.R;
import com.wlc.beacon.activity.ChangeLanguageActivity;
import com.wlc.beacon.activity.EditProfileActivity;
import com.wlc.beacon.activity.LoginActivity;
import com.wlc.beacon.activity.SavePreferenceActivity;
import com.wlc.beacon.model.UserLoginModel;
import com.wlc.beacon.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;




public class SettingFragment extends Fragment {


    GoogleSignInClient mGoogleSignInClient;

    Unbinder unbinder;
    @BindView(R.id.header_text_left_2)
    TextView headerTextLeft2;
    @BindView(R.id.tv_name_setting)
    TextView tvNameSetting;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        unbinder = ButterKnife.bind(this, view);

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

        headerTextLeft2.setText(getString(R.string.setting));

        if (Utils.isContainsPreference(getActivity(), Utils.UserDetail_Key)) {
            UserLoginModel userLoginModel = new Gson().fromJson(Utils.getStringPreference(getActivity(), Utils.UserDetail_Key, ""), UserLoginModel.class);

            tvNameSetting.setText("Hi " + userLoginModel.getRoot().get(0).getName());
        }


        return view;
    }


    public void logOutUser() {

        // remove preference
        Utils.removePreference(getActivity(), Utils.UserDetail_Key);

        // logout facebook
        LoginManager.getInstance().logOut();

        // logout gmail
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });


        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.card_edit_profile, R.id.edit_preference_setting, R.id.change_language_setting, R.id.help_setting, R.id.share_setting, R.id.about_app_setting, R.id.privay_policy_setting, R.id.logout_setting})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.card_edit_profile:
                startActivity(new Intent(getActivity(), EditProfileActivity.class));
                break;
            case R.id.edit_preference_setting:
                startActivity(new Intent(getActivity(), SavePreferenceActivity.class));
                break;
            case R.id.change_language_setting:
                startActivity(new Intent(getActivity(), ChangeLanguageActivity.class));
                break;
            case R.id.help_setting:
                break;
            case R.id.share_setting:
                break;
            case R.id.about_app_setting:
                break;
            case R.id.privay_policy_setting:
                break;
            case R.id.logout_setting:
                logOutUser();
                break;
        }
    }
}
