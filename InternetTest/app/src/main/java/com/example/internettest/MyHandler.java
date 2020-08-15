package com.example.internettest;

import android.util.Log;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

//SAX解析方式
public class MyHandler extends DefaultHandler {
    private String nodeName = "";

    private StringBuilder id;
    private StringBuilder name;
    private StringBuilder version;

    private static final String TAG = "MyHandler";

    //开始解析
    @Override
    public void startDocument() throws SAXException {

        id = new StringBuilder();
        name = new StringBuilder();
        version = new StringBuilder();
    }

    //结束解析
    @Override
    public void endDocument() throws SAXException {
        Log.e(TAG, "endDocument!");
    }

    //读取到开始标签的时候
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

        nodeName = localName;
    }

    //读取到结束标签的时候
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {

        if ("app".equals(localName)){
            Log.e(TAG, "id = " + id.toString().trim());
            Log.e(TAG, "name = " + name.toString().trim());
            Log.e(TAG, "version = " + version.toString().trim());

            id.setLength(0);
            name.setLength(0);
            version.setLength(0);
        }
    }

    //读取标签内容
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {

        if ("id".equals(nodeName)){
            id.append(ch, start, length);
        }else if("name".equals(nodeName)){
            name.append(ch, start, length);
        }else if("version".equals(nodeName)){
            version.append(ch, start, length);
        }
    }
}

