package com.example.serviceandthread;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;

//服务的最佳实践,完整版的下载实例
public class GoodSample extends AppCompatActivity implements View.OnClickListener {
    private Button buttonStart;
    private Button buttonPause;
    private Button buttonCancel;
    private DownloadService.DownloadBinder downloadBinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_good_sample);

        Intent intent = new Intent(GoodSample.this, DownloadService.class);
        bindService(intent, conn, Context.BIND_AUTO_CREATE);

        buttonStart = (Button) findViewById(R.id.button_start);
        buttonPause = (Button) findViewById(R.id.button_pause);
        buttonCancel = (Button) findViewById(R.id.button_cancel);
        buttonStart.setOnClickListener(this);
        buttonPause.setOnClickListener(this);
        buttonCancel.setOnClickListener(this);
    }

    //创建ServiceConnection与service通信,取得bind服务的时候返回IBinder,对下载的开始暂停以及取消进行操作
    private ServiceConnection conn = new ServiceConnection(){

        //绑定服务的时候会回调下面这个方法
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //在这里启动下载
            downloadBinder = (DownloadService.DownloadBinder)service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_start:
                downloadBinder.startDownload("www.12345.com");
                break;
            case R.id.button_pause:
                downloadBinder.pauseDownload();
                break;
            case R.id.button_cancel:
                downloadBinder.CanceledDownload();
                break;
            default:
                break;
        }
    }
}
