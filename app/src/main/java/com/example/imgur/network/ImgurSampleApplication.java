package com.example.imgur.network;

import android.app.Application;
import com.example.imgur.features.settings.SettingsManager;
import com.example.imgur.features.settings.SharedPreferencesSettingsManager;

public class ImgurSampleApplication extends Application {

  private static SettingsManager settingsManager;

  public void onCreate() {
    super.onCreate();

    settingsManager = SharedPreferencesSettingsManager.newInStance(this);
  }

  public void setSettingsManager(SettingsManager settingsManager) {
    this.settingsManager = settingsManager;
  }

  /**
   * http://stackoverflow.com/questions/2002288/static-way-to-get-context-on-android
   */

}
