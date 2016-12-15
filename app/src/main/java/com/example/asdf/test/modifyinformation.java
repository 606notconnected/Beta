package com.example.asdf.test;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.asdf.httpClient.httpClient;
import com.example.asdf.httpClient.httpImage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.logging.Handler;

/**
 * Created by Useradmin on 2016/11/6.
 */
public class modifyinformation extends Activity {
        private ImageView leftDrawer;//回退
        private Bitmap head;// 头像Bitmap
        private ImageView myhead;//头像
        private static String path = "/sdcard/myHeadOfData/";// sd路径
        private EditText newusername;
        private RadioButton man;
        private int nnn=0;
        private RadioButton womam;
        private EditText newintroduce;
        private RadioGroup radioGroup;
        private Button newsubmit;
        private String newsex;
        private httpClient tmp = new httpClient();
        private android.os.Handler handler;
        com.alibaba.fastjson.JSONObject object= new com.alibaba.fastjson.JSONObject();
        protected void onCreate(Bundle savedInstanceState) {
                // TODO Auto-generated method stub
                super.onCreate(savedInstanceState);
                setContentView(R.layout.modifyinformation);
                myhead = (ImageView) findViewById(R.id.headpicture);
                leftDrawer = (ImageView) findViewById(R.id.leftdrawer);
                newusername= (EditText) findViewById(R.id.newusername);
                man= (RadioButton) findViewById(R.id.man);
                womam= (RadioButton) findViewById(R.id.womam);
                radioGroup= (RadioGroup) findViewById(R.id.radio);
                newintroduce= (EditText) findViewById(R.id.newintroduce);
                newsubmit= (Button) findViewById(R.id.newsubmit);
                leftDrawer.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                modifyinformation.this.finish();
                        }
                });
                handler = new android.os.Handler()
                {
                        @Override
                        public void handleMessage(Message msg) {
                                String tmp = msg.obj.toString();
                                if(tmp.equals("true")) {
                                        Toast.makeText(modifyinformation.this, "修改成功", Toast.LENGTH_LONG).show();
                                        login.sex=newsex;
                                        login.introduce=newintroduce.getText().toString();
                                        login.uuername=newusername.getText().toString();
//                                        mainView.username.setText(login.uuername);
//                                        mainView.introduce.setText(login.introduce);
                                        modifyinformation.this.finish();
                                }
                                else if(tmp.equals("false"))
                                {
                                        Toast.makeText(modifyinformation.this, "修改失败", Toast.LENGTH_LONG).show();
                                }
                                else if(tmp.equals("error"))
                                {
                                        Toast.makeText(modifyinformation.this,"服务器出错",Toast.LENGTH_LONG).show();
                                }
                        }

                };
                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                                // TODO Auto-generated method stub
                                if(checkedId==womam.getId())
                                {
                                       newsex="女";
                                        nnn=0;
                                }else
                                {
                                      newsex="男";
                                        nnn=1;
                                }
                        }
                });

                newsubmit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                        object.put("account",login.account);
                                        object.put("userName",newusername.getText().toString());
                                        object.put("sex",nnn);
                                        object.put("introduction",newintroduce.getText().toString());
                                        object.put("email", "0000000");
//                                Toast.makeText(modifyinformation.this,newsex+"  "+newintroduce.getText().toString()+"  "+newusername.getText().toString(),Toast.LENGTH_LONG).show();
                                        new Thread() {
                                                @Override
                                                public void run() {
                                                        tmp.postParamsJson( "http://120.27.7.115:1010/api/usermessage",object,handler);
                                                }
                                        }.start();
                               // httpClient.postParamsJson("http://120.27.7.115:1010/api/peoplemessage", object);
                        }
                });
                Bitmap bt = BitmapFactory.decodeFile(path + "myhead.jpg");// 从Sd中找头像，转换成Bitmap
                if (bt != null) {
                        @SuppressWarnings("deprecation")
                        Drawable drawable = new BitmapDrawable(toRoundBitmap(bt));// 转换成drawable
                        myhead.setImageDrawable(drawable);
                } else {
                        /**
                         * 如果SD里面没有则需要从服务器取头像，取回来的头像再保存在SD中
                         *
                         */
                }
        }

        /**
         * 把bitmap转成圆形
         */
        public Bitmap toRoundBitmap(Bitmap bitmap) {
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                int r = 0;
                // 取最短边做边长
                if (width < height) {
                        r = width;
                } else {
                        r = height;
                }
                // 构建一个bitmap
                Bitmap backgroundBm = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                // new一个Canvas，在backgroundBmp上画图
                Canvas canvas = new Canvas(backgroundBm);
                Paint p = new Paint();
                // 设置边缘光滑，去掉锯齿
                p.setAntiAlias(true);
                RectF rect = new RectF(0, 0, r, r);
                // 通过制定的rect画一个圆角矩形，当圆角X轴方向的半径等于Y轴方向的半径时，
                // 且都等于r/2时，画出来的圆角矩形就是圆形
                canvas.drawRoundRect(rect, r / 2, r / 2, p);
                // 设置当两个图形相交时的模式，SRC_IN为取SRC图形相交的部分，多余的将被去掉
                p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
                // canvas将bitmap画在backgroundBmp上
                canvas.drawBitmap(bitmap, null, rect, p);
                return backgroundBm;
        }
}