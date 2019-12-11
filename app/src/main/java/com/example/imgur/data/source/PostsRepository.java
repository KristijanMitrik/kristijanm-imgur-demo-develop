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

package com.example.imgur.data.source;

import android.support.annotation.NonNull;
import com.example.imgur.data.ImagesData;
import com.example.imgur.features.settings.SharedPreferencesSettingsManager;
import com.example.imgur.model.DataResponse;
import com.example.imgur.model.ListItem;
import com.example.imgur.user.User;
import io.reactivex.Single;
import io.reactivex.functions.Consumer;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Concrete implementation to load tasks from the data sources into a cache.
 * <p>
 * For simplicity, this implements a dumb synchronisation between locally persisted data and data
 * obtained from the server, by using the remote data source only if the local database doesn't
 * exist or is empty.
 */
public class PostsRepository implements PostsDataSource {

  private static PostsRepository INSTANCE = null;

  private final PostsDataSource mTasksRemoteDataSource;

  private final PostsDataSource mTasksLocalDataSource;

  private final SharedPreferencesSettingsManager sharedPreferencesSettingsManager;

  /**
   * This variable has package local visibility so it can be accessed from tests.
   */

  /**
   * Marks the cache as invalid, to force an update the next time data is requested. This variable
   * has package local visibility so it can be accessed from tests.
   */

  // Prevent direct instantiation.
  private PostsRepository(@NonNull PostsDataSource tasksRemoteDataSource,
      @NonNull PostsDataSource tasksLocalDataSource) {
    mTasksRemoteDataSource = checkNotNull(tasksRemoteDataSource);
    mTasksLocalDataSource = checkNotNull(tasksLocalDataSource);
    sharedPreferencesSettingsManager = SharedPreferencesSettingsManager.getInstance();
  }

  /**
   * Returns the single instance of this class, creating it if necessary.
   *
   * @param tasksRemoteDataSource the backend data source
   * @param tasksLocalDataSource the device storage data source
   * @return the {@link PostsRepository} instance
   */
  public static PostsRepository getInstance(PostsDataSource tasksRemoteDataSource,
      PostsDataSource tasksLocalDataSource) {
    if (INSTANCE == null) {
      INSTANCE = new PostsRepository(tasksRemoteDataSource, tasksLocalDataSource);
    }
    return INSTANCE;
  }

  public static PostsRepository getInstance() {return INSTANCE;}

  /**
   * Used to force {@link #getInstance(PostsDataSource, PostsDataSource)} to create a new instance
   * next time it's called.
   */
  public static void destroyInstance() {
    INSTANCE = null;
  }


  @Override
  public Single<List<ImagesData>> getPosts(final int postType) {

    if (!sharedPreferencesSettingsManager.isInternetConnectionAvailable()
        && sharedPreferencesSettingsManager.canUseCachedPosts()) {
      return mTasksLocalDataSource.getPosts(postType);
    } else {
      return mTasksRemoteDataSource.getPosts(postType)
          .doAfterSuccess(new Consumer<List<ImagesData>>() {
            @Override public void accept(List<ImagesData> imagesData) throws Exception {
              mTasksLocalDataSource.deleteAllPosts();
              mTasksLocalDataSource.savePosts(imagesData);
            }
          });
    }
  }

  @Override public Single<User> getAccountInfo() {
     return  mTasksRemoteDataSource.getAccountInfo();
  }

  @Override
  public void savePosts(@NonNull List<ImagesData> task) {
    checkNotNull(task);
    mTasksRemoteDataSource.savePosts(task);
    mTasksLocalDataSource.savePosts(task);
  }



  @Override
  public void deleteAllPosts() {
    mTasksLocalDataSource.deleteAllPosts();
  }

  @Override
  public void deletePostsByType(final int postType) {

  }
}
