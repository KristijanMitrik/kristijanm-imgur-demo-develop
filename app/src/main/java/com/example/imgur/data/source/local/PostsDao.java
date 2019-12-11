/*
 * Copyright 2017, The Android Open Source Project
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

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import com.example.imgur.data.ImagesData;
import io.reactivex.Single;
import java.util.List;

/**
 * Data Access Object for the tasks table.
 */
@Dao
public interface PostsDao {

  /**
   * Select all tasks from the tasks table.
   *
   * @return all posts.
   */
  @Query("SELECT * FROM ImagesData")
  Single<List<ImagesData>> getPosts();


  @Query("SELECT * FROM ImagesData WHERE type = :typeOf")
  Single<List<ImagesData>> getPostByPostType(int typeOf);
  /**
   * Select a post by id.
   *
   * @param postId the post id.
   * @return the post with postId.
   */
  @Query("SELECT * FROM ImagesData WHERE entryid = :postId")
  ImagesData getPostByid(String postId);

  /**
   * Insert a post in the database. If the post already exists, replace it.
   *
   * @param post the post to be inserted.
   */
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insertPost(ImagesData post);

  /**
   * Update a image.
   *
   * @param post post to be updated
   * @return the number of posts updated. This should always be 1.
   */
  @Update
  int updatePost(ImagesData post);

  /**
   * Delete a post by id.
   *
   * @return the id of the deleted post. This should always be 1.
   */
  @Query("DELETE FROM ImagesData WHERE type = :postType")
  int deletePostByType(int postType);

  /**
   * Delete all Posts.
   */
  @Query("DELETE FROM ImagesData")
  void deletePosts();
}
