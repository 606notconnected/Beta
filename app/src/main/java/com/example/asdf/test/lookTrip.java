package com.example.asdf.test;

import android.animation.AnimatorSet;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.media.session.PlaybackState;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.example.asdf.httpClient.httpClient;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Useradmin on 2016/10/23.
 */
public class lookTrip extends Activity{
    private ImageView leftDrawer;
    private ListView triplist=null;
    httpClient tmp3 = new httpClient();
    public static  List<String> tripictureNames;
    private android.os.Handler handler3;
    public static List<String> longgg = new ArrayList<>();
    public static List<String> lattt= new ArrayList<>();
    public static List<String> picNames=new ArrayList<>();
    List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
    public static  ArrayList<String> tName=new ArrayList<>();
    public static ArrayList<String> tId=new ArrayList<>();
    JSONObject object = new JSONObject();
    private httpClient tmp = new httpClient();
    private httpClient tmp1= new httpClient();
    private android.os.Handler handler;
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.looktrip);
        final AlertDialog.Builder builder=new AlertDialog.Builder(this);
        leftDrawer= (ImageView) findViewById(R.id.leftdrawer);
        triplist= (ListView) findViewById(R.id.triplist);

        List<Map<String, Object>> list=getData();
        handler= new android.os.Handler()
        {
            @Override
            public void handleMessage(Message msg) {
                String tmp = msg.obj.toString();
                System.out.println(tmp+"looktrip获取信息");
                tmp = "{" + tmp + "}";
                Log.d("json", tmp);
//                Toast.makeText(login.this,tmp, Toast.LENGTH_LONG).show();
//  String jsonData = "{\"account\":\"John\", \"userName\":20,\"sex\":\"jj\",\"introduction\":\"111\",\"email\":\"Joh22n\",}";;
                Gson gson = new Gson();
                pictureinfor peopl = gson.fromJson(tmp, pictureinfor.class);
                //Log.i("2333", peopl.getLatitude() + "  0 " + peopl.getLongitude());
                lattt.add(peopl.getLatitude());
                longgg.add(peopl.getLongitude());
                picNames.add(peopl.getImageName());
                if(lattt!=null)
                {
                    Log.i("2333",lattt.get(0) + "  0 " + longgg.get(0)+picNames.get(0));
//                    Toast.makeText(login.this,"获取照片详情成功",Toast.LENGTH_SHORT).show();
                }
            }

        };
        handler3 = new android.os.Handler() {
            @Override
            public void handleMessage(Message msg) {
                String tmp = msg.obj.toString();
                System.out.println(tmp+"咯哦天天日票");
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
                        startActivity(new Intent(lookTrip.this, friend.class));
                        new Thread() {
                            @Override
                            public void run() {
                                for(int u=0;u<tripictureNames.size();u++)
                                {tmp1.getParamTest("http://120.27.7.115:1010/api/imagemessage?imagename=" + tripictureNames.get(u), handler);}
//                            if(na!=null)
//                            { tmpBitmap =tmp2.getBitmap("http://120.27.7.115:1010/api/image?name="+na,handler1);}
                            }
                        }.start();
                    }
                    System.out.println(tmp + "0000000000000000" + iii );
//                tripictureNames.add(md);
                }
//                if (tripictureNames!=null) {
//                    Toast.makeText(lookTrip.this, "获取行程图片成功", Toast.LENGTH_SHORT).show();
//                }
//                else {
////                    Toast.makeText(lookTrip.this, "服务器出错", Toast.LENGTH_LONG).show();
//                }
            }
        };
        triplist.setAdapter(new triplist(lookTrip.this, list));
        triplist.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2, long arg3) {
                new Thread() {
                    @Override
                    public void run() {
                        tmp3.getParamTest("http://120.27.7.115:1010/api/road?roadid=" +tId.get(arg2), handler3);
                    }
                }.start();
//                new Thread() {
//                    @Override
//                    public void run() {
//                        tmp.getParamTest("http://120.27.7.115:1010/api/imagemessage?imagename="+tName.get(arg2),handler);
//                    }
//                }.start();
            }
        });
        leftDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lookTrip.this.finish();
            }
        });
    }
    public List<Map<String, Object>> getData(){
        for (int i = 0; i < tName.size(); i++) {
            Map<String, Object> map=new HashMap<String, Object>();
            map.put("time",tName.get(i));
            list.add(map);
        }
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
