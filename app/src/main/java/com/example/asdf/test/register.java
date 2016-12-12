package com.example.asdf.test;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.example.asdf.httpClient.httpClient;

import java.util.PriorityQueue;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * Created by Useradmin on 2016/10/23.
 */

//这里账户验证还没做，不知道什么时候进行验证

public class register extends Activity{
    private ImageView leftDrawer;
    private httpClient tmp;

    private String account;//用户名
    private String phoneNumber;//手机号
    private String password;//密码

    private String userPhone;
    private static final int CODE_ING = 1;                  //已发送，倒计时
    private static final int CODE_REPEAT = 2;               //重新发送
    private static final int SMSDDK_HANDLER = 3;            //短信回调
    private int TIME = 60;//倒计时60s
    private EditText userName;
    private EditText telephone;
    private EditText checkNum;
    private EditText passwordEdi;
    private EditText passwordAgain;
    private Button cancel;
    private Button submit;
    private Button getNum;
    private Button check;
    private Button checkaccount;
    private int flag=1;
    private boolean admit=false;
    private android.os.Handler handler;
    private android.os.Handler handler2;
    JSONObject object = new JSONObject();

    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        tmp = new httpClient();
        setContentView(R.layout.register);
        leftDrawer= (ImageView) findViewById(R.id.leftdrawer);
        leftDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register.this.finish();
            }
        });
        submit= (Button) findViewById(R.id.submit);
        getNum= (Button) findViewById(R.id.getnumber);
        checkaccount= (Button) findViewById(R.id.checkaccount);
        check= (Button) findViewById(R.id.check);
        cancel= (Button) findViewById(R.id.cancel);
        userName= (EditText) findViewById(R.id.userName);
        telephone= (EditText) findViewById(R.id.telephone);
        checkNum= (EditText) findViewById(R.id.checkNumber);
        passwordEdi= (EditText) findViewById(R.id.passwordEdi);
        passwordAgain= (EditText) findViewById(R.id.passwordAgain);
        initSDK();//短信初始化
        handler = new android.os.Handler()
        {
            @Override
            public void handleMessage(Message msg) {
                String tmp = msg.obj.toString();
                if(tmp.equals("false"))
                {
                    Toast.makeText(register.this,"该用户名可以使用",Toast.LENGTH_LONG).show();
                }
                else if(tmp.equals("true"))
                {
                    Toast.makeText(register.this,"该用户名已被占用",Toast.LENGTH_LONG).show();
                }
                else if(tmp.equals("error"))
                {
                    Toast.makeText(register.this,"服务器出错",Toast.LENGTH_LONG).show();
                }
            }

        };
        handler2 = new android.os.Handler()
        {
            @Override
            public void handleMessage(Message msg) {
                String tmp = msg.obj.toString();
                if(tmp.equals("true"))
                {
                    Toast.makeText(register.this,"注册成功",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(register.this, login.class));
                }
                else if(tmp.equals("false"))
                {
                    Toast.makeText(register.this,"注册失败",Toast.LENGTH_LONG).show();
                }
                else if(tmp.equals("error"))
                {
                    Toast.makeText(register.this,"服务器出错",Toast.LENGTH_LONG).show();
                }
            }

        };
        getNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userPhone = telephone.getText().toString();
                new AlertDialog.Builder(register.this)
                        .setTitle("发送短信")
                        .setMessage("我们将把验证码发送到以下号码:\n" + "+86:" + userPhone)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SMSSDK.getVerificationCode("86", userPhone);
                                getNum.setClickable(false);
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        for (int i = 60; i > 0; i--) {
                                            handler1.sendEmptyMessage(CODE_ING);
                                            if (i <= 0) {
                                                break;
                                            }
                                            try {
                                                Thread.sleep(1000);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        handler1.sendEmptyMessage(CODE_REPEAT);
                                    }
                                }).start();
                            }
                        })
                        .create()
                        .show();
            }
        });
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SMSSDK.submitVerificationCode("86", userPhone, checkNum.getText().toString());//对验证码进行验证->回调函数
            }
        });
        checkaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                account=userName.getText().toString();//用户名
                new Thread()
                {
                    @Override
                    public void run()
                    {
//                            tmp.getParamTest("http://120.27.7.115:1010/api/sign" + "?account="+account,handler);
                        tmp.getParamTest("http://120.27.7.115:1010/api/sign?username=" + account, handler);
                    }
                }.start();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                account=userName.getText().toString();//用户名
                phoneNumber=telephone.getText().toString();//手机号
                String password1=passwordEdi.getText().toString();//密码
                String password2=passwordAgain.getText().toString();//密码
                if(password1.equals(password2)&&password1!=null)
                {
//            Toast.makeText(getApplicationContext(), "2次输入密码不一致",
//                    Toast.LENGTH_SHORT).show();
                    password=password1;
                }
                if(account!=null&&phoneNumber!=null&&password!=null&&flag==0)
                {
//                    if(resultString)
                    object.put("account", phoneNumber);
                    object.put("password",password);
                    object.put("username",account);
                    new Thread()
                    {
                        @Override
                        public void run()
                        {
//                            tmp.getParamTest("http://120.27.7.115:1010/api/sign" + "?account="+account,handler);
                            tmp.postParamsJson( "http://120.27.7.115:1010/api/sign",object,handler2);
                        }
                    }.start();
                    //httpClient.postParamsJson("http://120.27.7.115:1010/api/sign",object);
                }
            }
        });
    }
    //初始化SMSSDK
    private void initSDK()
    {
        SMSSDK.initSDK(register.this, "18e3768f516a3", "e05f202526cd41dcf17d2498d6a7aeeb");
        EventHandler eventHandler = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                msg.what = SMSDDK_HANDLER;
                handler1.sendMessage(msg);
            }
        };
        // 注册回调监听接口
        SMSSDK.registerEventHandler(eventHandler);
    }
    Handler handler1 = new Handler()
    {
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case CODE_ING://已发送,倒计时
                    getNum.setText("重新发送");
                    --TIME;
                    break;
                case CODE_REPEAT://重新发送
                    getNum.setText("获取验证码");
                    getNum.setClickable(true);
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
                            Toast.makeText(register.this, "验证成功", Toast.LENGTH_LONG).show();
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
                            org.json.JSONObject object = new org.json.JSONObject(throwable.getMessage());
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




