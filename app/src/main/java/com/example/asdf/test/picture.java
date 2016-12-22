
package com.example.asdf.test;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.example.asdf.httpClient.httpClient;
import com.example.asdf.httpClient.httpImage;
import com.example.asdf.test.adapter.tmpAdapter;
import com.example.asdf.test.attached.commen;
import com.example.asdf.test.attached.tmpBean;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by Useradmin on 2016/10/23.
 */
public class picture extends Activity {

    private ImageView leftDrawer;
    private TextView picture;//查看照片
    int num = 0;
    int i;
    int screenWidth;
    int screenHeigh;
    public static String intro;
    public static String na;
    public static  List<commen> comlist;
    private GridView gridView1;              //网格显示缩略图
    private GridView lv_main;
    private List<tmpBean> listDatas;
    private tmpAdapter lAdapter;
    private Handler handler;
    private Handler handler1;
    private Handler handler2;
    private Handler handler3;
    public static Bitmap tmpBitmap;
    httpImage tmp=new httpImage();
    httpClient tmp1 = new httpClient();
    httpImage tmp2 = new httpImage();
    httpClient tmp3 = new httpClient();
    httpClient tmp4 = new httpClient();
    int deleDosition =0;
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picture);
        lv_main = (GridView) findViewById(R.id.gridView);
        listDatas = new ArrayList<>();
        initData();
        lAdapter = new tmpAdapter(listDatas, this);
        lv_main.setAdapter(lAdapter);
        DisplayMetrics dm = new DisplayMetrics();
        //获取屏幕信息
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        screenHeigh = dm.heightPixels;
        leftDrawer = (ImageView) findViewById(R.id.leftdrawer);
        picture = (TextView) findViewById(R.id.picture);
//        gridView1 = (GridView) findViewById(R.id.gridView);
        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(picture.this, picture.class));
            }
        });
        leftDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picture.this.finish();
            }
        });
        handler = new Handler()
        {
            @Override
            public void handleMessage(Message msg) {
                String tmp = msg.obj.toString();
                tmp = "{" + tmp + "}";
                Log.d("json", tmp);
                Gson gson = new Gson();
                pictureinfor peopl = gson.fromJson(tmp, pictureinfor.class);
                na = peopl.getImageName();
                intro = peopl.getIntroduction();
                if(intro!=null)
                {
                    System.out.println(intro + "  0 " + na);
                    new Thread()
                    {
                        public void run()
                        {
                            if(na!=null)
                                tmpBitmap =tmp2.getBitmap("http://120.27.7.115:1010/api/image?name="+na,handler1);
                        }
                    }.start();
//                    Toast.makeText(picture.this,"获取照片详情成功",Toast.LENGTH_SHORT).show();
                }
            }

        };
        handler1 = new Handler()
        {
            @Override
            public void handleMessage(Message msg) {
                String tmp = msg.obj.toString();
                    System.out.println(tmp+"single");
                if(tmp.equals("true")) {
                    new Thread() {
                        @Override
                        public void run() {
                            System.out.println(na+"照片名");
                            tmp4.getParamTest("http://120.27.7.115:1010/api/Comment?imageName=" + na, handler3);
                        }
                    }.start();
//                    startActivity(new Intent(picture.this, singleDetail.class));
                }
                else{
                    System.out.println("获取照片详情失败");
                }
            }

        };
        handler2 = new android.os.Handler() {
            public void handleMessage(Message msg) {
                String tmp = msg.obj.toString();
                if(tmp.equals("true"))
                {
                    Toast.makeText(picture.this, "删除成功", Toast.LENGTH_SHORT).show();
                    login.lll.remove(deleDosition);
                    listDatas.clear();
                    initData();
                    lAdapter.notifyDataSetChanged();
                    System.out.println("t功");
                }
                else
                {
                    Toast.makeText(picture.this,"删除失败",Toast.LENGTH_SHORT).show();
                }
            }
        };
        handler3= new Handler()
        {
            @Override
            public void handleMessage(Message msg) {
                String tmp = msg.obj.toString();
                tmp = "{" + tmp + "}";
                System.out.println(tmp+"获取评论");
                Log.d("json", tmp);
                Gson gson = new Gson();
                comment comli = gson.fromJson(tmp, comment.class);
                comlist=comli.getcoList();
                String a=comli.getresult();
                if(a.equals("true"))
                {
                    System.out.println("获取评论成功");
                    startActivity(new Intent(picture.this, singleDetail.class));
//                    Toast.makeText(picture.this,"获取照片详情成功",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    System.out.println("获取评论失败");
                    Toast.makeText(picture.this,"获取评论失败",Toast.LENGTH_SHORT).show();
                }
            }

        };
        lv_main.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2, long arg3) {
                    new Thread() {
                        @Override
                        public void run() {
                            tmp1.getParamTest("http://120.27.7.115:1010/api/imagemessage?imagename=" + login.lll.get(arg2), handler);
                        }
                    }.start();
            }
        });
        lv_main.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                                            dialog(position);
                                            deleDosition = position;
                                            return true;
            }
        });
    }
    private void initData() {
        List<tmpBean> sun1 = new ArrayList<>() ;
        for(i=0;i<login.lll.size();i++) {
            if(login.lll.get(i).length()>6) {
                sun1.add(new tmpBean());
                sun1.get(i).setPicUrl("http://120.27.7.115:1010/api/image" + "?name=" + login.lll.get(i));
                listDatas.add(sun1.get(i));
            }
        }
    }
        /*
152.     * Dialog对话框提示用户删除操作
153.     * position为删除图片位置
154.     */
        protected void dialog(final int position) {
            AlertDialog.Builder builder = new AlertDialog.Builder(picture.this);
            builder.setMessage("确认移除已添加图片吗？");
            builder.setTitle("提示");
            builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new Thread() {
                        public void run() {
                            JSONObject object = new JSONObject();
                            System.out.println(position + "删除位置");
                            object.put("imageName", login.lll.get(position));
                            tmp3.postParamsJson("http://120.27.7.115:1010/api/Image_Delete", object, handler2);
                        }
                    }.start();
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
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
    class comment
    {
        private String result;
        private List<commen>commentBackList;
        public String getresult(){
            return result;
        }
        public List<commen> getcoList()
        {
            return commentBackList;
        }
    }

}
