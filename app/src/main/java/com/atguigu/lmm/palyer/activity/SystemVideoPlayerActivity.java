package com.atguigu.lmm.palyer.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.atguigu.lmm.palyer.R;
import com.atguigu.lmm.palyer.Utils.Utils;
import com.atguigu.lmm.palyer.bean.MediaItem;
import com.atguigu.lmm.palyer.view.VideoView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.R.attr.level;

public class SystemVideoPlayerActivity extends Activity implements VideoView.OnClickListener {
    VideoView videoView;
    private static final
    String TAG = SystemVideoPlayerActivity.class.getSimpleName();//
    /**
     * 视频默认屏幕大小播放
     */
    private static final int VIDEO_TYPE_DEFAULT = 1;
    /**
     * 视频全屏播放
     */
    private static final int VIDEO_TYPE_FULL = 2;
    /**
     * 进度更新
     */
    private static final int PROGRESS = 0;

    /**
     * 隐藏控制面板
     */
    private static final int HIDE_MEDIA_CONTROLLER = 1;
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
    private VideoView videoview;

    /**
     * 列表数据
     */

    private ArrayList<MediaItem> mediaItems;
    private int position;

    private GestureDetector detector;
    /**
     * 是否后显示控制面板
     */
    private boolean isShowMediaController = false;
    /**
     * 视频是否全屏显示
     */
    private boolean isFullScreen = false;
    private int screenWidth = 0;
    private int screeHeight = 0;
    private int videoWidth = 0;
    private int videoHeight = 0;

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

        hideMediaController();//隐藏控制面板
    }

    private void hideMediaController() {


        isShowMediaController = false;
        llTop.setVisibility(View.VISIBLE);
        llBottom.setVisibility(View.VISIBLE);

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

        //初始化手势识别器
        detector = new GestureDetector(this, new MySimpleOnGestureListener());


        //得到屏幕的宽和高
        DisplayMetrics outMetrics = new DisplayMetrics();
        //得到屏幕参数类
        getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        //屏幕的宽和高
        screenWidth = outMetrics.widthPixels;
        screeHeight = outMetrics.heightPixels;
    }


    class MySimpleOnGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public void onLongPress(MotionEvent e) {
            super.onLongPress(e);
            startAndPause();
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            if (isFullScreen) {
                //设置默认
                setvideoType(VIDEO_TYPE_DEFAULT);
            } else {
                //全屏显示
                setvideoType(VIDEO_TYPE_FULL);
            }
            return super.onDoubleTap(e);
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            if (isShowMediaController) {
                //隐藏
                hideMediaController();
                //把消息移除
                handler.removeMessages(HIDE_MEDIA_CONTROLLER);
            } else {
                //显示
                showMediaController();
                //重新发送消息--4秒
                handler.sendEmptyMessageDelayed(HIDE_MEDIA_CONTROLLER, 400);


            }
            return super.onSingleTapConfirmed(e);

        }
    }

    private void setvideoType(int videoTypeDefault) {
        switch (videoTypeDefault) {
            case VIDEO_TYPE_FULL:
                isFullScreen = true;

               /* VideoView.setViewSize(screenWidth, screeHeight);*/
                //把按钮设置/默认
                btnSwichScreen.setBackgroundResource(R.drawable.btn_screen_default_selector);
                break;
            case VIDEO_TYPE_DEFAULT://视频画面的默认
                isFullScreen = false;

                //视频原始的画面大小
                int mVideoWidth = videoWidth;
                int mVideoHeight = videoHeight;
/*
                if (mVideoWidth * height < width * mVideoHeight) {
                    //Log.i("@@@", "image too wide, correcting");
                    width = height * mVideoWidth / mVideoHeight;
                } else if (mVideoWidth * height > width * mVideoHeight) {
                    //Log.i("@@@", "image too tall, correcting");
                    height = width * mVideoHeight / mVideoWidth;
                }
                //把计算好的视频大小传递过去
                videoView.setViewSize(width, height);
                //把按钮设置--全屏*/
                btnSwichScreen.setBackgroundResource(R.drawable.btn_screen_full_selector);
                break;
        }

    }

    /**
     * 显示控制面板
     */
    private void showMediaController() {
        isShowMediaController = true;
        llTop.setVisibility(View.VISIBLE);
        llBottom.setVisibility(View.VISIBLE);


    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HIDE_MEDIA_CONTROLLER:
                    hideMediaController();//隐藏控制面板
                    break;
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
        //移除消息
        handler.removeMessages(HIDE_MEDIA_CONTROLLER);
        //重新发消息
        handler.sendEmptyMessageDelayed(HIDE_MEDIA_CONTROLLER, 4000);

    }


    @Override
    public void onClick(View v) {
        if (v == btnVoice) {

        } else if (v == btnSwichePlayer) {

        } else if (v == btnExit) {
            finish();
        } else if (v == btnPre) {
            setPreVideo();

        } else if (v == btnStartPause) {

            startAndPause();

        } else if (v == btnNext) {
            setNextVideo();

        } else if (v == btnSwichScreen) {
            if (isFullScreen) {
                //设置默认
                setvideoType(VIDEO_TYPE_DEFAULT);
            } else {
                //全屏显示、
                setvideoType(VIDEO_TYPE_FULL);
            }

        }

    }


    private void startAndPause() {
       /* if (videoView.isPlaying()) {
            //当前播放设置为暂停
            videoView.pause();
            //按钮状态-播放状态
            btnStartPause.setBackgroundResource(R.drawable.btn_start_selector);

        } else {
            //当前暂停状态要设置播放状态
            videoView.start();*/
        //按钮状态-暂停状态
        btnStartPause.setBackgroundResource(R.drawable.btn_pause_selector);
    }


    private void setNextVideo() {
        //判断一下列表
        if (mediaItems != null && mediaItems.size() > 0) {
            position++;
            if (position < mediaItems.size()) {
                MediaItem mediaItem = mediaItems.get(position);
                //设置标题
                tvName.setText(mediaItem.getName());
                //设置播放地址
                // videoView.setVideoPath(mediaItem.getData());
                checkButtonStatus();
            } else {
                //越界
                position = mediaItems.size() - 1;
                finish();
            }
        }
        // 单个的uri
        else if (uri != null) {
            finish();
        }

    }

    private void setPreVideo() {
        if (mediaItems != null && mediaItems.size() > 0) {
            position--;
            if (position > 0) {
                MediaItem mediaItem = mediaItems.get(position);
                //设置标题
                tvName.setText(mediaItem.getName());
                //设置播放地址
                //  videoView.setVideoPath(mediaItem.getData());
                checkButtonStatus();
            } else {
                //越界
                position = 0;
            }
        }
    }

    private void checkButtonStatus() {
        //判断一下列表
        if (mediaItems != null && mediaItems.size() > 0) {
            //其他默认设置
            setButtonEnable(true);
            //播放第0个。上一个i设置灰色
            if (position == 0) {
                btnPre.setBackgroundResource(R.drawable.btn_pre_gray);
                btnPre.setEnabled(false);

            }
            if (position == mediaItems.size() - 1) {
                btnNext.setBackgroundResource(R.drawable.btn_next_gray);
                btnNext.setEnabled(false);
            }
        }
        //单个URI
        else if (uri != null) {
            //上一个和下一个都要设置灰色
            setButtonEnable(false);
        }

    }

    /**
     * 设置按钮的可点状态
     *
     * @param isEnable
     */
    private void setButtonEnable(boolean isEnable) {

        if (isEnable) {
            btnPre.setBackgroundResource(R.drawable.btn_pre_selector);
            btnNext.setBackgroundResource(R.drawable.btn_next_selector);
        } else {
            btnPre.setBackgroundResource(R.drawable.btn_pre_gray);
            btnNext.setBackgroundResource(R.drawable.btn_next_gray);
        }
        btnPre.setEnabled(isEnable);
        btnNext.setEnabled(isEnable);

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

        checkButtonStatus();

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
            //移除消息
            handler.removeMessages(HIDE_MEDIA_CONTROLLER);

        }

        /**
         * 当手指离开的时候回调
         *
         * @param seekBar
         */
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

            //重新发消息
            handler.sendEmptyMessageDelayed(HIDE_MEDIA_CONTROLLER, 4000);
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
            setNextVideo();

        }

        private void setNextVideo() {

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
            //得到视频原始大小

            videoWidth = mp.getVideoWidth();
            videoHeight = mp.getVideoHeight();

            //设置默认大小
            setvideoType(VIDEO_TYPE_DEFAULT);

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
        detector.onTouchEvent(event);//将事件船体给手势识别器
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


