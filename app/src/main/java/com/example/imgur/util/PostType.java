package com.example.imgur.util;

public enum PostType {

  myPosts("myPosts", 0),
  hotPosts("hotPosts", 1),
  topPosts("topPosts", 2);

  private final String stringValue;
  private final int intValue;

  PostType(String toString, int value) {
    stringValue = toString;
    intValue = value;
  }

  @Override
  public String toString() {
    return stringValue;
  }

  public String getPostsType() {
    return stringValue;
  }

  public int getPostsValue() {
    return intValue;
  }

}
