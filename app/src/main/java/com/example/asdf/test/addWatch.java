
package com.example.asdf.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Useradmin on 2016/10/23.
 */
public class addWatch extends Activity implements AdapterView.OnItemClickListener{
    private ImageView leftDrawer;
    private ImageView enter;
    private ListView addWatchListView;
    public static List<Map<String, Object>> list;
    Adapter adapter;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addwatch);
        addWatchListView= (ListView) findViewById(R.id.addWatchListview);
//        enter= (ImageView) findViewById(R.id.enter);
        list=getData();
        adapter=new listViewAdapter(this, list,mListener);
        addWatchListView.setAdapter((ListAdapter) adapter);
        leftDrawer= (ImageView) findViewById(R.id.leftdrawer);
        leftDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               addWatch.this.finish();
            }
        });
    }
    public List<Map<String, Object>> getData(){
        List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
        for (int i = 0; i < 10; i++) {
            Map<String, Object> map=new HashMap<String, Object>();
            map.put("friendName", "oj木有小丁丁");
            map.put("love", R.drawable.before);
            map.put("lovetext","添加关注");
            map.put("examplePicture", R.drawable.ddd);
            map.put("exampletext", "oj木有小丁丁oj木有小丁丁oj木有小丁丁oj木有小丁丁oj木有小丁丁oj木有小丁丁");
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
        public void listViewItemClick(int position, View v) {
//            Toast.makeText(
//                    friend.this,
//                    "listview的内部的按钮被点击了！，位置是-->" + position + ",内容是-->"
//                            + list.get(position), Toast.LENGTH_SHORT)
//                    .show();
            List<Map<String, Object>> listt=new ArrayList<Map<String,Object>>();
            for (int i = 0; i < 10; i++) {
                Map<String, Object> map=new HashMap<String, Object>();
                map.put("friendName", "oj木有小丁丁");
                map.put("examplePicture", R.drawable.ddd);
                map.put("lovetext","添加关注");
                map.put("exampletext", "oj木有小丁丁oj木有小丁丁oj木有小丁丁oj木有小丁丁oj木有小丁丁oj木有小丁丁");
                if(i==position)
                {
                    map.put("love", R.drawable.love);
                }
                else
                    map.put("love", R.drawable.before);
                listt.add(map);
            }
            adapter=new friendListView(addWatch.this,listt,mListener);
            addWatchListView.setAdapter((ListAdapter) adapter);
        }
    };
}

