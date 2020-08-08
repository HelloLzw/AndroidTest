package com.example.filepersistencetest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 *LitePal没有看
 */
public class DbTestActivity extends BaseActivity {
    private static final String TAG = "DbTestActivity";
    private MyDataBaseHelper myDataBaseHelper;
    private Button button;
    private Button buttonUp;
    private Button buttonAdd;
    private Button buttonDelete;
    private Button buttonUpdate;
    private Button buttonSelect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.dbtest_layout);

        myDataBaseHelper = new MyDataBaseHelper(this, "BookStore.db", null, 1);

        button = (Button)findViewById(R.id.bn_dbcreate);
        buttonUp = (Button)findViewById(R.id.bn_dbupdate);
        buttonAdd = (Button)findViewById(R.id.bn_add);;
        buttonDelete = (Button)findViewById(R.id.bn_delete);;
        buttonUpdate = (Button)findViewById(R.id.bn_update);;
        buttonSelect = (Button)findViewById(R.id.bn_select);;

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //调用该方法的时候可能会触发对应的onCreate和upDate
                myDataBaseHelper.getWritableDatabase();
            }
        });

        buttonUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //都过版本号去控制下面的函数触发条件
                myDataBaseHelper = new MyDataBaseHelper(DbTestActivity.this, "BookStore.db", null, 2);
                myDataBaseHelper.getWritableDatabase();
            }
        });

        /**
            下面的增删改查是用的Android里面的函数去实现的
            当然也可以通过db.exeSQL("SQL语句")实现增删改 db.rawQuery("SQL语句")实现查询
         **/
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //由于上面数据库已经升级到了版本2所以再使用的时候要使用版本2不然会出错
                myDataBaseHelper = new MyDataBaseHelper(DbTestActivity.this, "BookStore.db", null, 2);
                SQLiteDatabase db = myDataBaseHelper.getWritableDatabase();
                ContentValues contentValues = new ContentValues();
                contentValues.put("author", "Lzw");
                contentValues.put("price", 88.8);
                contentValues.put("pages", 100);
                contentValues.put("name", "Tom");
                //contentValues.put("id", 1);
                //contentValues.put("Category_name", "mike");
                //contentValues.put("Category_code", 1);
                //db.insert("Category", null, contentValues);
                db.insert("Book", null,contentValues);
                contentValues.clear();
                Toast.makeText(DbTestActivity.this, "ADD PRESS!", Toast.LENGTH_SHORT).show();
            }
        });

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDataBaseHelper = new MyDataBaseHelper(DbTestActivity.this, "BookStore.db", null, 2);
                SQLiteDatabase db = myDataBaseHelper.getWritableDatabase();
                ContentValues contentValues = new ContentValues();
                contentValues.put("price", 100);
                //这里面的?是这个占位符号,替最后一个参数占位的
                db.update("Book", contentValues, "name = ? and id = ?", new String[]{"Tom", "1"});
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDataBaseHelper = new MyDataBaseHelper(DbTestActivity.this, "BookStore.db", null, 2);
                SQLiteDatabase db = myDataBaseHelper.getWritableDatabase();

                db.delete("Book", "id > ?", new String[]{"2"});
            }
        });

        buttonSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDataBaseHelper = new MyDataBaseHelper(DbTestActivity.this, "BookStore.db", null, 2);
                SQLiteDatabase db = myDataBaseHelper.getWritableDatabase();

                Cursor cursor = db.query("Book",null,null,null,null,null,null);

                if (cursor.moveToFirst()){
                    do{
                        String name = cursor.getString(cursor.getColumnIndex("name"));
                        String author = cursor.getString(cursor.getColumnIndex("author"));
                        int pages = cursor.getInt(cursor.getColumnIndex("pages"));
                        double price = cursor.getDouble(cursor.getColumnIndex("price"));
                        Log.e(TAG, "name: " + name);
                        Log.e(TAG, "author: " + author);
                        Log.e(TAG, "pages: " + pages);
                        Log.e(TAG, "price: " + price);
                    }while (cursor.moveToNext());
                }

                cursor.close();
            }
        });

    }
}
