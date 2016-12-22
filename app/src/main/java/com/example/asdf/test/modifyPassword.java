package com.example.asdf.test;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.asdf.httpClient.httpClient;

import org.json.JSONObject;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * Created by Useradmin on 2016/11/6.
 */
public class modifyPassword  extends Activity {
    private ImageView leftDrawer;//回退
    private String userPhone;
    private EditText tele;
    private EditText checkNumb;
    private Button cancelBtn;
    private Button submitBtn;
    private Button gettNumber;
    private Button checkk;
    private  EditText p1;
    private  EditText p2;
    private static final int CODE_ING = 1;                  //已发送，倒计时
    private static final int CODE_REPEAT = 2;               //重新发送
    private static final int SMSDDK_HANDLER = 3;            //短信回调
    private int TIME = 60;//倒计时60s
    private android.os.Handler handler1;
    private httpClient tmp = new httpClient();
    private int flag=1;
    com.alibaba.fastjson.JSONObject object = new com.alibaba.fastjson.JSONObject();

    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modifypassword);
        tele= (EditText) findViewById(R.id.tele);
        checkNumb= (EditText) findViewById(R.id.chenkNumb);
        gettNumber= (Button) findViewById(R.id.gettnumber);
        checkk= (Button) findViewById(R.id.checkk);
        cancelBtn= (Button) findViewById(R.id.cancelBtn);
        p1 = (EditText) findViewById(R.id.newpass1);
        p2 = (EditText) findViewById(R.id.newpass2);
        submitBtn= (Button) findViewById(R.id.submitBtn);
        initSDK();//短信初始化
        handler1 = new android.os.Handler()
        {
            @Override
            public void handleMessage(Message msg) {
                String tmp = msg.obj.toString();
                if(tmp.equals("true")) {
                    Toast.makeText(modifyPassword.this,"修改密码成功",Toast.LENGTH_LONG).show();
                    modifyPassword.this.finish();
                }
                else if(tmp.equals("false"))
                {
                    Toast.makeText(modifyPassword.this,"修改失败",Toast.LENGTH_LONG).show();
                }
                else if(tmp.equals("error"))
                {
                    Toast.makeText(modifyPassword.this,"服务器出错",Toast.LENGTH_LONG).show();
                }
            }

        };
        leftDrawer = (ImageView) findViewById(R.id.leftdrawer);
        leftDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifyPassword.this.finish();
            }
        });
        gettNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userPhone = tele.getText().toString();
                new AlertDialog.Builder(modifyPassword.this)
                        .setTitle("发送短信")
                        .setMessage("我们将把验证码发送到以下号码:\n" + "+86:" + userPhone)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SMSSDK.getVerificationCode("86", userPhone);
                                gettNumber.setClickable(false);
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        for (int i = 60; i > 0; i--) {
                                            handler.sendEmptyMessage(CODE_ING);
                                            if (i <= 0) {
                                                break;
                                            }
                                            try {
                                                Thread.sleep(1000);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        handler.sendEmptyMessage(CODE_REPEAT);
                                    }
                                }).start();
                            }
                        })
                        .create()
                        .show();
            }
        });
        checkk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SMSSDK.submitVerificationCode("86", userPhone, checkNumb.getText().toString());//对验证码进行验证->回调函数
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               modifyPassword.this.finish();
            }
        });
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userPhone = tele.getText().toString();
                String pass1 = p1.getText().toString();
                String pass2 = p2.getText().toString();
                object.put("account", userPhone);
                object.put("oldPassword", pass1);
                object.put("newPassword", pass2);
                    //  httpClient.postParamsJson("http://120.27.7.115:1010/api/forgetpassword", object);
                    new Thread() {
                        @Override
                        public void run() {
                            if(flag==0){
                            tmp.postParamsJson("http://120.27.7.115:1010/api/password", object, handler1);}
                        }
                    }.start();
                }
        });
    }

    //初始化SMSSDK
    private void initSDK() {
        SMSSDK.initSDK(modifyPassword.this, "18e3768f516a3", "e05f202526cd41dcf17d2498d6a7aeeb");
        EventHandler eventHandler = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                msg.what = SMSDDK_HANDLER;
                handler.sendMessage(msg);
            }
        };
        // 注册回调监听接口
        SMSSDK.registerEventHandler(eventHandler);
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CODE_ING://已发送,倒计时
                    gettNumber.setText("重新发送");
                    --TIME;
                    break;
                case CODE_REPEAT://重新发送
                    gettNumber.setText("获取验证码");
                    gettNumber.setClickable(true);
                    break;
                case SMSDDK_HANDLER:
                    int event = msg.arg1;
                    int result = msg.arg2;
                    Object data = msg.obj;
                    //回调完成
                    if (result == SMSSDK.RESULT_COMPLETE) {
                        //验证码验证成功
                        if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                            flag=0;
                            Toast.makeText(modifyPassword.this, "验证成功", Toast.LENGTH_LONG).show();

                        }
                        //已发送验证码
                        else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                            Toast.makeText(getApplicationContext(), "验证码已经发送",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            ((Throwable) data).printStackTrace();
                        }
                    }
                    if (result == SMSSDK.RESULT_ERROR) {
                        try {
                            Throwable throwable = (Throwable) data;
                            throwable.printStackTrace();
                            JSONObject object = new JSONObject(throwable.getMessage());
                            String des = object.optString("detail");//错误描述
                            int status = object.optInt("status");//错误代码
                            if (status > 0 && !TextUtils.isEmpty(des)) {
                                Toast.makeText(getApplicationContext(), des, Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (Exception e) {
                            //do something
                        }
                    }
                    break;
            }
        }
    };
}