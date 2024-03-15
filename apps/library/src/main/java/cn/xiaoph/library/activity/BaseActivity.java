package cn.xiaoph.library.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.net.SocketTimeoutException;

import cn.xiaoph.library.R;
import cn.xiaoph.library.adapter.IBasicsAdapter;
import cn.xiaoph.library.error.LoginException;
import cn.xiaoph.library.view.ProgressDialogHelper;
import es.dmoral.toasty.Toasty;

public abstract class BaseActivity extends FirstBaseActivity implements Runnable,
        OnClickListener, OnRefreshListener, OnLoadMoreListener {

    public int page = 1;
    public ProgressDialogHelper mProgressDialogHelper;
    public Integer reqCode = 1;
    public JSONObject formData = new JSONObject();
    public IBasicsAdapter basicsAdapter;

    public abstract void click(View view);

    public abstract void loadData() throws Exception, LoginException;

    public abstract void readData() throws Exception, LoginException;

    public abstract void relogin();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProgressDialogHelper = new ProgressDialogHelper(this);
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
    }

    public void showProgress(String msg) {
        if (this.mProgressDialogHelper != null)
            this.mProgressDialogHelper.showProgressDialog(msg);
    }

    public void dismissProgress() {
        if (this.mProgressDialogHelper != null)
            this.mProgressDialogHelper.dismissProgressDialog();
    }

    public void setTitle(String title) {
        TextView input_bar_title_text = (TextView) findViewById(R.id.input_bar_title_text);
        if (input_bar_title_text != null) {
            input_bar_title_text.setText(title);
        }
    }

    public void setBarTitleColor(int color) {
        TextView input_bar_title_text = (TextView) findViewById(R.id.input_bar_title_text);
        if (input_bar_title_text != null) {
            input_bar_title_text.setTextColor(color);
        }
    }

    protected boolean isBackButton = true;
    protected int backImageResourceId = 0;

    public void setBarLeftButton() {
        ImageView button = (ImageView) findViewById(R.id.img_bar_left_button);
        if (button != null) {
            if (isBackButton) {
                button.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setResult(1001);
                        finish();
                    }
                });
            } else {
                button.setOnClickListener(this);
            }
            if (backImageResourceId != 0) {
                button.setImageResource(backImageResourceId);
            }
        }
        button.setVisibility(View.VISIBLE);
    }

    /**
     * 设置标题栏右侧按钮
     *
     * @param type 0：文本按钮 1：图标按钮
     * @param text
     */
    public void setBarRightButton(int type, String text) {
        if (type == 0) {
            TextView rightButton = (TextView) findViewById(R.id.input_bar_right_button);
            rightButton.setText(text);
            rightButton.setOnClickListener(this);
            rightButton.setVisibility(View.VISIBLE);
        } else {
            ImageView rightButton = (ImageView) findViewById(R.id.img_bar_right_button);
            int imageResource = Integer.parseInt(text);
            rightButton.setImageResource(imageResource);
            rightButton.setOnClickListener(this);
            rightButton.setVisibility(View.VISIBLE);
        }
    }

    public void setButtonText(String text) {
        Button button = findViewById(R.id.btn_confirm);
        if (text != null) {
            button.setText(text);
        }
        button.setVisibility(View.VISIBLE);
        button.setOnClickListener(this);
    }

    public void setTitleBarBgColor(int color) {
        RelativeLayout bar = (RelativeLayout) findViewById(R.id.layout_title_bar);
        if (bar != null) {
            bar.setBackgroundColor(color);
        }
    }

    public void loading() {
        new Thread(this).start();
    }

    @Override
    public void onClick(View v) {
        click(v);
    }

    @Override
    public void run() {
        showProgress("数据正在处理中，请耐心等待....");
        Looper.prepare();
        try {
            loadData();
        } catch (LoginException e) {
            relogin();
        } catch (SocketTimeoutException e) {
            Toasty.warning(this, "与服务器连接超时", Toast.LENGTH_SHORT, true).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toasty.warning(this, "网络出现错误，请检查网络", Toast.LENGTH_SHORT, true).show();
        }
        dismissProgress();
        Looper.loop();
    }

    public void onRefresh(final RefreshLayout refreshlayout) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    reqCode = 1;
                    page = 1;
                    if (basicsAdapter != null) {
                        basicsAdapter.getData().clear();
                    }
                    readData();
                    BaseActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            refreshlayout.finishRefresh(100, true);
                        }
                    });
                } catch (LoginException e) {
                    relogin();
                } catch (SocketTimeoutException e) {
                    e.printStackTrace();
                    refreshlayout.finishRefresh(2000, false);
                } catch (Exception e) {
                    e.printStackTrace();
                    refreshlayout.finishRefresh(2000, false);
                }
            }
        }).start();
    }

    @Override
    public void onLoadMore(final RefreshLayout refreshlayout) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    reqCode = 1;
                    page++;
                    readData();
                    BaseActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            refreshlayout.finishLoadMore(true);
                        }
                    });
                } catch (LoginException e) {
                    relogin();
                } catch (SocketTimeoutException e) {
                    e.printStackTrace();
                    refreshlayout.finishLoadMore(false);
                } catch (Exception e) {
                    e.printStackTrace();
                    refreshlayout.finishLoadMore(false);
                }
            }
        }).start();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            setResult(1001);
            finish();
        }
        return false;
    }
}
