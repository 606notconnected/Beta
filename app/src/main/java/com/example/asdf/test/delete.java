package com.example.asdf.test;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.example.asdf.httpClient.httpClient;
import com.example.asdf.test.adapter.tmpAdapter;
import com.example.asdf.test.attached.tmpBean;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Useradmin on 2016/10/23.
 */
public class delete extends Activity {
    private ImageView leftDrawer;
    private List<tmpBean> listDatas;
    private tmpAdapter lAdapter;
    int screenWidth;
    int screenHeigh;
    private Handler handler2;
    private android.os.Handler handler1;
    httpClient tmp3 = new httpClient();
    private ListView de;
    int i;
    int position;
    private List<String>  deletedList = new ArrayList<String>();
    httpClient tmp1 =new httpClient();
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete);
        de= (ListView) findViewById(R.id.dddd);
        listDatas = new ArrayList<>();
        lAdapter = new tmpAdapter(listDatas, this);
        de.setAdapter(lAdapter);
        DisplayMetrics dm = new DisplayMetrics();
        //获取屏幕信息
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        screenHeigh = dm.heightPixels;
        leftDrawer= (ImageView) findViewById(R.id.leftdrawer);
        leftDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete.this.finish();
            }
        });
          /*
66.         * 监听GridView点击事件
67.         * 报错:该函数必须抽象方法 故需要手动导入import android.view.View;
68.         */
        de.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2, long arg3) {
                position=arg2;
                dialog(arg2);
            }
        });
        handler1 = new android.os.Handler() {
            public void handleMessage(Message msg) {
                String tmp = msg.obj.toString();
                tmp = "{" + tmp + "}";
                Gson gson = new Gson();
                pictures pic = gson.fromJson(tmp, pictures.class);
                String re = pic.getresult();
                deletedList = pic.getPictureList();
                System.out.println("删除列表"+deletedList);
                if(re.equals("true"))
                {
                    Toast.makeText(delete.this, "获取删除图片成功", Toast.LENGTH_SHORT).show();
                    initData();
                    lAdapter.notifyDataSetChanged();
                    System.out.println("t功");
                }
                else
                {
                    Toast.makeText(delete.this,"获取删除图片失败",Toast.LENGTH_SHORT).show();
                }
            }
        };
        new Thread() {
            @Override
            public void run() {
                tmp1.getParamTest("http://120.27.7.115:1010/api/Image_Delete?account="+login.account, handler1);
//                            { tmpBitmap =tmp2.getBitmap("http://120.27.7.115:1010/api/image?name="+na,handler1);}
            }
        }.start();
        handler2 = new android.os.Handler() {
            public void handleMessage(Message msg) {
                String tmp = msg.obj.toString();
                if(tmp.equals("true"))
                {
                    Toast.makeText(delete.this, "恢复成功", Toast.LENGTH_SHORT).show();
                    login.lll.add(deletedList.get(position));
                    deletedList.remove(position);
                    listDatas.clear();
                    initData();
                    lAdapter.notifyDataSetChanged();
                    System.out.println("t功");
                }
                else
                {
                    Toast.makeText(delete.this,"恢复失败",Toast.LENGTH_SHORT).show();
                }
            }
        };

    }
    private void initData() {
        List<tmpBean> sun1 = new ArrayList<>();
        for (i = 0; i < deletedList.size(); i++) {
            sun1.add(new tmpBean());
            sun1.get(i).setPicUrl("http://120.27.7.115:1010/api/image" + "?name=" + deletedList.get(i));
            listDatas.add(sun1.get(i));
        }
    }
        protected void dialog(final int position) {
            AlertDialog.Builder builder = new AlertDialog.Builder(delete.this);
            builder.setMessage("确认恢复这张图片吗？");
            builder.setTitle("提示");
            builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new Thread() {
                        public void run() {
                            JSONObject object = new JSONObject();
                            object.put("imageName", deletedList.get(position));
                            tmp3.postParamsJson("http://120.27.7.115:1010/api/Image_Delete_Cancel", object, handler2);
                        }
                    }.start();
                    dialog.dismiss();
//                        login.lll.add(picture.deletedList.get(position));

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
}
