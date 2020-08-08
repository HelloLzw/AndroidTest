package com.example.notificationtest;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button button;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        final NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        //Android8.0要求设置通知渠道
        if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
            //https://blog.csdn.net/yh18668197127/article/details/86299290
            /**
             *  * IMPORTANCE_NONE 关闭通知
             *  * IMPORTANCE_MIN 开启通知，不会弹出，但没有提示音，状态栏中无显示
             *  * IMPORTANCE_LOW 开启通知，不会弹出，不发出提示音，状态栏中显示
             *  * IMPORTANCE_DEFAULT 开启通知，不会弹出，发出提示音，状态栏中显示
             *  * IMPORTANCE_HIGH 开启通知，会弹出，发出提示音，状态栏中显示
             */
            NotificationChannel notificationChannel = new NotificationChannel("default", "name", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        Intent intent = new Intent(this, Test.class);
        //这里除了是Activity所以用getActivity 另外还有getBroastcast() getService()
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        //这里面的第二个参数要和通知渠道里面的id保持一致
        //还可以设置震动 音乐 提示灯闪烁等
        final Notification notification = new NotificationCompat.Builder(this, "default")
                .setContentTitle("This is Title")
                .setContentText("This is Text")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_test)//系统状态栏小图标(7.0之后图案有要求背景必须透明什么的,具体百度)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_test))
                .setContentIntent(pendingIntent)//设置点击意图
                //.setAutoCancel(true)//设置点击后状态栏通知消失//第一种取消通知的方法
                .build();

        button = (Button) findViewById(R.id.bn_sendNotf);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notificationManager.notify(1, notification);
            }
        });

    }
}
