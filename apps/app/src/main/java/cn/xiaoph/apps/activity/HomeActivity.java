package cn.xiaoph.apps.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import java.util.ArrayList;
import java.util.List;

import cn.xiaoph.apps.R;
import cn.xiaoph.apps.fragment.BaseFragment;
import cn.xiaoph.apps.fragment.HomeFragment;
import cn.xiaoph.apps.fragment.MyFragment;
import cn.xiaoph.apps.fragment.NewsFragment;
import cn.xiaoph.apps.fragment.TimeTableFragment;
import cn.xiaoph.library.http.ApiResources;


public class HomeActivity extends AppCompatActivity {

    public BottomBar bottomBar;
    private FragmentManager manager;
    private FragmentTransaction transaction;

    private Fragment mCurrentFrgment;
    private List<Fragment> fragmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            int flagTranslucentNavigation = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                WindowManager.LayoutParams attributes = window.getAttributes();
                attributes.flags |= flagTranslucentNavigation;
                window.setAttributes(attributes);
                getWindow().setStatusBarColor(Color.TRANSPARENT);
            } else {
                Window window = getWindow();
                WindowManager.LayoutParams attributes = window.getAttributes();
                attributes.flags |= flagTranslucentStatus | flagTranslucentNavigation;
                window.setAttributes(attributes);
            }
        }

        bottomBar = (BottomBar) findViewById(R.id.bottomBar);

        fragmentList = new ArrayList<>();
        fragmentList.add(new HomeFragment());
        fragmentList.add(new NewsFragment());
        fragmentList.add(new TimeTableFragment());
        fragmentList.add(new MyFragment());

//        bottomBar;
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                switch (tabId) {
                    case R.id.item_home:
                        changeFragment(0);
                        break;
                    case R.id.item_news:
                        changeFragment(1);
                        break;
                    case R.id.item_timetable:
                        changeFragment(2);
                        break;
                    case R.id.item_my:
                        changeFragment(3);
                        break;
                }
            }
        });
    }

    public void startActivity(int position) {
        if (ApiResources.token == null) {
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            changeFragment(position);
        }
    }

    public Integer typeIndex = 0;

    public void changeFragment(int index) {
        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        if (mCurrentFrgment != null) {
            transaction.hide(mCurrentFrgment);
        }
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(fragmentList.get(index).getClass().getName());
        if (null == fragment) {
            //如fragment为空，则之前未添加此Fragment。便从集合中取出
            fragment = fragmentList.get(index);
        }

        mCurrentFrgment = fragment;
        //判断此Fragment是否已经添加到FragmentTransaction事物中
        if (fragment.isAdded()) {
            if (fragment instanceof BaseFragment) {
                if (typeIndex != null)
                    ((BaseFragment) fragment).loading();
            }
            transaction.show(fragment);
        }
        if (!fragment.isAdded()) {
            transaction.add(R.id.fragment_view, fragment, fragment.getClass().getName());
        } else {
            transaction.show(fragment);
        }
        transaction.commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mCurrentFrgment instanceof BaseFragment) {
            ((BaseFragment) mCurrentFrgment).onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        onBack(keyCode, event);
        return false;
    }

    public void onBack(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent home = new Intent(Intent.ACTION_MAIN);
            home.addCategory(Intent.CATEGORY_HOME);
            startActivity(home);
        }
    }

}
