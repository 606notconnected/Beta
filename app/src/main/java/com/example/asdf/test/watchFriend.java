package com.example.asdf.test;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * Created by Useradmin on 2016/11/15.
 */
public class watchFriend extends Activity implements AdapterView.OnItemClickListener{
    private ImageView addFriend;
    private ImageView leftDrawer;
    private ListView watch_friend=null;
    private   List<Map<String, Object>> list;
    public static int friendsnumber=10;
    Adapter adapter;
    // TODO Auto-generated method stub
    protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.watch_friend);
    addFriend= (ImageView) findViewById(R.id.addFriend);
    list=getData();
        adapter=new friendListView(this, list,mListener);
        watch_friend= (ListView) findViewById(R.id.watch_friend);
        watch_friend.setAdapter((ListAdapter) adapter);
        addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(watchFriend.this, addWatch.class));
            }
        });
        watch_friend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(watchFriend.this,"oo"+position,Toast.LENGTH_LONG).show();
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

    }
    public List<Map<String, Object>> getData(){
        List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
        for (int i = 0; i < friendsnumber; i++) {
            Map<String, Object> map=new HashMap<String, Object>();
//            map.put("enter", R.drawable.enter);
            map.put("friendName", "oj木有小丁丁"+i);
//            map.put("entertext", "进入主页");
            map.put("love", R.drawable.love);
            map.put("lovetext","取消关注");
            map.put("examplePicture", R.drawable.ddd);
            map.put("exampletext", "oj木有小丁丁oj木有小丁丁oj木有小丁丁oj木有小丁丁oj木有小丁丁oj木有小丁丁");
            list.add(map);
        }
        return list;
    }

    // 响应item点击事件
//    @Override
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
        public void listViewItemClick(int position, View v) {
//            Toast.makeText(
//                    friend.this,
//                    "listview的内部的按钮被点击了！，位置是-->" + position + ",内容是-->"
//                            + list.get(position), Toast.LENGTH_SHORT)
//                    .show();
            List<Map<String, Object>> listt=new ArrayList<Map<String,Object>>();
            for (int i = 0; i < friendsnumber; i++) {
                Map<String, Object> map=new HashMap<String, Object>();
                map.put("friendName", "oj木有小丁丁"+i);
//            map.put("entertext", "进入主页");
                map.put("lovetext","取消关注");
                map.put("examplePicture", R.drawable.ddd);
                map.put("exampletext", "oj木有小丁丁oj木有小丁丁oj木有小丁丁oj木有小丁丁oj木有小丁丁oj木有小丁丁");
                if(i==position)
                {
                    map.put("love", R.drawable.before);
                }
                else
                    map.put("love", R.drawable.love);
                listt.add(map);
            }
            adapter=new friendListView(watchFriend.this,listt,mListener);
            watch_friend.setAdapter((ListAdapter) adapter);
        }
    };
}
