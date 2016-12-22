package com.example.asdf.test.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.asdf.test.R;
import com.example.asdf.test.attached.iClick;
import com.example.asdf.test.login;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Map;

/**
 * Created by Useradmin on 2016/12/16.
 */
public class imageAdapter extends BaseAdapter {
    // 要显示的数据的集合
    private List<Map<String, Object>> data;
    // 接受上下文
    private Context context;
    // 声明内部类对象
    private ViewHolder viewHolder;
    private LayoutInflater layoutInflater;
    private iClick mListener;

    /**
     * 构造函数
     *
     * @param context
     * @param data
     */
    public imageAdapter(Context context, List<Map<String, Object>> data,iClick listener) {
        this.context = context;
        this.data = data;
        this.layoutInflater= LayoutInflater.from(context);
        mListener = listener;
    }

    // 返回的总个数
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return data.size();
    }

    // 返回每个条目对应的数据
    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return data.get(position);
    }

    // 返回的id
    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    // 返回这个条目对应的控件对象
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 判断当前条目是否为null
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(context, R.layout.list_friend, null);
            viewHolder.iv_image = (ImageView) convertView
                    .findViewById(R.id.examplePicture);
            viewHolder.tv_url = (TextView) convertView
                    .findViewById(R.id.exampleText);
            viewHolder.love= (ImageView) convertView.findViewById(R.id.love);
            viewHolder.click= (LinearLayout) convertView.findViewById(R.id.click);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // 获取List集合中的map对象
        Map<String, Object> map = data.get(position);
        // 获取图片的url路径
        String url = map.get("url").toString();

        Picasso.with(context).load(data.get(position).get("url").toString()).fit()
                .placeholder(R.drawable.undownload)
                .error(R.drawable.undownload)//mapBitmap
                .into(viewHolder.iv_image);
        viewHolder.tv_url.setText(url);
//        viewHolder.click.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
        viewHolder.tv_url.setText((String)data.get(position).get("intro"));
        viewHolder.love.setImageBitmap(login.bbsha);
        viewHolder.click.setOnClickListener(mListener);
        viewHolder.click.setTag(position);
        return convertView;
    }

    /**
     * 内部类 记录单个条目中所有属性
     *
     * @author LeoLeoHan
     *
     */
    class ViewHolder {
        public ImageView iv_image;
        public TextView tv_url;
        public LinearLayout click;
        public ImageView love;
    }

}