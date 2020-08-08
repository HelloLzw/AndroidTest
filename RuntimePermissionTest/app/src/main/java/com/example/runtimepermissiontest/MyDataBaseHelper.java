package com.example.runtimepermissiontest;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class MyDataBaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "MyDataBaseHelper";
    private static final String CREATE_STUDENT = "create table Student("
            +"id integer primary key,"
            +"name text,"
            +"age int)";

    private static final String CREATE_CAR = "create table Car("
            +"id integer primary key,"
            +"name text,"
            +"pirce real)";

    private Context mContext;

    public MyDataBaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_STUDENT);
        db.execSQL(CREATE_CAR);
        Log.e(TAG, "onCreate: 创建Student表和Car表");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
