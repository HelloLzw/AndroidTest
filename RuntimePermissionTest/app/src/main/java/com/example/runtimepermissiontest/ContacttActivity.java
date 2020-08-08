package com.example.runtimepermissiontest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**官方推荐使用内容提供器去实现跨程序的数据共享**/
public class ContacttActivity extends AppCompatActivity {
    private static final String TAG = "ContacttActivity";
    ArrayAdapter<String> adapter;
    List<String> contactList = new ArrayList<>();
    ListView contactListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_layout);
        contactListView = (ListView)findViewById(R.id.lv_contact);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, contactList);
        contactListView.setAdapter(adapter);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, 1);
        }else{
            readContacts();
        }

    }

    public void readContacts(){
        Cursor cursor = null;
        try {
            //访问内容提供器的共享数据
            cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null,null);
            while (cursor.moveToNext()){
                String displayName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                Log.e(TAG, "displayName: " + displayName);
                Log.e(TAG, "number: " + number);
                contactList.add(displayName + "\n" + number);
            }
            adapter.notifyDataSetChanged();

        }catch (SecurityException e){
            e.printStackTrace();
        }finally {
            if (cursor != null)
                cursor.close();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    readContacts();
                }else{
                    Toast.makeText(ContacttActivity.this, "Don't have Permission!", Toast.LENGTH_SHORT).show();
                }
        }
    }
}
