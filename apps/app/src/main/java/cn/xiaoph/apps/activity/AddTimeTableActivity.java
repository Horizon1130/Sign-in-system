package cn.xiaoph.apps.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.myway.platform.api.Callback;
import com.myway.platform.api.ReturnResult;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.xiaoph.apps.R;
import cn.xiaoph.library.error.LoginException;
import cn.xiaoph.library.view.CustomListPicker;
import es.dmoral.toasty.Toasty;


public class AddTimeTableActivity extends BasicsActivity {

    @BindView(R.id.input_step)
    TextView inputStep;
    @BindView(R.id.input_title)
    EditText inputTitle;
    @BindView(R.id.input_class)
    TextView inputClass;

    private int day, start, week;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_timetable);
        ButterKnife.bind(this);

        setTitle("添加计划");
        setBarLeftButton();
        setBarRightButton(0, "保存");

        Intent intent = getIntent();
        day = intent.getIntExtra("day", 0) + 1;
        start = intent.getIntExtra("start", 0);
        week = intent.getIntExtra("week", 0);

        inputClass.setOnClickListener(this);

        classify = new ArrayList<>();
        loading();
    }

    private List<JSONObject> classify;

    @Override
    public void click(View view) {
        switch (view.getId()) {
            case R.id.input_class:
                if (!classify.isEmpty()) {
                    CustomListPicker customDatePicker = new CustomListPicker(this, classify,
                            "确认", "取消",
                            new CustomListPicker.ResultHandler() {
                                @Override
                                public void handle(JSONObject value) {
                                    inputClass.setTag(value.getString("id"));
                                    inputClass.setText(value.getString("title"));
                                }
                            });
                    customDatePicker.setIsLoop(false);
                    customDatePicker.show(inputClass.getText().toString());
                }
                break;
            case R.id.input_bar_right_button:
                reqCode = 2;
                loading();
                break;
        }
    }

    @Override
    public void loadData() throws Exception, LoginException {
        if (reqCode.equals(1)) {
            JSONObject values = new JSONObject();

            http.loginRequest("class/list", values).responseBody(new Callback() {
                @Override
                public void success(ReturnResult returnResult) {
                    List<JSONObject> list = JSONObject.parseArray(returnResult.getString("list"), JSONObject.class);
                    classify.addAll(list);
                }

                @Override
                public void error(String s) {
                    Toasty.error(AddTimeTableActivity.this, s, Toast.LENGTH_SHORT, true).show();
                }
            });
        } else if (reqCode.equals(2)) {
            JSONObject values = new JSONObject();
            values.put("title", inputTitle.getText().toString());
            values.put("classId", inputClass.getTag().toString());
            values.put("step", inputStep.getText().toString());
            values.put("day", day);
            values.put("start", start);
            values.put("week", week);

            http.loginRequest("timetable/save", values).responseBody(new Callback() {
                @Override
                public void success(ReturnResult returnResult) {
                    Toasty.success(AddTimeTableActivity.this, returnResult.getMessage(), Toast.LENGTH_SHORT, true).show();
                    setResult(3001);
                    finish();
                }

                @Override
                public void error(String s) {
                    Toasty.error(AddTimeTableActivity.this, s, Toast.LENGTH_SHORT, true).show();
                }
            });
        }
    }

    @Override
    public void readData() throws Exception, LoginException {

    }

    @OnClick(R.id.input_step)
    public void onViewClicked() {
        List<JSONObject> steps = new ArrayList<>();

        for (int i = 1; i <= 5; i++) {
            JSONObject item = new JSONObject();
            item.put("title", i);
            steps.add(item);
        }
        CustomListPicker customDatePicker = new CustomListPicker(this, steps,
                "确认", "取消",
                new CustomListPicker.ResultHandler() {
                    @Override
                    public void handle(JSONObject value) {
                        inputStep.setText(value.getString("title"));
                    }
                });
        customDatePicker.setIsLoop(false);
        customDatePicker.show(inputStep.getText().toString());
    }
}
