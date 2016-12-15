package com.example.asdf.test;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.example.asdf.httpClient.httpClient;
import com.example.asdf.httpClient.httpImage;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * Created by Useradmin on 2016/11/15.
 */
public class watchFriend extends Activity implements AdapterView.OnItemClickListener {
    private ImageView addFriend;
    private ImageView leftDrawer;
    private ListView watch_friend;
    httpClient tmp2=new httpClient();
    httpImage tmp4=new httpImage();
    private android.os.Handler handler4;
    private List<Bitmap> bitmaps=new ArrayList<>();
    private android.os.Handler handler1;
    private List<Map<String, Object>> list = null;
    int place;
    int jkl=0;
    private friendListView adapter;
    Bitmap bitmap;
    // TODO Auto-generated method stub
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.watch_friend);
        watch_friend = (ListView) findViewById(R.id.watch_friend);
        addFriend = (ImageView) findViewById(R.id.addFriend);
        addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                watchFriend.this.finish();
                startActivity(new Intent(watchFriend.this, addWatch.class));
            }
        });
        watch_friend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(watchFriend.this, friend.class));
            }
        });
        handler4 = new android.os.Handler()
        {
            @Override
            public void handleMessage(Message msg) {
                String tmp = msg.obj.toString();
                jkl++;
                if(tmp.equals("true"))
                {
                    bitmaps.add(bitmap);
                    if(jkl==login.wat.size())
                    {
                        list=getData();
                        adapter = new friendListView(watchFriend.this, list, mListener);
                        watch_friend.setAdapter((ListAdapter) adapter);
                        jkl=0;
                    }
                    Toast.makeText(watchFriend.this,"获取好友头像详情成功watchfroend",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    System.out.println("获取好友头像详情不成功watchfroend");
                }
            }
        };
        new Thread()
        {
            public void run()
            {
                    bitmaps.clear();
                    for (int i = 0; i < login.friendHeadList.size(); i++) {
                       if(login.friendHeadList.get(i)!=null)
                        bitmap = tmp4.getBitmap("http://120.27.7.115:1010/api/image?name=" + login.friendHeadList.get(i), handler4);
                    }
            }
        }.start();
        leftDrawer = (ImageView) findViewById(R.id.leftdrawer);
        leftDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                watchFriend.this.finish();
            }
        });
        handler1 = new android.os.Handler() {
            @Override
            public void handleMessage(Message msg) {
                String tmp = msg.obj.toString();
                if (tmp.equals("true") ) {
                    System.out.println("取消关注成功");
//                    adapter = new listViewAdapter(addWatch.this, list, mListener);
                    login.wat.remove(place);
                    login.friendHeadList.remove(place);
                    bitmaps.remove(place);
                    list.clear();
                    list.addAll(getData());
                    adapter.notifyDataSetChanged();
//                    Toast.makeText(watchFriend.this, "取消关注成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(watchFriend.this, "取消关注失败", Toast.LENGTH_LONG).show();
                }
            }
        };
    }

    public List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < login.wat.size(); i++) {
            if (login.wat.get(i).getIuserName() != null) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("friendName", login.wat.get(i).getIuserName());
                map.put("love", R.drawable.love);
                map.put("lovetext", "取消关注");
                Resources res=getResources();
//                Bitmap bmp= BitmapFactory.decodeResource(res, R.drawable.ddd);
                if(bitmaps.get(i)!=null){
                    System.out.println(i+"099999");
                Bitmap bmp=bitmaps.get(i);
                bmp=toRoundBitmap(bmp);
                BitmapDrawable bd= new BitmapDrawable(bmp);
                map.put("examplePicture",bd);}
                else{
                    System.out.println(i+"uuu99999");
                    Bitmap bmp= BitmapFactory.decodeResource(res, R.drawable.head);
                    bmp=toRoundBitmap(bmp);
                    BitmapDrawable bd= new BitmapDrawable(bmp);
                    map.put("examplePicture", bd);
                }
                map.put("exampletext", login.wat.get(i).getintroduction());
                list.add(map);
            }
        }
        return list;
    }

    // 响应item点击事件
//    @Override
    public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
    }

    /**
     * 实现类，响应按钮点击事件
     */
    private iClick mListener = new iClick() {
        @Override
        public void listViewItemClick(final int position, View v) {
                new Thread() {
                    @Override
                    public void run() {
                        place = position;
                        JSONObject object = new JSONObject();
                        object.put("FollowerAccount", login.account);
                        object.put("WasFollowederAccount", login.wat.get(position).getaccount());
                        tmp2.postParamsJson("http://120.27.7.115:1010/api/Follower_Delete", object, handler1);
                    }
                }.start();
            }
    };
    /**
     * 把bitmap转成圆形
     */
    public Bitmap toRoundBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int newWidth = 80;
        int newHeight = 80;
        //计算缩放率，新尺寸除原始尺寸
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width,
                height, matrix, true);
        Bitmap backgroundBm = Bitmap.createBitmap(80, 80, Bitmap.Config.ARGB_8888);
        // new一个Canvas，在backgroundBmp上画图
        Canvas canvas = new Canvas(backgroundBm);
        Paint p = new Paint();
        // 设置边缘光滑，去掉锯齿
        p.setAntiAlias(true);
        RectF rect = new RectF(0, 0, 80,80);
        // 通过制定的rect画一个圆角矩形，当圆角X轴方向的半径等于Y轴方向的半径时，
        // 且都等于r/2时，画出来的圆角矩形就是圆形
        canvas.drawRoundRect(rect, 80 / 2, 80 / 2, p);
        // 设置当两个图形相交时的模式，SRC_IN为取SRC图形相交的部分，多余的将被去掉
        p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        // canvas将bitmap画在backgroundBmp上
        canvas.drawBitmap(bitmap, null, rect, p);
        return backgroundBm;
    }
}