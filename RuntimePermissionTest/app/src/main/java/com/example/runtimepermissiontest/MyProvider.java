package com.example.runtimepermissiontest;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MyProvider extends ContentProvider {
    public static final int TABLE1_DIR = 0;
    public static final int TABLE1_ITEM = 1;
    public static final int TABLE2_DIR = 2;
    public static final int TABLE2_ITEM = 3;
    //这个类用来实现匹配Uri内容的功能
    /**
     * uri的格式一般有两种 "包名/表名" "包名/表名/id"
     * 可以用通配符去匹配这两种格式
     * * 表示匹配任意长度的字符    == 包名/*
     * # 表示匹配任意长度的数字*   == 包名/表名/# */
    private static UriMatcher uriMatcher;
    private MyDataBaseHelper myDataBaseHelper;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI("com.example.runtimepermissiontest", "Student", TABLE1_DIR);
        uriMatcher.addURI("com.example.runtimepermissiontest", "Student/#", TABLE1_ITEM);
        uriMatcher.addURI("com.example.runtimepermissiontest", "Car", TABLE2_DIR);
        uriMatcher.addURI("com.example.runtimepermissiontest", "Car/#", TABLE2_ITEM);
    }
    /**内容提供器初始化的时候才会去调用
     * 通常这里会完成数据库的创建和升级等操作
     * 只有当ContentResolver尝试访问我们程序的数据的时候才会去调用**/
    @Override
    public boolean onCreate() {
        myDataBaseHelper = new MyDataBaseHelper(getContext(), "FirstDb.db", null, 1);
        return true;
    }
    /**
     * 内容提供器查询数据
     * uri 是查询哪个表
     * projection 查询哪些列
     * selection selectionArgs 行的约束条件
     * sortOrder 排序**/
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db = myDataBaseHelper.getWritableDatabase();
        Cursor cursor = null;
        switch (uriMatcher.match(uri)){
            case TABLE1_DIR:
                //查询这个表table1
                cursor = db.query("Student", projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case TABLE1_ITEM:
                //查询表table1某条数据
                String StudentId = uri.getPathSegments().get(1);
                cursor = db.query("Student", projection, "id = ?", new String[]{StudentId}, null, null, sortOrder);
                break;
            case TABLE2_DIR:
                //查询这个表table2
                cursor = db.query("Car", projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case TABLE2_ITEM:
                //查询表table2某条数据
                String CarId = uri.getPathSegments().get(1);
                cursor = db.query("Car", projection, "id = ?", new String[]{CarId}, null, null, sortOrder);
                break;
            default:
                break;
        }
        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        SQLiteDatabase db = myDataBaseHelper.getWritableDatabase();
        Uri uriReturn = null;
        switch (uriMatcher.match(uri)){
            case TABLE1_DIR:
            case TABLE1_ITEM:
                long StudentId = db.insert("Student", null, values);
                uriReturn = Uri.parse("content://" + "com.example.runtimepermissiontest/" + "Student/" + StudentId);
                break;
            case TABLE2_DIR:
            case TABLE2_ITEM:
                long CarId = db.insert("Student", null, values);
                uriReturn = Uri.parse("content://" + "com.example.runtimepermissiontest/" + "Car/" + CarId);
                break;
            default:
                break;
        }
        return uriReturn;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = myDataBaseHelper.getWritableDatabase();
        int deleteRows = 0;
        switch (uriMatcher.match(uri)){
            case TABLE1_DIR:
                deleteRows = db.delete("Student", selection, selectionArgs);
                break;
            case TABLE1_ITEM:
                String StudentId = uri.getPathSegments().get(1);
                deleteRows = db.delete("Student", "id = ?", new String[]{StudentId});
                break;
            case TABLE2_DIR:
                deleteRows = db.delete("Car", selection, selectionArgs);
                break;
            case TABLE2_ITEM:
                String CarId = uri.getPathSegments().get(1);
                deleteRows = db.delete("Car", "id = ?", new String[]{CarId});
                break;
            default:
                break;
        }
        return deleteRows;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = myDataBaseHelper.getWritableDatabase();
        int updateRows = 0;
        switch (uriMatcher.match(uri)){
            case TABLE1_DIR:
                updateRows = db.update("Student", values, selection, selectionArgs);
                break;
            case TABLE1_ITEM:
                String StudentId = uri.getPathSegments().get(1);
                updateRows = db.update("Student", values, "id = ?", new String[]{StudentId});
                break;
            case TABLE2_DIR:
                updateRows = db.update("Student", values, selection, selectionArgs);
                break;
            case TABLE2_ITEM:
                String CarId = uri.getPathSegments().get(1);
                updateRows = db.update("Student", values, "id = ?", new String[]{CarId});
                break;
            default:
                break;
        }
        return updateRows;
    }

    /**所有内容提供器必须提供的一个方法
     * 用于获取Uri对象的MIME类型
     * 一个Uri对应的Mime字符串主要由下面3部分构成
     * 1.必须以vnd开头
     * 2.如果内容Uri以路径结尾则后接android.cursor.dir/ 如果以id结尾则后接android.cursor.item
     * 3.最后接上vnd.<authority>.<path>
     * **/
    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)){
            case TABLE1_DIR:
                return "vnd.android.cursor.dir/com.example.runtimepermissiontest.table1";
            case TABLE1_ITEM:
                return "vnd.android.cursor.item/com.example.runtimepermissiontest.table2";
            case TABLE2_DIR:
                return "vnd.android.cursor.dir/com.example.runtimepermissiontest.table1";
            case TABLE2_ITEM:
                return "vnd.android.cursor.item/com.example.runtimepermissiontest.table2";
            default:
                break;
        }
        return null;
    }
}
