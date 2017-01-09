package com.atguigu.lmm.palyer.adapter;

import android.content.Context;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.atguigu.lmm.palyer.R;
import com.atguigu.lmm.palyer.Utils.Utils;
import com.atguigu.lmm.palyer.bean.MediaItem;

import java.util.ArrayList;

/**
 * Created by Mancy_Lin on 2017/1/9.
 */

public class LocalVideoAdapter extends BaseAdapter {

    private final Context mContext;
    private final ArrayList<MediaItem> datas;
    private Utils utils;

    public LocalVideoAdapter(Context mContext, ArrayList<MediaItem> mediaItems) {
        this.mContext = mContext;
        this.datas = mediaItems;
        utils = new Utils();
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       /* TextView text/*View = new TextView(mContext);
        textView.setTextSize(20);
        textView.setTextColor(Color.BLACK);
        MediaItem mediaItem = datas.get(position);
        textView.setText(mediaItem.toString());*//*

        return textView;*/
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_local_video, null);
            viewHolder = new ViewHolder();
            viewHolder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.tv_duration = (TextView) convertView.findViewById(R.id.tv_duration);
            viewHolder.tv_size = (TextView) convertView.findViewById(R.id.tv_size);

            //设置TAG
            convertView.setTag(viewHolder);


        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //根据位置得到相应的数据
        MediaItem mediaItem = datas.get(position);
        viewHolder.tv_name.setText(mediaItem.getName());
        //设置大小
        viewHolder.tv_size.setText(Formatter.formatFileSize(mContext, mediaItem.getSize()));
        //设置时间
        viewHolder.tv_duration.setText(utils.stringForTime((int) mediaItem.getDuration()));
        return convertView;
    }


    static class ViewHolder {
        TextView tv_name;
        TextView tv_duration;
        TextView tv_size;
        ImageView iv_icon;

    }
}
