package com.atguigu.lmm.palyer.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.icu.text.SimpleDateFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.atguigu.lmm.palyer.R;
import com.atguigu.lmm.palyer.Utils.Utils;
import com.atguigu.lmm.palyer.bean.MediaItem;

import java.util.ArrayList;
import java.util.Date;

import static android.R.attr.level;

public class SystemVideoPlayerActivity extends Activity implements VideoView.OnClickListener {
    private static final String TAG = SystemVideoPlayerActivity.class.getSimpleName();//
    /**
     * 进度更新
     */
    private static final int PROGRESS = 0;
    private VideoView videoview;
    private VideoView videoView;
    private LinearLayout llTop;
    private TextView tvName;
    private ImageView ivBattery;
    private TextView tvSystetime;
    private Button btnVoice;
    private SeekBar seekbarVoice;
    private Button btnSwichePlayer;
    private LinearLayout llBottom;
    private TextView tvCurrenttime;
    private SeekBar seekbarVideo;
    private TextView tvDuration;
    private Button btnExit;
    private Button btnPre;
    private Button btnStartPause;
    private Button btnNext;
    private Button btnSwichScreen;
    private Utils utils;
    private MyBroadcastReceiver receiver;

    /**
     * 列表数据
     */

    private ArrayList<MediaItem> mediaItems;
    private int position;

    /**
     * 视频播放地址
     *
     * @param savedInstanceState
     */
    private Uri uri;

    private void findViews() {
        setContentView(R.layout.activity_system_video_player);
        videoview = (VideoView) findViewById(R.id.videoview);
        llTop = (LinearLayout) findViewById(R.id.ll_top);
        tvName = (TextView) findViewById(R.id.tv_name);
        ivBattery = (ImageView) findViewById(R.id.iv_battery);
        tvSystetime = (TextView) findViewById(R.id.tv_systetime);
        btnVoice = (Button) findViewById(R.id.btn_voice);
        seekbarVoice = (SeekBar) findViewById(R.id.seekbar_voice);
        btnSwichePlayer = (Button) findViewById(R.id.btn_swiche_player);
        llBottom = (LinearLayout) findViewById(R.id.ll_bottom);
        tvCurrenttime = (TextView) findViewById(R.id.tv_currenttime);
        seekbarVideo = (SeekBar) findViewById(R.id.seekbar_video);
        tvDuration = (TextView) findViewById(R.id.tv_duration);
        btnExit = (Button) findViewById(R.id.btn_exit);
        btnPre = (Button) findViewById(R.id.btn_pre);
        btnStartPause = (Button) findViewById(R.id.btn_start_pause);
        btnNext = (Button) findViewById(R.id.btn_next);
        btnSwichScreen = (Button) findViewById(R.id.btn_swich_screen);

        btnVoice.setOnClickListener(this);
        btnSwichePlayer.setOnClickListener(this);
        btnExit.setOnClickListener(this);
        btnPre.setOnClickListener(this);
        btnStartPause.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnSwichScreen.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*setContentView(R.layout.activity_system_video_player);
        videoView = (VideoView) findViewById(videoview);*/
        initData();
        findViews();
        getData();
        //设置视频加载的监听
        setLinstener();
        setData();
    }

    private void initData() {
        utils = new Utils();

        //注册监听电量广播
        receiver = new MyBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        //监听 电量变化
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(receiver, filter);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case PROGRESS://视频播放的进度
                    int currentPosition = videoview.getCurrentPosition();
                    //设置视频更新
                    seekbarVideo.setProgress(currentPosition);
                    //设置视频进度的时间
                    tvCurrenttime.setText(utils.stringForTime(currentPosition));
                    //得到系统的时间并且更新
                    tvSystetime.setText(getsystemTime());
                    //不断发消息
                    removeMessages(PROGRESS);
                    sendEmptyMessageDelayed(PROGRESS, 1000);
                    break;
            }
        }
    };

    private String getsystemTime() {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        return format.format(new Date());

    }

    class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            //得到电量0-100
            int leve1 = intent.getIntExtra("leve1", 0);
            //主线程
            setBattery(level);
        }
    }

    private void setBattery(int level) {
        if (level <= 0) {
            ivBattery.setImageResource(R.drawable.ic_battery_0);
        } else if (level <= 10) {
            ivBattery.setImageResource(R.drawable.ic_battery_10);
        } else if (level <= 20) {
            ivBattery.setImageResource(R.drawable.ic_battery_20);
        } else if (level <= 40) {
            ivBattery.setImageResource(R.drawable.ic_battery_40);
        } else if (level <= 60) {
            ivBattery.setImageResource(R.drawable.ic_battery_60);
        } else if (level <= 80) {
            ivBattery.setImageResource(R.drawable.ic_battery_80);
        } else if (level <= 100) {
            ivBattery.setImageResource(R.drawable.ic_battery_100);
        } else {
            ivBattery.setImageResource(R.drawable.ic_battery_100);
        }

    }

    @Override
    public void onClick(View v) {
        if (v == btnVoice) {

        } else if (v == btnSwichePlayer) {

        } else if (v == btnExit) {
            finish();
        } else if (v == btnPre) {

        } else if (v == btnStartPause) {

            if (videoView.isPlaying()) {
                //当前播放设置为暂停
                videoView.pause();
                //按钮状态-播放状态
                btnStartPause.setBackgroundResource(R.drawable.btn_start_selector);

            } else {
                //当前暂停状态要设置播放状态
                videoView.start();
                //按钮状态-暂停状态
                btnStartPause.setBackgroundResource(R.drawable.btn_pause_selector);
            }

        } else if (v == btnNext) {

        } else if (v == btnSwichScreen) {

        }

    }


    private void setData() {
       /* //设置播放地址
        videoView.setVideoURI(uri);*/
        if (mediaItems != null && mediaItems.size() > 0) {
            //根据位置获取播放视频的对象
            MediaItem mediaItem = mediaItems.get(position);
            videoView.setVideoPath(mediaItem.getData());
            tvName.setText(mediaItem.getName());
        } else if (uri != null) {
            //设置播放地址
            videoView.setVideoURI(uri);
            tvName.setText(uri.toString());
        }

    }

    private void setLinstener() {
        //设置好视频播放监听：准备好的监听，播放出错监听，播放完成监听

        videoView.setOnPreparedListener(new MyOnPreparedListener());
        videoView.setOnErrorListener(new MyOnErrorListener());
        videoView.setOnCompletionListener(new MyOnCompletionListener());
        //设置控制面板
        // videoView.setMediaController(new MediaController(this));


        //设置视频的拖动
        seekbarVideo.setOnSeekBarChangeListener(new MyOnSeekBarChangeListener());

    }

    class MyOnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {
        /**
         * 状态变化的时候回调
         *
         * @param seekBar
         * @param
         * @param
         */
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser) {
                videoView.seekTo(progress);
            }
        }

        /**
         * 当手指按下的时候回调
         *
         * @param seekBar
         */
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {


        }

        /**
         * 当手指离开的时候回调
         *
         * @param seekBar
         */
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }


    class MyOnCompletionListener implements MediaPlayer.OnCompletionListener {
        /**
         * 开始播放
         *
         * @param mp
         */


        @Override
        public void onCompletion(MediaPlayer mp) {
            //播放视频下一个

        }
    }

    class MyOnErrorListener implements MediaPlayer.OnErrorListener {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            return false;
        }
    }

    class MyOnPreparedListener implements MediaPlayer.OnPreparedListener {
        @Override
        /**
         * 当底层加载视频准备完成的时候回调
         */
        public void onPrepared(MediaPlayer mp) {
            //开始播放
            videoView.start();
            //准备好的时候

            //准备好的时候
            int duration = videoview.getDuration();
            seekbarVideo.setMax(duration);

            //设置 总时长

            tvDuration.setText(utils.stringForTime(duration));
            //发消息

            handler.sendEmptyMessage(PROGRESS);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            return true;
        }
        return super.onTouchEvent(event);
    }

    private void getData() {
        uri = getIntent().getData();
        //得到视频列表
        mediaItems = (ArrayList<MediaItem>) getIntent().getSerializableExtra("videolist");
        position = getIntent().getIntExtra("position", 0);
    }

    @Override
    protected void onDestroy() {
        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;
        }
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();

    }
}


