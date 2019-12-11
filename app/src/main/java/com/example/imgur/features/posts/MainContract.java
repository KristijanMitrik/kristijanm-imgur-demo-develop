package com.example.imgur.features.posts;

import com.example.imgur.data.ImagesData;
import com.example.imgur.features.common.ui.BasePresenter;
import com.example.imgur.features.common.ui.BaseView;
import com.example.imgur.user.User;
import java.util.List;

public interface MainContract {

  interface View extends BaseView<Presenter> {

    // called when the recycler needs to be created with new posts
    void showPosts(List<ImagesData> data);

    // called when more posts are added to the recycler
    void addPostsToTheRecycler(List<ImagesData> data);

    void showUserInformations(User data);

    void showErrorMessage(String message);

    void showProgressBar();

    void hideProgressBar();

    void postsNotAvilable();

    void accountNotAvilable();


  }

  interface Presenter extends BasePresenter {

    //void getPosts(int podobro ime, int pagenumber, boolean syncCheck); on start in the presenter get page 0; the presenter keeps track of sync check

    /**
     * Called when the user changes post type.
     *
     * @param postType the new post type
     */
    void onPostTypeChanged(int postType);

    /**
     * Called when the infinite scroll readhes the bottom.
     */
    void onMorePostsRequest(); //(the presenter keeps track of the page number and resets it when a new post type is requested)
  }
}

