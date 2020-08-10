package com.example.uitest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ListViewTest extends AppCompatActivity {

    private ListView listView;
    String data[] = {"1", "2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_list_view);

        listView = (ListView)findViewById(R.id.list_view);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ListViewTest.this,
                android.R.layout.simple_list_item_1, data);

        listView.setAdapter(arrayAdapter);

    }
}
