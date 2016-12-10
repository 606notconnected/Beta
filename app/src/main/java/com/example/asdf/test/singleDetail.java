<<<<<<< HEAD
﻿package com.example.asdf.test;

import android.app.Activity;
import android.os.Bundle;

import java.security.AccessControlContext;

/**
 * Created by Useradmin on 2016/11/12.
 */
public class singleDetail extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.singledetail);
    }
}
=======
package com.example.asdf.test;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asdf.httpClient.httpClient;
import com.example.asdf.httpClient.httpImage;
import com.google.gson.Gson;

import java.security.AccessControlContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Useradmin on 2016/11/12.
 */
public class singleDetail extends Activity {
    private ImageView leftDrawer;
//    private ListView replylist;
    private ImageView zp;
    private TextView wz;
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.singledetail);
        zp= (ImageView) findViewById(R.id.zp);
        wz= (TextView) findViewById(R.id.wz);
        int width=picture.tmpBitmap.getWidth();
        int height=picture.tmpBitmap.getHeight();
        int newWidth = 290;
        int newHeight = 360;
        //int newWidth=200;
        //   int newHeight=120;
        //计算缩放率，新尺寸除原始尺寸
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(picture.tmpBitmap, 0, 0, width,
                height, matrix, true);
        zp.setImageBitmap(resizedBitmap);
        wz.setText(picture.intro);
//        replylist= (ListView) findViewById(R.id.replylist);
        leftDrawer= (ImageView) findViewById(R.id.leftdrawer);
        leftDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singleDetail.this.finish();
            }
        });

//                try {
//                    Thread.sleep( 5000);
//                    if(picture.tmpBitmap!=null)
//                        zp.setImageBitmap(picture.tmpBitmap);
//                    else
//                        Toast.makeText(singleDetail.this,"照片为空",Toast.LENGTH_LONG).show();
//                    wz.setText(picture.intro);
////                    one1.stop();
//                } catch (InterruptedException e) {
//                    System.out.println("999999");
//                }
    }
}
>>>>>>> 0082396d07b02704bf0f9114ad22a9c19f39f29a
