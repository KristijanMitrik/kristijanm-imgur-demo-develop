package com.example.imgur.features.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;
import com.example.imgur.R;
import com.example.imgur.config.MyAppConstants;
import com.example.imgur.features.posts.MainActivity;
import com.example.imgur.user.UserManager;

import static com.google.common.base.Preconditions.checkNotNull;

public class LoginActivity extends AppCompatActivity implements LoginContract.View {

  private WebView mWebView;
  private LoginContract.Presenter mPresenter;
  private final UserManager userManager = UserManager.getInstance();
  private FrameLayout view;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    final Toolbar toolbar = findViewById(R.id.toolbar);
    final Button button = findViewById(R.id.loginToImgurBut);
    view = findViewById(R.id.activityContent);
    mPresenter = new LoginPresenter(this, userManager);
    setSupportActionBar(toolbar);
    if (userManager.isLoggedIn()) {
        startActivity(new Intent(LoginActivity.this,MainActivity.class));
    }
    button.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        mWebView = new WebView(LoginActivity.this);
        view.addView(mWebView);
        setContentView(view);
        mPresenter.start(mWebView);
        mWebView.loadUrl("https://api.imgur.com/oauth2/authorize?client_id="
            + MyAppConstants.MY_IMGUR_CLIENT_ID
            + "&response_type=token");
      }
    });
  }

  @Override public void finishLoginUser() {
    startActivity(
        new Intent(LoginActivity.this, MainActivity.class));

    runOnUiThread(new Runnable() {
      @Override
      public void run() {
        Toast.makeText(LoginActivity.this, R.string.logged_in, Toast.LENGTH_SHORT).show();
        finish();
      }
    });
  }

  @Override public void setPresenter(LoginContract.Presenter presenter) {
    mPresenter = checkNotNull(presenter);
  }
}