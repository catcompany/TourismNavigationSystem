/**
 * Copyright 2021 json.cn
 */
package com.imorning.senseinfohelper.bean.scenedata;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Auto-generated: 2021-04-24 16:59:43
 *
 * @author iMorning
 */
public class SenseInfoContentList implements Serializable {


    private static final long serialVersionUID = 3303852908997390800L;
    /**
     * 省级id
     */
    private String proId;
    /**
     * 景点描述
     */
    private String summary;
    /**
     * 景点的位置信息（经纬度）
     */
    private SenseLocationInfo location;
    /**
     * 景点所处市的id
     */
    private String cityId;
    /**
     * 市名
     */
    private String cityName;
    /**
     * 门票价格列表
     */
    private List<PriceBean> priceList;
    /**
     * 区/镇的id
     */
    private String areaId;
    /**
     * 建立时间
     */
    private Date ct;
    /**
     * 景点id
     */
    private String id;
    /**
     * 省名
     */
    private String proName;
    /**
     * 区名/镇名
     */
    private String areaName;
    /**
     * 景点地址（文字描述）
     */
    private String address;
    /**
     * 景点名称
     */
    private String name;
    /**
     * 注意事项
     */
    private String attention;
    /**
     * 开放时间
     */
    private String opentime;
    /**
     * 免票政策
     */
    private String coupon;
    /**
     * 图片列表
     */
    private List<PicList> picList;

    public SenseInfoContentList() {
        super();

    }


    public String getProId() {
        return proId;
    }

    public void setProId(String proId) {
        this.proId = proId;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public SenseLocationInfo getLocation() {
        return location;
    }

    public void setLocation(SenseLocationInfo location) {
        this.location = location;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public List<PriceBean> getPriceList() {
        return priceList;
    }

    public void setPriceList(List<PriceBean> priceList) {
        this.priceList = priceList;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public Date getCt() {
        return ct;
    }

    public void setCt(Date ct) {
        this.ct = ct;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAttention() {
        return attention;
    }

    public void setAttention(String attention) {
        this.attention = attention;
    }

    public String getOpentime() {
        return opentime;
    }

    public void setOpentime(String opentime) {
        this.opentime = opentime;
    }

    public String getCoupon() {
        return coupon;
    }

    public void setCoupon(String coupon) {
        this.coupon = coupon;
    }

    public List<PicList> getPicList() {
        return picList;
    }

    public void setPicList(List<PicList> picList) {
        this.picList = picList;
    }

}
