package com.atguigu.lmm.palyer.View;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.atguigu.lmm.palyer.R;

/**
 * Created by Mancy_Lin on 2017/1/6.
 */

public class TitleBarView extends LinearLayout implements View.OnClickListener {

    private Context mContext;
    private TextView tv_search;

    private RelativeLayout rl_game;
    private ImageView iv_record;

    public TitleBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    /**
     * 当布局加载完成后回调该方法
     * 得到孩子的 实例
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        tv_search = (TextView) getChildAt(1);
        rl_game = (RelativeLayout) getChildAt(2);
        iv_record = (ImageView) getChildAt(3);


        tv_search.setOnClickListener(this);
        rl_game.setOnClickListener(this);
        iv_record.setOnClickListener(this);


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_search:
                Toast.makeText(mContext, "搜索", Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_record:
                Toast.makeText(mContext, "记录", Toast.LENGTH_SHORT).show();
                break;
            case R.id.rl_game:
                Toast.makeText(mContext, "游戏", Toast.LENGTH_SHORT).show();
                break;
        }

    }
}