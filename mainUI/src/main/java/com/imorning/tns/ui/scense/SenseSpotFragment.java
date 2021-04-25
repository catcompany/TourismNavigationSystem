package com.imorning.tns.ui.scense;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Poi;
import com.amap.api.navi.AmapNaviPage;
import com.amap.api.navi.AmapNaviParams;
import com.amap.api.navi.AmapNaviType;
import com.imorning.senseinfohelper.SenseInfoHelper;
import com.imorning.senseinfohelper.bean.scenedata.SenseInfoContentList;
import com.imorning.senseinfohelper.bean.scenedata.SenseResBody;
import com.imorning.senseinfohelper.utils.Constants;
import com.imorning.tns.R;
import com.imorning.tns.activity.SenseInfoActivity;
import com.imorning.tns.ui.map.MapFragment;
import com.imorning.tns.utils.LocationInfo;
import com.imorning.tns.utils.NaviInfoCallback;
import com.imorning.tns.utils.ToastUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kotlin.collections.MapsKt;

public class SenseSpotFragment extends MapFragment implements AMapLocationListener, OnClickListener {
    private static final String TAG = "SenseSpotFragment";
    private static final String KEYWORD = "keyword";
    private static final String PROID = "proId";
    private static final String CITYID = "cityId";
    private static final String AREAID = "reaId";
    protected Handler mHandler = new Handler();
    private double lat, lng;
    private SenseInfoHelper senseInfoHelper;
    private List<SenseInfoContentList> contents;
    private JSONObject jsonObject;
    private SenseResBody senseResBody;
    private String name;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        searchLayout.setVisibility(View.GONE);
        senseInfoHelper = new SenseInfoHelper();
    }

    private void startShowSense(String keyword, String province, String city, String area) {
        Map<String, String> map = new HashMap<>();
        if (keyword != null)
            map.put(KEYWORD, keyword);
        if (province != null) {
            String proId = senseInfoHelper.getProvinceId(province.replaceAll("省", ""));
            map.put(PROID, proId);
            if (city != null) {
                map.put(CITYID, senseInfoHelper.getCityId(proId, city.replaceAll("市", "")));
            }
        }
        new Thread() {
            //在新线程中发送网络请求
            public void run() {
                //获取附近景点信息
                String data = senseInfoHelper.getSceneInfomation(map);
                //把返回内容通过handler对象更新到界面
                mHandler.post(new Thread() {
                    public void run() {
                        jsonObject = JSON.parseObject(data);
                        senseResBody = jsonObject.getObject(Constants.SHOWAPI_RES_BODY, SenseResBody.class);
                        if (senseResBody.getRet_code() != 0) {
                            return;
                        }
                        contents = senseResBody.getPagebean().getContentList();
                        if (contents == null) return;
                        for (SenseInfoContentList content : contents) {
                            lat = Double.parseDouble(content.getLocation().getLat());
                            lng = Double.parseDouble(content.getLocation().getLon());
                            addSceneMark(new LatLng(lat, lng), content.getName(), content);
                        }
                    }
                });
            }
        }.start();
    }

    /**
     * 添加标记点
     *
     * @param location 经纬度
     * @param title    标题
     * @param content  类
     */

    public void addSceneMark(LatLng location, String title, SenseInfoContentList content) {
        MarkerOptions markerOption = new MarkerOptions();
        markerOption.position(location);
        markerOption.title(title);
        markerOption.draggable(false);
        markerOption.icon(BitmapDescriptorFactory.fromBitmap(
                BitmapFactory.decodeResource(getResources(), R.mipmap.ic_scene_mark)));
        mAMap.addMarker(markerOption);
    }

    @Override
    public View getInfoWindow(final Marker marker) {
        goRootView = getLayoutInflater().inflate(R.layout.poikeywordsearch_uri, null);
        AppCompatTextView title = goRootView.findViewById(R.id.poi_keyword_uri_title);
        AppCompatTextView snippet = goRootView.findViewById(R.id.poi_keyword_uri_snippet);
        AppCompatButton go_button = goRootView.findViewById(R.id.poi_keyword_uri_go);
        AppCompatButton moreinfo_button = goRootView.findViewById(R.id.poi_keyword_more);
        snippet.setText(marker.getSnippet());
        title.setText(marker.getTitle());
        go_button.setOnClickListener(this);
        moreinfo_button.setOnClickListener(this);
        moreinfo_button.setVisibility(View.VISIBLE);
        targetLocation = new LocationInfo(marker.getPosition().latitude, marker.getPosition().longitude, null);
        name = marker.getTitle();
        return goRootView;
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
                currentLocationInfo = new LocationInfo(aMapLocation);
                // 如果不设置标志位，此时再拖动地图时，它会不断将地图移动到当前的位置
                if (isFirstLoc) {
                    isFirstLoc = false;
                    //设置缩放级别
                    mAMap.moveCamera(CameraUpdateFactory.zoomTo(10));
                    //将地图移动到定位点
                    mAMap.moveCamera(CameraUpdateFactory.changeLatLng(
                            new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude())));
                    //点击定位按钮 能够将地图的中心移动到定位点
                    locationChangedListener.onLocationChanged(aMapLocation);
                    //添加图钉
                    addMapMark(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude()));
                    //开始绘制景点地标
                    startShowSense(null,
                            aMapLocation.getProvince(),
                            aMapLocation.getCity(),
                            aMapLocation.getCountry());
                }
            } else {
                //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                Log.e(TAG, "onLocationChanged: location Error, ErrCode:" + aMapLocation.getErrorCode()
                        + ", errInfo:" + aMapLocation.getErrorInfo());
            }
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //点击导航（去这儿）按钮
            case R.id.poi_keyword_uri_go:
                if (currentLocationInfo == null) {
                    ToastUtil.show(getContext(), "起点信息错误");
                    break;
                }
                if (targetLocation == null) {
                    ToastUtil.show(getContext(), "目标地点信息错误");
                    break;
                }
                LatLng startLatLng = new LatLng(currentLocationInfo.getLatitudel(), currentLocationInfo.getLongitude());
                LatLng endLatLng = new LatLng(targetLocation.getLatitudel(), targetLocation.getLongitude());
                if (startLatLng.equals(endLatLng)) {
                    ToastUtil.show(getContext(), "起点和终点不能相同！");
                    break;
                }
                List<Poi> wayList = null; //必须经过的点
                AmapNaviParams params = new AmapNaviParams(new Poi(currentLocationInfo.getCity(), startLatLng, ""), wayList,
                        new Poi("", endLatLng, ""), AmapNaviType.DRIVER);
                params.setUseInnerVoice(true);
                AmapNaviPage amapNaviPage = AmapNaviPage.getInstance();
                amapNaviPage.showRouteActivity(getContext(), params, new NaviInfoCallback(getContext()));
                break;
            case R.id.poi_keyword_more:
                Intent intent = new Intent(requireContext(), SenseInfoActivity.class);
                intent.putExtra(SenseInfoActivity.SCENE_NAME, name);
                intent.putExtra(SenseInfoActivity.CITY,targetLocation.getCity());
                startActivity(intent);
                break;
            default:
                break;
        }
        //super.onClick(v);
    }
}