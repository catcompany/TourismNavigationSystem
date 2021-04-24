package com.imorning.tns.utils;

import android.text.TextUtils;

import com.amap.api.location.AMapLocation;

import org.jetbrains.annotations.NotNull;

public class LocationInfo {
    private final double latitudel;
    private final double longitude;
    private String city = null;
    private AMapLocation amapLocation;
    private float accuracy;
    private String address;
    private String country;
    private String province;
    private String district;
    private String street;
    private String streetNum;
    private String cityCode;
    private String aoiName;
    private String buildingId;
    private String floor;
    private int locationType;
    private int gpsAccuracyStatus;
    private String adCode;

    /**
     * 记录位置信息
     *
     * @param aMapLocation AMapLocation对象
     */
    public LocationInfo(AMapLocation aMapLocation) {
        if (aMapLocation == null) {
            throw new NullPointerException("AMapLocation should not be a null object!");
        }
        this.amapLocation = aMapLocation;
        latitudel = amapLocation.getLatitude();
        longitude = amapLocation.getLongitude();
        accuracy = amapLocation.getAccuracy();
        address = amapLocation.getAddress();
        country = amapLocation.getCountry();
        province = amapLocation.getProvince();
        city = amapLocation.getCity();
        district = amapLocation.getDistrict();
        street = amapLocation.getStreet();
        streetNum = amapLocation.getStreetNum();
        cityCode = amapLocation.getCityCode();
        aoiName = amapLocation.getAoiName();
        buildingId = amapLocation.getBuildingId();
        floor = amapLocation.getFloor();
        locationType = amapLocation.getLocationType();
        gpsAccuracyStatus = amapLocation.getGpsAccuracyStatus();
        adCode = amapLocation.getAdCode();
    }

    /**
     * 记录位置信息
     *
     * @param latitudel 经度
     * @param longitude 维度
     * @param city      城市
     */
    public LocationInfo(double latitudel, double longitude, String city) {
        if (city == null) city = "";
        this.latitudel = latitudel;
        this.longitude = longitude;
        this.city = city;
    }

    public AMapLocation getAmapLocation() {
        return amapLocation;
    }

    /**
     * 获取维度
     *
     * @return 维度
     */
    public double getLatitudel() {
        if (latitudel == 0)
            return Constants.XIAN.latitude;
        return latitudel;
    }

    /**
     * 获取经度
     *
     * @return 经度
     */
    public double getLongitude() {
        if (longitude == 0)
            return Constants.XIAN.longitude;
        return longitude;
    }

    /**
     * 获取精度信息
     *
     * @return 精度
     */
    public float getAccuracy() {
        return accuracy;
    }

    /**
     * 获取地址
     *
     * @return 地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
     */
    public String getAddress() {
        return address;
    }

    /**
     * 国家
     *
     * @return 国家
     */
    public String getCountry() {
        return country;
    }

    /**
     * 省份
     *
     * @return 省份
     */
    public String getProvince() {
        return province;
    }

    /**
     * 城市
     *
     * @return 城市
     */
    public String getCity() {
        if (TextUtils.isEmpty(city) || city == null)
            return Constants.DEFAULT_CITY;
        return city;
    }

    /**
     * 城区
     *
     * @return 城区
     */
    public String getDistrict() {
        return district;
    }

    /**
     * 街道
     *
     * @return 街道
     */
    public String getStreet() {
        return street;
    }

    /**
     * 街道，门牌号信息
     *
     * @return 门牌号
     */
    public String getStreetNum() {
        return streetNum;
    }

    /**
     * 城市代码
     *
     * @return 城市代码
     */
    public String getCityCode() {
        return cityCode;
    }

    /**
     * 地区编码
     *
     * @return 地区编码
     */
    public String getAdCode() {
        return adCode;
    }

    /**
     * 获取当前定位点的AOI信息
     *
     * @return 当前定位点的AOI信息
     */
    public String getAoiName() {
        return aoiName;
    }

    /**
     * 获取建筑物编号
     *
     * @return 建筑物编号
     */
    public String getBuildingId() {
        return buildingId;
    }

    /**
     * 获取楼层信息
     *
     * @return 楼层
     */
    public String getFloor() {
        return floor;
    }

    /**
     * 获取当前定位结果来源，如网络定位结果，详见定位类型表
     *
     * @return 定位结果来源
     */
    public int getLocationType() {
        return locationType;
    }

    /**
     * 获取GPS的当前状态
     *
     * @return 获取GPS的当前状态
     */
    public int getGpsAccuracyStatus() {
        return gpsAccuracyStatus;
    }


    @NotNull
    @Override
    public String toString() {
        return "LocationInfo{" +
                "  latitudel=" + latitudel +
                ", longitude=" + longitude +
                ", accuracy=" + accuracy +
                ", address='" + address + '\'' +
                ", country='" + country + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", district='" + district + '\'' +
                ", street='" + street + '\'' +
                ", streetNum='" + streetNum + '\'' +
                ", cityCode='" + cityCode + '\'' +
                ", adCode='" + adCode + '\'' +
                ", aoiName='" + aoiName + '\'' +
                ", buildingId='" + buildingId + '\'' +
                ", floor='" + floor + '\'' +
                ", locationType=" + locationType +
                ", gpsAccuracyStatus=" + gpsAccuracyStatus +
                '}';
    }
}
