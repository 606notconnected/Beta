package com.example.asdf.test;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.example.asdf.httpClient.httpClient;
import com.example.asdf.httpClient.httpImage;
import com.example.asdf.test.adapter.commentListview;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Useradmin on 2016/11/12.
 */
public class singleDetail extends Activity {
    private ImageView leftDrawer;
    private ListView replylist;
    private commentListview adapter;
    private List<Map<String, Object>> list = null;
    private ImageView zp;
    private TextView wz;
    private Button download;
    private Button back;
    httpImage tmp1=new httpImage();
    httpClient tmp2=new httpClient();
    Handler handler1;
    Handler handler;
    Handler handler2;
    Bitmap bigImg;
    private TextView lovenum;
    private TextView commentForFriend;
    private static String path = "/sdcard/旅图/";// sd路径
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.singledetail);
        zp= (ImageView) findViewById(R.id.zp);
        wz= (TextView) findViewById(R.id.wz);
        download= (Button) findViewById(R.id.download);
        replylist= (ListView) findViewById(R.id.replylist);
        lovenum= (TextView) findViewById(R.id.loveNum);
        back= (Button) findViewById(R.id.back);
        commentForFriend= (TextView) findViewById(R.id.commentForFriend);
        list=getData();
        adapter = new commentListview(this,list);
        replylist.setAdapter(adapter);
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
        handler = new Handler()
        {
            @Override
            public void handleMessage(Message msg) {
                String tmp = msg.obj.toString();
                System.out.println(tmp+"gg");
                if(tmp!="error") {
                    lovenum.setText(tmp);
                    System.out.println("获取朋友照片点赞数成功");
//                    startActivity(new Intent(picture.this, singleDetail.class));
                }
                else{
                    System.out.println("获取朋友照片点赞数失败");
                }
            }

        };
        handler2 = new Handler()
        {
            @Override
            public void handleMessage(Message msg) {
                String tmp = msg.obj.toString();
                if(tmp.equals("true"))
                {
                    System.out.println("上传评论成功sing");
//                    Toast.makeText(singleDetail.this, "上传评论成功", Toast.LENGTH_LONG).show();
                }
                else{
                    System.out.println("上传评论失败");
                }
            }

        };
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ss = commentForFriend.getText().toString();
                if (ss != null) {
                    final JSONObject object = new JSONObject();
                    object.put("account", login.account);
                    object.put("comment", ss);
                    object.put("imageName",picture.na);
                    System.out.println(login.account + " oo " + commentForFriend.getText() + " pp " + picture.na);
                    new Thread() {
                        @Override
                        public void run() {
                            tmp2.postParamsJson("http://120.27.7.115:1010/api/Comment", object, handler2);
                        }
                    }.start();
                } else {
                    Toast.makeText(singleDetail.this, "评论内容不能为空", Toast.LENGTH_LONG).show();
                }
            }
        });
        new Thread(){
            @Override
            public void run() {
                tmp2.getParamTest("http://120.27.7.115:1010/api/ClickLike?name=" + picture.na, handler);
            }
        }.start();
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
                        tmp2.getParamTest("http://120.27.7.115:1010/api/ClickLike?name=" + picture.na, handler);
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
    public List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < picture.comlist.size(); i++) {
            if (picture.comlist.get(i).getaccount()!=null) {
                System.out.println(picture.comlist.get(i).getaccount()+"获取的name");
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("name",picture.comlist.get(i).getaccount());
                map.put("comment", picture.comlist.get(i).getcomment());
                list.add(map);
            }
        }
        return list;
    }
}

