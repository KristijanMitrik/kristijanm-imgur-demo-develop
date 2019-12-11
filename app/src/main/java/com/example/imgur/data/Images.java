package com.example.imgur.data;

import com.google.gson.annotations.SerializedName;

public final class Images {

  public static final String TABLE_NAME = "ImagesData";
  public static final String COLUMN_ID = "id";
  public static final String COLUMN_Head = "title";
  public static final String COLUMN_Desc = "description";
  public static final String COLUMN_URL = "url";

  @SerializedName("link")
  private String link;
  @SerializedName("title")
  private String title;
  @SerializedName("description")
  private String desc;
  @SerializedName("id")
  private transient int id;
  @SerializedName("account_id")
  private int accid;

  public static final String CREATE_TABLE =
      "CREATE TABLE " + TABLE_NAME + "("
          + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
          + COLUMN_Head + " TEXT,"
          + COLUMN_Desc + " TEXT,"
          + COLUMN_URL + " TEXT"
          + ")";

  public Images(String link, String title, String desc) {
    this.link = link;
    this.title = title;
    this.desc = desc;
  }

  public Images() {}

  public String getLink() {
    return link;
  }

  public void setLink(String link) {
    this.link = link;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDesc() {
    return desc;
  }

  public void setDesc(String desc) {
    this.desc = desc;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }
}
