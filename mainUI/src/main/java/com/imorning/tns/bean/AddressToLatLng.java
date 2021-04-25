/**
 * Copyright 2021 json.cn
 */
package com.imorning.tns.bean;

import com.alibaba.fastjson.JSONObject;
import com.amap.api.maps.model.LatLng;
import com.imorning.senseinfohelper.utils.HttpUtils;

import java.util.List;

/**
 * Auto-generated: 2021-04-25 21:47:37
 *
 * @author json.cn (i@json.cn)
 * @website http://www.json.cn/java2pojo/
 */
public class AddressToLatLng {

    private String status;
    private String info;
    private String infocode;
    private String count;
    private Suggestion suggestion;
    private List<Districts> districts;

    public static LatLng Main(String keywords) {
        String data = HttpUtils.request("https://restapi.amap.com/v3/config/district")
                .param("keywords", keywords)
                .param("key", "4fc3b663178663c02307bc8c4bdf6dc2")
                .get();
        JSONObject jsonObject = JSONObject.parseObject(data);
        if (!jsonObject.getObject("", AddressToLatLng.class).getInfo().equals("OK"))
            return null;
        Districts districts = jsonObject.getObject("districts", Districts.class);
        String center = districts.getCenter();
        String[] split = center.split(",");
        return new LatLng(Double.parseDouble(split[0].trim()), Double.parseDouble(split[1].trim()));
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getInfocode() {
        return infocode;
    }

    public void setInfocode(String infocode) {
        this.infocode = infocode;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public Suggestion getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(Suggestion suggestion) {
        this.suggestion = suggestion;
    }

    public List<Districts> getDistricts() {
        return districts;
    }

    public void setDistricts(List<Districts> districts) {
        this.districts = districts;
    }
}