package com.example.asdf.test;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.example.asdf.httpClient.httpClient;
import com.example.asdf.test.attached.res;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;


public class login extends Activity {
    private Button login;//登录按钮
    private Button register;//注册按钮
    private TextView forgetPassword;//忘记密码
    public static String account;//获取输入的账号
    public static String uuername;
    public static String sex;
    public static String introduce;
    public static String email;
    public static Bitmap bbsha;
    private String password;//获取输入的密码
    private EditText accountEdi;
    private EditText passwordEdi;
    public static List<String> lll;
    private httpClient tmp = new httpClient();
    private httpClient tmp1= new httpClient();
    httpClient tmp3 = new httpClient();
    httpClient tmp4=new httpClient();
    JSONObject object = new JSONObject();
    private android.os.Handler handler;
    private android.os.Handler handler1;
    private android.os.Handler handler2;
    private android.os.Handler handler3;
    private android.os.Handler handler4;
    private android.os.Handler handler5;
    public static List<String> longg = new ArrayList<>();
    public static List<String> latt= new ArrayList<>();
    public static List<String> picName=new ArrayList<>();;
    public static List<res> wat;
    public static List<String> friendHeadList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        login = (Button) findViewById(R.id.login);
        register = (Button) findViewById(R.id.register);
        forgetPassword = (TextView) findViewById(R.id.forgetPassword);
        accountEdi = (EditText) findViewById(R.id.account);
        passwordEdi = (EditText) findViewById(R.id.password);
        Resources res=getResources();
        bbsha= BitmapFactory.decodeResource(res, R.drawable.admire);
        handler = new android.os.Handler() {
            @Override
            public void handleMessage(Message msg) {
                String tmpq = msg.obj.toString();
                if (tmpq.equals("true")) {
                    new Thread() {
                        @Override
                        public void run() {
                            tmp.getParamTest("http://120.27.7.115:1010/api/imagemessage?account=" + account, handler2);
                            tmp.getParamTest("http://120.27.7.115:1010/api/usermessage" + "?account=" + account, handler1);
//                            { tmpBitmap =tmp2.getBitmap("http://120.27.7.115:1010/api/image?name="+na,handler1);}
                        }
                    }.start();
                    login.this.finish();
                    startActivity(new Intent(login.this, mainView.class));
                } else if (tmpq.equals("false")) {
                    Toast.makeText(login.this, "账号或密码出错", Toast.LENGTH_LONG).show();
                } else if (tmpq.equals("error")) {
                    Toast.makeText(login.this, "服务器出错", Toast.LENGTH_LONG).show();
                }
                }
//                for(int i=0;i<lll.size();i++) {System.out.println(lll.get(i)+"99999");}
//                System.out.println(re+"00000");
            };
        handler3 = new android.os.Handler()
        {
            @Override
            public void handleMessage(Message msg) {
                String tmp = msg.obj.toString();
                System.out.println(tmp+"login获取信息");
                tmp = "{" + tmp + "}";
                Log.d("json", tmp);
                Gson gson = new Gson();
                pictureinfor peopl = gson.fromJson(tmp, pictureinfor.class);
               //Log.i("2333", peopl.getLatitude() + "  0 " + peopl.getLongitude());
                latt.add(peopl.getLatitude());
                longg.add(peopl.getLongitude());
                picName.add(peopl.getImageName());
                if(latt!=null)
                {
                    Log.i("2333",latt.get(0) + "  0 " + longg.get(0)+picName.get(0));
//                    Toast.makeText(login.this,"获取照片详情成功",Toast.LENGTH_SHORT).show();
                }
            }

        };
        handler5 = new android.os.Handler()
        {
            @Override
            public void handleMessage(Message msg) {
                String tmp = msg.obj.toString();
                tmp = "{" + tmp + "}";
                Gson gson = new Gson();
                friendHead frhe = gson.fromJson(tmp, friendHead.class);
                String y=frhe.getresult();
                if(y.equals("true"))
                {
//                    System.out.println(tmp+"000000000000000"+frhe.gethead());
                    friendHeadList.add(frhe.gethead());
//                    Toast.makeText(login.this,"获取好友头像详情成功",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    System.out.println("获取好友头像详情不成功");
                }
            }

        };
        handler2 = new android.os.Handler() {
            @Override
            public void handleMessage(Message msg) {
                String tmp = msg.obj.toString();
                tmp = "{" + tmp + "}";
                Gson gson = new Gson();
                pictures pic = gson.fromJson(tmp, pictures.class);
                lll = pic.getPictureList();
                String re = pic.getresult();
                if (re.equals("true")) {
                    new Thread() {
                        @Override
                        public void run() {
                            for(int u=0;u<lll.size();u++)
                            {tmp1.getParamTest("http://120.27.7.115:1010/api/imagemessage?imagename=" + lll.get(u), handler3);}
//                            if(na!=null)
//                            { tmpBitmap =tmp2.getBitmap("http://120.27.7.115:1010/api/image?name="+na,handler1);}
                        }
                    }.start();
//                    Toast.makeText(login.this, "获取图片列表成功", Toast.LENGTH_SHORT).show();
                } else if (re.equals("false")) {
                    Toast.makeText(login.this, "获取图片列表失败", Toast.LENGTH_LONG).show();
                } else if (re.equals("error")) {
                    Toast.makeText(login.this, "服务器出错", Toast.LENGTH_LONG).show();
                }
            }
//                for(int i=0;i<lll.size();i++) {System.out.println(lll.get(i)+"99999");}
//                System.out.println(re+"00000");
        };


        handler1 = new android.os.Handler() {
            @Override
            public void handleMessage(Message msg) {
                String tmp = msg.obj.toString();
                tmp = "{" + tmp + "}";
                Log.d("json", tmp);
                Gson gson = new Gson();
                People peopl = gson.fromJson(tmp, People.class);
                uuername=peopl.getuserName();
                if(peopl.getsex().equals("0"))
                {
                    sex="女";
                }
                else
                sex="男";
                introduce=peopl.getintroduction();
                email=peopl.getemail();
            }

        };
        handler4= new android.os.Handler()
        {
            @Override
            public void handleMessage(Message msg) {
                String tmp = msg.obj.toString();
                System.out.println(tmp+"管朱");
                tmp = "{" + tmp + "}";
                Gson gson = new Gson();
                tmp=tmp.trim();
                watchs watc = gson.fromJson(tmp, watchs.class);
                String rs=watc.getresult();
                if (rs.equals("true")) {
                        new Thread() {
                            @Override
                            public void run() {
                                for (int i = 0; i < wat.size(); i++) {
                                    System.out.println(wat.get(i).getaccount());
                                    tmp4.getParamTest("http://120.27.7.115:1010/api/Image_Head?account=" + wat.get(i).getaccount(), handler5);
                                }
                            }
                        }.start();
                    System.out.println("获取关注列表成功");
                    wat = watc.getwatchsList();
                }
                else {
                    System.out.println("获取关注列表不成功");
                }
            }

        };
        /*String  paramsString = object.toJSONString();*/

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                account = accountEdi.getText().toString();
                password = passwordEdi.getText().toString();
                if (account != null && password != null) {
                    // TODO Auto-generated method stub
                    account = accountEdi.getText().toString();
                    password = passwordEdi.getText().toString();
                    object.put("Account", account);
                    object.put("Password", password);

                    new Thread() {
                        @Override
                        public void run() {
                            if(account!=null&&password!=null) {
                                tmp.postParamsJson("http://120.27.7.115:1010/api/login", object, handler);
                                tmp3.getParamTest("http://120.27.7.115:1010/api/Follower?account=" +account, handler4);
                            }
                        }
                    }.start();


                }
                //startActivity(new Intent(login.this,mainView.class));
            }
        });
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(com.example.asdf.test.login.this, forgetPassword.class));
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(com.example.asdf.test.login.this, register.class));
            }
        });
    }
    class People {
        private String account;
        private String userName;  //属性都定义成String类型，并且属性名要和Json数据中的键值对的键名完全一样
        private String sex;
        private String introduction;
        private String email;
        public String getaccount() {
            return account;
        }
        public String getuserName() {
            return userName;
        }
        public String getsex() {
            return sex;
        }
        public String getintroduction() {
            return introduction;
        }
        public String getemail() {
            return email;
        }
    }
    class pictures
    {
        public  String result;
        public List<String>imageName;
        public String getresult(){
            return result;
        }
        public List<String> getPictureList() {
                   return imageName;
        }
    }
    class pictureinfor {
        private String imageName;
        private String dateTime;  //属性都定义成String类型，并且属性名要和Json数据中的键值对的键名完全一样
        private String longitude;
        private String latitude;
        private String introduction;
        public String getImageName() {
            return imageName;
        }
        public String getDateTime() {
            return dateTime;
        }
        public String getLongitude() {
            return longitude;
        }
        public String getLatitude() {
            return latitude;
        }
        public String getIntroduction() {
            return introduction;
        }
    }
    class watchs
    {
        private String result;
        private List<res>userList;
        public String getresult(){
            return result;
        }
        public List<res> getwatchsList()
        {
            return userList;
        }
    }
    class friendHead
    {
        private String result;
        private String headimageName;
        public String getresult(){
            return result;
        }
        public String gethead()
        {
            return headimageName;
        }
    }
}