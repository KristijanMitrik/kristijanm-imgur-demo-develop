package com.example.imgur.features.upload;

import android.Manifest;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.imgur.R;
import com.example.imgur.model.DataResponse;
import com.example.imgur.data.Images;
import com.example.imgur.network.ApiCalls;
import com.example.imgur.network.ApiClient;
import com.example.imgur.network.ApiInterface;
import com.example.imgur.user.UserManager;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import retrofit2.Call;

public class ChooseAnImageForUploadActivity extends AppCompatActivity implements ApiCalls.callback {

  private Context context;
  private Button btn;
  private Button importbtn;
  private Toolbar toolbar;
  private ImageView imageview;
  private static final String IMAGE_DIRECTORY = "/imgurimages";
  private int GALLERY = 1, CAMERA = 2;
  private EditText titleinput;
  private EditText descriptioninput;
  private ApiInterface apiInterface;
  private ApiCalls apiCalls;
  private final UserManager userManager = UserManager.getInstance();
  private Bitmap mybitmap;
  private final String NOTIFICATION_CHANNEL_ID = "234";
  private final int NOTIFICATION_ID = 1;
  private int isuploaded = 1;
  private NotificationManager notificationManager;
  private NotificationCompat.Builder builder;
  private Call<DataResponse<Images>> uploadCall = null;
  private int progressbar = 0;
  private String title;
  private String desc;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_choose_an_image_for_upload);
    toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    apiCalls = new ApiCalls(this);
    context = ChooseAnImageForUploadActivity.this;
    progressbar = 0;
    titleinput =  findViewById(R.id.inserttitle);
    descriptioninput =  findViewById(R.id.insertdesc);
    btn =  findViewById(R.id.btn);
    importbtn =  findViewById(R.id.importimage);
    imageview = findViewById(R.id.iv);
    apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
    btn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        requestMultiplePermissions();
        showPictureDialog();
      }
    });
    Intent intent = getIntent();
    if (intent.hasExtra("plus") && intent.getExtras() != null) {
      String extra = intent.getExtras().getString("plus");
      if (extra != null && extra.equals("cancel")) {
        cancelUpload();
      } else if (extra != null && extra.equals("retry") || (progressbar == 1)) {
        retry();
        imageview.setImageBitmap(mybitmap);
        titleinput.setText(title);
        descriptioninput.setText(desc);
      }
    }

    importbtn.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        if (!userManager.isLoggedIn()) {
          Toast.makeText(ChooseAnImageForUploadActivity.this, "YOU ARE NOT LOGED IN",
              Toast.LENGTH_SHORT)
              .show();
        } else {
          if (isuploaded == 0) {
            Intent cancel =
                new Intent(getApplicationContext(), ChooseAnImageForUploadActivity.class);
            cancel.putExtra("plus", "cancel");
            PendingIntent CancelPendingIntent =
                PendingIntent.getActivity(ChooseAnImageForUploadActivity.this, 0, cancel,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            Intent retry =
                new Intent(getApplicationContext(), ChooseAnImageForUploadActivity.class);
            retry.putExtra("plus", "retry");
            PendingIntent ReplyPendingIntent =
                PendingIntent.getActivity(ChooseAnImageForUploadActivity.this, 1, retry,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            createNotificationChannel();
            notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            builder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID);
            builder.setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("Upload Status...")
                .setContentText("Upload in progress...")
                .setProgress(100, 0, true)
                .addAction(R.drawable.ic_menu_send, "Cancel", CancelPendingIntent)
                .addAction(R.drawable.ic_menu_gallery, "Retry", ReplyPendingIntent)
                .build();
            notificationManager.notify(NOTIFICATION_ID, builder.build());
            progressbar = 1;
            saveImage(mybitmap);
          } else {
            Toast.makeText(ChooseAnImageForUploadActivity.this, "FIRST SELECT AN IMAGE",
                Toast.LENGTH_SHORT)
                .show();
          }
        }
      }
    });
  }
  private void createNotificationChannel() {
    // Create the NotificationChannel, but only on API 26+ because
    // the NotificationChannel class is new and not in the support library
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      int importance = NotificationManager.IMPORTANCE_DEFAULT;
      NotificationChannel channel = new NotificationChannel("234", NOTIFICATION_CHANNEL_ID, importance);
      channel.setDescription("CHANNEL DESCRIPTION");
      // Register the channel with the system; you can't change the importance
      // or other notification behaviors after this
      NotificationManager notificationManager = getSystemService(NotificationManager.class);
      notificationManager.createNotificationChannel(channel);
    }
  }

  public void cancelUpload() {
    notificationManager =
        (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
    notificationManager.cancel(NOTIFICATION_ID);
    if (uploadCall != null) {
      uploadCall.cancel();
    }
  }

  public void retry() {
    notificationManager =
        (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
    notificationManager.cancel(NOTIFICATION_ID);
    if (uploadCall != null) {
      uploadCall.cancel();
      saveImage(mybitmap);
    }
  }

  private void showPictureDialog() {
    AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
    pictureDialog.setTitle("Select Action");
    String[] pictureDialogItems = {
        "Select photo from gallery",
        "Capture photo from camera"
    };
    pictureDialog.setItems(pictureDialogItems,
        new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            switch (which) {
              case 0:
                choosePhotoFromGallary();
                break;
              case 1:
                takePhotoFromCamera();
                break;
            }
          }
        });
    pictureDialog.show();
  }

  public void choosePhotoFromGallary() {
    Intent galleryIntent = new Intent(Intent.ACTION_PICK,
        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

    startActivityForResult(galleryIntent, GALLERY);
  }

  private void takePhotoFromCamera() {
    Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
    startActivityForResult(intent, CAMERA);
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == RESULT_CANCELED) {
      return;
    }
    if (requestCode == GALLERY) {
      if (data != null) {
        Uri contentURI = data.getData();
        try {
          Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
          mybitmap = bitmap;
          imageview.setImageBitmap(bitmap);
        } catch (IOException e) {
          e.printStackTrace();
          Toast.makeText(ChooseAnImageForUploadActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
        }
      }
    } else if (requestCode == CAMERA) {
      Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
      imageview.setImageBitmap(thumbnail);
      mybitmap = thumbnail;
    }
    isuploaded = 0;
  }

  public void saveImage(Bitmap myBitmap) {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
    File wallpaperDirectory = new File(
        Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
    // have the object build the directory structure, if needed.
    if (!wallpaperDirectory.exists()) {
      wallpaperDirectory.mkdirs();
    }
    try {
      File f = new File(wallpaperDirectory, Calendar.getInstance()
          .getTimeInMillis() + ".jpg");
      f.createNewFile();
      FileOutputStream fo = new FileOutputStream(f);
      fo.write(bytes.toByteArray());
      fo.close();
      Log.d("TAG", "File Saved::---&gt;" + f.getAbsolutePath());
      String titlestr = titleinput.getText().toString();
      String descstr = descriptioninput.getText().toString();
      apiCalls.postImageOnline(titlestr,descstr,f);
      f.getAbsolutePath();
    } catch (IOException e1) {
      e1.printStackTrace();
    }
  }

  private void requestMultiplePermissions() {
    Dexter.withActivity(this)
        .withPermissions(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        .withListener(new MultiplePermissionsListener() {
          @Override
          public void onPermissionsChecked(MultiplePermissionsReport report) {
            // check if all permissions are granted
            if (report.areAllPermissionsGranted()) {
              Toast.makeText(getApplicationContext(), "All permissions are granted!",
                  Toast.LENGTH_SHORT).show();
            }

            // check for permanent denial of any permission
            if (report.isAnyPermissionPermanentlyDenied()) {
              // show alert dialog navigating to Settings
              Toast.makeText(getApplicationContext(),"You have to give permissions for camera and storage if you want to upload pictures",Toast.LENGTH_LONG).show();
            }
          }

          @Override
          public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions,
              PermissionToken token) {
            token.continuePermissionRequest();
          }
        }).
        withErrorListener(new PermissionRequestErrorListener() {
          @Override
          public void onError(DexterError error) {
            Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show();
          }
        })
        .onSameThread()
        .check();
  }



  @Override public void callbackFunctionUpload(Object data) {
    progressbar = 2;
    notificationManager =
        (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
    builder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID);
    builder.setProgress(0, 0, false);
    builder.setSmallIcon(R.drawable.ic_launcher_background);
    builder.setContentText("Upload Complete....");
    notificationManager.notify(NOTIFICATION_ID, builder.build());
    Toast.makeText(ChooseAnImageForUploadActivity.this, "Image uploaded succ",
        Toast.LENGTH_SHORT)
        .show();
    titleinput.getText().clear();
    descriptioninput.getText().clear();
    imageview.setImageResource(R.drawable.ic_menu_camera);
    isuploaded = 1;
  }
}
