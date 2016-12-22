package com.example.asdf.test;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.example.asdf.httpClient.httpClient;
import com.example.asdf.test.adapter.triplist;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Useradmin on 2016/12/20.
 */
public class lookFriendTrip extends Activity {
    private ImageView leftDrawer;
    private ListView triplist=null;
    httpClient tmp3 = new httpClient();
    private List<String> tripictureNames;
    private android.os.Handler handler3;
    public static List<String> friendPicNames=new ArrayList<>();
    public static List<String> friendCom=new ArrayList<>();
    List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
    JSONObject object = new JSONObject();
    private httpClient tmp = new httpClient();
    private httpClient tmp1= new httpClient();
    private android.os.Handler handler;
    private android.os.Handler handler1;
    int getnum=0;
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lookfriendtrip);
        final AlertDialog.Builder builder=new AlertDialog.Builder(this);
        leftDrawer= (ImageView) findViewById(R.id.leftdrawer);
        triplist= (ListView) findViewById(R.id.triplist);

        List<Map<String, Object>> list=getData();
        final int[] yy = {0};
        handler= new android.os.Handler()
        {
            @Override
            public void handleMessage(Message msg) {
                String tmp = msg.obj.toString();
                tmp = "{" + tmp + "}";
                Log.d("json", tmp);
                Gson gson = new Gson();
                pictureinfor peopl = gson.fromJson(tmp, pictureinfor.class);
                System.out.println(yy[0] + "999   " + peopl.getLatitude() + "   " + peopl.getLongitude() + "   " + peopl.getImageName());
                friendPicNames.add(peopl.getImageName());
                friendCom.add(peopl.getIntroduction());
                if(friendPicNames.size()!=0) {
                    System.out.println(yy[0]  + "   " + friendPicNames.get(yy[0]));
                    getnum++;
                    if(getnum==tripictureNames.size())
                    { startActivity(new Intent(lookFriendTrip.this,friend.class));getnum=0;}
                }
                else
                {
                    Toast.makeText(lookFriendTrip.this, "获取照片详情失败", Toast.LENGTH_SHORT).show();
                }
            }

        };
        handler3 = new android.os.Handler() {
            @Override
            public void handleMessage(Message msg) {
                String tmp = msg.obj.toString();
                System.out.println(tmp+"票");
                if(tmp!=null)
                {
                    tmp = "{" + tmp + "}";
                    Log.d("json", tmp);
                    Gson gson = new Gson();
                    trippictures tri = gson.fromJson(tmp, trippictures.class);
                    List<String> iii=tri.getreturnImageName();

                    if(iii.get(0).equals("null")||iii.get(0).equals("error")||iii.size()==0)
                    {
                        builder.setMessage("该行程没有照片");
                        // 创建对话框
                        AlertDialog ad = builder.create();
                        // 显示对话框
                        ad.show();
                    }
                    else {
                        tripictureNames=iii;
                        friendPicNames.clear();
                        friendCom.clear();
                        new Thread() {
                            @Override
                            public void run() {
                                for(int u=0;u<tripictureNames.size();u++)
                                { tmp1.getParamTest("http://120.27.7.115:1010/api/imagemessage?imagename=" + tripictureNames.get(u), handler);
                                }
                            }
                        }.start();
                    }
                }
            }
        };
        triplist.setAdapter(new triplist(lookFriendTrip.this, list));
        triplist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2, long arg3) {
                new Thread() {
                    @Override
                    public void run() {
                        tmp3.getParamTest("http://120.27.7.115:1010/api/road?roadid=" +watchFriend.tFriendId.get(arg2), handler3);
                    }
                }.start();
            }
        });
        leftDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lookFriendTrip.this.finish();
            }
        });
    }
    public List<Map<String, Object>> getData(){
        if(watchFriend.tFriendName.size()==0)
            Toast.makeText(lookFriendTrip.this,"该好友的行程里还没有照片",Toast.LENGTH_LONG).show();
        else{
        for (int i = 0; i < watchFriend.tFriendName.size(); i++) {
            Map<String, Object> map=new HashMap<String, Object>();
            map.put("time",watchFriend.tFriendName.get(i));
            list.add(map);
        }}
        return list;
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
