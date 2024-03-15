package cn.xiaoph.apps.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.jph.takephoto.model.TImage;
import com.myway.platform.api.Callback;
import com.myway.platform.api.ReturnResult;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.xiaoph.apps.R;
import cn.xiaoph.library.error.LoginException;
import cn.xiaoph.library.util.Bimp;
import cn.xiaoph.library.util.BitmapType;
import cn.xiaoph.library.view.CustomListPicker;
import es.dmoral.toasty.Toasty;


public class UserInfoActivity extends BasicsActivity {

    @BindView(R.id.img_user_face)
    ImageView imgUserFace;
    @BindView(R.id.input_nice)
    TextView inputNice;
    @BindView(R.id.layout_nice)
    LinearLayout layoutNice;
    @BindView(R.id.input_account)
    TextView inputAccount;
    @BindView(R.id.input_phone)
    TextView inputPhone;
    @BindView(R.id.layout_phone)
    LinearLayout layoutPhone;
    @BindView(R.id.input_sex)
    TextView inputSex;
    @BindView(R.id.layout_sex)
    LinearLayout layoutSex;
    @BindView(R.id.input_birthday)
    TextView inputBirthday;
    @BindView(R.id.layout_birthday)
    LinearLayout layoutBirthday;
    @BindView(R.id.input_email)
    TextView inputEmail;
    @BindView(R.id.layou_email)
    LinearLayout layouEmail;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        ButterKnife.bind(this);

        setBarLeftButton();
        setTitle("个人信息");
        setBarRightButton(0, "保存");

        inputSex.setText("男");
        inputSex.setTag(1);

        imgUserFace.setOnClickListener(this);
        layoutNice.setOnClickListener(this);
        layoutPhone.setOnClickListener(this);
        layoutSex.setOnClickListener(this);
        layoutBirthday.setOnClickListener(this);
        layouEmail.setOnClickListener(this);

        loading();
    }

    @Override
    public void click(View view) {
        switch (view.getId()) {
            case R.id.img_user_face:
                showWindowPhoto(0);
                break;
            case R.id.layout_nice:
                Intent intent = new Intent(this, UpdateNiceActivity.class);
                startActivityForResult(intent, 3000);
                break;
            case R.id.layou_email:
                intent = new Intent(this, UpdateEmailActivity.class);
                startActivityForResult(intent, 4000);
                break;
            case R.id.layout_phone:
                intent = new Intent(this, UpdatePhoneActivity.class);
                startActivityForResult(intent, 5000);
                break;
            case R.id.layout_sex:
                List<JSONObject> sexList = new ArrayList<>();
                JSONObject item1 = new JSONObject();
                item1.put("id", "1");
                item1.put("title", "男");
                JSONObject item2 = new JSONObject();
                item2.put("id", "0");
                item2.put("title", "女");
                sexList.add(item1);
                sexList.add(item2);
                CustomListPicker customDatePicker = new CustomListPicker(this, sexList,
                        "确认", "取消",
                        new CustomListPicker.ResultHandler() {
                            @Override
                            public void handle(JSONObject value) {
                                inputSex.setTag(value.getString("id"));
                                inputSex.setText(value.getString("title"));
                            }
                        });
                customDatePicker.setIsLoop(false);
                customDatePicker.show(inputSex.getText().toString());
                break;
            case R.id.layout_birthday:
                try {
                    showDatePicker(inputBirthday, false, "yyyy-MM-dd", -50, 0);
                } catch (ParseException e) {
                    e.printStackTrace();
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
        if (reqCode == 1) {
            JSONObject values = new JSONObject();
            http.loginRequest("user/detail/view", values).responseBody(new Callback() {
                @Override
                public void success(ReturnResult result) {
                    Message msg = new Message();
                    msg.obj = result.get("data");
                    handler.sendMessage(msg);
                }

                @Override
                public void error(String errorMsg) {

                }
            });
        } else {
            JSONObject values = new JSONObject();
            values.put("nice", inputNice.getText().toString());
            values.put("sex", inputSex.getTag().toString());
            values.put("birthday", inputBirthday.getText().toString());
            values.put("email", inputEmail.getText().toString());
            values.put("phone", inputPhone.getText().toString());
            if (face != null) {
                values.put("image", face);
            }
            http.loginRequest("user/update/detail", values).responseBody(new Callback() {
                @Override
                public void success(ReturnResult returnResult) {
                    Toasty.success(UserInfoActivity.this, returnResult.getMessage(), Toast.LENGTH_SHORT, true).show();
                    setResult(3001);
                    finish();
                }

                @Override
                public void error(String s) {
                    Toasty.error(UserInfoActivity.this, s, Toast.LENGTH_SHORT, true).show();
                }
            });
        }
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            JSONObject item = (JSONObject) msg.obj;
            inputAccount.setText(item.getString("account"));
            inputNice.setText(item.getString("nice"));
            if (item.getString("sex") != null) {
                inputSex.setText(item.getString("sex").equals("1") ? "男" : "女");
                inputSex.setTag(item.getString("sex"));
            }
            inputPhone.setText(item.getString("phone"));
            inputBirthday.setText(item.getString("birthday"));
            inputEmail.setText(item.getString("email"));
            ImageLoader.getInstance().displayImage(item.getString("face"), imgUserFace, BitmapType.circleImage);
        }
    };

    @Override
    public void readData() throws Exception, LoginException {

    }

    private File face;

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 3000 && resultCode == 3001) {
            String nice = data.getStringExtra("nice");
            inputNice.setText(nice);
        } else if (requestCode == 4000 && resultCode == 4001) {
            String email = data.getStringExtra("email");
            inputEmail.setText(email);
        } else if (requestCode == 5000 && resultCode == 5001) {
            String phone = data.getStringExtra("phone");
            inputPhone.setText(phone);
        } else {
            getTakePhoto().onActivityResult(requestCode, resultCode, data);
        }
    }

    public void getImages(ArrayList<TImage> images) {
        String imagePath = images.get(0).getPath();
        face = new File(imagePath);
        Bitmap bitmapFromUrl = Bimp.toRoundBitmap(imagePath, 200, 200);
        imgUserFace.setImageBitmap(bitmapFromUrl);
    }
}
