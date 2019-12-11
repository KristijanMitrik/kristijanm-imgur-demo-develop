package com.example.imgur.features.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public final class SharedPreferencesSettingsManager implements SettingsManager {

  private static SharedPreferencesSettingsManager sharedPreferencesSettingsManager = null;
  private SharedPreferences sharedPreferences;
  private static String key = "sync";
  private ConnectivityManager connectivityManager;

  public static SharedPreferencesSettingsManager newInStance(Context context) {
    if (sharedPreferencesSettingsManager == null) {
      sharedPreferencesSettingsManager = new SharedPreferencesSettingsManager(context);
    }
    return sharedPreferencesSettingsManager;
  }

  public static SharedPreferencesSettingsManager getInstance() {
    return sharedPreferencesSettingsManager;
  }

  private SharedPreferencesSettingsManager(Context applicationContext) {
    this.sharedPreferences =
        applicationContext.getSharedPreferences("MyPrefrences", Context.MODE_PRIVATE);
    connectivityManager =
        (ConnectivityManager) applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE);
  }

  @Override public void setCanUseCachedPosts(boolean syncSwitch) {
    sharedPreferences.edit().putBoolean(key, syncSwitch).apply();
  }

  @Override public boolean canUseCachedPosts() {
    return sharedPreferences.getBoolean(key, false);
  }

  @Override public boolean isInternetConnectionAvailable() {

    if (connectivityManager != null) {
      NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
      return (activeNetwork != null && activeNetwork.isConnected());
    } else {
      return false;
    }
  }
}

