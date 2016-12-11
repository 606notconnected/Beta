
package com.example.asdf.test;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.session.PlaybackState;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.renderscript.Script;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asdf.httpClient.httpClient;
import com.example.asdf.httpClient.httpImage;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Useradmin on 2016/10/23.
 */
public class picture extends Activity {
    private Button add;//添加
    private Button btnupload;//上传
    private Button btndelete;//删除
    private ImageView leftDrawer;
    private TextView Map;//地图
    private TextView picture;//查看照片
    int num = 0;
    int i;
    int screenWidth;
    int screenHeigh;
    public static String intro;
    public static String na;
//    private GridView gridView1;              //网格显示缩略图
    private ListView lv_main;
    httpClient tmp1 = new httpClient();
    private Handler handler;
    private List<tmpBean> listDatas;
    private tmpAdapter lAdapter;
    private Handler handler1;
    public static Bitmap tmpBitmap;
    httpImage tmp2 = new httpImage();
    public static ArrayList<String> deletedList = new ArrayList<String>();
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picture);
        lv_main = (ListView) findViewById(R.id.list_sun);
        listDatas = new ArrayList<>();
        initData();
        lAdapter = new tmpAdapter(listDatas, this);
        lv_main.setAdapter(lAdapter);
        DisplayMetrics dm = new DisplayMetrics();
        //获取屏幕信息
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        screenHeigh = dm.heightPixels;
        add = (Button) findViewById(R.id.add);
        btnupload = (Button) findViewById(R.id.btnupload);
        leftDrawer = (ImageView) findViewById(R.id.leftdrawer);
        Map = (TextView) findViewById(R.id.Map);
        picture = (TextView) findViewById(R.id.picture);
        btndelete = (Button) findViewById(R.id.btndelete);
//        gridView1 = (GridView) findViewById(R.id.gridView);
        Map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              picture.this.finish();
            }
        });
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
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (num == 0) {
                    btndelete.setVisibility(View.VISIBLE);
                    btnupload.setVisibility(View.VISIBLE);
                    num = 1;
                } else if (num == 1) {
                    btndelete.setVisibility(View.INVISIBLE);
                    btnupload.setVisibility(View.INVISIBLE);
                    num = 0;
                }
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
                startActivity(new Intent(picture.this, singleDetail.class));
            }

        };
        lv_main.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2, long arg3) {
                if (btndelete.getVisibility() == View.VISIBLE) {
                    dialog(arg2);
                } else {
                    new Thread() {
                        @Override
                        public void run() {
                            tmp1.getParamTest("http://120.27.7.115:1010/api/imagemessage?imagename=" + login.lll.get(arg2), handler);
//                            if(na!=null)
//                            { tmpBitmap =tmp2.getBitmap("http://120.27.7.115:1010/api/image?name="+na,handler1);}
                        }
                    }.start();
                }
            }
        });
        btnupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnupload.getVisibility() == View.VISIBLE) {
                    startActivity(new Intent(picture.this, upload.class));
                }
            }
        });
        btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btndelete.getVisibility() == View.VISIBLE) {
                    startActivity(new Intent(picture.this, delete.class));
                } else {
                    startActivity(new Intent(picture.this, singleDetail.class));
                }
            }
        });

    }
    private void initData() {
        List<tmpBean> sun1 = new ArrayList<>() ;
        for(i=0;i<login.lll.size();i++) {
            sun1.add(new tmpBean());
            sun1.get(i).setPicUrl("http://120.27.7.115:1010/api/image" + "?name=" + login.lll.get(i));
            listDatas.add(sun1.get(i));
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
                    deletedList.add(login.lll.get(position));
                    login.lll.remove(position);
                    dialog.dismiss();
                    listDatas.clear();
                    initData();
                    lAdapter.notifyDataSetChanged();
//                    deletedList.add(arrayList.get(position));
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
}
