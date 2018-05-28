package com.wlc.beacon.network;

import android.content.Context;

import com.wlc.beacon.model.UserLoginModel;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;



public class ApiResponse {

    public UserLoginModel getLoginData(Context context, String email, String password){

        final UserLoginModel[] userLoginModel = {null};

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        //Observable<Response<UserLoginModel>>

        apiService.loginUser("email","password","header")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<UserLoginModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<UserLoginModel> userLoginModelResponse) {

                        userLoginModel[0] = userLoginModelResponse.body();

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

        return userLoginModel[0];

    }

}
