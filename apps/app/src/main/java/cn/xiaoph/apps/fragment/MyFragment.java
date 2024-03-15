package cn.xiaoph.apps.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.myway.platform.api.Callback;
import com.myway.platform.api.ReturnResult;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.xiaoph.apps.R;
import cn.xiaoph.apps.activity.LoginActivity;
import cn.xiaoph.apps.activity.StatisticsActivity;
import cn.xiaoph.apps.activity.UpdatePwdActivity;
import cn.xiaoph.apps.activity.UserInfoActivity;
import cn.xiaoph.apps.activity.ViewStudentActivity;
import cn.xiaoph.library.error.LoginException;
import cn.xiaoph.library.http.ApiResources;
import cn.xiaoph.library.util.BitmapType;
import cn.xiaoph.library.view.ConfrimDialog;


public class MyFragment extends BaseFragment {

    @BindView(R.id.img_user_face)
    ImageView imgUserFace;
    @BindView(R.id.input_nice)
    TextView inputNice;
    @BindView(R.id.input_account)
    TextView inputAccount;
    @BindView(R.id.layout_user_detail)
    LinearLayout layoutUserDetail;
    @BindView(R.id.layout_password)
    LinearLayout layoutPassword;
    @BindView(R.id.btn_logout)
    LinearLayout btnLogout;
    Unbinder unbinder;
    @BindView(R.id.layout_statistics)
    LinearLayout layoutStatistics;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my, container, false);
        unbinder = ButterKnife.bind(this, view);

        setTitle("我的");
        RefreshLayout refreshLayout = (RefreshLayout) view.findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(this);

        layoutUserDetail.setOnClickListener(this);
        layoutPassword.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
        layoutStatistics.setOnClickListener(this);

        loading();
        return view;
    }

    @Override
    public void loadData() throws Exception, LoginException {
        if (reqCode.equals(-1)) {
            JSONObject values = new JSONObject();
            http.loginRequest("user/logout", values);
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        } else if (reqCode.equals(1)) {
            JSONObject values = new JSONObject();
            http.loginRequest("user/detail", values).responseBody(new Callback() {
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
        }
    }

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            JSONObject item = (JSONObject) msg.obj;
            ImageLoader.getInstance().displayImage(item.getString("face"), imgUserFace, BitmapType.circleImage);
            inputNice.setText(item.getString("nice"));
            inputAccount.setText(item.getString("account"));
        }
    };

    @Override
    public void readData() throws Exception, LoginException {

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_user_detail:
                Intent intent = new Intent(getActivity(), UserInfoActivity.class);
                getActivity().startActivityForResult(intent, 3000);
                break;
            case R.id.layout_password:
                intent = new Intent(getActivity(), UpdatePwdActivity.class);
                startActivity(intent);
                break;
            case R.id.layout_statistics:
                intent = new Intent(getActivity(), StatisticsActivity.class);
                if (ApiResources.type.equals(1)) {
                    intent = new Intent(getActivity(), ViewStudentActivity.class);
                }
                startActivity(intent);
                break;
            case R.id.btn_logout:
                final ConfrimDialog dialog = new ConfrimDialog("注销账号", "确认注销账号吗？", getActivity());

                dialog.setClicklistener(new ConfrimDialog.ClickListenerInterface() {

                    @Override
                    public void doConfirm() {
                        reqCode = -1;
                        loading();
                        dialog.dismiss();
                    }

                    @Override
                    public void doCancel() {
                        dialog.dismiss();
                    }
                });
                dialog.show();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 3000 && resultCode == 3001) {
            reqCode = 1;
            loading();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
