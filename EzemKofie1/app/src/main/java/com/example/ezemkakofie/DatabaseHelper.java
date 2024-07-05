package com.example.ezemkakofie;

import android.app.usage.UsageStats;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteAbortException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonUiContext;
import androidx.annotation.Nullable;

import kotlin.jvm.internal.PropertyReference0Impl;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Reviews.db";
    private static final int DATABASE_VERSION = 2;

    private static final String TABLE_REVIEWS = "reviews";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_REVIEW = "review";
    private static final String COLUMN_RATING = "rating";
    private static final String COLUMN_PHOTO_URI = "photo_uri";

    private static final String CREATE_TABLE_REVIEWS = "CREATE TABLE " + TABLE_REVIEWS + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_REVIEW + " TEXT, " +
            COLUMN_RATING + " INTEGER NOT NULL, " +
            COLUMN_PHOTO_URI + " TEXT" +
            ");";

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_REVIEWS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + TABLE_REVIEWS + " ADD COLUMN " + COLUMN_PHOTO_URI + " TEXT");
        }
    }

    public long addReview(String review, int rating, String photoUri) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_REVIEW, review);
        values.put(COLUMN_RATING, rating);
        values.put(COLUMN_PHOTO_URI, photoUri);

        long id = db.insert(TABLE_REVIEWS, null, values);
        db.close();
        return id;
    }
}
