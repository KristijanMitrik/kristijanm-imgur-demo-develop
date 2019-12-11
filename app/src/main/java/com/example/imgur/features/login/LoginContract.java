package com.example.imgur.features.login;

import android.webkit.WebView;
import com.example.imgur.features.common.ui.BaseView;

public interface LoginContract {

  interface View extends BaseView<LoginContract.Presenter> {

    void finishLoginUser();
  }

  interface Presenter {

    void start(WebView mWebView);
  }
}
