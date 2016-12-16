package com.example.asdf.test;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.example.asdf.httpClient.httpClient;
import com.example.asdf.httpClient.httpImage;
import com.example.asdf.test.adapter.friendListView;
import com.example.asdf.test.attached.iClick;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Useradmin on 2016/11/15.
 */
public class watchFriend extends Activity implements AdapterView.OnItemClickListener {
    private ImageView addFriend;
    private ImageView leftDrawer;
    private ListView watch_friend;
    httpClient tmp1=new httpClient();
    httpClient tmp2=new httpClient();
    httpImage tmp4=new httpImage();
    httpClient tmp3= new httpClient();
    private android.os.Handler handler4;
    private List<Bitmap> bitmaps=new ArrayList<>();
    private android.os.Handler handler1;
    private android.os.Handler handler2;
    private android.os.Handler handler3;
    private android.os.Handler handler;
    private List<Map<String, Object>> list = null;
    private List<String> friendtripictureNames;
    public static List<String> friendPicNames=new ArrayList<>();
    public static List<String> friendPicIntr=new ArrayList<>();
    public static  String friendTripId;
    int place;
    int jkl=0;
    int num=0;
    private friendListView adapter;
    Bitmap bitmap;
    // TODO Auto-generated method stub
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.watch_friend);
        watch_friend = (ListView) findViewById(R.id.watch_friend);
        addFriend = (ImageView) findViewById(R.id.addFriend);
        final AlertDialog.Builder builder=new AlertDialog.Builder(this);
        addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                watchFriend.this.finish();
                startActivity(new Intent(watchFriend.this, addWatch.class));
            }
        });
        handler2= new android.os.Handler()
        {
            @Override
            public void handleMessage(Message msg) {
                String tmp = msg.obj.toString();
                tmp = "{" + tmp + "}";
                Log.d("json", tmp);
                Gson gson = new Gson();
                pictureinfor peopl = gson.fromJson(tmp, pictureinfor.class);
                friendPicNames.add(peopl.getImageName());
                friendPicIntr.add(peopl.getIntroduction());
                if(friendPicNames.size()!=0) {
                    num++;
                    if(num==friendtripictureNames.size())
                    {System.out.println("跳转至friend");startActivity(new Intent(watchFriend.this, friend.class));num=0;}
                }
                else
                {
                    System.out.println("获取照片详情失败");
                }
            }

        };
        handler3 = new android.os.Handler() {
            @Override
            public void handleMessage(Message msg) {
                String tmp = msg.obj.toString();
                System.out.println(tmp+"66666666666");
                if(tmp!=null)
                {
                    tmp = "{" + tmp + "}";
                    Gson gson = new Gson();
                    trippictures tri = gson.fromJson(tmp, trippictures.class);
                    List<String> iii=tri.getreturnImageName();
                    System.out.println(tri.getreturnImageName()+"呼噜噜"+tri);
                    if(iii.size()==0||iii.get(0).equals("null")||iii.get(0).equals("error"))
                    {
                        builder.setMessage("好友的最新行程没有照片哟");
                        // 创建对话框
                        AlertDialog ad = builder.create();
                        // 显示对话框
                        ad.show();
                    }
                    else {
                        friendtripictureNames=iii;
                        new Thread() {
                            @Override
                            public void run() {
                                for(int u=0;u<friendtripictureNames.size();u++)
                                { tmp3.getParamTest("http://120.27.7.115:1010/api/imagemessage?imagename=" + friendtripictureNames.get(u), handler2);
                                }
                            }
                        }.start();
                    }
                }
            }
        };
        handler = new android.os.Handler()
        {
            @Override
            public void handleMessage(Message msg) {
                String tmp = msg.obj.toString();
//                    startActivity(new Intent(upload.this, mainView.class));
                tmp = "{" + tmp + "}";
                System.out.println(tmp+"获取好友行程");
                Gson gson = new Gson();
                trip tri = gson.fromJson(tmp, trip.class);
                if (tri.gettripList()!=null) {
                        friendTripId =tri.gettripList().get(0).getIId();
                        System.out.println("获取好友行程成功");
                        new Thread() {
                            @Override
                            public void run() {
                                tmp3.getParamTest("http://120.27.7.115:1010/api/road?roadid=" + friendTripId, handler3);
                            }
                        }.start();
//                        Toast.makeText(watchFriend.this, "获取好友行程成功", Toast.LENGTH_SHORT).show();
                }
                else {
                    builder.setMessage("好友还没有行程");
                    // 创建对话框
                    AlertDialog ad = builder.create();
                    // 显示对话框
                    ad.show();
                    System.out.println("获取好友行程失败或者行程列表为空");
//                    Toast.makeText(watchFriend.this, "获取好友行程失败", Toast.LENGTH_LONG).show();
                }
            }

        };
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
//                    Toast.makeText(watchFriend.this,"获取好友头像详情成功watchfroend",Toast.LENGTH_SHORT).show();
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

        watch_friend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                new Thread() {
                    @Override
                    public void run() {
                        tmp1.getParamTest("http://120.27.7.115:1010/api/road?account=" + login.wat.get(position).getaccount(), handler);
                    }
                }.start();
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
        bitmaps.clear();
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
    class trip
    {
        private List<ttrip>returnMessage;
        public class ttrip{
            private String RoadID;
            private String RoadName;
            private String Introduction;
            public String getIntroduction(){
                return Introduction;
            }
            public String getIId(){
                return RoadID;
            }
            public String getName(){
                return RoadName;
            }
        }
        public List<ttrip> gettripList()
        {
            return returnMessage;
        }
    }
    class trippictures
    {
        public List<String> returnImageName;
        public List<String> getreturnImageName() {
            return returnImageName;
        }
    }
    class pictureinfor {
        private String imageName;
        private String dateTime;  //属性都定义成String类型，并且属性名要和Json数据中的键值对的键名完全一样
        private String longitude;
        private String latitude;
        private String introduction;
        public String getImageName() {
            return imageName;
        }
        public String getDateTime() {
            return dateTime;
        }
        public String getLongitude() {
            return longitude;
        }
        public String getLatitude() {
            return latitude;
        }
        public String getIntroduction() {
            return introduction;
        }
    }

}