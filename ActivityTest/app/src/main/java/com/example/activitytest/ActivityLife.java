package com.example.activitytest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class ActivityLife extends AppCompatActivity {

    private static final String TAG = "ActivityLife";

    //活动第一次创建的时候调用
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.life_layout);
        Log.e(TAG, "onCreate: ");

        if (savedInstanceState != null)
            Log.e(TAG, "savedInstanceState:" + savedInstanceState.getString("data"));

        Button buttonDialog = (Button) findViewById(R.id.button_dialog);
        Button buttonNormal = (Button) findViewById(R.id.button_normal);

        buttonDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.exmaple.activitytest.DIALOG");
                startActivity(intent);
            }
        });

        buttonNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityLife.this, ThirdActivity.class);
                startActivity(intent);
            }
        });
    }

    //活动由不可见变可见的时候调用
    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG, "onStart: ");
    }

    //活动准备好和用户进行交互的时候调用,此时活动一定位于栈顶,并且处于运行状态
    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume: ");
    }

    //在系统准备去启动或者恢复另一个活动的时候调用
    //通常会在这个活动中把一些消耗CPU的资源释放掉,这个方法的执行速度一定要够快,不然会影响到新的栈顶活动使用
    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "onPause: ");
    }

    //在活动完全不可见的时候调用,和onPause的区别在于如果启动的新的活动为对话框的形式那么会调用onPause但不会调用onStop
    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG, "onStop: ");
    }

    //在活动被销毁前调用,之后该活动会变为销毁状态
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy: ");
    }

    //在活动由停止状态变为运行状态的时候调用,也就是活动被重启
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(TAG, "onRestart: ");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("data", "hello this is onSaveInstanceState data");
    }
}
