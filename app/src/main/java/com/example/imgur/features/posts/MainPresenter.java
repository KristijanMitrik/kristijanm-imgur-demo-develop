package com.example.imgur.features.posts;

import android.support.annotation.NonNull;
import com.example.imgur.R;
import com.example.imgur.data.ImagesData;
import com.example.imgur.data.source.PostsDataSource;
import com.example.imgur.data.source.PostsRepository;
import com.example.imgur.model.DataResponse;
import com.example.imgur.model.ListItem;
import com.example.imgur.user.User;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class MainPresenter
    implements MainContract.Presenter {

  private final MainContract.View mPostsView;
  private final PostsRepository mTasksRepository;
  private boolean pageNumber;
  private int postType;

  public MainPresenter(@NonNull PostsRepository tasksRepository,
      @NonNull MainContract.View postsView) {
    mPostsView = checkNotNull(postsView, "tasksView cannot be null!");
    mTasksRepository = checkNotNull(tasksRepository, "tasksRepository cannot be null");
    mPostsView.setPresenter(this);
    postType = R.id.MyPosts;
    pageNumber = false;
  }

  @Override public void start() {
    getUserInfo();
    onPostTypeChanged(postType);
  }

  @Override public void stop() {

  }

  private void getUserInfo() {
    mTasksRepository.getAccountInfo().subscribe(new DisposableSingleObserver<User>() {
      @Override public void onSuccess(User userSingle) {
        mPostsView.showUserInformations(userSingle);
      }

      @Override public void onError(Throwable e) {
        mPostsView.accountNotAvilable();
      }
    });
  }

  @Override public void onPostTypeChanged(int postType) {
    this.postType = postType;
    this.pageNumber = false;
    mPostsView.showProgressBar();
    mTasksRepository.getPosts(this.postType)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new DisposableSingleObserver<List<ImagesData>>() {
          @Override public void onSuccess(List<ImagesData> posts) {
            if (posts.size() == 0) {
              mPostsView.postsNotAvilable();
            } else {
              mPostsView.showPosts(posts);
            }
            mPostsView.hideProgressBar();
          }

          @Override public void onError(Throwable e) {
            mPostsView.showErrorMessage(e.getMessage());
            mPostsView.hideProgressBar();
          }
        });
  }

  @Override public void onMorePostsRequest() {
    pageNumber = true;
    mPostsView.showProgressBar();
    mTasksRepository.getPosts(postType).subscribe(new DisposableSingleObserver<List<ImagesData>>() {
      @Override public void onSuccess(List<ImagesData> posts) {
        if(posts.size()==0)
        {
          mPostsView.postsNotAvilable();
        }
        else {
          mPostsView.addPostsToTheRecycler(posts);
        }
        mPostsView.hideProgressBar();
      }

      @Override public void onError(Throwable e) {
        mPostsView.showErrorMessage(e.getMessage());
      }
    });
  }
}
