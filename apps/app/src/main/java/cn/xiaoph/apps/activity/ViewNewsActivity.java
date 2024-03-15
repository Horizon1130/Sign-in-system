package cn.xiaoph.apps.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.sufficientlysecure.htmltextview.HtmlHttpImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.xiaoph.apps.R;
import cn.xiaoph.library.error.LoginException;

public class ViewNewsActivity extends BasicsActivity {

    @BindView(R.id.input_title)
    TextView inputTitle;
    @BindView(R.id.input_time)
    TextView inputTime;
    @BindView(R.id.input_content)
    HtmlTextView inputContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_news);
        ButterKnife.bind(this);
        setBarLeftButton();
        setTitle("新闻详情");

        JSONObject item = JSON.parseObject(getIntent().getStringExtra("item"));
        if (!item.isEmpty()) {
            inputTitle.setText(item.getString("title"));
            inputTime.setText(item.getString("time"));
            inputContent.setHtml(item.getString("content"), new HtmlHttpImageGetter(inputContent));
        }
    }

    @Override
    public void click(View view) {

    }

    @Override
    public void loadData() throws Exception, LoginException {

    }

    @Override
    public void readData() throws Exception, LoginException {

    }
}
