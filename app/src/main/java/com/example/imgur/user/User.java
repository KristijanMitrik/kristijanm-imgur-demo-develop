package com.example.imgur.user;

import android.databinding.BaseObservable;
import com.google.gson.annotations.SerializedName;

public class User extends BaseObservable {

  @SerializedName("avatar")
  private String avatar;

  @SerializedName("reputation_name")
  private String reputation_name;

  @SerializedName("url")
  private String url;

  public User(String avatar, String reputation, String url) {
    this.avatar = avatar;
    reputation_name = reputation;
    this.url = url;
  }

  public User() {

  }

  public String getURL() {
    return url;
  }

  public String getAvatar() {
    return avatar;
  }

  public String getReputation_name() {
    return reputation_name;
  }

  public void setReputation_name(String reputation_name) {
    this.reputation_name = reputation_name;
  }

  public void setURL(String url) {
    this.url = url;
  }

  public void setAvatar(String avatar) {
    this.avatar = avatar;
  }
}
