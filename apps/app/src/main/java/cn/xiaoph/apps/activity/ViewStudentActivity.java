package cn.xiaoph.apps.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
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
import cn.xiaoph.apps.R;
import cn.xiaoph.apps.adapter.StudentSignAdapter;
import cn.xiaoph.library.error.LoginException;
import es.dmoral.toasty.Toasty;

public class ViewStudentActivity extends BasicsActivity implements AdapterView.OnItemClickListener {

    String sysUserId;
    @BindView(R.id.list_view)
    SwipeMenuListView listView;
    @BindView(R.id.layout_empty)
    LinearLayout layoutEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_course_sign);
        ButterKnife.bind(this);

        RefreshLayout refreshLayout = (RefreshLayout) findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadMoreListener(this);

        setTitle("学生列表");
        setBarLeftButton();

        loading();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        JSONObject item = (JSONObject) adapterView.getAdapter().getItem(i);
        Intent intent = new Intent(ViewStudentActivity.this, StatisticsActivity.class);
        intent.putExtra("sysUserId", item.getString("sysUserId"));
        startActivity(intent);
    }

    @Override
    public void click(View view) {

    }

    @Override
    public void loadData() throws Exception, LoginException {
        if (reqCode == 1) {
            JSONObject values = new JSONObject();
            values.put("page", page);

            http.loginRequest("student/list", values).responseBody(new Callback() {

                @Override
                public void success(ReturnResult result) {
                    Message msg = new Message();
                    msg.obj = result.get("list");
                    listHandler.sendMessage(msg);
                }

                @Override
                public void error(String errorMsg) {
                    Toasty.error(ViewStudentActivity.this, errorMsg, Toast.LENGTH_SHORT, true).show();
                }
            });
        }
    }

    private Handler listHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            List<JSONObject> list = (List<JSONObject>) msg.obj;
            basicsAdapter = new StudentSignAdapter(ViewStudentActivity.this, list);
            listView.setEmptyView(layoutEmpty);
            listView.setAdapter(basicsAdapter);
            listView.setOnItemClickListener(ViewStudentActivity.this);
        }
    };

    @Override
    public void readData() throws Exception, LoginException {
        JSONObject values = new JSONObject();
        values.put("page", page);

        http.loginRequest("student/list", values).responseBody(new Callback() {
            @Override
            public void success(ReturnResult messageResult) {
                final List<JSONObject> list = (List<JSONObject>) messageResult.get("list");
                ViewStudentActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        basicsAdapter.setData(list);
                        basicsAdapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void error(String s) {
                Toasty.error(ViewStudentActivity.this, s, Toast.LENGTH_SHORT, true).show();
            }
        });
    }
}
