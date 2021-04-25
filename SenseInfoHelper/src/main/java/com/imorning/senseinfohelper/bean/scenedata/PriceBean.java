/**
 * Copyright 2021 json.cn
 */
package com.imorning.senseinfohelper.bean.scenedata;

import java.util.List;

/**
 * 门票Bean
 *
 * @author iMorning
 */
public class PriceBean {

    private List<Price> entityList;
    /**
     * 门票类型
     */
    private String type;

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