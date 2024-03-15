package cn.xiaoph.apps.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.myway.platform.api.Callback;
import com.myway.platform.api.ReturnResult;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.xiaoph.apps.R;
import cn.xiaoph.library.error.LoginException;
import es.dmoral.toasty.Toasty;

public class ViewMapActivity extends BasicsActivity {

    @BindView(R.id.map)
    MapView mMapView;

    private AMap mAMap;
    private Integer signId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_map);
        ButterKnife.bind(this);

        mMapView.onCreate(savedInstanceState);
        setTitle("签到详情");
        setBarLeftButton();

        JSONObject item = JSON.parseObject(getIntent().getStringExtra("item"));
        signId = item.getInteger("signId");

        requestPermission();

        if (mAMap == null) {
            mAMap = mMapView.getMap();
        }
        UiSettings uiSettings = mAMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);

        loading();
    }

    @Override
    public void click(View view) {

    }

    @Override
    public void loadData() throws Exception, LoginException {
        if (reqCode.equals(1)) {
            JSONObject values = new JSONObject();
            values.put("id", signId);

            http.loginRequest("sign/view", values).responseBody(new Callback() {
                @Override
                public void success(ReturnResult returnResult) {
                    Message msg = new Message();
                    msg.obj = returnResult.get("item");
                    handler.sendMessage(msg);
                }

                @Override
                public void error(String s) {
                    Toasty.error(ViewMapActivity.this, s, Toast.LENGTH_SHORT, true).show();
                }
            });
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            JSONObject item = (JSONObject) msg.obj;

            LatLng rangeLatLng = new LatLng(item.getDouble("courseLat"), item.getDouble("courseLng"));

            mAMap.clear();
            mAMap.addCircle(new CircleOptions().
                    center(rangeLatLng).
                    radius(item.getInteger("range")).
                    fillColor(getResources().getColor(R.color.range_color)).
                    strokeWidth(0));

            LatLng latLng = new LatLng(item.getDouble("lat"), item.getDouble("lng"));
            //首次定位
            mAMap.addMarker(new MarkerOptions().position(latLng)
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.navi_map_gps_locked))
                    .anchor(0.5f, 0.5f));
            mAMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
        }
    };

    @Override
    public void readData() throws Exception, LoginException {

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }
}
