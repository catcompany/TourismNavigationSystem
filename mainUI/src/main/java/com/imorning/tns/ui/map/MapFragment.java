package com.imorning.tns.ui.map;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
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
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Poi;
import com.amap.api.navi.AmapNaviPage;
import com.amap.api.navi.AmapNaviParams;
import com.amap.api.navi.AmapNaviType;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.help.Tip;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.imorning.tns.R;
import com.imorning.tns.activity.InputTipsActivity;
import com.imorning.tns.overlay.PoiOverlay;
import com.imorning.tns.utils.Constants;
import com.imorning.tns.utils.LocationInfo;
import com.imorning.tns.utils.NaviInfoCallback;
import com.imorning.tns.utils.ToastUtil;
import com.mylhyl.circledialog.CircleDialog;
import com.mylhyl.circledialog.callback.ConfigButton;
import com.mylhyl.circledialog.callback.ConfigDialog;
import com.mylhyl.circledialog.params.ButtonParams;
import com.mylhyl.circledialog.params.DialogParams;
import com.mylhyl.circledialog.view.listener.OnLvItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author iMorning
 * <p>
 * ????????????
 */
public class MapFragment extends Fragment implements AMap.OnMarkerClickListener, AMap.InfoWindowAdapter,
        PoiSearch.OnPoiSearchListener, View.OnClickListener,
        AMapLocationListener, LocationSource {

    public static final int REQUEST_CODE = 100;
    public static final int RESULT_CODE_INPUTTIPS = 101;
    public static final int RESULT_CODE_KEYWORDS = 102;
    public static final String CURRENT_CITY = "CURRENT_CITY";

    private static final String TAG = "MapFragment";
    //??????AMapLocationClient?????????
    public AMapLocationClient mLocationClient = null;
    //????????????,???????????????
    protected AmapNaviType naviType = AmapNaviType.DRIVER;
    //??????AMapLocationClientOption??????
    protected AMapLocationClientOption mLocationOption = null;
    protected RelativeLayout searchLayout;
    //??????????????????
    protected LocationInfo currentLocationInfo = null;
    //???????????????????????????????????????????????????????????????????????????
    protected boolean isFirstLoc = true;
    //???????????????????????????????????????????????????
    protected OnLocationChangedListener locationChangedListener;
    //????????????
    protected MapView mapView;
    protected AMap mAMap;
    //???????????????????????????????????????
    protected View rootView;
    protected View goRootView;
    //??????????????????
    protected LocationInfo targetLocation = null;
    private MapViewModel mViewModel;
    // ????????????poi???????????????
    private String mKeyWords = "";
    // ??????????????????
    private ProgressDialog progDialog = null;
    // poi???????????????
    private PoiResult poiResult;
    // Poi???????????????
    private PoiSearch.Query query;
    // POI??????
    private PoiSearch poiSearch;

    private TextView mKeywordsTextView;
    //????????????????????????
    private ImageView mCleanKeyWords;

    public static MapFragment newInstance() {
        return new MapFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.map_fragment, container, false);
        searchLayout = rootView.findViewById(R.id.map_fragment_search_view);
        mapView = rootView.findViewById(R.id.mapview);
        setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initMapSDK();
        initMapView(savedInstanceState);
        init();
    }


    /**
     * ????????????poi??????
     *
     * @param keywords ?????????
     */
    public void doSearchQuery(String keywords) {
        showProgressDialog();
        int currentPage = 1;
        // ????????????????????????????????????????????????????????????poi????????????????????????????????????poi??????????????????????????????????????????
        query = new PoiSearch.Query(keywords, "", currentLocationInfo.getCity());
        // ?????????????????????????????????poiitem
        query.setPageSize(20);
        // ??????????????????
        query.setPageNum(currentPage);

        poiSearch = new PoiSearch(getContext(), query);
        poiSearch.setOnPoiSearchListener(this);
        //????????????POI
        poiSearch.searchPOIAsyn();
    }

    /**
     * ????????????????????????
     *
     * @param marker ?????????????????????????????????{@link Marker}
     * @return ...
     */
    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        return false;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    /**
     * ?????????????????????????????????
     *
     * @param marker ??????
     * @return ??????
     */

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
        // TODO: 2021-04-24 ????????????
        targetLocation = new LocationInfo(marker.getPosition().latitude, marker.getPosition().longitude, null);
        return goRootView;
    }

    /**
     * poi?????????????????????????????????????????????????????????
     *
     * @param cities ?????????????????????
     */
    private void showSuggestCity(List<SuggestionCity> cities) {
        StringBuilder infomation = new StringBuilder("????????????\n");
        for (int i = 0; i < cities.size(); i++) {
            infomation.append("????????????:").append(cities.get(i).getCityName()).append("????????????:").append(cities.get(i).getCityCode()).append("????????????:").append(cities.get(i).getAdCode()).append("\n");
        }
        ToastUtil.show(getContext(), infomation.toString());
    }


    /**
     * POI????????????????????????
     */
    @Override
    public void onPoiSearched(PoiResult result, int rCode) {
        dissmissProgressDialog();
        //rCode???1000??????????????????
        if (rCode == 1000) {
            if (result != null && result.getQuery() != null) {
                // ??????poi?????????
                if (result.getQuery().equals(query)) {
                    // ??????????????????
                    poiResult = result;
                    // ??????????????????poiitems????????????
                    ArrayList<PoiItem> poiItems = poiResult.getPois();// ??????????????????poiitem????????????????????????0??????
                    List<SuggestionCity> suggestionCities = poiResult.getSearchSuggestionCitys();// ???????????????poiitem?????????????????????????????????????????????????????????
                    if (poiItems != null && poiItems.size() > 0) {
                        // ?????????????????????
                        mAMap.clear();
                        PoiOverlay poiOverlay = new PoiOverlay(mAMap, poiItems);
                        poiOverlay.removeFromMap();
                        poiOverlay.addToMap();
                        poiOverlay.zoomToSpan();
                    } else if (suggestionCities != null && suggestionCities.size() > 0) {
                        showSuggestCity(suggestionCities);
                    } else {
                        ToastUtil.show(getContext(), R.string.no_result);
                    }
                }
            } else {
                ToastUtil.show(getContext(), R.string.no_result);
            }
        } else {
            ToastUtil.showerror(getContext(), rCode);
        }

    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int rCode) {

    }


    /**
     * ????????????activity??????????????????????????????
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        if (resultCode == RESULT_CODE_INPUTTIPS) {
            mAMap.clear();
            Tip tip = data.getParcelableExtra(Constants.EXTRA_TIP);
            if (TextUtils.isEmpty(tip.getPoiID().trim())) {
                doSearchQuery(tip.getName());
            } else {
                addTipMarker(tip);
            }
            mKeywordsTextView.setText(tip.getName());
            if (!TextUtils.isEmpty(tip.getName().trim())) {
                mCleanKeyWords.setVisibility(View.VISIBLE);
            }
        } else if (resultCode == RESULT_CODE_KEYWORDS) {
            mAMap.clear();
            String keywords = data.getStringExtra(Constants.KEY_WORDS_NAME).trim();
            if (!TextUtils.isEmpty(keywords)) {
                doSearchQuery(keywords);
            }
            mKeywordsTextView.setText(keywords);
            if (!TextUtils.isEmpty(keywords)) {
                mCleanKeyWords.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * ???marker??????????????????list????????????
     *
     * @param tip ??????
     */
    public void addTipMarker(Tip tip) {
        if (tip == null) {
            return;
        }
        Marker mPoiMarker = mAMap.addMarker(new MarkerOptions());
        LatLonPoint point = tip.getPoint();
        if (point != null) {
            LatLng markerPosition = new LatLng(point.getLatitude(), point.getLongitude());
            mPoiMarker.setPosition(markerPosition);
            mAMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerPosition, 17));
        }
        mPoiMarker.setTitle(tip.getName());
        mPoiMarker.setSnippet(tip.getAddress());
    }

    /**
     * ??????????????????
     *
     * @param location LatLng??????
     */
    public void addMapMark(LatLng location) {
        MarkerOptions markerOption = new MarkerOptions();
        //???????????????
        markerOption.position(location);
        //???????????????
        markerOption.title(getString(R.string.my_location));
        markerOption.draggable(false);
        //???????????????
        markerOption.icon(BitmapDescriptorFactory.fromBitmap(
                BitmapFactory.decodeResource(getResources(), R.mipmap.ic_location)));
        mAMap.addMarker(markerOption);
    }

    /**
     * ????????????????????????
     *
     * @param v ??????????????????
     */
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_keywords:
                Intent intent = new Intent(getActivity(), InputTipsActivity.class);
                intent.putExtra(CURRENT_CITY, currentLocationInfo.getCity());
                startActivityForResult(intent, REQUEST_CODE);
                break;
            //?????????????????????
            case R.id.clean_keywords:
                mKeywordsTextView.setText("");
                mAMap.clear();
                mCleanKeyWords.setVisibility(View.GONE);
                break;
            //?????????????????????????????????
            case R.id.poi_keyword_uri_go:
                startNavi(currentLocationInfo, targetLocation, null);
                break;
            default:
                break;
        }
    }

    /**
     * ??????????????????
     *
     * @param startLocation ?????????
     * @param endLocation   ?????????
     * @param wayList       ???????????????????????? {@link List<Poi>}
     */
    protected void startNavi(LocationInfo startLocation, LocationInfo endLocation, List<Poi> wayList) {
        if (startLocation == null) {
            ToastUtil.show(getContext(), getString(R.string.error_start));
            return;
        }
        if (endLocation == null) {
            ToastUtil.show(getContext(), getString(R.string.error_traget_location));
            return;
        }
        LatLng startLatLng = new LatLng(currentLocationInfo.getLatitudel(), currentLocationInfo.getLongitude());
        LatLng endLatLng = new LatLng(targetLocation.getLatitudel(), targetLocation.getLongitude());
        if (startLatLng.equals(endLatLng)) {
            ToastUtil.show(getContext(), getString(R.string.start_is_target));
            return;
        }
        final String[] items = {"??????", "??????", "??????"};
        new CircleDialog.Builder()
                .configDialog(params -> {
                    //??????????????????
                    params.animStyle = R.style.dialogWindowAnim;
                })
                .setTitle("??????????????????")
                .setTitleColor(Color.BLUE)
                .setItems(items, (parent, view, position, id) -> {
                    switch (position) {
                        case 0:
                            naviType = AmapNaviType.DRIVER;
                            break;
                        case 1:
                            naviType = AmapNaviType.WALK;
                            break;
                        case 2:
                            naviType = AmapNaviType.RIDE;
                            break;
                        default:
                            naviType = AmapNaviType.DRIVER;
                            break;
                    }
                    AmapNaviParams params = new AmapNaviParams(new Poi(currentLocationInfo.getCity(), startLatLng, ""), wayList,
                            new Poi("", endLatLng, ""), naviType);
                    params.setUseInnerVoice(true);
                    AmapNaviPage amapNaviPage = AmapNaviPage.getInstance();
                    amapNaviPage.showRouteActivity(getContext(), params, new NaviInfoCallback(getContext()));
                    return true;
                })
                .setNegative(getString(android.R.string.cancel), null)
                .configNegative(params -> {
                    //????????????????????????
                    params.textColor = Color.RED;
                })
                .show(getParentFragmentManager());
    }

    /**
     * ???????????????SDK
     */
    public void initMapSDK() {
        //???????????????
        mLocationClient = new AMapLocationClient(requireContext());
        //????????????????????????
        mLocationClient.setLocationListener(this);
        //?????????AMapLocationClientOption??????
        mLocationOption = new AMapLocationClientOption();
        //?????????????????????????????????????????????????????????????????????????????????????????????
        mLocationOption.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.SignIn);
        //?????????????????????AMapLocationMode.Hight_Accuracy?????????????????????
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //??????????????????
        mLocationOption.setLocationCacheEnable(false);
        //????????????????????????,??????????????????false???
        mLocationOption.setOnceLocation(true);
        //????????????3s????????????????????????????????????,??????setOnceLocationLatest(boolean b)?????????true??????????????????SDK???????????????3s???????????????????????????????????????
        //??????????????????true???setOnceLocation(boolean b)????????????????????????true???????????????????????????false???
        //mLocationOption.setOnceLocationLatest(true);
        //??????????????????,????????????,?????????2000ms?????????1000ms???
        mLocationOption.setInterval(1000);
        //????????????????????????????????????????????????????????????
        mLocationOption.setNeedAddress(true);
        if (null != mLocationClient) {
            mLocationClient.setLocationOption(mLocationOption);
            //???????????????????????????????????????stop????????????start???????????????????????????
            mLocationClient.stopLocation();
            mLocationClient.startLocation();
        }
    }

    /**
     * ?????????MapView??????????????????
     *
     * @param savedInstanceState ???????????????
     */
    public void initMapView(Bundle savedInstanceState) {
        mapView.onCreate(savedInstanceState);// ?????????????????????
        mAMap = mapView.getMap();
        // ????????????????????????
        mAMap.setTrafficEnabled(true);
        //???????????????????????????MAP_TYPE_NORMAL,MAP_TYPE_SATELLITE,MAP_TYPE_NIGHT
        mAMap.setMapType(AMap.MAP_TYPE_NORMAL);
        mAMap.setLocationSource(this);
        UiSettings uiSettings = mAMap.getUiSettings();
        //??????????????????
        uiSettings.setMyLocationButtonEnabled(false);
        //??????????????????
        uiSettings.setZoomControlsEnabled(true);
        //?????????????????????????????????
        uiSettings.setZoomGesturesEnabled(true);
        //?????????????????????????????????
        uiSettings.setScrollGesturesEnabled(true);
        //????????????????????????????????????
        uiSettings.setRotateGesturesEnabled(false);
        //????????????????????????
        mAMap.setMyLocationEnabled(true);
        //???????????????????????????
        currentLocationInfo = new LocationInfo(Constants.XIAN.latitude, Constants.XIAN.longitude, Constants.DEFAULT_CITY);
    }

    /**
     * ?????????????????????
     */
    public void init() {
        mKeyWords = "";
        mViewModel = new ViewModelProvider(this).get(MapViewModel.class);
        mCleanKeyWords = rootView.findViewById(R.id.clean_keywords);
        mCleanKeyWords.setOnClickListener(this);
        mKeywordsTextView = rootView.findViewById(R.id.main_keywords);
        mKeywordsTextView.setOnClickListener(this);
        mAMap.setOnMarkerClickListener(this);// ????????????marker????????????
        mAMap.setInfoWindowAdapter(this);// ????????????infowindow????????????
        mAMap.getUiSettings().setRotateGesturesEnabled(false);
    }

    /**
     * ???????????????????????????
     * ????????????AMapLocation?????????????????????????????????????????????0??????????????????
     *
     * @param aMapLocation aMapLocation??????
     */
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                //??????????????????amapLocation?????????????????????
                currentLocationInfo = new LocationInfo(aMapLocation);
                // ???????????????????????????????????????????????????????????????????????????????????????????????????
                if (isFirstLoc) {
                    //??????????????????
                    mAMap.moveCamera(CameraUpdateFactory.zoomTo(17));
                    //???????????????????????????
                    mAMap.moveCamera(CameraUpdateFactory.changeLatLng(
                            new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude())));
                    //?????????????????? ??????????????????????????????????????????
                    locationChangedListener.onLocationChanged(aMapLocation);
                    //????????????
                    addMapMark(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude()));
                    isFirstLoc = false;
                }
            } else {
                //???????????????????????????ErrCode????????????????????????????????????????????????errInfo???????????????????????????????????????
                Log.e(TAG, "onLocationChanged: location Error, ErrCode:" + aMapLocation.getErrorCode()
                        + ", errInfo:" + aMapLocation.getErrorInfo());
            }
        }
    }


    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        locationChangedListener = onLocationChangedListener;
    }

    @Override
    public void deactivate() {
        locationChangedListener = null;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        //???activity??????onResume?????????mMapView.onResume ()?????????????????????????????????
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        //???activity??????onPause?????????mMapView.onPause ()?????????????????????????????????
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        if (mAMap != null) mAMap.clear();
        //??????????????????????????????????????????????????????
        mLocationClient.stopLocation();
        //?????????????????????????????????????????????????????????
        mLocationClient.onDestroy();
        super.onDestroy();
    }

    /**
     * ???????????????
     */
    public void showProgressDialog() {
        progDialog = new ProgressDialog(getContext());
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDialog.setIndeterminate(false);
        progDialog.setCancelable(false);
        progDialog.setMessage("????????????:\n" + mKeyWords);
        progDialog.show();
    }

    /**
     * ???????????????
     */
    public void dissmissProgressDialog() {
        if (progDialog != null) {
            progDialog.dismiss();
        }
    }

    public void showNaviTypeChooser() {
        //??????item
        final String[] items3 = new String[]{"??????1", "??????2", "??????3", "??????4", "??????5", "??????6", "??????7", "??????8"};
        AlertDialog alertDialog3 = new AlertDialog.Builder(requireContext())
                .setTitle("????????????")
                .setIcon(R.mipmap.ic_launcher)
                .setItems(items3, new DialogInterface.OnClickListener() { //????????????
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(requireContext(), "????????????" + items3[i], Toast.LENGTH_SHORT).show();
                    }
                })
                .create();
        alertDialog3.show();

    }
}