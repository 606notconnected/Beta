package com.example.asdf.test;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asdf.httpClient.httpClient;
import com.example.asdf.httpClient.httpImage;
import com.google.gson.Gson;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
    private Button download;
    httpImage tmp1=new httpImage();
    Handler handler1;
    Bitmap bigImg;
    private static String path = "/sdcard/旅图/";// sd路径
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.singledetail);
        zp= (ImageView) findViewById(R.id.zp);
        wz= (TextView) findViewById(R.id.wz);
        download= (Button) findViewById(R.id.download);
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
        handler1 = new android.os.Handler() {
            public void handleMessage(Message msg) {
                String tmp = msg.obj.toString();
                if(tmp.equals("true"))
                {
                    Toast.makeText(singleDetail.this, "下载原图成功", Toast.LENGTH_LONG).show();
                        setPicToView(bigImg);// 保存在SD卡中
                    System.out.println("t功");
                }
                else
                {
                    Toast.makeText(singleDetail.this,"下载原图失败",Toast.LENGTH_SHORT).show();
                    System.out.println("保存失败");
                }
            }
        };
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(){
                    @Override
                    public void run() {
                        bigImg=tmp1.getBitmap("http://120.27.7.115:1010/api/Image?imagename=" + picture.na, handler1);
                    }
                }.start();
            }
        });
    }
    private void setPicToView(Bitmap mBitmap) {
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            return;
        }
        FileOutputStream b = null;
        File file = new File(path);
        file.mkdirs();// 创建文件夹
        String fileName = path +picture.na+".jpg";// 图片名字
        try {
            b = new FileOutputStream(fileName);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
            Toast.makeText(singleDetail.this,"保存成功",Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                // 关闭流
                b.flush();
                b.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}

