package com.example.mainactivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.annotation.Target;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "MainActivity";
    //private WebView webView;
    private Button buttonSendRequest;
    private TextView textViewShowResponse;
    private ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);

        buttonSendRequest = (Button)findViewById(R.id.button_send_requeset);
        textViewShowResponse = (TextView) findViewById(R.id.textview_respone);
        scrollView = (ScrollView) findViewById(R.id.scrollview);

        buttonSendRequest.setOnClickListener(this);
/*        webView = (WebView)findViewById(R.id.web_view);
        //设置属性支持javaScript解析
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.loadUrl("http://www.baidu.com");*/
    }

    public void sendRequestWithHttpURLConnection(){
        //网络操作放在多线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader reader = null;

                try {
                    URL url = new URL("http://10.0.2.2/get_data.xml");
                    //初始化HttpURLConnection
                    connection = (HttpURLConnection) url.openConnection();
                    //设置相关参数
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    connection.setRequestMethod("GET");

                    /*
                        如果是发送给服务器数据
                        connection.setRequestMethod("POST");
                        OutputStream outputStream = connection.getOutputStream();
                        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
                        dataOutputStream.writeBytes("666666");
                     */

                    //访问服务器获取数据
                    InputStream inputStream = connection.getInputStream();

                    reader = new BufferedReader(new InputStreamReader(inputStream));
                    final StringBuilder response = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null){
                        response.append(line);
                    }

                    Log.e(TAG, "response = " + response.toString());
                    //记住,Android不允许在子线程中操作UI,使用下面的方法可以切换到主线程去操作UI
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textViewShowResponse.setText(response.toString());
                        }
                    });

                }catch (Exception e){

                }finally {
                    if (reader != null)
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    if (connection != null)
                        connection.disconnect();
                }
            }
        }).start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_send_requeset:
                sendRequestWithHttpURLConnection();
                break;
            default:
                break;
        }
    }
}
