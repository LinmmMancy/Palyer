package com.atguigu.lmm.palyer.fragment;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.atguigu.lmm.palyer.base.BaseFragment;

/**
 * Created by Mancy_Lin on 2017/1/6.
 */

public class netMusicFragment extends BaseFragment {
    private TextView textView;

    @Override
    public View initView() {

        textView = new TextView(mContext);
        textView.setTextColor(Color.RED);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(25);

        return textView;
    }

    public void initData() {
        super.initData();
        textView.setText("网络音频");
    }

    @Override
    public void onRefrshData() {
        super.onRefrshData();
        textView.setText("本地音乐刷新");
    }
}
