package com.example.asdf.test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.asdf.test.attached.iClick;
import com.example.asdf.test.attached.tmpBean;

import java.util.List;
import java.util.Map;

/**
 * Created by Useradmin on 2016/11/15.
 */
public class listViewForFriend extends BaseAdapter {
    private List<Map<String, Object>> data;
    private LayoutInflater layoutInflater;
    private Context context;
    private List<tmpBean> list_sun;
    private static final String TAG = "ContentAdapter";
    private iClick mListener;
    public  listViewForFriend(Context context,List<Map<String, Object>> data,iClick listener){
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
        private ImageView examplePicture;
        private TextView exampletext;
        private Button love;
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
            convertView=layoutInflater.inflate(R.layout.list_friend, null);
            zujian.examplePicture=(ImageView)convertView.findViewById(R.id.examplePicture);
            zujian.exampletext=(TextView)convertView.findViewById(R.id.exampleText);
            zujian.love= (Button) convertView.findViewById(R.id.love);
            convertView.setTag(zujian);
        }else{
            zujian=(Zujian)convertView.getTag();
        }
        //绑定数据
        zujian.examplePicture.setBackgroundResource((Integer)data.get(position).get("examplePicture"));
        zujian.exampletext.setText((String) data.get(position).get("exampletext"));
        zujian.love.setBackgroundResource((Integer) data.get(position).get("love"));
        zujian.love.setOnClickListener(mListener);
        zujian.love.setTag(position);
        return convertView;
//        Picasso.with(context).load(list_sun.get(position).getPicUrl()).fit()
//                .placeholder(R.drawable.undownload)
//                .error(R.drawable.undownload)//mapBitmap
//                .into(zujian.examplePicture);
    }

}