package com.imorning.tns.ui.map;

import android.annotation.SuppressLint;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.imorning.tns.R;
import com.imorning.tns.utils.MapInfoUtils;

public class MapFragment extends Fragment implements AMapLocationListener, LocationSource {

    private static final String TAG = "MapFragment";
    //高德地图相关API
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;
    //保存位置信息
    private MapInfoUtils mapInfoUtils = null;
    //标识，用于判断是否只显示一次定位信息和用户重新定位
    private boolean isFirstLoc = true;

    private OnLocationChangedListener mListener;
    private MapView mapView;
    private AMap aMap;

    private MapViewModel mViewModel;
    private View rootView;

    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.map_fragment, container, false);
        mapView = rootView.findViewById(R.id.mapview);
        setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MapViewModel.class);
        initMapSDK();
        initMapView(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.map_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_direct_to) {
            // TODO: 2021-04-23 添加导航 
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 初始化地图SDK
     */
    private void initMapSDK() {
        //初始化定位
        mLocationClient = new AMapLocationClient(requireContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(this);
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        //设置定位场景，目前支持三种场景（签到、出行、运动，默认无场景）
        mLocationOption.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.SignIn);
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //关闭缓存机制
        mLocationOption.setLocationCacheEnable(false);
        //获取一次定位结果,该方法默认为false。
        mLocationOption.setOnceLocation(true);
        //获取最近3s内精度最高的一次定位结果,设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。
        //如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
        //mLocationOption.setOnceLocationLatest(true);
        //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
        mLocationOption.setInterval(1000);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        if (null != mLocationClient) {
            mLocationClient.setLocationOption(mLocationOption);
            //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
            mLocationClient.stopLocation();
            mLocationClient.startLocation();
        }
    }

    /**
     * 初始化MapView及其相关设置
     *
     * @param savedInstanceState 保存的数据
     */
    private void initMapView(Bundle savedInstanceState) {
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        aMap = mapView.getMap();
        // 显示实时交通状况  地图模式可选类型：MAP_TYPE_NORMAL,MAP_TYPE_SATELLITE,MAP_TYPE_NIGHT
        aMap.setTrafficEnabled(true);
        aMap.setMapType(AMap.MAP_TYPE_NORMAL);
        aMap.setLocationSource(this);
        UiSettings uiSettings = aMap.getUiSettings();
        //显示定位按键
        uiSettings.setMyLocationButtonEnabled(true);
        //显示缩放图标
        uiSettings.setZoomControlsEnabled(true);
        //设置为可以使用缩放手势
        uiSettings.setZoomGesturesEnabled(true);
        //设置为可以使用滑动手势
        uiSettings.setScrollGesturesEnabled(true);
        //设置为不可以使用旋转手势
        uiSettings.setRotateGesturesEnabled(false);
        //定位图标可以点击
        aMap.setMyLocationEnabled(true);
        //location();
    }

    /**
     * 设置地图标记
     *
     * @param location LatLng对象
     */
    public void addMapMark(LatLng location) {
        MarkerOptions markerOption = new MarkerOptions();
        markerOption.position(location);
        markerOption.title("UserName");
        markerOption.draggable(false);
        markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(),
                R.mipmap.ic_default_user)));
        aMap.addMarker(markerOption);
    }

    /**
     * 声明定位回调监听器
     * 可以判断AMapLocation对象不为空，当定位错误码类型为0时定位成功。
     *
     * @param aMapLocation aMapLocation对象
     */
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                //可在其中解析amapLocation获取相应内容。
                mapInfoUtils = new MapInfoUtils(aMapLocation);
                // 如果不设置标志位，此时再拖动地图时，它会不断将地图移动到当前的位置
                if (isFirstLoc) {
                    //设置缩放级别
                    aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
                    //将地图移动到定位点
                    aMap.moveCamera(CameraUpdateFactory.changeLatLng(
                            new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude())));
                    //点击定位按钮 能够将地图的中心移动到定位点
                    mListener.onLocationChanged(aMapLocation);
                    //添加图钉
                    addMapMark(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude()));
                    isFirstLoc = false;
                    //控制台输出信息
                    Log.d(TAG, "onLocationChanged: " + mapInfoUtils.toString());
                }
            } else {
                //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                Log.e(TAG, "onLocationChanged: location Error, ErrCode:" + aMapLocation.getErrorCode()
                        + ", errInfo:" + aMapLocation.getErrorInfo());
            }
        }
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
    }

    @Override
    public void deactivate() {
        mListener = null;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，实现地图生命周期管理
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，实现地图生命周期管理
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        if (aMap != null) aMap.clear();
        //停止定位后，本地定位服务并不会被销毁
        mLocationClient.stopLocation();
        //销毁定位客户端，同时销毁本地定位服务。
        mLocationClient.onDestroy();
        super.onDestroy();
    }

}