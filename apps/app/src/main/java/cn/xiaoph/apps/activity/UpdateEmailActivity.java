package cn.xiaoph.apps.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.xiaoph.apps.R;
import cn.xiaoph.library.error.LoginException;

public class UpdateEmailActivity extends BasicsActivity {

    @BindView(R.id.input_update_email)
    EditText inputUpdateEmail;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_email);
        ButterKnife.bind(this);
        setBarLeftButton();
        setTitle("修改邮箱");
        setBarRightButton(0, "完成");
    }

    @Override
    public void click(View view) {
        switch (view.getId()) {
            case R.id.input_bar_right_button:
                Intent intent = new Intent();
                intent.putExtra("email", inputUpdateEmail.getText().toString());
                setResult(4001, intent);
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
