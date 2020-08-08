package com.example.serviceandthread;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "[lzw-MainActivity]";
    private Button buttonChangeText;
    private TextView textViewText;
    private Button buttonDownload;
    private TextView downloadText;
    private Button buttonStartService;
    private Button buttonStopService;
    private Button buttonBindService;
    private Button buttonUnBindService;
    private Button buttonGoodSample;
    private TextView textViewService;
    private MyAsyncTask myAsyncTask;
    public static final int UPDATE_TEXT = 1;


    //线程通信
    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case UPDATE_TEXT:
                    Log.i(TAG, "get UPDATE_TEXT msg");
                    textViewText.setText("通过线程通信更改UI内容成功!");
                    Log.i(TAG, "setText After");
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);

        init();
        makeUiFunction();

    }

    private void init(){
        buttonChangeText = (Button) findViewById(R.id.button_change_text);
        textViewText = (TextView) findViewById(R.id.tv_text);
        buttonDownload = (Button) findViewById(R.id.button_download);
        downloadText = (TextView) findViewById(R.id.tv_download_text);
        buttonStartService = (Button) findViewById(R.id.button_start_service);
        buttonStopService = (Button) findViewById(R.id.button_stop_service);
        textViewService = (TextView) findViewById(R.id.tv_service);
        buttonBindService = (Button) findViewById(R.id.button_bind_service);
        buttonUnBindService = (Button) findViewById(R.id.button_unbind_service);
        buttonGoodSample = (Button) findViewById(R.id.button_to_goodsample_service);
    }

    private void makeUiFunction(){
        buttonChangeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i(TAG, "onClick Here");
                        Message message = new Message();
                        message.what = UPDATE_TEXT;
                        handler.sendMessage(message);
                    }
                }).start();
            }
        });

        buttonDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myAsyncTask = new MyAsyncTask();
                myAsyncTask.execute();
            }
        });

        buttonStartService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startServiceIntent = new Intent(MainActivity.this, MyService.class);
                startService(startServiceIntent);
            }
        });

        buttonStopService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent stopServiceIntent = new Intent(MainActivity.this, MyService.class);
                stopService(stopServiceIntent);
            }
        });

        buttonBindService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MyService.class);
                intent.putExtra("Name", "Lzw");
                intent.putExtra("Age", 25);

                //第三个参数表示绑定后调用Create
                bindService(intent, conn, Context.BIND_AUTO_CREATE);

            }
        });

        buttonUnBindService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unbindService(conn);
            }
        });
        buttonGoodSample.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, GoodSample.class));
            }
        });
    }


    ServiceConnection conn = new ServiceConnection() {
        private MyService.DownloadBinder downloadBinder;
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i(TAG, "onServiceConnected Enter");

            downloadBinder = (MyService.DownloadBinder)service;
            downloadBinder.startDownload();
            downloadBinder.getProgress();
        }
        //onServiceDisconnected() 在连接正常关闭的情况下是不会被调用的,
        // 该方法只在Service 被破坏了或者被杀死的时候调用. 例如, 系统资源不足, 要关闭一些Services,
        // 刚好连接绑定的 Service 是被关闭者之一,  这个时候onServiceDisconnected() 就会被调用。
        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG, "onServiceDisconnected Enter");
        }
    };

    /*
    异步任务
    AsyncTask<Params, Progress, Result>
    Params      执行异步任务的需要传的参数,doInBackground的参数类型
    Progress    显示执行的进度的类型
    Result      异步任务执行完后返回的参数类型,doInBackground的返回类型
    */
    private class MyAsyncTask extends AsyncTask<Void, Integer, Boolean> {
        private int progressDown = 0;

        //该方法会在doInBackground前执行以下,常用来做一些界面初始化的操作
        @Override
        protected void onPreExecute() {
            Toast.makeText(MainActivity.this, "开始下载!", Toast.LENGTH_SHORT).show();
        }

        //在异步任务中要实现的功能,该函数所有操作都会在子线程中运行
        @Override
        protected Boolean doInBackground(Void... voids) {
            //模拟一个下载任务
            while (progressDown <= 100){
                //用来传递异步任务处理进度的方法,它的参数类型由泛型中的第二个决定
                publishProgress(progressDown);
                ++progressDown;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            if (values[0].intValue() < 100){
                downloadText.setText("正在下载[" + values[0].intValue() + "%]");
            }else {
                downloadText.setText("下载完成[" + values[0].intValue() + "%]");
            }

        }

        //当doInBackground处理完return的时候会调用这个函数
        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if(aBoolean)
                Toast.makeText(MainActivity.this, "下载成功!", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(MainActivity.this, "下载失败!", Toast.LENGTH_SHORT).show();
        }
    }

}
