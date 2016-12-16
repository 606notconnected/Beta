package com.example.asdf.test;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.example.asdf.httpClient.httpClient;
import com.example.asdf.httpClient.httpImage;
import com.example.asdf.test.adapter.commentListview;

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
    private commentListview adapter;
    private ImageView zp;
    private TextView wz;
    private Button comment;
    private TextView lovenum;
    private TextView commentForFriend;
    httpClient tmp2=new httpClient();
    private List<Map<String, Object>> list = null;
    httpImage tmp1=new httpImage();
    private Handler handler;
    Handler handler1;
    Handler handler2;
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.friend_detailed);
        replylist= (ListView) findViewById(R.id.replylist);
        commentForFriend= (TextView) findViewById(R.id.commentForFriend);
        zp= (ImageView) findViewById(R.id.zp);
        wz= (TextView) findViewById(R.id.wz);
        comment= (Button) findViewById(R.id.comment);
        lovenum= (TextView) findViewById(R.id.loveNum);
        zp.setImageBitmap(friend.friendPic);
        wz.setText(friend.friendInt);
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
        handler2 = new Handler()
        {
            @Override
            public void handleMessage(Message msg) {
                String tmp = msg.obj.toString();
                System.out.println(tmp+"gg");
                if(tmp!="error") {
                    lovenum.setText(tmp);
                    System.out.println("获取朋友照片点赞数成功");
//                    startActivity(new Intent(picture.this, singleDetail.class));
                }
                else{
                    System.out.println("获取朋友照片点赞数失败");
                }
            }
        };
        new Thread(){
            @Override
            public void run() {
                tmp2.getParamTest("http://120.27.7.115:1010/api/ClickLike?name=" + friend.friendNam, handler2);
            }
        }.start();
        handler = new Handler()
        {
            @Override
            public void handleMessage(Message msg) {
                String tmp = msg.obj.toString();
                if(tmp.equals("true"))
                {
                    Toast.makeText(friendDetailed.this, "上传评论成功", Toast.LENGTH_LONG).show();
                }
                else{
                    System.out.println("上传评论失败");
                }
            }

        };
        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ss=commentForFriend.getText().toString();
                if(ss!=null){
                    final JSONObject object = new JSONObject();
                    object.put("account", login.account);
                    object.put("comment",ss);
                    object.put("imageName", friend.friendNam);
                    System.out.println(login.account+" oo "+comment.getText()+" pp "+friend.friendNam);
                    new Thread()
                    {
                        @Override
                        public void run()
                        {
                            tmp2.postParamsJson("http://120.27.7.115:1010/api/Comment", object, handler);
                        }
                    }.start();
                }
                else{
                    Toast.makeText(friendDetailed.this,"评论内容不能为空",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < friend.friendcomlist.size(); i++) {
            if (friend.friendcomlist.get(i).getaccount()!=null) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("name",friend.friendcomlist.get(i).getaccount());
                map.put("comment", friend.friendcomlist.get(i).getcomment());
                list.add(map);
            }
        }
        return list;
    }
}
