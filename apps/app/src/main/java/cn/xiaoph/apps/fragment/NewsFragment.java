package cn.xiaoph.apps.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.myway.platform.api.Callback;
import com.myway.platform.api.ReturnResult;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.xiaoph.apps.R;
import cn.xiaoph.apps.activity.ViewNewsActivity;
import cn.xiaoph.apps.adapter.NewsAdapter;
import cn.xiaoph.library.error.LoginException;
import es.dmoral.toasty.Toasty;

public class NewsFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    @BindView(R.id.list_view)
    SwipeMenuListView listView;
    @BindView(R.id.layout_empty)
    LinearLayout layoutEmpty;
    Unbinder unbinder;
    @BindView(R.id.input_search_title)
    EditText inputSearchTitle;
    @BindView(R.id.btn_search)
    TextView btnSearch;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_news, container, false);
        unbinder = ButterKnife.bind(this, view);

        RefreshLayout refreshLayout = (RefreshLayout) view.findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadMoreListener(this);

        btnSearch.setOnClickListener(this);

        setTitle("校内新闻");

        loading();
        return view;
    }

    @Override
    public void loadData() throws Exception, LoginException {
        if (reqCode == 1) {
            JSONObject values = new JSONObject();
            values.put("page", page);
            if (!TextUtils.isEmpty(inputSearchTitle.getText())) {
                values.put("value", inputSearchTitle.getText().toString());
            }

            http.loginRequest("news/list", values).responseBody(new Callback() {
                @Override
                public void success(ReturnResult messageResult) {
                    Message msg = new Message();
                    msg.obj = messageResult.get("list");
                    listHandler.sendMessage(msg);
                }

                @Override
                public void error(String s) {
                    Toasty.error(getActivity(), s, Toast.LENGTH_SHORT, true).show();
                }
            });
        }
    }

    private Handler listHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            List<JSONObject> list = (List<JSONObject>) msg.obj;
            basicsAdapter = new NewsAdapter(getActivity(), list);
            listView.setEmptyView(layoutEmpty);
            listView.setAdapter(basicsAdapter);
            listView.setOnItemClickListener(NewsFragment.this);
        }
    };


    @Override
    public void readData() throws Exception, LoginException {
        JSONObject values = new JSONObject();
        values.put("page", page);
        if (!TextUtils.isEmpty(inputSearchTitle.getText())) {
            values.put("value", inputSearchTitle.getText().toString());
        }

        http.loginRequest("news/list", values).responseBody(new Callback() {
            @Override
            public void success(ReturnResult messageResult) {
                final List<JSONObject> list = (List<JSONObject>) messageResult.get("list");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        basicsAdapter.setData(list);
                        basicsAdapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void error(String s) {
                Toasty.error(getActivity(), s, Toast.LENGTH_SHORT, true).show();
            }
        });
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        JSONObject item = (JSONObject) adapterView.getAdapter().getItem(i);
        Intent intent = new Intent(getActivity(), ViewNewsActivity.class);
        intent.putExtra("item", item.toJSONString());
        startActivity(intent);
    }
}
