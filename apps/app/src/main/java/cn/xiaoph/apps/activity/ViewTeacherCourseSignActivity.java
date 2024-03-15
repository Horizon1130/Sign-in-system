package cn.xiaoph.apps.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
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

public class ViewTeacherCourseSignActivity extends BasicsActivity implements OnItemClickListener {

    @BindView(R.id.list_view)
    SwipeMenuListView listView;
    @BindView(R.id.layout_empty)
    LinearLayout layoutEmpty;
    private Integer courseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_course_sign);
        ButterKnife.bind(this);

        RefreshLayout refreshLayout = (RefreshLayout) findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadMoreListener(this);

        setTitle("课程签到");
        setBarLeftButton();
        JSONObject item = JSON.parseObject(getIntent().getStringExtra("item"));
        courseId = item.getInteger("id");

        loading();
    }

    @Override
    public void click(View view) {

    }

    @Override
    public void loadData() throws Exception, LoginException {
        if (reqCode == 1) {
            JSONObject values = new JSONObject();
            values.put("page", page);
            values.put("courseId", courseId);

            http.loginRequest("sign/list", values).responseBody(new Callback() {
                @Override
                public void success(ReturnResult result) {
                    Message msg = new Message();
                    msg.obj = result.get("list");
                    listHandler.sendMessage(msg);
                }

                @Override
                public void error(String errorMsg) {
                    Toasty.error(ViewTeacherCourseSignActivity.this, errorMsg, Toast.LENGTH_SHORT, true).show();
                }
            });
        }
    }

    private Handler listHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            List<JSONObject> list = (List<JSONObject>) msg.obj;
            basicsAdapter = new StudentSignAdapter(ViewTeacherCourseSignActivity.this, list);
            listView.setEmptyView(layoutEmpty);
            listView.setAdapter(basicsAdapter);
            listView.setOnItemClickListener(ViewTeacherCourseSignActivity.this);
        }
    };


    @Override
    public void readData() throws Exception, LoginException {
        JSONObject values = new JSONObject();
        values.put("page", page);
        values.put("courseId", courseId);

        http.loginRequest("sign/list", values).responseBody(new Callback() {
            @Override
            public void success(ReturnResult messageResult) {
                final List<JSONObject> list = (List<JSONObject>) messageResult.get("list");
                ViewTeacherCourseSignActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        basicsAdapter.setData(list);
                        basicsAdapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void error(String s) {
                Toasty.error(ViewTeacherCourseSignActivity.this, s, Toast.LENGTH_SHORT, true).show();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        JSONObject item = (JSONObject) adapterView.getAdapter().getItem(i);
        Intent intent = new Intent(this, ViewMapActivity.class);
        intent.putExtra("item", item.toJSONString());
        startActivity(intent);
    }
}
