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
import cn.xiaoph.library.http.ApiResources;
import es.dmoral.toasty.Toasty;


public class LoginActivity extends BasicsActivity {

    @BindView(R.id.input_phone)
    EditText inputPhone;
    @BindView(R.id.input_password)
    EditText inputPassword;
    @BindView(R.id.btn_login)
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        btnLogin.setOnClickListener(this);
    }

    @Override
    public void click(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                loading();
                break;
        }
    }

    @Override
    public void loadData() throws Exception, LoginException {
        if (reqCode.equals(1)) {
            JSONObject values = new JSONObject();
            values.put("account", inputPhone.getText().toString());
            values.put("password", inputPassword.getText().toString());
            http.loginRequest("login", values).responseBody(new Callback() {

                @Override
                public void success(ReturnResult result) {
                    ApiResources.token = result.getString("token");
                    ApiResources.type = result.getInteger("type");
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void error(String errorMsg) {
                    Toasty.error(LoginActivity.this, errorMsg, Toast.LENGTH_SHORT, true).show();
                }
            });
        }
    }

    @Override
    public void readData() throws Exception, LoginException {

    }
}
