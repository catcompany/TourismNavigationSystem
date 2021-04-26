/**
 * Copyright 2021 json.cn
 */
package com.imorning.senseinfohelper.bean.scenedata;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

/**
 * 门票Bean
 *
 * @author iMorning
 */
public class PriceBean implements Serializable {


    private static final long serialVersionUID = 7290539357496743254L;
    private List<Price> entityList;
    /**
     * 门票类型
     */
    private String type;

    protected PriceBean(Parcel in) {
        type = in.readString();
    }

    public PriceBean() {
    }

    public List<Price> getEntityList() {
        return entityList;
    }

    public void setEntityList(List<Price> price) {
        this.entityList = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


}