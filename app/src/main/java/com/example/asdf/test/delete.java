package com.example.asdf.test;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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
    private ListView de;
    int i;
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete);
        de= (ListView) findViewById(R.id.dddd);
        listDatas = new ArrayList<>();
        initData();
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
                dialog(arg2);
            }
        });
    }

    private void initData() {
        List<tmpBean> sun1 = new ArrayList<>();
        for (i = 0; i < picture.deletedList.size(); i++) {
            sun1.add(new tmpBean());
            sun1.get(i).setPicUrl("http://120.27.7.115:1010/api/image" + "?name=" + picture.deletedList.get(i));
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
                    dialog.dismiss();
                    login.lll.add(picture.deletedList.get(position));
                    picture.deletedList.remove(position);
                    listDatas.clear();
                    initData();
                    lAdapter.notifyDataSetChanged();
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
}
