package com.example.broadcasttest;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends BaseActivity {
/**动态广播注册**/
    //意图过滤器
    private IntentFilter intentFilter;
    //广播监听器
    private NetworkChangeReceiver networkChangeReceiver;
    //private LocalNetworkChangeReceiver localNetworkChangeReceiver;
    //private LocalBroadcastManager localBroadcastManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //本地广播管理器,获取实例,只能对应用程序中的广播进行接收发送
        //localBroadcastManager = LocalBroadcastManager.getInstance(this);
        intentFilter = new IntentFilter();
        //设置广播接收的优先级
        //intentFilter.setPriority(100);
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        intentFilter.addAction("com.example.broadcasttest.MY_BROADCAST");

        networkChangeReceiver = new NetworkChangeReceiver();
        //localNetworkChangeReceiver = new LocalNetworkChangeReceiver();
        //注册
        registerReceiver(networkChangeReceiver, intentFilter);
        //本地广播注册
        //localBroadcastManager.registerReceiver(localNetworkChangeReceiver,intentFilter);

        Button button = (Button)findViewById(R.id.button_send);
        Button buttonGo = (Button)findViewById(R.id.button_go);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**发送广播**/
                Intent intent = new Intent("com.example.broadcasttest.MY_BROADCAST");
                //发送标准广播
                //sendBroadcast(intent);
                //发送有序广播
                sendOrderedBroadcast(intent, null);
                //发送本地广播
                //localBroadcastManager.sendBroadcast(intent);
            }
        });

        buttonGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction("com.example.broadcasttest.GOTEST");
                startActivity(intent);
            }
        });
    }

    class NetworkChangeReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() == "com.example.broadcasttest.MY_BROADCAST"){
                Toast.makeText(MainActivity.this, "My BROADCAST", Toast.LENGTH_SHORT).show();
            }else {
                //系统服务类,管理网络连接的
                ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                //需要加权限
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isAvailable()){
                    Toast.makeText(MainActivity.this, "network isAvailable", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity.this, "network isUnAvailable", Toast.LENGTH_SHORT).show();
                }
            }


        }
    }

    //这个用来验证本地广播
    class LocalNetworkChangeReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(networkChangeReceiver);
       // localBroadcastManager.unregisterReceiver(localNetworkChangeReceiver);
    }
}
