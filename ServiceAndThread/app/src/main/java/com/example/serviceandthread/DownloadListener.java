package com.example.serviceandthread;

//接口
/*
    作用自定义功能implements
    回调 通过初始化传参(例如本章的例子)
 */
public interface DownloadListener {

    void onProgress(int progress);

    void onSuccess();

    void onFailed();

    void onPaused();

    void onCanceled();
}
