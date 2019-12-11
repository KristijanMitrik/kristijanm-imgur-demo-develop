package com.example.imgur.user;

public class UserCredentials {

  private String refreshToken;
  private String accessToken;
  private long expiresIn;
  private String accountID;
  private String accountUser;

  public UserCredentials(String refTok, String accTok, long expIn, String id,
      String user) {
    this.refreshToken = refTok;
    this.accessToken = accTok;
    this.expiresIn = expIn;
    this.accountID = id;
    this.accountUser = user;
  }

  public String getRefreshToken() {
    return refreshToken;
  }

  public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }

  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  public long getExpiresIn() {
    return expiresIn;
  }

  public void setExpiresIn(long expiresIn) {
    this.expiresIn = expiresIn;
  }

  public String getAccountID() {
    return accountID;
  }

  public void setAccountID(String accountID) {
    this.accountID = accountID;
  }

  public String getAccountUser() {
    return accountUser;
  }

  public void setAccountUser(String accountUser) {
    this.accountUser = accountUser;
  }
}
