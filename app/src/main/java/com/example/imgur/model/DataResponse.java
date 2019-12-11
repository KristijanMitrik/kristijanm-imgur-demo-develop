package com.example.imgur.model;

import com.google.gson.annotations.SerializedName;

public class DataResponse<T> {

  @SerializedName("data")
  T images;

  @SerializedName("success")
  private String status;

  public String getStatus() {
    return status;
  }

  public T getData() {
    return images;
  }
}

