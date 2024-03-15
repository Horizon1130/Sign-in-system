package cn.xiaoph.apps.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class RegisterActivity extends BasicsActivity {

    @BindView(R.id.input_phone)
    EditText inputPhone;
    @BindView(R.id.input_password)
    EditText inputPassword;
    @BindView(R.id.btn_register)
    Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        setBarLeftButton();
        btnRegister.setOnClickListener(this);
    }

    @Override
    public void click(View view) {
        switch (view.getId()) {
            case R.id.btn_register:
                loading();
                break;
        }
    }

    @Override
    public void loadData() throws Exception, LoginException {
        JSONObject values = new JSONObject();
        values.put("account", inputPhone.getText().toString());
        values.put("password", inputPassword.getText().toString());
        http.loginRequest("register", values).responseBody(new Callback() {

            @Override
            public void success(ReturnResult result) {
                Toasty.success(RegisterActivity.this, result.getMessage(), Toast.LENGTH_SHORT, true).show();
                Intent data = new Intent();
                data.putExtra("account", inputPhone.getText().toString());
                setResult(3001, data);
                finish();
            }

            @Override
            public void error(String errorMsg) {
                Toasty.error(RegisterActivity.this, errorMsg, Toast.LENGTH_SHORT, true).show();
            }
        });
    }

    @Override
    public void readData() throws Exception, LoginException {

    }
}
