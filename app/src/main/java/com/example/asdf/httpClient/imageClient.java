package com.example.asdf.httpClient;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;

import com.example.asdf.test.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by admin on 2016/11/13.
 */
public class imageClient extends Activity{


    private Handler handler;
    private Handler handler1;
    ImageView sendImage;
    ImageView recieveImage;
    private static String path = "/sdcard/myTmpImage/";// sd路径
    private PopupWindow mPopWindow;
    private Button btn_picture, btn_photo, btn_cancle;
    private Bitmap tmpImage;// 头像Bitmap
    private Bitmap tmpBitmap;

    private Button Send,Recieve;
    httpImage tmp = new httpImage();
    httpClient tmpClient = new httpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.image);
        Send =(Button) findViewById(R.id.Send);
        Recieve =(Button)findViewById(R.id.Recieve);
        sendImage = (ImageView)findViewById(R.id.SendimageView);
        recieveImage =(ImageView)findViewById(R.id.imageView);


        handler = new Handler()
        {
            @Override
            public void handleMessage(Message msg) {
                String tmp = msg.obj.toString();
                if(tmp.equals("true"))
                {
                    recieveImage.setImageBitmap(tmpBitmap);
                }
            }

        };

        handler1 = new Handler()
        {
            @Override
            public void handleMessage(Message msg) {
                String tmp = msg.obj.toString();
                if(tmp!=null)
                {
//                    tmpClient.postParamsJson();
                }
            }
        };
        //发送
        Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new Thread()
                {
                    @Override
                    public void run()
                    {
                        File tmp1 = new File(path + "TmpImage1.jpg");
               //        tmp.uploadFile(tmp1, "http://120.27.7.115:1010/api/image",handler1);
                    }
                }.start();

            }
        });

        //接收
        Recieve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new Thread()
                {
                    @Override
                    public void run()
                    {
                        tmpBitmap =tmp.getBitmap("http://120.27.7.115:1010/api/image?name=BodyPart_becc7314-e3da-461b-80e5-eeb7ee1b9fac",handler);
                    }
                }.start();


            }
        });

        sendImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }

        });



    }

    private void showDialog() {
        View contentView = LayoutInflater.from(imageClient.this).inflate(R.layout.sztx, null);
        mPopWindow = new PopupWindow(contentView,
                RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.WRAP_CONTENT, true);
        mPopWindow.setContentView(contentView);
        btn_picture = (Button) contentView.findViewById(R.id.btn_picture);
        btn_photo = (Button) contentView.findViewById(R.id.btn_photo);
        btn_cancle = (Button) contentView.findViewById(R.id.btn_cancle);

        btn_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Intent.ACTION_PICK, null);
                intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent1, 1);
                mPopWindow.dismiss();
            }
        });
        btn_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent2.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "TmpImage.jpg")));
                startActivityForResult(intent2, 2);// 采用ForResult打开
                mPopWindow.dismiss();
            }
        });
        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopWindow.dismiss();
            }
        });
        View rootview = LayoutInflater.from(imageClient.this).inflate(R.layout.image, null);
        mPopWindow.showAtLocation(rootview, Gravity.BOTTOM, 0, 0);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    cropPhoto(data.getData());// 裁剪图片
                }

                break;
            case 2:
                if (resultCode == RESULT_OK) {
                    File temp = new File(Environment.getExternalStorageDirectory() + "/TmpImage.jpg");
                    cropPhoto(Uri.fromFile(temp));// 裁剪图片
                }

                break;
            case 3:
                if (data != null) {
                    Bundle extras = data.getExtras();
                    tmpImage = extras.getParcelable("data");
                    if (tmpImage != null) {

                        setPicToView(tmpImage);// 保存在SD卡中
                        sendImage.setImageBitmap(tmpImage);// 用ImageView显示出来
                    }
                }
                break;
            default:
                break;

        }
        super.onActivityResult(requestCode, resultCode, data);
    };

    /**
     * 调用系统的裁剪
     *
     * @param uri
     */
    public void cropPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 3);
    }

    private void setPicToView(Bitmap mBitmap) {
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            return;
        }
        FileOutputStream b = null;
        File file = new File(path);
        file.mkdirs();// 创建文件夹
        String fileName = path + "TmpImage1.jpg";// 图片名字

        try {
            b = new FileOutputStream(fileName);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件

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
