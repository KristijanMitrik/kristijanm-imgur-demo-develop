package com.example.imgur.features.posts;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.imgur.R;
import com.squareup.picasso.Picasso;

public class ImageFullScreenActivity extends AppCompatActivity {

  private ImageView img;
  private ScaleGestureDetector mScaleGestureDetector;
  private float mScaleFactor = 1.0f;
  private Toolbar toolbar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_image_properties);
    toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    final TextView title = (TextView) findViewById(R.id.TitleProp);
    final TextView desc = (TextView) findViewById(R.id.DescriptionProp);
    img = (ImageView) findViewById(R.id.imageThumbnail);
    mScaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());

    Intent intent = getIntent();

    String Desc = intent.getExtras().getString("Desc", null);
    String Title = intent.getExtras().getString("Title", "");
    String Url = intent.getExtras().getString("URL", "");

    title.setText(Title);
    desc.setText(Desc);
    Picasso.with(getApplicationContext()).load(Url).into(img);
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    return super.onTouchEvent(event);
  }

  private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

    @Override
    public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
      mScaleFactor *= scaleGestureDetector.getScaleFactor();
      mScaleFactor = Math.max(0.1f,
          Math.min(mScaleFactor, 10.0f));
      img.setScaleX(mScaleFactor);
      img.setScaleY(mScaleFactor);
      return true;
    }
  }
}
