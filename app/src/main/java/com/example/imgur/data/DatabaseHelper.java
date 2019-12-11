package com.example.imgur.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

  // Database Version
  private static final int DATABASE_VERSION = 1;

  // Database Name
  private String DATABASE_NAME = "";

  public DatabaseHelper(Context context, String Database) {
    super(context, Database, null, DATABASE_VERSION);
    this.DATABASE_NAME = Database;
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    db.execSQL(Images.CREATE_TABLE);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    db.execSQL("DROP TABLE IF EXISTS " + Images.TABLE_NAME);
    onCreate(db);
  }

  public void dropTable(SQLiteDatabase db)
  {
    db.execSQL("DROP TABLE IF EXISTS " + Images.TABLE_NAME);
  }
  public long insertImage(String title, String desc, String url) {
    // get writable database as we want to write data
    SQLiteDatabase db = this.getWritableDatabase();

    ContentValues values = new ContentValues();
    // `id` and `timestamp` will be inserted automatically.
    // no need to add them

    values.put(Images.COLUMN_Head, title);
    values.put(Images.COLUMN_Desc, desc);
    values.put(Images.COLUMN_URL, url);

    // insert row
    long id1 = db.insert(Images.TABLE_NAME, null, values);

    // close db connection
    db.close();
    return id1;
    // return newly inserted row id

  }

  //Delete all
  public void deleteAll() {
    SQLiteDatabase db = this.getWritableDatabase();
    db.execSQL("DELETE FROM " + Images.TABLE_NAME);
    db.close();
  }

  public Images getImage(String id) {
    // get readable database as we are not inserting anything
    SQLiteDatabase db = this.getReadableDatabase();

    Cursor cursor = db.query(Images.TABLE_NAME,
        new String[] {Images.COLUMN_ID, Images.COLUMN_Head, Images.COLUMN_Desc, Images.COLUMN_URL},
        Images.COLUMN_ID + "=?",
        new String[] {String.valueOf(id)}, null, null, null, null);

    if (cursor != null) { cursor.moveToFirst(); }

    // prepare note object
    Images image = new Images(
        cursor.getString(cursor.getColumnIndex(Images.COLUMN_URL)),
        cursor.getString(cursor.getColumnIndex(Images.COLUMN_Head)),
        cursor.getString(cursor.getColumnIndex(Images.COLUMN_Desc))
    );

    // close the db connection
    cursor.close();

    return image;
  }

  public List<Images> getAllImages() {
    List<Images> images = new ArrayList<>();

    // Select All Query
    String selectQuery = "SELECT  * FROM " + Images.TABLE_NAME + " ORDER BY " +
        Images.COLUMN_Head + " DESC";

    SQLiteDatabase db = this.getWritableDatabase();
    Cursor cursor = db.rawQuery(selectQuery, null);

    // looping through all rows and adding to list
    if (cursor.moveToFirst()) {
      do {
        Images image = new Images();
        image.setId(cursor.getInt(cursor.getColumnIndex(Images.COLUMN_ID)));
        image.setTitle(cursor.getString(cursor.getColumnIndex(Images.COLUMN_Head)));
        image.setDesc(cursor.getString(cursor.getColumnIndex(Images.COLUMN_Desc)));
        image.setLink(cursor.getString(cursor.getColumnIndex(Images.COLUMN_URL)));

        images.add(image);
      } while (cursor.moveToNext());
    }

    // close db connection
    db.close();

    // return notes list
    return images;
  }

  public int getImagesCount() {
    String countQuery = "SELECT  * FROM " + Images.TABLE_NAME;
    SQLiteDatabase db = this.getReadableDatabase();
    Cursor cursor = db.rawQuery(countQuery, null);

    int count = cursor.getCount();
    cursor.close();

    // return count
    return count;
  }

  public int updateImage(Images image) {
    SQLiteDatabase db = this.getWritableDatabase();

    ContentValues values = new ContentValues();
    values.put(Images.COLUMN_Head, image.getTitle());
    values.put(Images.COLUMN_Desc, image.getDesc());
    values.put(Images.COLUMN_URL, image.getLink());

    // updating row
    return db.update(Images.TABLE_NAME, values, Images.COLUMN_ID + " = ?",
        new String[] {String.valueOf(image.getId())});
  }

  public void deleteNote(Images image) {
    SQLiteDatabase db = this.getWritableDatabase();
    db.delete(Images.TABLE_NAME, Images.COLUMN_ID + " = ?",
        new String[] {String.valueOf(image.getId())});
    db.close();
  }
}
