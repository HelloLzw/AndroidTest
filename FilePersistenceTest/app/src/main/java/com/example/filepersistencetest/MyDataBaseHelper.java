package com.example.filepersistencetest;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MyDataBaseHelper extends SQLiteOpenHelper {

    public static final String CREATE_CATEGORY = "create table Category("
            + "id integer primary key autoincrement,"
            + "Category_name text,"
            + "Category_code integer)";

    public static final String CREATE_BOOK = "create table Book("
            + "id integer primary key autoincrement,"
            + "author text,"
            + "price real,"
            + "pages integer,"
            + "name text)";

    private Context mContext;

    public MyDataBaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    //创建数据库的时候一次会调用下面这个方法
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_BOOK);
        Toast.makeText(mContext, "Create Success!", Toast.LENGTH_SHORT).show();
    }

    //升级数据库
    //如上,如果我创建了一个数据库 已经创建了一个表 现在希望再创建新的表 可以通过升级数据库的方法去实现
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists Book");
        db.execSQL("drop table if exists Category");
        db.execSQL(CREATE_BOOK);
        db.execSQL(CREATE_CATEGORY);
        Toast.makeText(mContext, "onUpgrade Success!", Toast.LENGTH_SHORT).show();
    }
}
