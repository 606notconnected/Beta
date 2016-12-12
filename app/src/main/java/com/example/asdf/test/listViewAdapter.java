package com.example.asdf.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class listViewAdapter extends BaseAdapter {
        private List<Map<String, Object>> data;
        private LayoutInflater layoutInflater;
        private Context context;
        private iClick mListener;
        public  listViewAdapter(Context context,List<Map<String, Object>> data,iClick listener){
            this.context=context;
            this.data=data;
            this.layoutInflater=LayoutInflater.from(context);
            mListener = listener;
        }
        /**
         * 组件集合，对应list.xml中的控件
         * @author Administrator
         */
        public final class Zujian{
            public TextView friendName;
            private ImageView examplePicture;
            private TextView exampletext;
            public Button love;
            public TextView lovetext;
        }
        @Override
        public int getCount() {
            return data.size();
        }
        /**
         * 获得某一位置的数据
         */
        @Override
        public Object getItem(int position) {
            return data.get(position);
        }
        /**
         * 获得唯一标识
         */
        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Zujian zujian=null;
            if(convertView==null){
                zujian=new Zujian();
                //获得组件，实例化组件
                convertView=layoutInflater.inflate(R.layout.list_item, null);
                zujian.friendName=(TextView)convertView.findViewById(R.id.friendName);
                zujian.examplePicture=(ImageView)convertView.findViewById(R.id.examplePicture);
                zujian.exampletext=(TextView)convertView.findViewById(R.id.exampleText);
                zujian.love = (Button) convertView.findViewById(R.id.love);
                zujian.lovetext = (TextView) convertView.findViewById(R.id.lovetext);
                convertView.setTag(zujian);
            }else{
                zujian=(Zujian)convertView.getTag();
            }
            //绑定数据
            zujian.love.setBackgroundResource((Integer)data.get(position).get("love"));
            zujian.examplePicture.setBackgroundResource((Integer)data.get(position).get("examplePicture"));
            zujian.friendName.setText((String) data.get(position).get("friendName"));
            zujian.exampletext.setText((String)data.get(position).get("exampletext"));
            zujian.lovetext.setText("添加关注");
            zujian.love.setOnClickListener(mListener);
            zujian.love.setTag(position);
            return convertView;
        }

    }