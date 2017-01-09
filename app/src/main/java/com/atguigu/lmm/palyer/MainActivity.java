package com.atguigu.lmm.palyer;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioGroup;

import com.atguigu.lmm.palyer.base.BaseFragment;
import com.atguigu.lmm.palyer.fragment.localMusicFragment;
import com.atguigu.lmm.palyer.fragment.localVideoFragment;
import com.atguigu.lmm.palyer.fragment.netMusicFragment;
import com.atguigu.lmm.palyer.fragment.netVideoFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RadioGroup rg_main;
    /**
     * 集合
     */
    private ArrayList<BaseFragment> fragments;

    //获得下标

    private int position;
    private Fragment tempFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rg_main = (RadioGroup) findViewById(R.id.rg_main);

        isGrantExternalRW(this);

        initFragment();


        //设置RadioGroup的监听

        initListennr();

    }

    public static boolean isGrantExternalRW(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && activity.checkSelfPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            activity.requestPermissions(new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 1);

            return false;
        }

        return true;
    }


    private void initListennr() {
        rg_main.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_local_video:

                        position = 0;
                        break;
                    case R.id.rb_local_music:
                        position = 1;
                        break;
                    case R.id.rb_net_music:
                        position = 2;
                        break;
                    case R.id.rb_net_video:
                        position = 3;
                        break;
                }
                Fragment currentFragment = fragments.get(position);
                switchFragment(currentFragment);

            }
        });

        rg_main.check(R.id.rb_local_video);
    }


    private void switchFragment(Fragment currentFragment) {
        if (tempFragment != currentFragment) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            if (currentFragment != null) {
                //是否添加过
                if (!currentFragment.isAdded()) {
                    //把之前显示的隐藏
                    if (tempFragment != null) {
                        ft.hide(tempFragment);
                    }
                    ft.add(R.id.fl_mainc_content, currentFragment);
                } else {
                    //把之前的隐藏
                    if (tempFragment != null) {
                        ft.hide(tempFragment);
                    }
                    ft.show(currentFragment);
                }
                //最后一部，提交事物
                ft.commit();

            }
            tempFragment = currentFragment;
        }
    }

    private void initFragment() {
        fragments = new ArrayList<>();
        fragments.add(new localVideoFragment());
        fragments.add(new localMusicFragment());
        fragments.add(new netMusicFragment());
        fragments.add(new netVideoFragment());


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
