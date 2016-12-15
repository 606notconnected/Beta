package com.example.asdf.test;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.asdf.httpClient.httpImage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Useradmin on 2016/12/11.
 */
public class friendDetailed extends Activity {
    private ImageView leftDrawer;
    private ListView replylist;
    private  commentListview adapter;
    private List<Map<String, Object>> list = null;
    httpImage tmp1=new httpImage();
    Handler handler1;
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_detailed);
        replylist= (ListView) findViewById(R.id.replylist);
        list=getData();
        adapter = new commentListview(this,list);
        replylist.setAdapter(adapter);
        leftDrawer= (ImageView) findViewById(R.id.leftdrawer);
        leftDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                friendDetailed.this.finish();
            }
        });
    }
    public List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < friend.friendcomlist.size(); i++) {
            if (login.wat.get(i).getaccount()!=null) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("name",friend.friendcomlist.get(i).getaccount());
                map.put("comment", friend.friendcomlist.get(i).getcomment());
                list.add(map);
            }
        }
        return list;
    }
}
