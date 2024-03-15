package cn.xiaoph.apps.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.OnMapClickListener;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.GeocodeSearch.OnGeocodeSearchListener;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.myway.platform.api.Callback;
import com.myway.platform.api.ReturnResult;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.xiaoph.apps.R;
import cn.xiaoph.library.error.LoginException;
import cn.xiaoph.library.view.CustomListPicker;
import es.dmoral.toasty.Toasty;

public class AddSignActivity extends BasicsActivity implements AMapLocationListener, LocationSource, OnMapClickListener, OnGeocodeSearchListener {

    @BindView(R.id.input_title)
    EditText inputTitle;
    @BindView(R.id.input_time)
    TextView inputTime;
    @BindView(R.id.map)
    MapView mMapView;
    @BindView(R.id.input_range)
    TextView inputRange;
    @BindView(R.id.input_location)
    TextView inputLocation;
    @BindView(R.id.input_class)
    TextView inputClass;
    private AMap mAMap;

    private OnLocationChangedListener mListener;
    private AMapLocationClient locationClient;
    private AMapLocationClientOption mLocationOption;
    private GeocodeSearch geocodeSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sign);
        ButterKnife.bind(this);

        mMapView.onCreate(savedInstanceState);

        setTitle("发布签到");
        setBarLeftButton();
        setBarRightButton(0, "完成");

        requestPermission();

        init();
        classify = new ArrayList<>();
        inputClass.setOnClickListener(this);
        inputRange.setOnClickListener(this);
        inputTime.setOnClickListener(this);

        loading();
    }

    void init() {
        if (mAMap == null) {
            mAMap = mMapView.getMap();
        }
        UiSettings uiSettings = mAMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);

        mAMap.setLocationSource(this);// 设置定位监听
        mAMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        mAMap.setOnMapClickListener(this);

        geocodeSearch = new GeocodeSearch(this);
        geocodeSearch.setOnGeocodeSearchListener(this);
    }

    Integer range = 1000;

    @Override
    public void click(View view) {
        switch (view.getId()) {
            case R.id.input_range:
                List<JSONObject> list = new ArrayList<>();
                for (int i = 1; i <= 5; i++) {
                    JSONObject item = new JSONObject();
                    item.put("title", i + "公里内");
                    item.put("id", i * 1000);
                    list.add(item);
                }
                CustomListPicker customDatePicker = new CustomListPicker(this, list,
                        "确认", "取消",
                        new CustomListPicker.ResultHandler() {
                            @Override
                            public void handle(JSONObject value) {
                                range = value.getInteger("id");
                                inputRange.setTag(value.getInteger("id"));
                                inputRange.setText(value.getString("title"));

                                mAMap.clear();
                                mAMap.addCircle(new CircleOptions().
                                        center(latLng).
                                        radius(range).
                                        fillColor(getResources().getColor(R.color.range_color)).
                                        strokeWidth(0));

                                mAMap.addMarker(new MarkerOptions().position(latLng)
                                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.navi_map_gps_locked))
                                        .anchor(0.5f, 0.5f));
                            }
                        });
                customDatePicker.setIsLoop(false);
                customDatePicker.show(inputRange.getText().toString());
                break;
            case R.id.input_time:
                try {
                    showDatePicker(inputTime, false, "yyyy-MM-dd", 0, +1);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.input_bar_right_button:
                reqCode = 2;
                loading();
                break;
            case R.id.input_class:
                if (!classify.isEmpty()) {
                    CustomListPicker picker = new CustomListPicker(this, classify,
                            "确认", "取消",
                            new CustomListPicker.ResultHandler() {
                                @Override
                                public void handle(JSONObject value) {
                                    inputClass.setTag(value.getString("id"));
                                    inputClass.setText(value.getString("title"));
                                }
                            });
                    picker.setIsLoop(false);
                    picker.show(inputClass.getText().toString());
                }
                break;
        }
    }

    private List<JSONObject> classify;

    @Override
    public void loadData() throws Exception, LoginException {
        if (reqCode.equals(1)) {
            JSONObject values = new JSONObject();

            http.loginRequest("class/list", values).responseBody(new Callback() {
                @Override
                public void success(ReturnResult returnResult) {
                    List<JSONObject> list = JSONObject.parseArray(returnResult.getString("list"), JSONObject.class);
                    classify.addAll(list);
                }

                @Override
                public void error(String s) {
                    Toasty.error(AddSignActivity.this, s, Toast.LENGTH_SHORT, true).show();
                }
            });
        }else if (reqCode.equals(2)) {
            JSONObject values = new JSONObject();
            values.put("title", inputTitle.getText().toString());
            values.put("endTime", inputTime.getText().toString());
            values.put("address", inputLocation.getText().toString());
            values.put("classId", inputClass.getTag().toString());
            values.put("rangeVal", range);
            values.put("lat", String.valueOf(latLng.latitude));
            values.put("lng", String.valueOf(latLng.longitude));

            http.loginRequest("course/save", values).responseBody(new Callback() {
                @Override
                public void success(ReturnResult returnResult) {
                    Toasty.success(AddSignActivity.this, returnResult.getMessage(), Toast.LENGTH_SHORT, true).show();
                    setResult(3001);
                    finish();
                }

                @Override
                public void error(String s) {
                    Toasty.error(AddSignActivity.this, s, Toast.LENGTH_SHORT, true).show();
                }
            });
        }
    }

    @Override
    public void readData() throws Exception, LoginException {

    }

    LatLng latLng;

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (mListener != null && aMapLocation != null) {
            if (aMapLocation.getAddress() != null && aMapLocation.getErrorCode() == 0) {

                latLng = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());

                mAMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

                mAMap.clear();
                mAMap.addCircle(new CircleOptions().
                        center(latLng).
                        radius(range).
                        fillColor(getResources().getColor(R.color.range_color)).
                        strokeWidth(0));

                mAMap.addMarker(new MarkerOptions().position(latLng)
                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.navi_map_gps_locked))
                        .anchor(0.5f, 0.5f));

                LatLonPoint latLonPoint = new LatLonPoint(latLng.latitude, latLng.longitude);
                RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200, GeocodeSearch.AMAP);
                geocodeSearch.getFromLocationAsyn(query);

                locationClient.stopLocation();
            }
        }
    }

    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (locationClient == null) {
            locationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            locationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //是指定位间隔
            mLocationOption.setInterval(1000);
            //设置定位参数
            locationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            locationClient.startLocation();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void deactivate() {

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
        if (null != locationClient) {
            locationClient.onDestroy();
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        this.latLng = latLng;

        mAMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, mAMap.getCameraPosition().zoom));

        mAMap.clear();
        mAMap.addCircle(new CircleOptions().
                center(latLng).
                radius(range).
                fillColor(getResources().getColor(R.color.range_color)).
                strokeWidth(0));

        mAMap.addMarker(new MarkerOptions().position(latLng)
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.navi_map_gps_locked))
                .anchor(0.5f, 0.5f));

        LatLonPoint latLonPoint = new LatLonPoint(latLng.latitude, latLng.longitude);
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200, GeocodeSearch.AMAP);
        geocodeSearch.getFromLocationAsyn(query);
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
        inputLocation.setText(regeocodeResult.getRegeocodeAddress().getFormatAddress());
        System.out.println("位置为："+regeocodeResult.getRegeocodeAddress().getFormatAddress());
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
        inputLocation.setText(geocodeResult.getGeocodeQuery().getLocationName());
    }
}
