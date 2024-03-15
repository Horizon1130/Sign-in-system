package cn.xiaoph.apps.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.xiaoph.apps.R;
import cn.xiaoph.library.error.LoginException;

public class UpdateNiceActivity extends BasicsActivity {

    @BindView(R.id.input_update_nice)
    EditText inputUpdateNice;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_nice);
        ButterKnife.bind(this);
        setBarLeftButton();
        setTitle("修改昵称");
        setBarRightButton(0, "完成");
    }

    @Override
    public void click(View view) {
        switch (view.getId()) {
            case R.id.input_bar_right_button:
                Intent intent = new Intent();
                intent.putExtra("nice", inputUpdateNice.getText().toString());
                setResult(3001, intent);
                finish();
                break;
        }
    }

    @Override
    public void loadData() throws Exception, LoginException {

    }

    @Override
    public void readData() throws Exception, LoginException {

    }

}
