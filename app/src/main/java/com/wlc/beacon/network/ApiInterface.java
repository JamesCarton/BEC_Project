package com.wlc.beacon.network;

import com.wlc.beacon.model.BeaconListModel;
import com.wlc.beacon.model.CampaignModel;
import com.wlc.beacon.model.FavouriteModel;
import com.wlc.beacon.model.InterestModel;
import com.wlc.beacon.model.SearchModel;
import com.wlc.beacon.model.UserLoginModel;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;



public interface ApiInterface {

    @FormUrlEncoded
    @POST("api/v1/login")
    Observable<Response<UserLoginModel>> loginUser(@Field("email") String email,
                                                   @Field("password") String password,
                                                   @Header("x-access-token") String accesstoken);

    @FormUrlEncoded
    @POST("api/v1/social_login")
    Observable<Response<UserLoginModel>> socialMediaLoginUser(@Field("email") String email,
                                                              @Field("name") String name,
                                                              @Field("social_id") String social_id,
                                                              @Field("social_type") String social_type,
                                                              @Field("user_avatar") String user_avatar);

    @Multipart
    @POST("api/v1/register")
    Observable<Response<ResponseBody>> signupUser(@Part("name") RequestBody name,
                                                  @Part("email") RequestBody email,
                                                  @Part("password") RequestBody password,
                                                  @Part MultipartBody.Part params);

    @GET("api/v1/campaign/mytag")
    Observable<Response<CampaignModel>> myTagCampaign(@Query("user_id") String user_id,
                                                      @Header("x-access-token") String accesstoken);


    @FormUrlEncoded
    @POST("api/v1/campaign/list")
    Observable<Response<ResponseBody>> campaignList(@Field("user_id") String user_id,
                                                    @Field("beacon_major") String beacon_major,
                                                    @Field("beacon_minor") String beacon_minor,
                                                    @Field("campaign_type") String campaign_type,
                                                    @Header("x-access-token") String accesstoken);

    @GET("api/v1/beacon/list")
    Observable<Response<BeaconListModel>> beaconList(@Query("user_id") String user_id,
                                                     @Header("x-access-token") String accesstoken);

    @DELETE("api/v1/campaign/delete_ping")
    Observable<Response<ResponseBody>> deletePing(@Query("user_id") String user_id,
                                                  @Query("ping_id") String ping_id,
                                                  @Header("x-access-token") String accesstoken);

    @FormUrlEncoded
    @POST("api/v1/campaign/read_ping")
    Observable<Response<ResponseBody>> readPing(@Field("user_id") String user_id,
                                                @Field("ping_id") String ping_id,
                                                @Header("x-access-token") String accesstoken);

    @GET("api/v1/user/forgot_pass")
    Observable<Response<ResponseBody>> forgotPassword(@Query("email") String email,
                                                      @Header("x-access-token") String accesstoken);

    @GET("api/v1/filter/store")
    Observable<Response<ResponseBody>> popularStore(@Query("user_id") String user_id,
                                                    @Query("filter_type") String filter_type,
                                                    @Header("x-access-token") String accesstoken);

    @GET("api/v1/filter/state_list")
    Observable<Response<ResponseBody>> stateList(@Query("user_id") String user_id,
                                                 @Header("x-access-token") String accesstoken);

    @GET("api/v1/filter/city_list")
    Observable<Response<ResponseBody>> cityList(@Query("user_id") String user_id,
                                                @Query("state_id") String state_id,
                                                @Header("x-access-token") String accesstoken);

    @GET("api/v1/user/intrust/list")
    Observable<Response<InterestModel>> interestList(@Query("user_id") String user_id,
                                                     @Header("x-access-token") String accesstoken);

    @GET("api/v1/favourite/list")
    Observable<Response<FavouriteModel>> favouriteList(@Query("user_id") String user_id,
                                                       @Header("x-access-token") String accesstoken);

    @FormUrlEncoded
    @POST("api/v1/favourite/add")
    Observable<Response<ResponseBody>> addFavourite(@Field("user_id") String user_id,
                                                    @Field("store_id") String store_id,
                                                    @Header("x-access-token") String accesstoken);

    @FormUrlEncoded
    @POST("api/v1/user/campaign_preference")
    Observable<Response<ResponseBody>> savePreference(@Field("user_id") String user_id,
                                                      @Field("gender_preference") String gender_preference,
                                                      @Field("age_preference") String age_preference,
                                                      @Field("interest_preference") String interest_preference,
                                                      @Header("x-access-token") String accesstoken);

    @GET("api/v1/filter/store")
    Observable<Response<SearchModel>> filterCityState(@Query("user_id") String user_id,
                                                      @Query("state_name") String state_name,
                                                      @Query("city_name") String city_name,
                                                      @Query("filter_type") String filter_type,
                                                      @Header("x-access-token") String accesstoken);

    @GET("api/v1/filter/store")
    Observable<Response<SearchModel>> filterLocationWise(@Query("user_id") String user_id,
                                                         @Query("state_name") String state_name,
                                                         @Query("filter_type") String filter_type,
                                                         @Header("x-access-token") String accesstoken);

    @GET("api/v1/filter/store")
    Observable<Response<SearchModel>> filterDistanceWise(@Query("user_id") String user_id,
                                                         @Query("latitude") String latitude,
                                                         @Query("longitude") String longitude,
                                                         @Query("distance") int distance,
                                                         @Query("filter_type") String filter_type,
                                                         @Header("x-access-token") String accesstoken);

    @GET("api/v1/filter/store/campaign")
    Observable<Response<CampaignModel>> storeIDWiseCampaign(@Query("user_id") String user_id,
                                                            @Query("store_id") String store_id,
                                                            @Query("campaign_view") String campaign_view,
                                                            @Header("x-access-token") String accesstoken);

    @GET("api/v1/push_notification")
    Observable<Response<ResponseBody>> registerNotificationToken(@Query("device_token") String device_token,
                                                                 @Query("type") String type);

    @FormUrlEncoded
    @POST("api/v1/user/changePass")
    Observable<Response<ResponseBody>> changePassword(@Field("user_id") String user_id,
                                                      @Field("oldpassword") String oldpassword,
                                                      @Field("newpassword") String newpassword,
                                                      @Header("x-access-token") String accesstoken);

    @Multipart
    @POST("api/v1/user/profile/update")
    Observable<Response<ResponseBody>> profileUpdateWithImage(@Part("user_id") RequestBody user_id,
                                                              @Part("name") RequestBody name,
                                                              @Part("email") RequestBody email,
                                                              @Part("phone") RequestBody phone,
                                                              @Part("gender") RequestBody gender,
                                                              @Part("birthdate") RequestBody birthdate,
                                                              @Header("x-access-token") String accesstoken,
                                                              @Part MultipartBody.Part params);

    @FormUrlEncoded
    @POST("api/v1/user/profile/update")
    Observable<Response<ResponseBody>> profileUpdateWithoutImage(@Field("user_id") String user_id,
                                                                 @Field("name") String name,
                                                                 @Field("email") String email,
                                                                 @Field("phone") String phone,
                                                                 @Field("gender") String gender,
                                                                 @Field("birthdate") String birthdate,
                                                                 @Header("x-access-token") String accesstoken);


}
