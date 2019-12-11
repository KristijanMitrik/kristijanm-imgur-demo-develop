package com.example.imgur.network;

import com.example.imgur.model.DataResponse;
import com.example.imgur.data.Images;
import com.example.imgur.model.ListItem;
import com.example.imgur.user.User;
import io.reactivex.Single;
import java.util.List;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiInterface {



    @GET("account/{username}/images/{page}")
    Single<DataResponse<List<ListItem>>> getImages(@Path("username") String username , @Path("page") int page, @Header("Authorization") String auth);


    @GET("gallery/top/top/month/{page}?showViral=true&mature=true")
    Single<DataResponse<List<ListItem>>>getTopImages(@Path("page") int page , @Header("Authorization") String auth);

    @GET("gallery/hot/viral/month/{page}?showViral=true&mature=true")
    Single<DataResponse<List<ListItem>>> getHotImages(@Path("page") int page , @Header("Authorization") String auth);

    @Multipart
    @POST("image")
    Call<DataResponse<Images>> uploadPost(
            @Header("Authorization") String auth,
            @Part("title") RequestBody title,
                            @Part("description") RequestBody description,
                            @Part MultipartBody.Part image);

    @GET("account/{username}")
    Single<DataResponse<User>> getUser(@Path("username") String username , @Header("Authorization") String auth);

}
