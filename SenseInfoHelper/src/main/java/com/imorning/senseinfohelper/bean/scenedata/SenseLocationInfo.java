/**
 * Copyright 2021 json.cn
 */
package com.imorning.senseinfohelper.bean.scenedata;

import java.io.Serializable;

/**
 * 景点的位置信息
 *
 * @author iMorning
 */
public class SenseLocationInfo implements Serializable {

    private static final long serialVersionUID = 973996466037013850L;
    private String lon;
    private String lat;

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    @Override
    public String toString() {
        return "SenseLocationInfo{" +
                "lon='" + lon + '\'' +
                ", lat='" + lat + '\'' +
                '}';
    }
}