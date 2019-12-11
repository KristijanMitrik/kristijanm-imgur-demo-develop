/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.imgur.data.source.remote;

import android.os.Handler;
import android.support.annotation.NonNull;
import com.example.imgur.data.ImagesData;
import com.example.imgur.data.source.PostsDataSource;
import com.example.imgur.network.ApiCalls;
import com.example.imgur.user.User;
import com.example.imgur.user.UserManager;
import io.reactivex.Single;
import java.util.List;

/**
 * Implementation of the data source that adds a latency simulating network.
 */
public class PostsRemoteDataSource implements PostsDataSource {

  private static PostsRemoteDataSource INSTANCE;

  private static final int SERVICE_LATENCY_IN_MILLIS = 5000;

  private final UserManager userManager;
  private final ApiCalls apiCalls;
  private int pageNumber;
  private int postType;

  public static PostsRemoteDataSource getInstance(@NonNull UserManager userManager,
      ApiCalls apiCalls) {
    if (INSTANCE == null) {
      INSTANCE = new PostsRemoteDataSource(userManager, apiCalls);
    }
    return INSTANCE;
  }

  // Prevent direct instantiation.
  private PostsRemoteDataSource(@NonNull UserManager userManager,
      ApiCalls apiCalls) {
    this.userManager = userManager;
    this.apiCalls = apiCalls;
    postType = 100000;
    pageNumber = 0;
  }

  @Override
  public Single<List<ImagesData>> getPosts(final int postType) {
    // Simulate network by delaying the execution.

    if (this.postType != postType) {
      this.postType = postType;
      pageNumber = 0;
    }
    Handler handler = new Handler();
    handler.postDelayed(new Runnable() {
      @Override
      public void run() {

        apiCalls.getImagesFromApi(postType, pageNumber, userManager);
        pageNumber++;
      }
    }, SERVICE_LATENCY_IN_MILLIS);
    return apiCalls.getImagesFromApi(postType, pageNumber, userManager);
  }

  @Override public Single<User> getAccountInfo() {

    return apiCalls.CreateUser(userManager);
  }

  @Override public void savePosts(@NonNull List<ImagesData> task) {

  }

  @Override
  public void deleteAllPosts() {

  }

  @Override
  public void deletePostsByType(final int postType) {

  }
}
