package com.example.asdf.test;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.asdf.httpClient.httpClient;

/**
 * Created by Useradmin on 2016/11/6.
 */
public class modifyinformation extends Activity {
        private ImageView leftDrawer;//回退
        private Bitmap head;// 头像Bitmap
        private ImageView myhead;//头像
        private static String path = "/sdcard/myHeadOfData/";// sd路径
        private EditText newusername;
        private RadioButton man;
        private int nnn=3;
        private RadioButton womam;
        private EditText newintroduce;
        private RadioGroup radioGroup;
        private Button newsubmit;
        private String newsex;
        private httpClient tmp = new httpClient();
        private android.os.Handler handler;
        com.alibaba.fastjson.JSONObject object= new com.alibaba.fastjson.JSONObject();
        protected void onCreate(Bundle savedInstanceState) {
                // TODO Auto-generated method stub
                super.onCreate(savedInstanceState);
                setContentView(R.layout.modifyinformation);
                myhead = (ImageView) findViewById(R.id.headpicture);
                leftDrawer = (ImageView) findViewById(R.id.leftdrawer);
                newusername= (EditText) findViewById(R.id.newusername);
                man= (RadioButton) findViewById(R.id.man);
                womam= (RadioButton) findViewById(R.id.womam);
                radioGroup= (RadioGroup) findViewById(R.id.radio);
                newintroduce= (EditText) findViewById(R.id.newintroduce);
                newsubmit= (Button) findViewById(R.id.newsubmit);
                leftDrawer.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                modifyinformation.this.finish();
                        }
                });
                handler = new android.os.Handler()
                {
                        @Override
                        public void handleMessage(Message msg) {
                                String tmp = msg.obj.toString();
                                if(tmp.equals("true")) {
                                        Toast.makeText(modifyinformation.this, "修改成功", Toast.LENGTH_LONG).show();
                                        modifyinformation.this.finish();
                                }
                                else if(tmp.equals("false"))
                                {
                                        Toast.makeText(modifyinformation.this, "修改失败", Toast.LENGTH_LONG).show();
                                }
                                else if(tmp.equals("error"))
                                {
                                        Toast.makeText(modifyinformation.this,"服务器出错",Toast.LENGTH_LONG).show();
                                }
                        }

                };
                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                                // TODO Auto-generated method stub
                                if (checkedId == womam.getId()) {
                                        newsex = "女";
                                        nnn = 0;
                                } else {
                                        newsex = "男";
                                        nnn = 1;
                                }
                        }
                });

                newsubmit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                String a,b,c;
                                if (newusername.getText().toString() == null||newusername.getText().toString().length()<=0) {
                                      a= login.uuername;
                                }
                                else{
                                a=newusername.getText().toString();
                                login.uuername=newusername.getText().toString();}
                                if (nnn == 3) {
                                        if(login.sex.equals("女"))
                                                b="0";
                                        else
                                                b="1";
                                }
                                else
                                {       b= String.valueOf(nnn);
                                        login.sex=newsex;}
                                if (newintroduce.getText().toString() == null||newintroduce.getText().toString().length()<=0) {
                                       c= login.introduce;
                                } else
                                {
                                        c=newintroduce.getText().toString();
                                        login.introduce=newintroduce.getText().toString();
                                }
                                if(a!=null&&b!=null&&c!=null) {
                                        object.put("account", login.account);
                                        object.put("userName",a );
                                        object.put("sex", b);
                                        object.put("introduction",c);
                                        object.put("email", "0000000");
                                        System.out.println(newusername.getText().toString()+"098"+object);
//                                Toast.makeText(modifyinformation.this,newsex+"  "+newintroduce.getText().toString()+"  "+newusername.getText().toString(),Toast.LENGTH_LONG).show();
                                        new Thread() {
                                                @Override
                                                public void run() {
                                                        tmp.postParamsJson("http://120.27.7.115:1010/api/usermessage", object, handler);
                                                }
                                        }.start();
                                }
                                // httpClient.postParamsJson("http://120.27.7.115:1010/api/peoplemessage", object);
                        }
                });
//                Bitmap bt = BitmapFactory.decodeFile(path + "myhead.jpg");// 从Sd中找头像，转换成Bitmap
                if (mainView.myheadDrawable != null) {
                        myhead.setImageDrawable(mainView.myheadDrawable);
                }
        }

}