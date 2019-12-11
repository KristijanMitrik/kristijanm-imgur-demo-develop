package com.example.imgur.features.settings;

import com.example.imgur.features.common.ui.BasePresenter;
import com.example.imgur.features.common.ui.BaseView;
import com.example.imgur.features.posts.MainContract;
import com.example.imgur.data.Images;
import com.example.imgur.user.User;
import java.util.List;

public interface SetingsContract {

  interface View extends BaseView<MainContract.Presenter> {

    void showPosts(List<Images> data, boolean syncche);

    void showUserInformations(User data);

    void showErrorMessage(String message);

    void showProgressBar();

    void hideProgressBar();
  }

  interface Presenter extends BasePresenter {

    void getImages(int id, int pagenumber, boolean syncCheck);
  }



}
