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
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.example.asdf.httpClient.httpClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    private android.os.Handler handler1;
    private List<Map<String, Object>> list = null;
    int place;
    friendListView adapter;

    // TODO Auto-generated method stub
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.watch_friend);
        addFriend = (ImageView) findViewById(R.id.addFriend);
        watch_friend = (ListView) findViewById(R.id.watch_friend);
        list = getData();
        System.out.println("关注" + list);
        adapter = new friendListView(this, list, mListener);
        watch_friend.setAdapter((ListAdapter) adapter);
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
                    list.clear();
                    list=getData();
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
                Bitmap bmp= BitmapFactory.decodeResource(res, R.drawable.ddd);
                bmp=toRoundBitmap(bmp);
                BitmapDrawable bd= new BitmapDrawable(bmp);
                map.put("examplePicture",bd);
                map.put("exampletext", login.wat.get(i).getintroduction());
                list.add(map);
            }
        }
        return list;
    }

    // 响应item点击事件
//    @Override
    public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
        Toast.makeText(this, "listview的item被点击了！，点击的位置是-->" + position,
                Toast.LENGTH_SHORT).show();
//        startActivity(new Intent(watchFriend.this, friend.class));

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
        //int newWidth=200;
        //   int newHeight=120;
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