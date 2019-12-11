package com.example.imgur.features.settings;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v14.preference.SwitchPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.util.Log;
import com.example.imgur.R;
import com.example.imgur.features.login.LoginActivity;
import com.example.imgur.user.UserManager;
import com.example.imgur.util.SharedPreferencesUtil;
import java.util.Objects;

public class SettingsFragment extends PreferenceFragmentCompat {

  private SharedPreferencesSettingsManager sharedPreferencesSettingsManager = SharedPreferencesSettingsManager.getInstance();
  UserManager userManager;
  @Override
  public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
    setPreferencesFromResource(R.xml.preferences, rootKey);
    userManager = UserManager.getInstance();
    final SwitchPreference sw = (SwitchPreference) findPreference("sync");
    sw.setDefaultValue(true);
    final Preference pref = findPreference("sample_key");

    pref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
      @RequiresApi(api = Build.VERSION_CODES.KITKAT) @Override
      public boolean onPreferenceClick(Preference preference) {
        userManager.deleteEverything();
        Intent myint = new Intent(getActivity(), LoginActivity.class);
        Objects.requireNonNull(getActivity()).startActivity(myint);

        return true;
      }
    });
    sw.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
      @Override
      public boolean onPreferenceChange(Preference preference, Object o) {

        if (sw.isChecked()) {
          sharedPreferencesSettingsManager.setCanUseCachedPosts(false);
          Log.w("IS IT CHECKED ", "FALSE");
          sw.setChecked(false);
        } else {
          sharedPreferencesSettingsManager.setCanUseCachedPosts(true);
          Log.w("IS IT CHECKED ", "TRUE");
          sw.setChecked(true);
        }
        return true;
      }
    });
  }
}
