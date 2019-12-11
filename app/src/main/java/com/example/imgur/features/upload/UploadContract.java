package com.example.imgur.features.upload;

import android.webkit.WebView;
import com.example.imgur.features.common.ui.BaseView;
import com.example.imgur.features.login.LoginContract;

public interface UploadContract {


  interface View extends BaseView<UploadContract.Presenter> {

    void finishLoginUser();
  }

  interface Presenter {

    void start(WebView mWebView);
  }


}
