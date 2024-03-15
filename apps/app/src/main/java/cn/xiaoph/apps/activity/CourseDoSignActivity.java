package cn.xiaoph.apps.activity;

import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.Projection;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.myway.platform.api.Callback;
import com.myway.platform.api.ReturnResult;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.xiaoph.apps.R;
import cn.xiaoph.library.error.LoginException;
import cn.xiaoph.library.util.BitmapType;
import es.dmoral.toasty.Toasty;


public class CourseDoSignActivity extends BasicsActivity implements AMapLocationListener, LocationSource, AMap.OnMapTouchListener {

    @BindView(R.id.input_title)
    TextView inputTitle;
    @BindView(R.id.input_status)
    TextView inputStatus;
    @BindView(R.id.input_time)
    TextView inputTime;
    @BindView(R.id.input_address)
    TextView inputAddress;
    @BindView(R.id.img_teacher_face)
    ImageView imgTeacherFace;
    @BindView(R.id.input_teacher_name)
    TextView inputTeacherName;
    @BindView(R.id.input_position)
    TextView inputPosition;
    @BindView(R.id.map)
    MapView mMapView;
    @BindView(R.id.btn_sign)
    TextView btnSign;

    private AMap mAMap;
    private Integer courseId;

    private OnLocationChangedListener mListener;
    private AMapLocationClient locationClient;
    private AMapLocationClientOption mLocationOption;

    Circle circle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_dosign);

        ButterKnife.bind(this);

        mMapView.onCreate(savedInstanceState);
        setTitle("课程签到");
        setBarLeftButton();

        JSONObject item = JSON.parseObject(getIntent().getStringExtra("item"));
        courseId = item.getInteger("id");

        inputTime.setText(item.getString("time"));
        inputAddress.setText(item.getString("address"));
        inputTitle.setText(item.getString("title"));
        inputStatus.setText(item.getString("status"));
        btnSign.setOnClickListener(this);

        requestPermission();

        if (mAMap == null) {
            mAMap = mMapView.getMap();
        }
        UiSettings uiSettings = mAMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);

        mAMap.setLocationSource(this);// 设置定位监听
        mAMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false

        LatLng latLng = new LatLng(item.getDouble("lat"), item.getDouble("lng"));

        mAMap.clear();
        circle = mAMap.addCircle(new CircleOptions().
                center(latLng).
                radius(item.getInteger("range")).
                fillColor(getResources().getColor(R.color.range_color)).
                strokeWidth(0));

        loading();
    }

    @Override
    public void click(View view) {
        switch (view.getId()) {
            case R.id.btn_sign:
                reqCode = 2;
                loading();
                break;
        }
    }

    @Override
    public void loadData() throws Exception, LoginException {
        if (reqCode.equals(1)) {
            JSONObject values = new JSONObject();
            values.put("courseId", courseId);

            http.loginRequest("course/view", values).responseBody(new Callback() {
                @Override
                public void success(ReturnResult messageResult) {
                    Message msg = new Message();
                    msg.obj = messageResult.get("item");
                    handler.sendMessage(msg);
                }

                @Override
                public void error(String s) {
                    Toasty.error(CourseDoSignActivity.this, s, Toast.LENGTH_SHORT, true).show();
                }
            });
        } else  if (reqCode.equals(2)) {
            JSONObject values = new JSONObject();
            values.put("courseId", courseId);
            values.put("lat", String.valueOf(latLng.latitude));
            values.put("lng", String.valueOf(latLng.longitude));
            values.put("normal", normal);

            http.loginRequest("course/sign", values).responseBody(new Callback() {
                @Override
                public void success(ReturnResult messageResult) {
                    Toasty.success(CourseDoSignActivity.this, messageResult.getMessage(), Toast.LENGTH_SHORT, true).show();
                    setResult(3001);
                    finish();
                }

                @Override
                public void error(String s) {
                    Toasty.error(CourseDoSignActivity.this, s, Toast.LENGTH_SHORT, true).show();
                }
            });
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            JSONObject item = (JSONObject) msg.obj;

            ImageLoader.getInstance().displayImage(item.getString("face"), imgTeacherFace, BitmapType.commodityImage);
            inputTeacherName.setText(item.getString("nice"));
            inputPosition.setText(item.getString("position"));
        }
    };

    @Override
    public void readData() throws Exception, LoginException {

    }

    boolean useMoveToLocationWithMapMode = true;
    //自定义定位小蓝点的Marker
    Marker locationMarker;
    LatLng latLng;

    Integer normal;

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (mListener != null && aMapLocation != null) {
            if (aMapLocation.getAddress() != null && aMapLocation.getErrorCode() == 0) {

                latLng = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());

                if (circle.contains(latLng)) {
                    btnSign.setBackgroundResource(R.drawable.bg_shape_sign_button);
                    normal = 1;
                } else {
                    btnSign.setBackgroundResource(R.drawable.bg_shape_late_button);
                    normal = 0;
                }

                //展示自定义定位小蓝点
                if (locationMarker == null) {
                    // 设置当前地图显示为当前位置

                    //首次定位
                    locationMarker = mAMap.addMarker(new MarkerOptions().position(latLng)
                            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.navi_map_gps_locked))
                            .anchor(0.5f, 0.5f));
                    mAMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
                } else {

                    if (useMoveToLocationWithMapMode) {
                        //二次以后定位，使用sdk中没有的模式，让地图和小蓝点一起移动到中心点（类似导航锁车时的效果）
                        startMoveLocationAndMap(latLng);
                    } else {
                        startChangeLocation(latLng);
                    }
                }
            }
        }
    }

    /**
     * 修改自定义定位小蓝点的位置
     *
     * @param latLng
     */
    private void startChangeLocation(LatLng latLng) {
        if (locationMarker != null) {
            LatLng curLatlng = locationMarker.getPosition();
            if (curLatlng == null || !curLatlng.equals(latLng)) {
                locationMarker.setPosition(latLng);
            }
        }
    }

    //坐标和经纬度转换工具
    Projection projection;

    /**
     * 同时修改自定义定位小蓝点和地图的位置
     *
     * @param latLng
     */
    private void startMoveLocationAndMap(LatLng latLng) {
        //将小蓝点提取到屏幕上
        if (projection == null) {
            projection = mAMap.getProjection();
        }
        if (locationMarker != null && projection != null) {
            LatLng markerLocation = locationMarker.getPosition();
            Point screenPosition = mAMap.getProjection().toScreenLocation(markerLocation);
            locationMarker.setPositionByPixels(screenPosition.x, screenPosition.y);

        }
        //移动地图，移动结束后，将小蓝点放到放到地图上
        myCancelCallback.setTargetLatlng(latLng);
        //动画移动的时间，最好不要比定位间隔长，如果定位间隔2000ms 动画移动时间最好小于2000ms，可以使用1000ms
        //如果超过了，需要在myCancelCallback中进行处理被打断的情况
        mAMap.animateCamera(CameraUpdateFactory.changeLatLng(latLng), 1000, myCancelCallback);
    }

    MyCancelCallback myCancelCallback = new MyCancelCallback();

    @Override
    public void onTouch(MotionEvent motionEvent) {
        useMoveToLocationWithMapMode = false;
    }

    /**
     * 监控地图动画移动情况，如果结束或者被打断，都需要执行响应的操作
     */
    class MyCancelCallback implements AMap.CancelableCallback {

        LatLng targetLatlng;

        public void setTargetLatlng(LatLng latlng) {
            this.targetLatlng = latlng;
        }

        @Override
        public void onFinish() {
            if (locationMarker != null && targetLatlng != null) {
                locationMarker.setPosition(targetLatlng);
            }
        }

        @Override
        public void onCancel() {
            if (locationMarker != null && targetLatlng != null) {
                locationMarker.setPosition(targetLatlng);
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
        mListener = null;
        if (locationClient != null) {
            locationClient.stopLocation();
            locationClient.onDestroy();
        }
        locationClient = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
        useMoveToLocationWithMapMode = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
        useMoveToLocationWithMapMode = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        if (null != locationClient) {
            locationClient.onDestroy();
        }
    }
}
