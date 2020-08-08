package com.example.activitytest;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.time.LocalDate;

public class FirstActivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_item:
                Toast.makeText(FirstActivity.this, "u click add", Toast.LENGTH_SHORT).show();
                break;
            case R.id.remove_item:
                Toast.makeText(FirstActivity.this, "u click remove", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if(resultCode == RESULT_OK){
                    Log.e("onActivityResult", data.getStringExtra("backData"));
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("=FirstActivity=", this.toString());
        setContentView(R.layout.first_layout);
        Button button1 = findViewById(R.id.button_1);
        Button buttonWeb = (Button) findViewById(R.id.button_web);
        Button buttonCall = (Button) findViewById(R.id.button_call);
        Button buttonLife = (Button) findViewById(R.id.button_life);
        Button button_start_model = (Button) findViewById(R.id.button_start_model);

        button_start_model.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstActivity.this, FirstActivity.class);
                startActivity(intent);
            }
        });
        buttonLife.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstActivity.this, ActivityLife.class);
                startActivity(intent);
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 /**显示启动意图
                Intent toSecontIntent = new Intent(FirstActivity.this, SecondActivity.class);
                startActivity(toSecontIntent);**/
                //隐式启动意图
                Intent toSecondIntent = new Intent("com.example.activitytest.ACTION_START");
                toSecondIntent.addCategory("com.example.activitytest.MY_CATEGORY");
                //传递数据给下一个活动
                toSecondIntent.putExtra("data", "这是1发来的数据");
                //startActivity(toSecondIntent);
                /**返回数据给上一个活动(期望在活动销毁的时候返回数据给上一个活动)
                第二个参数是请求码,用于在之后的回调中判断数据的来源
                /tartActivityForResult启动一个活动,在启动的活动被销毁的时候,会回调当前Activity的onActivityResult()**/
                startActivityForResult(toSecondIntent, 1);
            }
        });

        //intent还可以去别的APP
        buttonWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent webIntent = new Intent(Intent.ACTION_VIEW);
                webIntent.setData(Uri.parse("http://www.baidu.com"));
                startActivity(webIntent);
            }
        });

        buttonCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:10086"));
                startActivity(intent);
            }
        });

    }
}
