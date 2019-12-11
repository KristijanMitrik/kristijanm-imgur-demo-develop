/*
 * Copyright (C) 2015 The Android Open Source Project
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

package com.example.imgur.data;

import android.content.Context;
import android.support.annotation.NonNull;
import com.example.imgur.data.source.PostsDataSource;
import com.example.imgur.data.source.PostsRepository;
import com.example.imgur.data.source.local.PostsDatabase;
import com.example.imgur.data.source.local.PostsLocalDataSource;
import com.example.imgur.data.source.remote.PostsRemoteDataSource;
import com.example.imgur.network.ApiCalls;
import com.example.imgur.user.UserManager;
import com.example.imgur.util.AppExecutors;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Enables injection of mock implementations for
 * {@link PostsDataSource} at compile time. This is useful for testing, since it allows us to use
 * a fake instance of the class to isolate the dependencies and run a test hermetically.
 */

public class Injection {

  public static PostsRepository provideTasksRepository(@NonNull Context context, @NonNull
      UserManager userManager, @NonNull ApiCalls apiCalls) {
    checkNotNull(context);
    PostsDatabase database = PostsDatabase.getInstance(context);
    return PostsRepository.getInstance(PostsRemoteDataSource.getInstance(userManager, apiCalls),
        PostsLocalDataSource.getInstance(new AppExecutors(),
            database.taskDao()));
  }
}
