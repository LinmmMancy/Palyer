package com.atguigu.lmm.palyer.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Mancy_Lin on 2017/1/6.
 * <p>
 * 基类
 */

public abstract class BaseFragment extends Fragment {
    /**
     * 上下文
     */

    public Context mContext;

    /**
     * 创建basefragmendt的时候回调
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getActivity();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return initView();
    }

    public abstract View initView();

    /**
     * 当Activity创建成功的时候回调该方法
     * 初始胡按数据
     * 联网请求数据
     * 绑定数据
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    /**
     * 当子类需要
     * 1.联网请求网络的时候重写该方法
     * 2.绑定数据
     */
    protected void initData() {


    }


}
