package cn.xiaoph.apps.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.myway.platform.api.Callback;
import com.myway.platform.api.ReturnResult;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.xiaoph.apps.R;
import cn.xiaoph.library.error.LoginException;
import es.dmoral.toasty.Toasty;

public class UpdatePwdActivity extends BasicsActivity {

    @BindView(R.id.input_old_pwd)
    EditText inputOldPwd;
    @BindView(R.id.input_new_pwd)
    EditText inputNewPwd;
    @BindView(R.id.input_confirm_pwd)
    EditText inputConfirmPwd;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_pwd);
        ButterKnife.bind(this);
        setBarLeftButton();
        setTitle("修改密码");
        setBarRightButton(0, "完成");
    }

    @Override
    public void click(View view) {
        switch (view.getId()) {
            case R.id.input_bar_right_button:
                loading();
                break;
        }
    }

    @Override
    public void loadData() throws Exception, LoginException {
        JSONObject values = new JSONObject();
        values.put("oldPwd", inputOldPwd.getText().toString());
        values.put("newPwd", inputNewPwd.getText().toString());
        http.loginRequest("user/update/password", values).responseBody(new Callback() {
            @Override
            public void success(ReturnResult returnResult) {
                Toasty.success(UpdatePwdActivity.this, returnResult.getMessage(), Toast.LENGTH_SHORT, true).show();
                setResult(3001);
                finish();
            }

            @Override
            public void error(String s) {
                Toasty.error(UpdatePwdActivity.this, s, Toast.LENGTH_SHORT, true).show();
            }
        });
    }

    @Override
    public void readData() throws Exception, LoginException {

    }

}
