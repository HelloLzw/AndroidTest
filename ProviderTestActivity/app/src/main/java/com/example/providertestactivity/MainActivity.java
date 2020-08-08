package com.example.providertestactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    private Button buttonAdd;
    private Button buttonDelete;
    private Button buttonUpdate;
    private Button buttonSelect;
    ContentResolver contentResolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        buttonAdd = (Button)findViewById(R.id.bn_add);;
        buttonDelete = (Button)findViewById(R.id.bn_delete);;
        buttonUpdate = (Button)findViewById(R.id.bn_update);;
        buttonSelect = (Button)findViewById(R.id.bn_select);;

        buttonAdd.setOnClickListener(this);
        buttonDelete.setOnClickListener(this);
        buttonUpdate.setOnClickListener(this);
        buttonSelect.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bn_add:
                Log.e(TAG, "bn_add");
                //这个content://是固定的
                Uri uriAdd = Uri.parse("content://com.example.runtimepermissiontest/Student");

                ContentValues values = new ContentValues();
                values.put("id", 1);
                values.put("name", "Lzw");
                values.put("age", 25);

                final Uri uri = getContentResolver().insert(uriAdd, values);
                values.clear();
                Log.e(TAG, "uriAdd: " + uri.toString());
                break;
            case R.id.bn_delete:
                Log.e(TAG, "bn_delete");
                //这个content://是固定的
                //这里我把id加上去删除id为1的
                Uri uriDel = Uri.parse("content://com.example.runtimepermissiontest/Student/1");
                int delRows = 0;
                delRows = getContentResolver().delete(uriDel, null, null);
                Toast.makeText(MainActivity.this,"删除Student表" + delRows + "行数据", Toast.LENGTH_SHORT).show();
                break;
            case R.id.bn_update:
                Log.e(TAG, "bn_update");
                ContentValues updateValues = new ContentValues();
                updateValues.put("name", "Sunam");
                Uri upUri = Uri.parse("content://com.example.runtimepermissiontest/Student");
                int upRows = getContentResolver().update(upUri, updateValues,"name = ?", new String[]{"Lzw"});
                updateValues.clear();
                Toast.makeText(MainActivity.this,"更新Student表name为Lzw的" + upRows + "行数据", Toast.LENGTH_SHORT).show();
                break;
            case R.id.bn_select:
                Log.e(TAG, "bn_select");
                Uri selectUri = Uri.parse("content://com.example.runtimepermissiontest/Student");
                Cursor cursor = getContentResolver().query(selectUri, null, null, null, null);
                if (cursor.moveToFirst()){
                    do {
                        String name = cursor.getString(cursor.getColumnIndex("name"));
                        int age = cursor.getInt(cursor.getColumnIndex("age"));
                        Log.e(TAG, "查询Student数据成功 name = " + name + "age = " + age);
                    }while(cursor.moveToNext());
                }
                cursor.close();
                break;
            default:
                break;
        }
    }
}
