package com.example.asdf.test;

import android.app.Activity;
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
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.asdf.httpClient.httpClient;
import com.example.asdf.httpClient.httpImage;

import org.json.JSONObject;

/**
 * Created by Useradmin on 2016/11/8.
 */
public class information extends Activity {
    private ImageView leftDrawer;
    private Handler handler;
    private Bitmap head;// 头像Bitmap
    private TextView username;
    private TextView introduce;
    private Handler handler1;
    private TextView sex;
    private TextView email;
    private ImageView myhead;//头像
    Bitmap tmpBitmap;
    httpImage tmp = new httpImage();
    private static String path = "/sdcard/myHeadOfData/";// sd路径
    JSONObject object=new JSONObject();
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.information);
        myhead = (ImageView) findViewById(R.id.head);
        username= (TextView) findViewById(R.id.username);
        username.setText(login.uuername);
        introduce= (TextView) findViewById(R.id.introduce);
        introduce.setText(login.introduce);
        sex= (TextView) findViewById(R.id.sex);
        sex.setText(login.sex);
        email= (TextView) findViewById(R.id.email);
        email.setText(login.account);
        leftDrawer= (ImageView) findViewById(R.id.leftdrawer);
        leftDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                information.this.finish();
            }
        });
        handler = new Handler()
        {
            @Override
            public void handleMessage(Message msg) {
                String tmp = msg.obj.toString();
                if(tmp.equals("true"))
                {
                    myhead.setImageBitmap(tmpBitmap);
                }
            }

        };
        httpClient.getParamTest("http://120.27.7.115:1010/api/peoplemessage",handler);
        // TODO Auto-generated method stub
        new Thread() {
            @Override
            public void run() {
               tmpBitmap = tmp.getBitmap("http://120.27.7.115:1010/api/image?name=", handler);
            }
        }.start();
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
