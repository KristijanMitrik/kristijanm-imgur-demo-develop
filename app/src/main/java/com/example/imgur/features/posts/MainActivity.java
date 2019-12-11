package com.example.imgur.features.posts;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.imgur.R;
import com.example.imgur.data.ImagesData;
import com.example.imgur.data.Injection;
import com.example.imgur.features.settings.SettingsActivity;
import com.example.imgur.features.settings.SharedPreferencesSettingsManager;
import com.example.imgur.features.upload.ChooseAnImageForUploadActivity;
import com.example.imgur.network.ApiCalls;
import com.example.imgur.user.User;
import com.example.imgur.user.UserManager;
import com.squareup.picasso.Picasso;
import java.util.List;
import org.jetbrains.annotations.NotNull;

import static com.google.common.base.Preconditions.checkNotNull;

public class MainActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener, MainContract.View,
    ApiCalls.callback {

  private TextView username;
  private ImageView avatar;
  private TextView url;
  private RecyclerView recyclerView;
  private ProgressBar progressBar;
  private GridLayoutManager layoutManager;
  private int PageNumber = 0;
  private RecyclerAdapter adapter;
  private static final String TAG = "Imgur Applicaton";
  private static int id;
  private UserManager userManager;
  private int previousTotal = 0;
  private boolean loading = true;
  private ApiCalls apiCalls = new ApiCalls(this);
  private MainContract.Presenter mPresenter;
  private SharedPreferencesSettingsManager sharedPreferencesSettingsManager;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    if (android.os.Build.VERSION.SDK_INT > 16) {
      StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
      StrictMode.setThreadPolicy(policy);
    }
    sharedPreferencesSettingsManager = SharedPreferencesSettingsManager.getInstance();
    mPresenter = new MainPresenter(
        Injection.provideTasksRepository(this, UserManager.getInstance(), apiCalls), this);
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    initializeRecyclerView();
    initializeNavigationViewElements();
    progressBar = findViewById(R.id.ProgressBar);
    DrawerLayout drawer = findViewById(R.id.drawer_layout);
    FloatingActionButton fab = findViewById(R.id.fab);

    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        startActivity(new Intent(MainActivity.this, ChooseAnImageForUploadActivity.class));
      }
    });

    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
        this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    drawer.addDrawerListener(toggle);
    toggle.syncState();

    mPresenter.start();

    recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
      @Override
      public void onScrolled(@NonNull RecyclerView mrecyclerView, int dx, int dy) {
        super.onScrolled(mrecyclerView, dx, dy);

        onScrolledFunction();
      }
    });

    final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);
    pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        PageNumber = 0;
        loading = true;
        previousTotal = 0;
        mPresenter.start();
        pullToRefresh.setRefreshing(false);
      }
    });
  }

  @Override
  public void onBackPressed() {
    DrawerLayout drawer = findViewById(R.id.drawer_layout);
    if (drawer.isDrawerOpen(GravityCompat.START)) {
      drawer.closeDrawer(GravityCompat.START);
    } else {
      super.onBackPressed();
    }
  }

  private void initializeNavigationViewElements() {
    NavigationView navigationView = findViewById(R.id.nav_view);
    navigationView.setNavigationItemSelectedListener(this);
    View hView = navigationView.getHeaderView(0);
    username = hView.findViewById(R.id.Username);
    avatar = hView.findViewById(R.id.avatar);
    url = hView.findViewById(R.id.urlforavatar);
  }

  private void initializeRecyclerView() {
    recyclerView = findViewById(R.id.my_recycler_view);
    layoutManager = new GridLayoutManager(this, 3);
    recyclerView.setHasFixedSize(true);
    recyclerView.setLayoutManager(layoutManager);
  }

  protected void onScrolledFunction() {
    final int visibleItemCount = recyclerView.getChildCount();
    final int totalItemCount = layoutManager.getItemCount();
    final int firstVisibleItem = layoutManager.findFirstVisibleItemPosition();
    Log.w(TAG, " msg = "
        + visibleItemCount
        + "  "
        + totalItemCount
        + "   "
        + firstVisibleItem);
    if (loading) {
      if (totalItemCount > previousTotal) {
        loading = false;
        previousTotal = totalItemCount;
      }
    }
    final int visibleThreshold = 3;
    if (!loading && (totalItemCount - visibleItemCount)
        <= (firstVisibleItem + visibleThreshold)) {
      // End has been reached



        // Do something
        PageNumber++;

        mPresenter.onMorePostsRequest();
        loading = true;

    }
  }

  @Override
  public boolean onNavigationItemSelected(@NotNull MenuItem item) {
    // Handle navigation view item clicks here.
    id = item.getItemId();
    Log.w("ASD", Integer.toString(id));
    if (id == R.id.settings) {

      startActivity(new Intent(this, SettingsActivity.class));
    }
    else {

      PageNumber = 0;
      loading = true;
      previousTotal = 0;

      mPresenter.onPostTypeChanged(id);
    }

    DrawerLayout drawer = findViewById(R.id.drawer_layout);
    drawer.closeDrawer(GravityCompat.START);
    return true;
  }

  @Override public void callbackFunctionUpload(Object data) {

  }

  @Override public void setPresenter(MainContract.Presenter presenter) {
    mPresenter = checkNotNull(presenter);
  }

  @Override public void showPosts(List<ImagesData> data) {
    adapter = new RecyclerAdapter(data, MainActivity.this);
    recyclerView.setAdapter(adapter);
    Toast.makeText(MainActivity.this, "first page is loaded...",
        Toast.LENGTH_LONG).show();

  }

  @Override public void addPostsToTheRecycler(List<ImagesData> data) {
    adapter.addImages(data);
    Toast.makeText(MainActivity.this, "more posts are loaded...",
        Toast.LENGTH_LONG).show();
  }

  @Override public void showUserInformations(User user) {
    Picasso.with(MainActivity.this).load(user.getAvatar()).into(avatar);
    url.setText(user.getReputation_name());
    username.setText(user.getURL());
  }

  @Override public void showErrorMessage(String message) {
    Log.w("EROOR " , message);
  }

  @Override public void showProgressBar() {
    progressBar.setVisibility(View.VISIBLE);
  }

  @Override public void hideProgressBar() {
    progressBar.setVisibility(View.GONE);
  }

  @Override public void postsNotAvilable() {
        Toast.makeText(MainActivity.this,"there are no posts avilable to show...",Toast.LENGTH_LONG).show();
  }

  @Override public void accountNotAvilable() {
    Toast.makeText(MainActivity.this,"there are no information avilable for this account...",Toast.LENGTH_LONG).show();
  }
}
