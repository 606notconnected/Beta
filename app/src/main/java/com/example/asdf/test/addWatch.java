
package com.example.asdf.test;

import android.app.Activity;
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
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.example.asdf.httpClient.httpClient;
import com.example.asdf.httpClient.httpImage;
import com.example.asdf.test.adapter.listViewAdapter;
import com.example.asdf.test.attached.iClick;
import com.example.asdf.test.attached.res;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Useradmin on 2016/10/23.
 */
public class addWatch extends Activity implements AdapterView.OnItemClickListener{
    private ImageView leftDrawer;
    private Button search;
    private  EditText searname;
    private String searchname;
    private ListView addWatchListView;
    httpClient tmp1=new httpClient();
    httpClient tmp2=new httpClient();
    httpClient tmp3=new httpClient();
    httpImage tmp4=new httpImage();
    httpClient tmp5=new httpClient();
    private Handler handler;
    private Handler handler1;
    private android.os.Handler handler3;
    private android.os.Handler handler5;
    private android.os.Handler handler4;
    public static List<Map<String, Object>> list;
    private List<Bitmap> bitmapss=new ArrayList<>();
    private List<String> addfriendHeadList=new ArrayList<>();
    private List<res> allresault=new ArrayList<>();
    listViewAdapter adapter;
    int jkll;
    int place;
    Bitmap bitmap;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addwatch);
        addWatchListView = (ListView) findViewById(R.id.addWatchListview);
        search = (Button) findViewById(R.id.search);
        searname= (EditText) findViewById(R.id.searname);
        leftDrawer = (ImageView) findViewById(R.id.leftdrawer);
        leftDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addWatch.this.finish();
            }
        });
        handler4 = new android.os.Handler()
        {
            @Override
            public void handleMessage(Message msg) {
                String tmp = msg.obj.toString();
                jkll++;
                if(tmp.equals("true"))
                {
                    bitmapss.add(bitmap);
                    if(jkll==allresault.size())
                    {
                        list=getData();
                        adapter = new listViewAdapter(addWatch.this, list, mListener);
                        addWatchListView.setAdapter(adapter);
                        jkll=0;
                    }
                }
                else
                {
                    System.out.println("获取好友头像详情不成功addwatch");
                }
            }
        };
        handler3 = new android.os.Handler()
        {
            @Override
            public void handleMessage(Message msg) {
                String tmp = msg.obj.toString();
                tmp = "{" + tmp + "}";
                Gson gson = new Gson();
                friendHead frhe = gson.fromJson(tmp, friendHead.class);
                String y=frhe.getresult();
                if(y.equals("true"))
                {
                    System.out.println(tmp+"000000mazz00000"+frhe.gethead());
                    addfriendHeadList.add(frhe.gethead());
                    if(addfriendHeadList.size()==allresault.size()) {
                        new Thread() {
                            public void run() {
                                if (bitmapss.size() != 0)
                                    bitmapss.clear();
                                for (int i = 0; i < addfriendHeadList.size(); i++) {
                                    if (addfriendHeadList.get(i) != null)
                                        bitmap = tmp4.getBitmap("http://120.27.7.115:1010/api/image?name=" + addfriendHeadList.get(i), handler4);
                                }
                            }
                        }.start();
                    }
                }
                else
                {
                    addfriendHeadList.add("null");
                    System.out.println("获取好友头像详情不成功");
                }
            }

        };
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                String tmp = msg.obj.toString();
//                    startActivity(new Intent(upload.this, mainView.class));
                tmp = "{" + tmp + "}";
                System.out.println(tmp+"添加关注返回信息");
                Gson gson = new Gson();
                searchresult sear = gson.fromJson(tmp, searchresult.class);
                if(allresault.size()!=0)
                allresault.clear();
                allresault = sear.getresuList();
                String resault=sear.getresult();
                if (resault.equals("true") ) {
                    new Thread() {
                        @Override
                        public void run() {
                            for (int i = 0; i < allresault.size(); i++) {
//                                System.out.println( allresault.get(i).getaccount());
                                if(addfriendHeadList.size()!=0)
                                addfriendHeadList.clear();
                                tmp5.getParamTest("http://120.27.7.115:1010/api/Image_Head?account=" + allresault.get(i).getaccount(), handler3);
                            }
                        }
                    }.start();
                        System.out.println("搜索成功");
//                        Toast.makeText(addWatch.this, "搜索成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(addWatch.this, "搜索失败", Toast.LENGTH_LONG).show();
                }
            }
        };
        handler1 = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                String tmp = msg.obj.toString();
                if (tmp.equals("true") ) {
//                    adapter = new listViewAdapter(addWatch.this, list, mListener);
                    int flag=1;
                    for(int i=0;i<login.wat.size();i++) {
                        if(login.wat.get(i).getaccount().equals(allresault.get(place).getaccount()))
                            flag=0;
                        System.out.println(login.wat .get(i).getaccount()+ "关注c功");
                    }
                    if(flag==1)
                    {
                        login.wat.add(allresault.get(place));
                        login.friendHeadList.add(addfriendHeadList.get(place));
                    }
                    allresault.remove(place);
                    list.clear();
                    list=getData();
                   adapter.notifyDataSetChanged();
                    Toast.makeText(addWatch.this, "关注成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(addWatch.this, "关注失败", Toast.LENGTH_LONG).show();
                }
            }
        };
            search.setOnClickListener(new View.OnClickListener()            {
                @Override
                public void onClick (View v){
                    searchname=searname.getText().toString();
                    if(searchname!=null) {
                        new Thread() {
                            @Override
                            public void run() {
                                JSONObject object = new JSONObject();
                                object.put("account",searchname);
                                tmp1.postParamsJson("http://120.27.7.115:1010/api/User_Find", object, handler);
                            }
                        }.start();
                    }
                    else {
                        Toast.makeText(addWatch.this,"请输入搜索目标",Toast.LENGTH_LONG).show();
                    }
            }
            }

            );
        }

    public List<Map<String, Object>> getData(){
        List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
        for (int i = 0; i < allresault.size(); i++) {
            Map<String, Object> map=new HashMap<String, Object>();
            map.put("friendName",allresault.get(i).getIuserName() );
            map.put("love", R.drawable.before);
            map.put("lovetext","添加关注");
            Resources res=getResources();
            if(bitmapss.get(i)!=null){
                System.out.println(i+"099999");
                Bitmap bmp=bitmapss.get(i);
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
            map.put("exampletext",allresault.get(i).getintroduction());
            list.add(map);
        }
        return list;
    }
    public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
//        Toast.makeText(this, "listview的item被点击了！，点击的位置是-->" + position,
//                Toast.LENGTH_SHORT).show();
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
                    place=position;
                    JSONObject object = new JSONObject();
                    object.put("FollowerAccount",login.account);
                    object.put("WasFollowederAccount",allresault.get(position).getaccount());
                    tmp2.postParamsJson("http://120.27.7.115:1010/api/Follower", object, handler1);
                }
            }.start();
//            addWatchListView.removeHeaderView(v);
        }
    };
    class searchresult
    {
        private String result;
        private List<res>userList;
        public String getresult(){
            return result;
        }
        public List<res> getresuList()
        {
            return userList;
        }
    }
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
    class friendHead
    {
        private String result;
        private String headimageName;
        public String getresult(){
            return result;
        }
        public String gethead()
        {
            return headimageName;
        }
    }
}

