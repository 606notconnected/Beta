package com.example.asdf.test;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Matrix;
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
import com.example.asdf.test.attached.commen;

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
        int width=friend.friendPic.getWidth();
        int height=friend.friendPic.getHeight();
        int newWidth = 290;
        int newHeight = 320;
        //int newWidth=200;
        //   int newHeight=120;
        //计算缩放率，新尺寸除原始尺寸
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(friend.friendPic, 0, 0, width,
                height, matrix, true);
        zp.setImageBitmap(resizedBitmap);
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
                    commentForFriend.setText("");
                    list.clear();
                    List<Map<String, Object>> addList = null;
                    addList=getData();
                    list.addAll(addList);
                    adapter.notifyDataSetChanged();
//                    Toast.makeText(friendDetailed.this, "上传评论成功", Toast.LENGTH_LONG).show();
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
                    commen addCom=new commen();
                    addCom.commentID="null";
                    addCom.comment=ss;
                    addCom.account=login.account;
                    addCom.imageName="null";
                    addCom.dateTime="null";
                    friend.friendcomlist.add(addCom);
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
                map.put("commen", friend.friendcomlist.get(i).getcomment());
                list.add(map);
            }
        }
        return list;
    }
}
