package com.example.uitest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "MainActivity";
    private Button button;

    public void viewInit(){
        button = (Button)findViewById(R.id.button_to_listview);
        button.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);

        viewInit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_to_listview:
                Log.e(TAG, "button_to_listview click");
                startActivity(new Intent(MainActivity.this, ListViewTest.class));
                break;
            default:
                break;
        }
    }
}
