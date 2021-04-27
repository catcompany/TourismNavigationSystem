package com.imorning.senseinfohelper;

import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Process;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.imorning.senseinfohelper.bean.citydata.area.AreaList;
import com.imorning.senseinfohelper.bean.citydata.area.AreaResBody;
import com.imorning.senseinfohelper.bean.citydata.city.CityList;
import com.imorning.senseinfohelper.bean.citydata.city.CityResBody;
import com.imorning.senseinfohelper.bean.citydata.province.ProvinceList;
import com.imorning.senseinfohelper.bean.citydata.province.ProvinceResBody;
import com.imorning.senseinfohelper.utils.APIInterface;
import com.imorning.senseinfohelper.utils.Constants;
import com.imorning.senseinfohelper.utils.HttpUtils;
import com.imorning.senseinfohelper.utils.JniUtils;
import com.imorning.senseinfohelper.utils.StringUtils;

import java.lang.annotation.Target;
import java.util.Map;

/**
 * 获取周边景点的API接口帮助类
 *
 * @author iMorning
 */
public class SenseInfoHelper extends JniUtils {
    private static String API_KEY = getKey(BuildConfig.LIBRARY_PACKAGE_NAME);
    private static String API_ID = getApiId(API_KEY);
    private static APIInterface apiInterface;

    public SenseInfoHelper(String API_ID, String API_KEY) {
        SenseInfoHelper.API_ID = API_ID;
        SenseInfoHelper.API_KEY = API_KEY;
        apiInterface = new APIInterface(API_ID, API_KEY);
    }

    public SenseInfoHelper() {
        apiInterface = new APIInterface(API_ID, API_KEY);
    }

    /**
     * 查询各省或直辖市对应的ID
     *
     * @param provinceName 省名
     * @return 省对应的id
     */
    public String getProvinceId(String provinceName) {
        String httpData = HttpUtils
                .request(apiInterface.API_GET_PROVINCE_ID_HOST())
                .get();
        JSONObject jsonObject = JSON.parseObject(httpData);
        ProvinceResBody areaResBody = jsonObject.getObject(Constants.SHOWAPI_RES_BODY, ProvinceResBody.class);
        for (ProvinceList provinceList : areaResBody.getList()) {
            if (provinceList.getName().equals(provinceName))
                return provinceList.getId();
        }
        return null;
    }

    /**
     * 查询省或直辖市的下级区域ID
     *
     * @param provinceCode 省级id
     * @param cityName     城市名
     * @return 城市id
     */
    public String getCityId(String provinceCode, String cityName) {
        if (StringUtils.isEmpty(provinceCode)) {
            return null;
        }
        String httpData = HttpUtils
                .request(apiInterface.API_GET_CITY_ID_HOST())
                .param("proId", provinceCode)
                .get();
        JSONObject jsonObject = JSON.parseObject(httpData);
        CityResBody cityResBody = jsonObject.getObject(Constants.SHOWAPI_RES_BODY, CityResBody.class);
        for (CityList cityList : cityResBody.getList()) {
            if (cityList.getName().equals(cityName))
                return cityList.getId();
        }
        return null;
    }

    /**
     * 根据市的id获取地区代码
     *
     * @param cityCode 市id
     * @param areaName 地区名字
     * @return 区域id
     */
    public String getAreaId(String cityCode, String areaName) {
        if (StringUtils.isEmpty(cityCode)) return null;
        String httpData = HttpUtils
                .request(apiInterface.API_GET_AREA_ID_HOST())
                .get();
        JSONObject jsonObject = JSON.parseObject(httpData);
        AreaResBody areaResBody = jsonObject.getObject(Constants.SHOWAPI_RES_BODY, AreaResBody.class);
        for (AreaList areaList : areaResBody.getList()) {
            if (areaList.getCityId().equals(areaName)) {
                return areaList.getId();
            }
        }
        return null;
    }

    /**
     * 获取景点信息
     *
     * @param param 参数
     *              <li>keyword	景点名称的汉字</li>
     *              <li>proId 省级id</li>
     *              <li>cityId 城市id</li>
     *              <li>reaId 镇id</li>a
     *              <li>page	String 第几页</li>
     * @return 景点信息
     */
    public String getSceneInfomation(Map<String, String> param) {
        return HttpUtils
                .request(Constants.SENSE_INFO_HOST)
                .param(Constants.API_ID, API_ID)
                .param(Constants.API_KEY, API_KEY)
                .params(param)
                .get();
    }

}