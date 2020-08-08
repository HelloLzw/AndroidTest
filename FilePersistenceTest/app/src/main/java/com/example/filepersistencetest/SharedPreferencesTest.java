package com.example.filepersistencetest;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SharedPreferencesTest extends BaseActivity {
    private Button ButtonSave;
    private Button ButtonGet;
    private static final String TAG = "SharedPreferencesTest";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sharedpreferences_layout);

        ButtonSave = (Button)findViewById(R.id.bn_save);
        ButtonGet = (Button)findViewById(R.id.bn_get);

        ButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //参数1 文件名, 参数2 表示只有该应用程序可以进行读写(目前只能选这个模式)
                //文件会保存在data/data下面
                SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
                editor.putString("name", "Tom");
                editor.putInt("age", 28);
                editor.putBoolean("married", false);
                editor.apply();
                Toast.makeText(SharedPreferencesTest.this, "保存成功!", Toast.LENGTH_SHORT).show();
            }
        });

        ButtonGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
                String name = sharedPreferences.getString("name", "");
                int age = sharedPreferences.getInt("age", -1);
                boolean married = sharedPreferences.getBoolean("married", false);
                Log.e(TAG, "name:" + name);
                Log.e(TAG, "age:" + age);
                Log.e(TAG, "married:" + married);
                Toast.makeText(SharedPreferencesTest.this, "取数据成功!", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
