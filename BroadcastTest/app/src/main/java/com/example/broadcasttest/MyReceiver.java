package com.example.broadcasttest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
//静态注册广播接收器(Andorid8.0开始增加诸多限制,建议不要再使用静态注册了)
public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        Toast.makeText(context, "Boot Complete", Toast.LENGTH_SHORT).show();
        // an Intent broadcast.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
