package com.example.imgur.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import com.google.gson.annotations.SerializedName;
import java.util.UUID;
import javax.annotation.Nullable;

@Entity(tableName = "ImagesData")
public final class ImagesData {


  @PrimaryKey
  @NonNull
  @ColumnInfo(name = "entryid")
  @SerializedName("id")
  private final  String mId;

  @Nullable
  @ColumnInfo(name = "link")
  @SerializedName("link")
  private final String link;

  @Nullable
  @ColumnInfo(name = "title")
  @SerializedName("title")
  private final String title;

  @Nullable
  @ColumnInfo(name = "description")
  @SerializedName("description")
  private final String desc;




  @ColumnInfo(name = "type")
  @SerializedName("type")
  private final transient int type;

  public ImagesData(@Nullable String link, @Nullable String title, @Nullable String desc, int type , @NonNull  String id) {
    this.mId = id;
    this.desc = desc;
    this.title = title;
    this.link = link;
    this.type = type;
  }

  public String getLink() {
    return link;
  }

  public String getTitle() {
    return title;
  }

  public String getDesc() {
    return desc;
  }

  public String getId() {
    return mId;
  }

  public int getType(){return type;}
}
