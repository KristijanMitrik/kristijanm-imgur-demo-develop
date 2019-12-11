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

package com.example.imgur.data.source.local;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.util.Log;
import com.example.imgur.data.ImagesData;
import com.example.imgur.data.source.PostsDataSource;
import com.example.imgur.user.User;
import com.example.imgur.util.AppExecutors;
import io.reactivex.Single;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Concrete implementation of a data source as a db.
 */
public class PostsLocalDataSource implements PostsDataSource {

  private static volatile PostsLocalDataSource INSTANCE;

  private PostsDao mPostsDao;

  private AppExecutors mAppExecutors;

  private int postType = 1000;

  // Prevent direct instantiation.
  private PostsLocalDataSource(@NonNull AppExecutors appExecutors,
      @NonNull PostsDao tasksDao) {
    mAppExecutors = appExecutors;
    mPostsDao = tasksDao;
  }

  public static PostsLocalDataSource getInstance(@NonNull AppExecutors appExecutors,
      @NonNull PostsDao tasksDao) {
    if (INSTANCE == null) {
      synchronized (PostsLocalDataSource.class) {
        if (INSTANCE == null) {
          INSTANCE = new PostsLocalDataSource(appExecutors, tasksDao);
        }
      }
    }
    return INSTANCE;
  }

  @Override
  public Single<List<ImagesData>> getPosts(final int postType) {
    if (this.postType != postType) {
      this.postType = postType;
    }
    return mPostsDao.getPostByPostType(postType);
  }

  @Override public Single<User> getAccountInfo() {
    return null;
  }

  @Override
  public void savePosts(@NonNull final List<ImagesData> posts) {
    checkNotNull(posts);
    Runnable saveRunnable = new Runnable() {
      @Override
      public void run() {
        for (ImagesData post : posts) { mPostsDao.insertPost(post); }
      }
    };
    mAppExecutors.diskIO().execute(saveRunnable);
  }

  @Override
  public void deleteAllPosts() {
    Runnable deleteRunnable = new Runnable() {
      @Override
      public void run() {
        mPostsDao.deletePosts();
      }
    };

    mAppExecutors.diskIO().execute(deleteRunnable);
  }

  @Override
  public void deletePostsByType(final int postType) {
    if (this.postType != postType) {
      this.postType = postType;
      Runnable deleteRunnable = new Runnable() {
        @Override
        public void run() {

          mPostsDao.deletePostByType(postType);
        }
      };

      mAppExecutors.diskIO().execute(deleteRunnable);
    }
  }

  @VisibleForTesting
  static void clearInstance() {
    INSTANCE = null;
  }
}
