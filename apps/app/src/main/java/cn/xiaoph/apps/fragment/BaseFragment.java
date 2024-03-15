package cn.xiaoph.apps.fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.net.SocketTimeoutException;

import cn.xiaoph.apps.R;
import cn.xiaoph.library.adapter.IBasicsAdapter;
import cn.xiaoph.library.error.LoginException;
import cn.xiaoph.library.http.WebHttpEngine;
import cn.xiaoph.library.view.ProgressDialogHelper;
import es.dmoral.toasty.Toasty;

import static com.scwang.smartrefresh.layout.util.DensityUtil.dp2px;

public abstract class BaseFragment extends Fragment implements OnRefreshListener,OnLoadMoreListener,Runnable,View.OnClickListener {

    public Context context;
    public View view;
    public ProgressDialogHelper mProgressDialogHelper;
    public Integer reqCode = 1;
    public JSONObject formData = new JSONObject();
    public IBasicsAdapter basicsAdapter;
    public int page = 1;

    public WebHttpEngine http = new WebHttpEngine();

    public void relogin(){
//        Intent intent = new Intent(getActivity(),
//                LoginActivity.class);
//        startActivity(intent);
//        Toasty.warning(getActivity(), "登录失效，请重新登录", Toast.LENGTH_SHORT, true).show();
//        getActivity().finish();
    }

    public SwipeMenuItem getSwipeMenuItem(int id, String bgColor, int width, String title, int setTitleColor, int titleSize) {
        SwipeMenuItem deleteItem = new SwipeMenuItem(
                getActivity().getApplicationContext());
        deleteItem.setId(id);
        // set item background
        deleteItem.setBackground(new ColorDrawable(Color.parseColor(bgColor)));
        // set item width
        deleteItem.setWidth(dp2px(width));
        deleteItem.setTitle(title);
        deleteItem.setTitleColor(setTitleColor);
        deleteItem.setTitleSize(titleSize);
        return deleteItem;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mProgressDialogHelper = new ProgressDialogHelper(getActivity());
        context = getActivity();
        return null;
    }

    public void setTitle(String title) {
        TextView input_bar_title_text = (TextView) view.findViewById(R.id.input_bar_title_text);
        if (input_bar_title_text != null) {
            input_bar_title_text.setText(title);
        }
    }

    public void setBarTitleColor(int color) {
        TextView input_bar_title_text = (TextView) view.findViewById(R.id.input_bar_title_text);
        if (input_bar_title_text != null) {
            input_bar_title_text.setTextColor(color);
        }
    }

    protected boolean isBackButton = true;
    protected int backImageResourceId = 0;

    public void setBarLeftButton() {
        ImageView button = (ImageView) view.findViewById(R.id.img_bar_left_button);
        if (button != null) {
            if (isBackButton) {
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getActivity().setResult(1001);
                        getActivity().finish();
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
            TextView rightButton = (TextView) view.findViewById(R.id.input_bar_right_button);
            rightButton.setText(text);
            rightButton.setOnClickListener(this);
            rightButton.setVisibility(View.VISIBLE);
        } else {
            ImageView rightButton = (ImageView) view.findViewById(R.id.img_bar_right_button);
            int imageResource = Integer.parseInt(text);
            rightButton.setImageResource(imageResource);
            rightButton.setOnClickListener(this);
            rightButton.setVisibility(View.VISIBLE);
        }
    }

    public void setButtonText(String text) {
        Button button = view.findViewById(R.id.btn_confirm);
        if (text != null) {
            button.setText(text);
        }
        button.setVisibility(View.VISIBLE);
        button.setOnClickListener(this);
    }

    public void setTitleBarBgColor(int color) {
        RelativeLayout bar = (RelativeLayout) view.findViewById(R.id.layout_title_bar);
        if (bar != null) {
            bar.setBackgroundColor(color);
        }
    }

    public void showProgress(String msg) {
        if(this.mProgressDialogHelper == null){
            mProgressDialogHelper = new ProgressDialogHelper(getActivity());
        }
        if (this.mProgressDialogHelper != null)
            this.mProgressDialogHelper.showProgressDialog(msg);
    }

    public void dismissProgress() {
        if(this.mProgressDialogHelper == null){
            mProgressDialogHelper = new ProgressDialogHelper(getActivity());
        }
        if (this.mProgressDialogHelper != null)
            this.mProgressDialogHelper.dismissProgressDialog();
    }

    public abstract void loadData() throws Exception, LoginException;

    public abstract void readData() throws Exception, LoginException;

    public void loading() {
        new Thread(this).start();
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
            Toasty.warning(getActivity(), "与服务器连接超时", Toast.LENGTH_SHORT, true).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toasty.warning(getActivity(), "网络出现错误，请检查网络", Toast.LENGTH_SHORT, true).show();
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
                    if(basicsAdapter != null){
                        basicsAdapter.getData().clear();
                    }
                    readData();
                    getActivity().runOnUiThread(new Runnable() {
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
                    getActivity().runOnUiThread(new Runnable() {
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

    public void isEmpty(Object...obj) {
        boolean isEmptyParam = false;
        for (Object object : obj) {
            if (object instanceof TextView) {
                String text = ((TextView) object).getText().toString();
                String messageHint = ((TextView) object).getHint().toString();
                if ("".equals(text) || text == null) {
                    isEmptyParam = true;
                    Toasty.warning(getActivity(), messageHint, Toast.LENGTH_SHORT, true).show();
                }
            }
        }
        if (isEmptyParam) {
            return ;
        }
    }

}
