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
import com.example.asdf.httpClient.httpImage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Useradmin on 2016/11/8.
 */
public class friend extends Activity {
//    implements AdapterView.OnItemClickListener
    private ImageView leftDrawer;
//    private ImageView cancelWatch;
    private ListView friendListview=null;
    List<Map<String, Object>> list;
    private ListView lv_main;
    private List<tmpBean> listDatas;

    private tmpAdapter lAdapter;

    int i;
    int w=0;
    Adapter adapter;
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend);
        lv_main = (ListView) findViewById(R.id.friendListview);
        listDatas = new ArrayList<>();
        initData();
        lAdapter = new tmpAdapter(listDatas, this);
        lv_main.setAdapter(lAdapter);
//        ooo= (ImageView) findViewById(R.id.ooo);照片详情
        leftDrawer= (ImageView) findViewById(R.id.leftdrawer);
//        friendListview= (ListView) findViewById(R.id.friendListview);
//        cancelWatch= (ImageView) findViewById(R.id.cancelWatch);
//        list=getData();
//        adapter=new listViewForFriend(this, list,mListener);
//        friendListview.setAdapter((ListAdapter) adapter);
        leftDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                friend.this.finish();
            }
        });
//        cancelWatch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                cancelWatch.setBackgroundResource(R.drawable.before);
//                watchFriend.friendsnumber--;
//            }
//        });
        lv_main.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(watchFriend.this,"oo"+position,Toast.LENGTH_LONG).show();
                startActivity(new Intent(friend.this, tripLine.class));
            }
        });
    }
//        public List<Map<String, Object>> getData(){
//            List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
//            for (int i = 0; i < 10; i++) {
//                Map<String, Object> map=new HashMap<String, Object>();
//                map.put("examplePicture", R.drawable.ddd);
//                map.put("love",R.drawable.notadm);
//                map.put("exampletext", "oj木有小丁丁oj木有小丁丁oj木有小丁丁oj木有小丁丁oj木有小丁丁oj木有小丁丁");
//                list.add(map);
//            }
//            return list;
//        }

//    // 响应item点击事件
//    @Override
//    public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
////        startActivity(new Intent(friend.this, singleDetail.class));
//    }

    /**
     * 实现类，响应按钮点击事件
     */
//    private iClick mListener = new iClick() {
//        @Override
//        public void listViewItemClick(int position, View v) {
//            Toast.makeText(
//                    friend.this,
//                    "listview的内部的按钮被点击了！，位置是-->" + position + ",内容是-->"
//                            + list.get(position), Toast.LENGTH_SHORT)
//                    .show();
//            List<Map<String, Object>> listt=new ArrayList<Map<String,Object>>();
//            for (int i = 0; i < 10; i++) {
//                Map<String, Object> map=new HashMap<String, Object>();
//                map.put("examplePicture", R.drawable.ddd);
//                if(i==position)
//                {
//                    map.put("love", R.drawable.admire);
//                }
//                else
//                map.put("love", R.drawable.notadm);
//                map.put("exampletext", "oj木有小丁丁oj木有小丁丁oj木有小丁丁oj木有小丁丁oj木有小丁丁oj木有小丁丁");
//                listt.add(map);
//            }
//            adapter=new listViewForFriend(friend.this,listt,mListener);
//            friendListview.setAdapter((ListAdapter) adapter);
//        }
//    };
    private void initData() {
        List<tmpBean> sun1 = new ArrayList<>() ;
        for(i=0;i<lookTrip.tripictureNames.size();i++) {
            sun1.add(new tmpBean());
            sun1.get(i).setPicUrl("http://120.27.7.115:1010/api/image" + "?name=" + lookTrip.tripictureNames.get(i));
            System.out.println( lookTrip.tripictureNames.get(i)+"friend 界面");
            listDatas.add(sun1.get(i));
        }
    }


}

