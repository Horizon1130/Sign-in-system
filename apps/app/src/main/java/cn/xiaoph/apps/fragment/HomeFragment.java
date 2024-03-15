package cn.xiaoph.apps.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
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
import cn.xiaoph.apps.activity.AddSignActivity;
import cn.xiaoph.apps.activity.CourseDoSignActivity;
import cn.xiaoph.apps.activity.ViewMapActivity;
import cn.xiaoph.apps.activity.ViewTeacherCourseSignActivity;
import cn.xiaoph.apps.adapter.CourseAdapter;
import cn.xiaoph.library.error.LoginException;
import cn.xiaoph.library.http.ApiResources;
import es.dmoral.toasty.Toasty;

public class HomeFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    @BindView(R.id.list_view)
    SwipeMenuListView listView;
    @BindView(R.id.layout_empty)
    LinearLayout layoutEmpty;
    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);

        RefreshLayout refreshLayout = (RefreshLayout) view.findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadMoreListener(this);

        setTitle("课程考勤");
        if (ApiResources.type.equals(1)) {
            setBarRightButton(0, "发布签到");
        }

        loading();
        return view;
    }

    @Override
    public void loadData() throws Exception, LoginException {
        if (reqCode == 1) {
            JSONObject values = new JSONObject();
            values.put("page", page);

            http.loginRequest("course/list", values).responseBody(new Callback() {

                @Override
                public void success(ReturnResult result) {
                    Message msg = new Message();
                    msg.obj = result.get("list");
                    listHandler.sendMessage(msg);
                }

                @Override
                public void error(String errorMsg) {
                    Toasty.error(getActivity(), errorMsg, Toast.LENGTH_SHORT, true).show();
                }
            });
        }
    }

    private Handler listHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            List<JSONObject> list = (List<JSONObject>) msg.obj;
            basicsAdapter = new CourseAdapter(getActivity(), list);
            listView.setEmptyView(layoutEmpty);
            listView.setAdapter(basicsAdapter);
            listView.setOnItemClickListener(HomeFragment.this);
        }
    };

    @Override
    public void readData() throws Exception, LoginException {
        JSONObject values = new JSONObject();
        values.put("page", page);

        http.loginRequest("course/list", values).responseBody(new Callback() {
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
        switch (view.getId()) {
            case R.id.input_bar_right_button:
                Intent intent = new Intent(getActivity(), AddSignActivity.class);
                getActivity().startActivityForResult(intent, 3000);
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        JSONObject item = (JSONObject) adapterView.getAdapter().getItem(i);
        Intent intent = new Intent(getActivity(), CourseDoSignActivity.class);
        if (item.getBooleanValue("isTeacher")) {
            intent = new Intent(getActivity(), ViewTeacherCourseSignActivity.class);
        } else if (item.getInteger("status").equals(1)) {
            intent = new Intent(getActivity(), ViewMapActivity.class);
        }
        intent.putExtra("item", item.toJSONString());
        getActivity().startActivityForResult(intent, 3000);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        reqCode = 1;
        loading();
    }
}
