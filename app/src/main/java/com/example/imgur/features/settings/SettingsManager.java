package com.example.imgur.features.settings;

public interface SettingsManager {

  void setCanUseCachedPosts(boolean syncSwitch);

  boolean canUseCachedPosts();

  boolean isInternetConnectionAvailable();
}
