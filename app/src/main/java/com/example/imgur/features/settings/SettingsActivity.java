package com.example.imgur.features.settings;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import com.example.imgur.R;
import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {

  @RequiresApi(api = Build.VERSION_CODES.KITKAT) @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_settings);
    final Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    getSupportFragmentManager()
        .beginTransaction()
        .replace(R.id.fragment_container, new SettingsFragment())
        .commit();
  }
}
