package cn.xiaoph.apps.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.myway.platform.api.Callback;
import com.myway.platform.api.ReturnResult;
import com.zhuangfei.timetable.TimetableView;
import com.zhuangfei.timetable.listener.ISchedule;
import com.zhuangfei.timetable.listener.IWeekView;
import com.zhuangfei.timetable.listener.OnItemBuildAdapter;
import com.zhuangfei.timetable.model.Schedule;
import com.zhuangfei.timetable.view.WeekView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.xiaoph.apps.R;
import cn.xiaoph.apps.activity.AddTimeTableActivity;
import cn.xiaoph.apps.model.MySubject;
import cn.xiaoph.library.error.LoginException;
import cn.xiaoph.library.http.ApiResources;
import cn.xiaoph.library.view.ConfrimDialog;
import es.dmoral.toasty.Toasty;

public class TimeTableFragment extends BaseFragment {

    @BindView(R.id.id_title)
    TextView idTitle;
    @BindView(R.id.id_weekview)
    WeekView idWeekview;
    @BindView(R.id.id_timetableView)
    TimetableView idTimetableView;
    Unbinder unbinder;
    @BindView(R.id.id_layout)
    LinearLayout idLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_timetable, container, false);
        unbinder = ButterKnife.bind(this, view);

        setTitle("我的课表");
        idLayout.setOnClickListener(this);
        initTimetableView();

        loading();
        return view;
    }

    @Override
    public void loadData() throws Exception, LoginException {
        if (reqCode.equals(1)) {
            JSONObject values = new JSONObject();

            http.loginRequest("timetable/list", values).responseBody(new Callback() {
                @Override
                public void success(ReturnResult returnResult) {
                    Message msg = new Message();
                    msg.obj = returnResult.get("list");
                    handler.sendMessage(msg);
                }

                @Override
                public void error(String s) {
                    Toasty.error(getActivity(), s, Toast.LENGTH_SHORT, true).show();
                }
            });
        } else  if (reqCode.equals(2)) {
            JSONObject values = new JSONObject();
            values.putAll(formData);

            http.loginRequest("timetable/delete", values).responseBody(new Callback() {
                @Override
                public void success(ReturnResult returnResult) {
                    formData.clear();
                    reqCode = 1;
                    loading();
                }

                @Override
                public void error(String s) {
                    Toasty.error(getActivity(), s, Toast.LENGTH_SHORT, true).show();
                }
            });
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            List<JSONObject> list = (List<JSONObject>) msg.obj;
            List<MySubject> mySubjects = new ArrayList<>();

            for (JSONObject jsonObject : list) {
                MySubject mySubject = new MySubject();
                mySubject.setId(jsonObject.getInteger("id"));
                mySubject.setName(jsonObject.getString("content"));
                mySubject.setDay(jsonObject.getInteger("day"));
                mySubject.setStart(jsonObject.getInteger("start"));
                mySubject.setStep(jsonObject.getInteger("step"));
                mySubject.getWeekList().add(jsonObject.getInteger("week"));
                mySubject.setRoom(jsonObject.getInteger("status").toString());
                mySubjects.add(mySubject);
            }
            idWeekview.source(mySubjects).showView();
            idTimetableView.source(mySubjects).showView();
        }
    };


    private void initTimetableView() {
        //设置周次选择属性
        idWeekview.curWeek(1)
                .callback(new IWeekView.OnWeekItemClickedListener() {
                    @Override
                    public void onWeekClicked(int week) {
                        int cur = idTimetableView.curWeek();
                        //更新切换后的日期，从当前周cur->切换的周week
                        idTimetableView.onDateBuildListener()
                                .onUpdateDate(cur, week);
                        idTimetableView.changeWeekOnly(week);
                    }
                })
                .callback(new IWeekView.OnWeekLeftClickedListener() {
                    @Override
                    public void onWeekLeftClicked() {
                        onWeekLeftLayoutClicked();
                    }
                })
                .isShow(false)//设置隐藏，默认显示
                .showView();

        idTimetableView.curWeek(1)
                .curTerm("我的课表")
                .callback(new ISchedule.OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, List<Schedule> scheduleList) {
                        if (ApiResources.type.equals(1)) {
                            Schedule schedule = scheduleList.get(0);
                            Intent intent = new Intent(getActivity(), AddTimeTableActivity.class);
                            intent.putExtra("day", schedule.getDay() - 1);
                            intent.putExtra("start", schedule.getStart());
                            intent.putExtra("week", Integer.parseInt(idTitle.getTag().toString()));
                            getActivity().startActivityForResult(intent, 3000);
                        }
                    }
                })
                .callback(new ISchedule.OnSpaceItemClickListener() {

                    @Override
                    public void onSpaceItemClick(int day, int start) {
                        if (ApiResources.type.equals(1)) {
                            Intent intent = new Intent(getActivity(), AddTimeTableActivity.class);
                            intent.putExtra("day", day);
                            intent.putExtra("start", start);
                            intent.putExtra("week", Integer.parseInt(idTitle.getTag().toString()));
                            getActivity().startActivityForResult(intent, 3000);
                        }
                    }

                    @Override
                    public void onInit(LinearLayout flagLayout, int monthWidth, int itemWidth, int itemHeight, int marTop, int marLeft) {
                    }
                })
                .callback(new ISchedule.OnItemLongClickListener() {
                    @Override
                    public void onLongClick(View v, int day, int start) {
                        if (ApiResources.type.equals(1)) {
                            formData.put("day", day);
                            formData.put("start", start);
                            formData.put("week", Integer.parseInt(idTitle.getTag().toString()));
                            final ConfrimDialog dialog = new ConfrimDialog("删除计划", "确认要删除此条课程安排？", getActivity());

                            dialog.setClicklistener(new ConfrimDialog.ClickListenerInterface() {

                                @Override
                                public void doConfirm() {
                                    reqCode = 2;
                                    loading();
                                    dialog.dismiss();
                                }

                                @Override
                                public void doCancel() {
                                    formData.clear();
                                    dialog.dismiss();
                                }
                            });
                            dialog.show();

                        }
                    }
                })
                .callback(new ISchedule.OnWeekChangedListener() {
                    @Override
                    public void onWeekChanged(int curWeek) {
                        idTitle.setText("第" + curWeek + "周");
                        idTitle.setTag(curWeek);
                    }
                })
                .callback(new OnItemBuildAdapter() {
                    @Override
                    public void onItemUpdate(FrameLayout layout, TextView textView, TextView countTextView, Schedule schedule, GradientDrawable gd) {
//                        if (schedule.getRoom().equals("-1")) {
//                            textView.setBackgroundResource(R.drawable.bg_solid_plan_overdue);
//                        }
                        super.onItemUpdate(layout, textView, countTextView, schedule, gd);
                    }
                })
                .showView();
    }

    int target = -1;

    /**
     * 周次选择布局的左侧被点击时回调<br/>
     * 对话框修改当前周次
     */
    protected void onWeekLeftLayoutClicked() {
        final String items[] = new String[20];
        int itemCount = idWeekview.itemCount();
        for (int i = 0; i < itemCount; i++) {
            items[i] = "第" + (i + 1) + "周";
        }
        target = -1;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("设置当前周");
        builder.setSingleChoiceItems(items, idTimetableView.curWeek() - 1,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        target = i;
                    }
                });
        builder.setPositiveButton("设置为当前周", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (target != -1) {
                    idWeekview.curWeek(target + 1).updateView();
                    idTimetableView.changeWeekForce(target + 1);
                }
            }
        });
        builder.setNegativeButton("取消", null);
        builder.create().show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_layout:
                //如果周次选择已经显示了，那么将它隐藏，更新课程、日期
                //否则，显示
                if (idWeekview.isShowing()) hideWeekView();
                else showWeekView();
                break;
        }
    }

    /**
     * 隐藏周次选择，此时需要将课表的日期恢复到本周并将课表切换到当前周
     */
    public void hideWeekView() {
        idWeekview.isShow(false);
        idTitle.setTextColor(getResources().getColor(R.color.app_course_textcolor_blue));
        int cur = idTimetableView.curWeek();
        idTimetableView.onDateBuildListener()
                .onUpdateDate(cur, cur);
        idTimetableView.changeWeekOnly(cur);
    }

    public void showWeekView() {
        idWeekview.isShow(true);
        idTitle.setTextColor(getResources().getColor(R.color.app_red));
    }

    @Override
    public void readData() throws Exception, LoginException {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        reqCode = 1;
        loading();
    }
}
