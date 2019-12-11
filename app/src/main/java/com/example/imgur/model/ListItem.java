package com.example.imgur.model;

import com.example.imgur.data.Images;
import com.example.imgur.data.ImagesData;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ListItem {

  @SerializedName("title")
  private String Head;
  @SerializedName("description")
  private String Desc;
  @SerializedName("link")
  private String ImageUrl;
  @SerializedName("is_album")
  private boolean isalbum;
  @SerializedName("images")
  private List<ImagesData> img;

  public ListItem(String head, boolean is, String desc, String imageUrl, List<ImagesData> img) {
    Head = head;
    Desc = desc;
    ImageUrl = imageUrl;
    this.img = img;
    isalbum = is;
  }

  public String getHead() {
    return Head;
  }

  public String getDesc() {
    return Desc;
  }

  public String getImageUrl() {
    return ImageUrl;
  }

  public List<ImagesData> getImg() {
    return img;
  }



  public boolean getIsalbum() {
    return isalbum;
  }

  public void setIsalbum(boolean isalbum) {
    this.isalbum = isalbum;
  }
}