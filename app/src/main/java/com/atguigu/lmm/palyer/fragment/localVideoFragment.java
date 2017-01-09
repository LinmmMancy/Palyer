package com.atguigu.lmm.palyer.fragment;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.atguigu.lmm.palyer.R;
import com.atguigu.lmm.palyer.adapter.LocalVideoAdapter;
import com.atguigu.lmm.palyer.adapter.MediaItem;
import com.atguigu.lmm.palyer.base.BaseFragment;

import java.util.ArrayList;

/**
 * Created by Mancy_Lin on 2017/1/6.
 */

public class localVideoFragment extends BaseFragment {
    private TextView textView;
    private ListView listView;
    private TextView tv_no_media;
    private LocalVideoAdapter adapter;
    /**
     * 数据集合
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
                adapter = new LocalVideoAdapter(mContext, mediaItems);
                //设置适配器
                listView.setAdapter(adapter);
            } else {
                //没有数据
                tv_no_media.setVisibility(View.VISIBLE);
            }
        }
    };


    @Override
    public View initView() {

      /*  textView = new TextView(mContext);
        textView.setTextColor(Color.RED);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(25);*/

        View view = View.inflate(mContext, R.layout.fragment_local_video, null);
        listView = (ListView) view.findViewById(R.id.listview);
        tv_no_media = (TextView) view.findViewById(R.id.tv_no_media);


        return view;
    }

    public void initData() {
        super.initData();

        //在子线程程中加载视频
        getDataFromLocal();

    }

    private void getDataFromLocal() {
        new Thread() {
            @Override
            public void run() {
                super.run();

                //初始化集合
                mediaItems = new ArrayList<MediaItem>();
                ContentResolver resolver = mContext.getContentResolver();
                //SD卡的视频路径
                Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                String[] objs = {
                        MediaStore.Video.Media.DISPLAY_NAME,
                        MediaStore.Video.Media.DURATION,
                        MediaStore.Video.Media.SIZE,
                        MediaStore.Video.Media.DATA,
                        MediaStore.Video.Media.ARTIST,

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
                        String data = cursor.getString(3);
                        mediaItem.setData(data);
                        String artist = cursor.getString(4);
                        mediaItem.setArtist(artist);

                    }

                    cursor.close();
                }

                handler.sendEmptyMessage(2);
            }
        }.start();

    }

    public void onRefrshData() {
        super.onRefrshData();


    }


}
