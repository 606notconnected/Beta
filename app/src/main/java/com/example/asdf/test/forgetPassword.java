package com.example.asdf.test;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
 * Created by Useradmin on 2016/11/5.
 */
public class forgetPassword extends Activity{
    private int flag=1;
    private Button check;
    private String userPhone;
    private Button getNumber;
    private int TIME = 60;//倒计时60s
    private Boolean isaccount=false;
    private Handler handler1;
    private EditText phoneNumber;
    private EditText checkNunber;
    private ImageView leftDrawer;//回退
    private Button submitNewpassword;
    private static final int CODE_ING = 1;                  //已发送，倒计时
    private static final int CODE_REPEAT = 2;               //重新发送
    private static final int SMSDDK_HANDLER = 3;            //短信回调
    private httpClient tmp = new httpClient();
    com.alibaba.fastjson.JSONObject object = new com.alibaba.fastjson.JSONObject();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgetpassword);
        getNumber= (Button) findViewById(R.id.getnumber);
        phoneNumber= (EditText) findViewById(R.id.phoneNumber);
        checkNunber= (EditText) findViewById(R.id.checkNumber);
        leftDrawer= (ImageView) findViewById(R.id.leftdrawer);
        check= (Button) findViewById(R.id.check);
        submitNewpassword= (Button) findViewById(R.id.submitNewPassword);
        initSDK();//短信初始化
        leftDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgetPassword.this.finish();
            }
        });
        getNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userPhone = phoneNumber.getText().toString();
                new AlertDialog.Builder(forgetPassword.this)
                        .setTitle("发送短信")
                        .setMessage("我们将把验证码发送到以下号码:\n" + "+86:" + userPhone)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SMSSDK.getVerificationCode("86", userPhone);
                                getNumber.setClickable(false);
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
        handler1 = new android.os.Handler()
        {
            @Override
            public void handleMessage(Message msg) {
                String tmp = msg.obj.toString();
                if(tmp.equals("true")) {
                    Toast.makeText(forgetPassword.this,"修改密码成功",Toast.LENGTH_LONG).show();
                    forgetPassword.this.finish();
                }
                else if(tmp.equals("false"))
                {
                    Toast.makeText(forgetPassword.this,"该账号不存在",Toast.LENGTH_LONG).show();
                }
                else if(tmp.equals("error"))
                {
                    Toast.makeText(forgetPassword.this,"服务器出错",Toast.LENGTH_LONG).show();
                }
            }

        };
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SMSSDK.submitVerificationCode("86", userPhone, checkNunber.getText().toString());//对验证码进行验证->回调函数
            }
        });
        submitNewpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText p1= (EditText) findViewById(R.id.pass1);
                        String pass1=p1.getText().toString();
                EditText p2= (EditText) findViewById(R.id.pass2);
                String pass2=p2.getText().toString();
                if(pass1.equals(pass2))//&&isaccount.equals("true")
                {
                    if(pass1.length()<6||pass1.length()>16)
                    {
                        Toast.makeText(forgetPassword.this,"密码长度应该在6~16之间",Toast.LENGTH_LONG).show();
                    }
                    else {
                        userPhone = phoneNumber.getText().toString();
                        object.put("account", userPhone);
                        object.put("newPassword", pass1);
                        new Thread() {
                            @Override
                            public void run() {
                                if (flag == 0) {
                                    tmp.postParamsJson("http://120.27.7.115:1010/api/forgetpassword", object, handler1);
                                }
                            }
                        }.start();
                    }
                    // httpClient.postParamsJson("http://120.27.7.115:1010/api/forgetpassword", object);
                }
                else{
                    Toast.makeText(forgetPassword.this, "2次输入密码不一致",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    //初始化SMSSDK
    private void initSDK()
    {
        SMSSDK.initSDK(forgetPassword.this, "18e3768f516a3", "e05f202526cd41dcf17d2498d6a7aeeb");
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
    Handler handler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case CODE_ING://已发送,倒计时
                    getNumber.setText("重新发送");
                    --TIME;
                    break;
                case CODE_REPEAT://重新发送
                    getNumber.setText("获取验证码");
                    getNumber.setClickable(true);
                    break;
                case SMSDDK_HANDLER:
                    int event = msg.arg1;
                    int result = msg.arg2;
                    Object data = msg.obj;
                    //回调完成
                    if (result == SMSSDK.RESULT_COMPLETE)
                    {
                        //验证码验证成功
                        if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE)
                        {
                            flag=0;
                            Toast.makeText(forgetPassword.this, "验证成功", Toast.LENGTH_LONG).show();
                            isaccount=true;
                        }
                        //已发送验证码
                        else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE)
                        {
                            Toast.makeText(getApplicationContext(), "验证码已经发送",
                                    Toast.LENGTH_SHORT).show();
                        } else
                        {
                            ((Throwable) data).printStackTrace();
                        }
                    }
                    if(result==SMSSDK.RESULT_ERROR)
                    {
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
