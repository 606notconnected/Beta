package com.example.asdf.test;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.asdf.httpClient.httpClient;
import com.example.asdf.httpClient.httpImage;
import com.example.asdf.test.adapter.imageAdapter;
import com.example.asdf.test.adapter.listViewForFriend;
import com.example.asdf.test.adapter.tmpAdapter;
import com.example.asdf.test.attached.commen;
import com.example.asdf.test.attached.iClick;
import com.example.asdf.test.attached.tmpBean;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Useradmin on 2016/11/8.
 */
public class friend extends Activity implements AdapterView.OnItemClickListener{
//
    private ImageView leftDrawer;
//    private ImageView cancelWatch;
//    private ListView friendListview=null;
//    List<Map<String, Object>> list;
    private List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
    private GridView lv_main;
    private Handler handler;
    private List<tmpBean> listDatas;
    private tmpAdapter lAdapter;
    private Handler handler3;
    private Handler handler1;
    public static Bitmap friendPic;
    public static String friendInt;
    public static String friendNam;
    httpImage  tmp3=new httpImage();
    httpClient tmp2 =new httpClient();
    httpClient tmp1 = new httpClient();
    public static  List<commen> friendcomlist;
    int i;
    listViewForFriend adapter;
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend);
        lv_main = (GridView) findViewById(R.id.gview);
        leftDrawer= (ImageView) findViewById(R.id.leftdrawer);
//        list=getData();
//        adapter=new listViewForFriend(this, list,mListener);
        listDatas = new ArrayList<>();
//        initData();
//        lAdapter = new tmpAdapter(listDatas, this);
//        lv_main.setAdapter(lAdapter);
        getData();
        BaseAdapter adapter = new imageAdapter(this, data,mListener);
        //设置适配器
        lv_main.setAdapter(adapter);
        leftDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                friend.this.finish();
            }
        });
        handler = new Handler()
        {
            @Override
            public void handleMessage(Message msg) {
                String tmp = msg.obj.toString();
                if(tmp.equals("true"))
                {
                    System.out.println("点赞成功");
                    Toast.makeText(friend.this,"点赞成功",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    System.out.println("点赞不成功");
                    Toast.makeText(friend.this,"点赞不成功",Toast.LENGTH_SHORT).show();
                }
            }

        };
        handler1 = new Handler()
        {
            @Override
            public void handleMessage(Message msg) {
                String tmp = msg.obj.toString();
                System.out.println(tmp+"singlefriend");
                if(tmp.equals("true")) {
                    System.out.println("获取朋友照片详情成功");
//                    startActivity(new Intent(picture.this, singleDetail.class));
                }
                else{
                    System.out.println("获取照片详情失败");
                }
            }

        };
        handler3= new Handler()
        {
            @Override
            public void handleMessage(Message msg) {
                String tmp = msg.obj.toString();
                tmp = "{" + tmp + "}";
                System.out.println(tmp+"获取评论");
                Log.d("json", tmp);
                Gson gson = new Gson();
                comment comli = gson.fromJson(tmp, comment.class);
                friendcomlist=comli.getcoList();
                String a=comli.getresult();
                if(a.equals("true"))
                {
                    System.out.println("获取评论成功");
                    startActivity(new Intent(friend.this, friendDetailed.class));
//                    Toast.makeText(picture.this,"获取照片详情成功",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    System.out.println("获取评论失败");
                    Toast.makeText(friend.this,"获取评论失败",Toast.LENGTH_SHORT).show();
                }
            }

        };
        lv_main.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
//                Toast.makeText(friend.this,"oo"+position,Toast.LENGTH_LONG).show();
                new Thread() {
                    @Override
                    public void run() {
                        friendNam=lookFriendTrip.friendPicNames.get(position);
                        friendInt=lookFriendTrip.friendCom.get(position);
                        friendPic =tmp3.getBitmap("http://120.27.7.115:1010/api/image?name="+lookFriendTrip.friendPicNames.get(position),handler1);
                        tmp2.getParamTest("http://120.27.7.115:1010/api/Comment?imageName="+lookFriendTrip.friendPicNames.get(position) , handler3);
                    }
                }.start();
            }
        });
    }

    // 响应item点击事件
    @Override
    public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
//        startActivity(new Intent(friend.this, singleDetail.class));
    }

    public void getData() {
        for(i=0;i<lookFriendTrip.friendPicNames.size();i++) {
            String url1="http://120.27.7.115:1010/api/image" + "?name=" +lookFriendTrip.friendPicNames.get(i);
            Map<String, Object> map1 = new HashMap<String, Object>();
            map1.put("url", url1);
            map1.put("intro",lookFriendTrip.friendPicNames.get(i));
            System.out.println(url1);
            data.add(map1);}
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
                    tmp2.getParamTest("http://120.27.7.115:1010/api/ClickLike?imageName=" + lookFriendTrip.friendPicNames.get(position), handler);
                }
            }.start();
//            addWatchListView.removeHeaderView(v);
        }
    };
    class comment
    {
        private String result;
        private List<commen>commentBackList;
        public String getresult(){
            return result;
        }
        public List<commen> getcoList()
        {
            return commentBackList;
        }
    }
}

