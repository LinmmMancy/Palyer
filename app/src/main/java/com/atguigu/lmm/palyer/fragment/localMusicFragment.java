package com.atguigu.lmm.palyer.fragment;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.atguigu.lmm.palyer.R;
import com.atguigu.lmm.palyer.activity.SystemVideoPlayerActivity;
import com.atguigu.lmm.palyer.adapter.LocalVideoAdapter;
import com.atguigu.lmm.palyer.base.BaseFragment;
import com.atguigu.lmm.palyer.bean.MediaItem;

import java.util.ArrayList;

/**
 * Created by Mancy_Lin on 2017/1/6.
 */

public class localMusicFragment extends BaseFragment {

    private ListView listView;
    private TextView tv_no_media;
    private LocalVideoAdapter adapter;


    /**
     * 数据集合
     *
     * @return
     */
    private ArrayList<MediaItem> mediaItems;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //设置适配器
            if (mediaItems != null && mediaItems.size() > 0) {
                //有数据
                //文本隐藏
                tv_no_media.setVisibility(View.GONE);
                adapter = new LocalVideoAdapter(mContext, mediaItems, false);
                //设置适配器
                listView.setAdapter(adapter);

            } else {
                //没有数据
                //文本显示
                tv_no_media.setVisibility(View.VISIBLE);
            }
        }
    };

    @Override
    public View initView() {

        View view = View.inflate(mContext, R.layout.fragment_local_video, null);
        listView = (ListView) view.findViewById(R.id.listview);
        tv_no_media = (TextView) view.findViewById(R.id.tv_no_media);


        //设置item的监听
        listView.setOnItemClickListener(new MyOnItemClickListener());
        return view;
    }


    class MyOnItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            MediaItem mediaItem = mediaItems.get(position);


            Intent intent = new Intent(mContext, SystemVideoPlayerActivity.class);

            Bundle bundle = new Bundle();
            // 列表数据
            bundle.putSerializable("videolist", mediaItems);
            intent.putExtras(bundle);
            //传递点击位置
            intent.putExtra("position", position);
            startActivity(intent);

        }
    }

    @Override
    protected void initData() {
        super.initData();
        //在子线程中加载视频
        getDataFromlocal();
    }

    private void getDataFromlocal() {
        new Thread() {
            @Override
            public void run() {
                super.run();

                //出书画集合
                mediaItems = new ArrayList<MediaItem>();
                ContentResolver resolver = mContext.getContentResolver();
                //sdcard的视频路劲
                Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                String[] objs = {
                        MediaStore.Audio.Media.DISPLAY_NAME,
                        MediaStore.Audio.Media.DURATION,
                        MediaStore.Audio.Media.SIZE,
                        MediaStore.Audio.Media.DATA,
                        MediaStore.Audio.Media.ARTIST,
                };

                Cursor cursor = resolver.query(uri, objs, null, null, null);
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        MediaItem mediaItem = new MediaItem();


                        //添加到集合中


                        mediaItems.add(mediaItem);

                        String name = cursor.getString(0);
                        mediaItem.setName(name);

                        long duration = cursor.getLong(1);
                        mediaItem.setDuration(duration);

                        long size = cursor.getLong(2);
                        mediaItem.setSize(size);

                        String data = cursor.getString(3);
                        mediaItem.setData(data);

                        String artist = cursor.getString(4);
                        mediaItem.setArtist(artist);

                    }
                    cursor.close();
                }
                ///发消息 --切换到主线程
                handler.sendEmptyMessage(2);
            }

        }.start();
    }

    @Override
    public void onRefrshData() {
        super.onRefrshData();

    }
}
