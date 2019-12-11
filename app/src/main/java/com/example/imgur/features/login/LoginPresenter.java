package com.example.imgur.features.login;

import android.Manifest;
import android.support.annotation.NonNull;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.example.imgur.config.MyAppConstants;
import com.example.imgur.user.UserCredentials;
import com.example.imgur.user.UserManager;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkNotNull;

public class LoginPresenter implements LoginContract.Presenter {

  private static final Pattern accessTokenPattern = Pattern.compile("access_token=([^&]*)");
  private static final Pattern refreshTokenPattern = Pattern.compile("refresh_token=([^&]*)");
  private static final Pattern accountID = Pattern.compile("account_id=([^&]*)");
  private static final Pattern accountUser = Pattern.compile("account_username=([^&]*)");
  private static final Pattern expiresInPattern = Pattern.compile("expires_in=(\\d+)");
  private final LoginContract.View loginView;
  private final UserManager userManager;

  public LoginPresenter(@NonNull LoginContract.View loginView, @NonNull UserManager userManager) {
    this.loginView = checkNotNull(loginView, "tasksView cannot be null!");
    this.userManager = checkNotNull(userManager, "userManger cannot be null!");
    this.loginView.setPresenter(this);
  }

  @Override public void start(final WebView mWebView) {

    mWebView.setWebViewClient(new WebViewClient() {
      @Override
      public boolean shouldOverrideUrlLoading(WebView view, String url) {
        // intercept the tokens
        // http://example.com#access_token=ACCESS_TOKEN&token_type=Bearer&expires_in=3600
        boolean tokensURL = false;
        if (url.startsWith(MyAppConstants.MY_IMGUR_REDIRECT_URL)) {
          tokensURL = true;
          Matcher m;

          m = refreshTokenPattern.matcher(url);
          m.find();
          String refreshToken = m.group(1);

          m = accessTokenPattern.matcher(url);
          m.find();
          String accessToken = m.group(1);

          m = expiresInPattern.matcher(url);
          m.find();
          long expiresIn = Long.valueOf(m.group(1));

          m = accountID.matcher(url);
          m.find();
          String accID = m.group(1);

          m = accountUser.matcher(url);
          m.find();
          String accUser = m.group(1);

          userManager.setUserCredentials(
              new UserCredentials(refreshToken, accessToken, expiresIn, accID, accUser));
          loginView.finishLoginUser();
        }
        return tokensURL;
      }
    });
  }
}