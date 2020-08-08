package com.example.filepersistencetest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class MainActivity extends BaseActivity {
    private EditText editText;
    private Button button;
    private Button toSql;
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        editText = (EditText) findViewById(R.id.et_input);
        button = (Button)findViewById(R.id.bn_toShare);
        toSql = (Button)findViewById(R.id.bn_toSql);
        String getData = load();
        Log.e(TAG, "getData = " + getData);
        if (!TextUtils.isEmpty(getData)){
            editText.setText(getData);
            editText.setSelection(getData.length());
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SharedPreferencesTest.class));
            }
        });

        toSql.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, DbTestActivity.class));
            }
        });
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        String text = editText.getText().toString();
        save(text);
    }

    /**文件存储方式(不太适合存储结构较为复杂的数据)**/
    //文件存储(流)写内容到文件
    public void save(String str) {
        FileOutputStream out = null;
        BufferedWriter writer = null;
        try {
            //默认写在/data/data/包名/files/
            //第一个参数不能包含文件夹路径,第二个参数表示覆盖
            out = openFileOutput("testdata", Context.MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(out));
            writer.write(str);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null)
                    writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public String load() {
        BufferedReader reader = null;
        FileInputStream in = null;
        StringBuilder content = new StringBuilder();
        try {
            in = openFileInput("testdata");
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            Log.e(TAG, "load[0]: ");
            while ((line = reader.readLine()) != null) {
                content.append(line);
                Log.e(TAG, "load[1]: " + content.toString());
            }
        } catch (IOException e) {
            Log.e(TAG, "load[2]: ");
            e.printStackTrace();
        } finally {
            try {
                if (reader != null)
                    reader.close();
            } catch (IOException e) {
                Log.e(TAG, "load[3]: ");
                e.printStackTrace();
            }

        }
        return content.toString();
    }
}
