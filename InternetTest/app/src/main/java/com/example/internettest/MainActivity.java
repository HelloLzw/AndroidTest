package com.example.internettest;

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

import com.example.internettest.MyHandler;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.lang.annotation.Target;
import java.net.ContentHandler;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

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
                    //*********************Pull方式解析*********************
                    parseXMLWithPull(response.toString());
                    //*****************************************************

                    //*********************SAX方式解析**********************
                    Log.e(TAG, "SAX Begin!");
                    parseXMLWithSAX(response.toString());
                    Log.e(TAG, "SAX End!");
                    //*****************************************************
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
    private void parseXMLWithSAX (String xmlData) throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        XMLReader xmlReader = saxParserFactory.newSAXParser().getXMLReader();
        MyHandler handler = new MyHandler();

        xmlReader.setContentHandler(handler);
        xmlReader.parse(new InputSource(new StringReader(xmlData)));
    }
    private void parseXMLWithPull(String xmlData) {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(new StringReader(xmlData));

            //这里获取解析事件,判断是文档的开始结束 标签的开始结束
            int eventType = xmlPullParser.getEventType();

            String id = "";
            String name = "";
            String version = "";

            //当不是文档结束的时候循环解析
            while (eventType != XmlPullParser.END_DOCUMENT){
                //获取当前签标的名字
                String nodeName = xmlPullParser.getName();

                //这里判断是开始签标还是结束签标
                switch (eventType){
                    case XmlPullParser.START_TAG:
                        if ("id".equals(nodeName)){
                            id = xmlPullParser.nextText();
                        }else if("name".equals(nodeName)){
                            name = xmlPullParser.nextText();
                        }else if("version".equals(nodeName)){
                            version = xmlPullParser.nextText();
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if ("app".equals(nodeName)){
                            Log.e(TAG, "id = " + id);
                            Log.e(TAG, "name = " + name);
                            Log.e(TAG, "version = " + version);
                        }
                        break;
                    default:
                        break;
                }

                //下个标签
                eventType = xmlPullParser.next();
            }

        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }


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
