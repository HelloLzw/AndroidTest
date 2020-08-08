package com.example.cameraphototest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Button buttonTakePhoto;
    private Button buttonChooseFromAlbum;
    private ImageView imageViewPicture;
    private Uri imageUri;
    private final int TAKE_PHOTO = 1;
    private final int CHOOSE_PHOTO = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        buttonTakePhoto = (Button)findViewById(R.id.bn_tkphoto);
        buttonChooseFromAlbum = (Button)findViewById(R.id.bn_choose_album);
        imageViewPicture = (ImageView)findViewById(R.id.picture);

        buttonChooseFromAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick: press0");
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED){
                    Log.e(TAG, "onClick: press1");
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 1);
                }else{
                    /**打开相册 [GET_CONTENT允许用户选择特殊种类的数据（特殊种类的数据：照一张相片或录一段音）]**/
                    Log.e(TAG, "onClick: press2");
                    Intent intent = new Intent("android.intent.action.GET_CONTENT");
                    intent.setType("image/*");
                    startActivityForResult(intent, CHOOSE_PHOTO);
                }
            }
        });
        buttonTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //创建File对象用来保存图片,外部存储私有目录,app卸载时也会删除
                File outputImage = new File(getExternalCacheDir(), "output_image.jpg");
                /**
                    内部存储:/data/
                    外部存储:mnt | storage | sdcard
                    Environment.getDataDirectory() = /data
                    Environment.getDownloadCacheDirectory() = /cache
                    Environment.getExternalStorageDirectory() = /mnt/sdcard
                    Environment.getExternalStoragePublicDirectory(“test”) = /mnt/sdcard/test
                    Environment.getRootDirectory() = /system
                    getPackageCodePath() = /data/app/com.my.app-1.apk
                    getPackageResourcePath() = /data/app/com.my.app-1.apk
                    getCacheDir() = /data/data/com.my.app/cache
                    getDatabasePath(“test”) = /data/data/com.my.app/databases/test
                    getDir(“test”, Context.MODE_PRIVATE) = /data/data/com.my.app/app_test
                    getExternalCacheDir() = /mnt/sdcard/Android/data/com.my.app/cache
                    getExternalFilesDir(“test”) = /mnt/sdcard/Android/data/com.my.app/files/test
                    getExternalFilesDir(null) = /mnt/sdcard/Android/data/com.my.app/files
                    getFilesDir() = /data/data/com.my.app/files
                * */

                try{
                    if (outputImage.exists()){
                        outputImage.delete();
                    }
                    outputImage.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //Android 7.0之后使用共享file要使用FileProvider
                /**
                 * https://www.jianshu.com/p/f0b2cf0e0353
                 * manifest中声明FileProvider
                 *res/xml中定义对外暴露的文件夹路径
                 *生成content://类型的Uri
                 *给Uri授予临时权限
                 *使用Intent传递Uri
                **/
                if (Build.VERSION.SDK_INT >= 24){
                    Log.e(TAG, "Before FileProvider");
                    imageUri = FileProvider.getUriForFile(MainActivity.this, "com.example.cameraphototest.fileprovider", outputImage);
                }else {
                    Log.e(TAG, "Before fromFile");
                    imageUri = Uri.fromFile(outputImage);
                }
                Log.e(TAG, "Before Intent");
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, TAKE_PHOTO);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK){
                    //显示照片
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        imageViewPicture.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CHOOSE_PHOTO:
                break;
            default:
                break;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Log.e(TAG, "onClick: press2");
                    Intent intent = new Intent("android.intent.action.GET_CONTENT");
                    intent.setType("image/*");
                    startActivityForResult(intent, 1);
                }else {
                    Toast.makeText(MainActivity.this, "申请权限失败!", Toast.LENGTH_LONG).show();
                }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void handleImageOnKitKat(Intent data){
        String imagePath = null;
        Uri uri = data.getData();

        //如果是document类型的uri
        if (DocumentsContract.isDocumentUri(this, uri)){
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())){
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            }else if("com.android.providers.downloads.documents".equals(uri.getAuthority())){

            }
        }
    }

    private String getImagePath(Uri uri, String selection){
        String path = null;

        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null){
            if (cursor.moveToFirst()){
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }

            cursor.close();
        }

        return path;
    }
}
