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

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class DownloadService extends Service {
    private DownloadTask downloadTask;
    private NotificationManager notificationManager;
    private DownloadBinder downloadBinder = new DownloadBinder();

    //创建一个Binder用于绑定服务的时候返回给GoodSample活动,GoodSample活动通过这个Binder去操作下载任务(downloadTask)
    class DownloadBinder extends Binder {

        public void startDownload(String str){
            if(downloadTask == null){
                //其实这里downloadListener的工作原理就是一个C++中的回调函数
                downloadTask = new DownloadTask(downloadListener);
            }

            //执行异步任务的时候可以通过传参给doInBackGround传递参数
            downloadTask.execute(str);
            //启动前台服务
            startForeground(1, getNofification("Download...", 0));
        }

        public void pauseDownload(){
            downloadTask.pauseDownload();
        }

        public void CanceledDownload(){
            downloadTask.cancelDownload();
        }

    }

    //实现该接口传给异步任务,可以在异步任务中根据下载的情况实时更新通知信息
    /*
        这里在异步任务中操作的也是服务中的 通知对象,如果再异步任务中去创建通知是否也可以更新覆盖服务中创建的通知 待验证。。。
     */
    private DownloadListener downloadListener = new DownloadListener() {
        @Override
        public void onProgress(int progress) {
            if(notificationManager == null){
                notificationManager = getNotificationManager();
            }

            notificationManager.notify(1, getNofification("Download...", progress));
        }

        @Override
        public void onSuccess() {
            downloadTask = null;//垃圾回收机制
            //下载成功的时候,把前台服务关闭,并创建一个下载成功的通知
            stopForeground(true);
           if(notificationManager == null){
               notificationManager = getNotificationManager();
           }

           notificationManager.notify(1, getNofification("Download Success", -1));
        }

        @Override
        public void onFailed() {
            downloadTask = null;
            stopForeground(true);
            if(notificationManager == null){
                notificationManager = getNotificationManager();
            }

            notificationManager.notify(1, getNofification("Download Failed", -1));
        }

        @Override
        public void onPaused() {
            downloadTask = null;
        }

        @Override
        public void onCanceled() {
            downloadTask = null;
            stopForeground(true);
        }
    };


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return downloadBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public NotificationManager getNotificationManager(){
        if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(
                    "downloadService", "downloadServiceNotificationChannel", NotificationManager.IMPORTANCE_LOW);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
            return notificationManager;
        }else{
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            return notificationManager;
        }

    }

    public Notification getNofification(String title, int progress){
        Intent intent = new Intent(this, GoodSample.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "downloadService")
                .setContentTitle(title)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setContentIntent(pendingIntent);

        if(progress > 0){
            builder.setContentText(progress + "%")
                    .setProgress(100, progress, false);
        }

        return builder.build();

    }
}
