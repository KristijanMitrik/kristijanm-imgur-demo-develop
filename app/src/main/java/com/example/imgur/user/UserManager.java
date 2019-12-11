package com.example.imgur.user;

import android.content.SharedPreferences;
import com.example.imgur.data.source.PostsRepository;

public class UserManager {

  private User user;
  private UserCredentials userCredentials;
  private static UserManager ourInstance;
  private static SharedPreferences prefs;

  public static UserManager getInstance() {

    if (ourInstance == null) {
      ourInstance = new UserManager();
    }
    return ourInstance;
  }

  private UserManager() {

  }

  public void setUser(User user) {
    this.user = user;
  }

  public User getUser() {
    return this.user;
  }

  public boolean isLoggedIn() {
    return userCredentials != null;
  }

  public void deleteEverything() {
    this.user = null;
    this.userCredentials = null;
    PostsRepository.getInstance().deleteAllPosts();
  }

  public UserCredentials getUserCredentials() {
    return userCredentials;
  }

  public void setUserCredentials(UserCredentials userCredentials) {
    this.userCredentials = userCredentials;
  }
}
