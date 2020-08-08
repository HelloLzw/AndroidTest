package com.example.serviceandthread;

import android.os.AsyncTask;
import android.util.Log;

/*
异步任务
AsyncTask<Params, Progress, Result>
Params      执行异步任务的需要传的参数,doInBackground的参数类型
Progress    显示执行的进度的类型
Result      异步任务执行完后返回的参数类型,doInBackground的返回类型
*/

public class DownloadTask extends AsyncTask<String, Integer, Integer>{
    private static final String TAG = "DownloadTask";
    private final int TYPE_SUCCESS = 0;
    private final int TYPE_FAILED = 1;
    private final int TYPE_PAUSED = 2;
    private final int TYPE_CANCELED = 3;
    //这里是假设100为下载完成,实际可以通过获取下载url地址文件的大小来确定
    private final int lastProgress = 100;

    private String downloadUrl;
    private int downloadProgress = 0;
    private boolean isPaused = false;
    private boolean isCanceled = false;

    private DownloadListener downloadListener;


    public DownloadTask(DownloadListener downloadListener){
        this.downloadListener = downloadListener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Integer doInBackground(String... strings) {
        //取下载地址
        downloadUrl = strings[0];
        Log.i(TAG, "下载地址为" + downloadUrl);
        /*真正的下载操作后面再写吧,这个里写个假的模拟一下*/
        for(int i = 0; i < lastProgress; i++){
            ++downloadProgress;
            publishProgress(downloadProgress);

            if(isCanceled){
                return TYPE_CANCELED;
            }else if(isPaused){
                return TYPE_PAUSED;
            }

            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (downloadProgress == lastProgress){
            return TYPE_SUCCESS;
        }else {
            return TYPE_FAILED;
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        //把下载的进度通过接口抛出去
        downloadListener.onProgress(values[0]);
    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);
        switch (integer){
            case TYPE_SUCCESS:
                downloadListener.onSuccess();
                break;
            case TYPE_FAILED:
                downloadListener.onFailed();
                break;
            case TYPE_PAUSED:
                downloadListener.onPaused();
                break;
            case TYPE_CANCELED:
                downloadListener.onCanceled();
                break;
            default:
                break;
        }
    }

    public void pauseDownload(){
        isPaused = true;
    }

    public void cancelDownload(){
        isCanceled = true;
    }
}
