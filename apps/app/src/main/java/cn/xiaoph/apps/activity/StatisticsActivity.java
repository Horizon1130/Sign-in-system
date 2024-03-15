package cn.xiaoph.apps.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.myway.platform.api.Callback;
import com.myway.platform.api.ReturnResult;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.xiaoph.apps.R;
import cn.xiaoph.library.error.LoginException;
import cn.xiaoph.library.util.BitmapType;



public class StatisticsActivity extends BasicsActivity {

    @BindView(R.id.img_face)
    ImageView imgFace;
    @BindView(R.id.input_name)
    TextView inputName;
    @BindView(R.id.input_class)
    TextView inputClass;
    @BindView(R.id.chart1)
    PieChart chart1;
    @BindView(R.id.chart2)
    PieChart chart2;

    String sysUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_statistics);
        ButterKnife.bind(this);

        setTitle("考勤统计");
        setBarLeftButton();

        sysUserId = getIntent().getStringExtra("sysUserId");

        loading();
    }

    @Override
    public void click(View view) {

    }

    @Override
    public void loadData() throws Exception, LoginException {
        if (reqCode == 1) {
            JSONObject values = new JSONObject();
            values.put("sysUserId", sysUserId);

            http.loginRequest("sign/statistics", values).responseBody(new Callback() {
                @Override
                public void success(ReturnResult result) {
                    Message msg = new Message();
                    msg.obj = result.get("item");
                    handler.sendMessage(msg);
                }

                @Override
                public void error(String errorMsg) {

                }
            });
        }
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            JSONObject item = (JSONObject) msg.obj;
            inputClass.setText(item.getString("className"));
            inputName.setText(item.getString("nice"));
            ImageLoader.getInstance().displayImage(item.getString("face"), imgFace, BitmapType.commodityImage);


            List<PieEntry> result1 = new ArrayList<>();
            result1.add(new PieEntry(item.getIntValue("signTotal"), "签到比率"));
            result1.add(new PieEntry(item.getIntValue("lackTotal"), "缺卡比率"));

            PieDataSet pieDataSet = new PieDataSet(result1, "");
            PieData pieData = new PieData(pieDataSet);
            chart1.setData(pieData);
            pieDataSet.setColors(Color.parseColor("#1E9FFF"), Color.parseColor("#FF5722"));

            List<PieEntry> result2 = new ArrayList<>();
            result2.add(new PieEntry(item.getIntValue("normalTotal"), "正常比率"));
            result2.add(new PieEntry(item.getIntValue("lateTotal"), "外勤比率"));

            PieDataSet pieDataSet2 = new PieDataSet(result2, "");
            PieData pieData2 = new PieData(pieDataSet2);
            chart2.setData(pieData2);
            pieDataSet2.setColors(Color.parseColor("#1E9FFF"), Color.parseColor("#FFB800"));
        }
    };

    @Override
    public void readData() throws Exception, LoginException {

    }
}
