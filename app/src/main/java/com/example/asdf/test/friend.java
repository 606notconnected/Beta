package com.example.asdf.test;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.example.asdf.httpClient.httpClient;
import com.example.asdf.httpClient.httpImage;
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
    List<Map<String, Object>> list;
    private ListView lv_main;
    private Handler handler;
    private Handler handler3;
    httpClient tmp2 =new httpClient();
    httpClient tmp1 = new httpClient();
    public static  List<commen> friendcomlist;
    int i;
    listViewForFriend adapter;
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend);
        lv_main = (ListView) findViewById(R.id.friendListview);
        leftDrawer= (ImageView) findViewById(R.id.leftdrawer);
        list=getData();
        adapter=new listViewForFriend(this, list,mListener);
        lv_main.setAdapter((ListAdapter) adapter);
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
                tmp = "{" + tmp + "}";
                if(tmp.equals("true"))
                {
                    System.out.println("点赞成功");
                    Toast.makeText(friend.this,"点赞成功",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(friend.this,"点赞不成功",Toast.LENGTH_SHORT).show();
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
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(friend.this,"oo"+position,Toast.LENGTH_LONG).show();
                new Thread() {
                    @Override
                    public void run() {
                        tmp2.getParamTest("http://120.27.7.115:1010/api/Comment?imageName=" , handler3);
                    }
                }.start();
            }
        });
    }
        public List<Map<String, Object>> getData(){
            List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
            for (int i = 0; i < 10; i++) {
                Map<String, Object> map=new HashMap<String, Object>();
                map.put("examplePicture", R.drawable.icon);
                map.put("love",R.drawable.notadm);
                map.put("exampletext", "oj木有小丁丁oj木有小丁丁oj木有小丁丁oj木有小丁丁oj木有小丁丁oj木有小丁丁");
                list.add(map);
            }
            return list;
        }

    // 响应item点击事件
    @Override
    public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
//        startActivity(new Intent(friend.this, singleDetail.class));
    }

    /**
     * 实现类，响应按钮点击事件
     */
    private iClick mListener = new iClick() {
        @Override
        public void listViewItemClick(int position, View v) {
//            Toast.makeText(
//                    friend.this,
//                    "listview的内部的按钮被点击了！，位置是-->" + position + ",内容是-->"
//                            + list.get(position), Toast.LENGTH_SHORT)
//                    .show();
            new Thread() {
                @Override
                public void run() {
                    tmp1.getParamTest("http://120.27.7.115:1010/api/imagemessage?imagename=", handler);
//                            if(na!=null)
//                            { tmpBitmap =tmp2.getBitmap("http://120.27.7.115:1010/api/image?name="+na,handler1);}
                }
            }.start();
            List<Map<String, Object>> listt=new ArrayList<Map<String,Object>>();
            for (int i = 0; i < 10; i++) {
                Map<String, Object> map=new HashMap<String, Object>();
                map.put("examplePicture", R.drawable.icon);
                if(i==position)
                {
                    map.put("love", R.drawable.admire);
                }
                else
                map.put("love", R.drawable.notadm);
                map.put("exampletext", "oj木有小丁丁oj木有小丁丁oj木有小丁丁oj木有小丁丁oj木有小丁丁oj木有小丁丁");
                listt.add(map);
            }
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
    public class commen {
        private String commentID;
        private String account;
        private String comment;
        private String imageName;
        private String dateTime;
        public String getcommentID(){
            return commentID;
        }
        public String getaccount(){
            return account;
        }
        public String getcomment(){
            return comment;
        }
        public String getimageName(){
            return imageName;
        }
        public String getdateTime(){
            return dateTime;
        }
    }
}

