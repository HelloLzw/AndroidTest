package com.example.serviceandthread;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

/*
相关配置内容见AndroidManifest.xml
 */
public class MyService extends Service {
    private static final String TAG = "[lzw-MyService]";
    private DownloadBinder downloadBinder = new DownloadBinder();

    public MyService() {
    }

    //解绑的时候回调用,参数intent是干嘛的暂时没研究,要去抽烟了,下次研究
    @Override
    public boolean onUnbind(Intent intent) {
        Log.e(TAG, "onUnbind Enter");
        return super.onUnbind(intent);
    }

    //这里模拟一个下载任务,当在Activity中控制服务中下载的开始
     class DownloadBinder extends Binder{

        public void startDownload(){
            Log.i(TAG, "startDownload Enter");
        }

        public int getProgress(){
            Log.i(TAG, "getProgress Enter");
            return 0;
        }
    }

    //用来和Context通信
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        Log.e(TAG, "Intent Message Name = " + intent.getStringExtra("Name"));
        Log.e(TAG, "Intent Message Age = " + intent.getIntExtra("Age", 0));
        return downloadBinder;
        //throw new UnsupportedOperationException("Not yet implemented");
    }

    //服务创建的时候调用
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG,"service onCreate!");

        if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel notificationChannel = new NotificationChannel(
                    "myService", "myServiceNotificationChannel", NotificationManager.IMPORTANCE_LOW);
            notificationManager.createNotificationChannel(notificationChannel);

        }
        //前台服务创建方法
        //设置点击意图
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        Notification notification = new NotificationCompat.Builder(this, "myService")
                .setContentTitle("This is Content Title")
                .setContentText("This is ContentText！")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setContentIntent(pendingIntent)
                .build();

        //这里启动通知,同时把该服务变成一个前台服务
        startForeground(1, notification);

    }

    //每次服务启动的时候调用,无论start多少次都只会存在一个服务在后台运行
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG,"service onStartCommand!");
        return super.onStartCommand(intent, flags, startId);
    }

    //服务销毁的时候调用
    //真正杀死一个进程需要不绑定 如果之前调了start还要调stop
   @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG,"service onDestroy!");
    }
}
