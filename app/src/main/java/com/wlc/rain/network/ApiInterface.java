package com.rainbowloveapp.app.network;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 *  on 17/5/17.
 */

public interface ApiInterface {

    // ----------- Temp device Info -----------------------
    @FormUrlEncoded
    @POST("api/register")
    Call<ResponseBody> storeDeviceInfo(@Field("user_firstname") String user_firstname, @Field("user_lastname") String user_lastname, @Field("user_email") String user_email, @Field("user_password") String user_password);
    // ----------------------------------

    @FormUrlEncoded
    @POST("register.php?")
    Call<ResponseBody> registerUser(@Field("name") String name, @Field("email") String email, @Field("device_type") String device_type);

    @GET("category_web.php?type=art")
    Call<ResponseBody> getArtCategory();

    @GET("category_web.php?type=card")
    Call<ResponseBody> getCardCategory();

    @GET("category_image_web.php?type=card")
    Call<ResponseBody> getCardImages(@Query("cat_id") String id);

    @GET("category_image_web.php?type=art")
    Call<ResponseBody> getArtImages(@Query("cat_id") String id);

    @Multipart
    @POST(ApiClient.UploadToGallery)
    Call<ResponseBody> uploadImageGalleryApi(@Part MultipartBody.Part param);

    // ----------------------------------
    @GET("api/notification/list")
    Call<ResponseBody> getNotificationData(@Query("user_id") String user_id);

    // ----------------------------------

}
