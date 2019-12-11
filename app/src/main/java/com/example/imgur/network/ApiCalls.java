package com.example.imgur.network;

import android.support.annotation.NonNull;
import android.util.Log;
import com.example.imgur.R;
import com.example.imgur.config.MyAppConstants;
import com.example.imgur.data.ImagesData;
import com.example.imgur.data.source.PostsDataSource;
import com.example.imgur.features.posts.MainContract;
import com.example.imgur.model.DataResponse;
import com.example.imgur.data.Images;
import com.example.imgur.model.ListItem;
import com.example.imgur.user.User;
import com.example.imgur.user.UserManager;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiCalls {

  private ApiInterface apiInterface;

  private callback<Images> getUploadcallback;
  private final UserManager userManager = UserManager.getInstance();
  private Disposable disposable;

  public interface callback<T> {

    void callbackFunctionUpload(T data);
  }

  public ApiCalls(callback<Images> callbackUpload) {
    this.getUploadcallback = callbackUpload;
    apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
  }

  public Single<User> CreateUser(final UserManager userManager) {

    return apiInterface.getUser(userManager.getUserCredentials().getAccountUser(),
        "Client-ID " + MyAppConstants.MY_IMGUR_CLIENT_ID)
        .map(new Function<DataResponse<User>, User>() {
          @Override public User apply(DataResponse<User> userDataResponse) throws Exception {
            return userDataResponse.getData();
          }
        });

   /* disposable = call.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeWith(
            new DisposableSingleObserver<DataResponse<User>>() {

              @Override public void onSuccess(DataResponse<User> userDataResponse) {
                userManager.setUser(userDataResponse.getData());
                disposable.dispose();
              }

              @Override public void onError(Throwable e) {
                Log.w("USER FAILURE", e.toString());
              }
            });

    return userManager.getUser();*/
  }

  public void postImageOnline(final String titlestr, final String descstr, final File f) {
    RequestBody title = RequestBody.create(MultipartBody.FORM, titlestr);
    RequestBody description = RequestBody.create(MultipartBody.FORM, descstr);
    final RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), f);
    MultipartBody.Part body = MultipartBody.Part.createFormData("image", f.getName(), reqFile);
    Call<DataResponse<Images>> uploadCall;
    uploadCall =
        apiInterface.uploadPost("Bearer " + userManager.getUserCredentials().getAccessToken(),
            title, description, body);
    uploadCall.enqueue(new Callback<DataResponse<Images>>() {
      @Override
      public void onResponse(@NotNull Call<DataResponse<Images>> call,
          @NotNull Response<DataResponse<Images>> response) {
        assert response.body() != null;
        getUploadcallback.callbackFunctionUpload(response.body().getData());
      }

      @Override
      public void onFailure(@NotNull Call<DataResponse<Images>> call, @NotNull Throwable t) {
        Log.w("ERROOR", "ERROR WITH THE DATA");
      }
    });
  }

  public Single<List<ImagesData>> getImagesFromApi(final int id, final int pageNumber,
      final UserManager userManager) {
    final List<ImagesData> images = new ArrayList<>();
    Single<DataResponse<List<ListItem>>> returnObject;
    if (id == R.id.MyPosts) {
      returnObject =
          apiInterface.getImages(userManager.getUserCredentials().getAccountUser(), pageNumber,
              "Bearer " + userManager.getUserCredentials().getAccessToken());
    } else if (id == R.id.top_posts) {
      returnObject =
          apiInterface.getTopImages(pageNumber, "Client-ID " + MyAppConstants.MY_IMGUR_CLIENT_ID);
    } else {

      returnObject =
          apiInterface.getHotImages(pageNumber, "Client-ID " + MyAppConstants.MY_IMGUR_CLIENT_ID);
    }

    return returnObject
        .map(new Function<DataResponse<List<ListItem>>, List<ImagesData>>() {
          @Override public List<ImagesData> apply(DataResponse<List<ListItem>> listDataResponse)
              throws Exception {
            List<ListItem> listitems = listDataResponse.getData();
            for (ListItem image : listitems) {
              if (image.getIsalbum()) {
                for (ImagesData imgs : image.getImg()) {

                  images.add(new ImagesData(imgs.getLink(), image.getHead(), image.getDesc(), id,
                      UUID.randomUUID().toString()));
                }
              } else {
                images.add(new ImagesData(image.getImageUrl(), image.getHead(), image.getDesc(), id,
                    UUID.randomUUID().toString()));
              }
            }
            return images;
          }
        });
    /*Call<DataResponse<List<ListItem>>> call;
    if (id == R.id.MyPosts) {
      call = apiInterface.getImages(userManager.getUserCredentials().getAccountUser(), pageNumber,
          "Bearer " + userManager.getUserCredentials().getAccessToken());
    } else if (id == R.id.top_posts) {
      call =
          apiInterface.getTopImages(pageNumber, "Client-ID " + MyAppConstants.MY_IMGUR_CLIENT_ID);
    } else {
      call =
          apiInterface.getHotImages(pageNumber, "Client-ID " + MyAppConstants.MY_IMGUR_CLIENT_ID);
    }

    call.enqueue(new Callback<DataResponse<List<ListItem>>>() {
      @Override
      public void onResponse(@NotNull Call<DataResponse<List<ListItem>>> call,
          @NotNull Response<DataResponse<List<ListItem>>> response) {

        assert response.body() != null;
        List<ListItem> listitems = response.body().getData();

        for (ListItem image : listitems) {
          if (image.getIsalbum()) {
            for (ImagesData imgs : image.getImg()) {

              images.add(new ImagesData(imgs.getLink(), image.getHead(), image.getDesc(), id,
                  UUID.randomUUID().toString()));


            }
          } else {
            images.add(new ImagesData(image.getImageUrl(), image.getHead(), image.getDesc(), id,
                UUID.randomUUID().toString()));
          }
        }
        callback.onPostsLoaded(images);
      }

      @Override
      public void onFailure(@NotNull Call<DataResponse<List<ListItem>>> call,
          @NotNull Throwable t) {

        Log.w("getImagesFailure", "WRONG MESSAGE :::", t);
        if (t instanceof IOException) {
          Log.w("getImagesFailure",
              "this is an actual network failure :( inform the user and possibly retry");

          // logging probably not necessary
        } else {
          Log.w("getImagesFailure", "conversion issue! big problems :(");
        }
      }
    });
    return apiInterface.getHotImages();
    return images;*/
  }
}
