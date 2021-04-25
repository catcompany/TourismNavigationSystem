package com.imorning.senseinfohelper.utils;


public class APIInterface {

    private String API_ID;
    private String API_KEY;

    public APIInterface(String apiID, String apiKey) {
        if (apiID == null || apiKey == null)
            throw new NullPointerException("api id or api key is null");
        this.API_ID = apiID;
        this.API_KEY = apiKey;
    }


    public String API_GET_PROVINCE_ID_HOST() {
        return String.format("https://route.showapi.com/268-2?showapi_appid=%s&showapi_sign=%s"
                , API_ID, API_KEY);
    }

    public String API_GET_CITY_ID_HOST() {
        return String.format("https://route.showapi.com/268-3?showapi_appid=%s&showapi_sign=%s"
                , API_ID, API_KEY);
    }

    public String API_GET_AREA_ID_HOST() {
        return String.format("https://route.showapi.com/268-4?showapi_appid=%s&showapi_sign=%s"
                , API_ID, API_KEY);
    }


}
