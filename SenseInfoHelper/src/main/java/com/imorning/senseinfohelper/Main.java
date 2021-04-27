package com.imorning.senseinfohelper;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.imorning.senseinfohelper.bean.scenedata.PicList;
import com.imorning.senseinfohelper.bean.scenedata.Price;
import com.imorning.senseinfohelper.bean.scenedata.PriceBean;
import com.imorning.senseinfohelper.bean.scenedata.SenseInfoContentList;
import com.imorning.senseinfohelper.bean.scenedata.SenseResBody;
import com.imorning.senseinfohelper.utils.Constants;
import com.imorning.senseinfohelper.utils.FileUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    private static final String filePath = "G:\\Projects\\AndroidStudioProjects\\TourismNavigationSystem\\SenseInfoHelper\\test_data.json";
    private static final String keyword = "白塔山";

    public static void main(String[] args) {
        SenseInfoHelper senseInfoHelper = new SenseInfoHelper();
        Map<String, String> map = new HashMap<>();
        map.put("keyword", keyword);
        String data = FileUtils.txt2String(new File(filePath));
        JSONObject jsonObject = JSON.parseObject(data);
        SenseResBody senseResBody = jsonObject.getObject(Constants.SHOWAPI_RES_BODY, SenseResBody.class);
        List<SenseInfoContentList> senseInfoContentLists = senseResBody.getPagebean().getContentList();
        for (SenseInfoContentList content : senseInfoContentLists) {
            System.out.println("景点名称" + content.getName());
            System.out.println("简介：" + content.getSummary());
            System.out.println("开业日期：" + content.getCt());
            System.out.println("省：" + content.getProName());
            System.out.println("市：" + content.getCityName());
            System.out.println("区：" + content.getAreaName());
            System.out.println("地址：" + content.getAddress());
            System.out.println("注意事项：" + content.getAttention());
            System.out.println("营业时间：" + content.getOpentime());
            System.out.println("免票政策：" + content.getCoupon());
            if (content.getPriceList() != null) {
                System.out.println("价格列表：");
                List<PriceBean> priceBeans = content.getPriceList();
                for (PriceBean priceBean : priceBeans) {
                    System.out.println("门票类型：" + priceBean.getType());
                    List<Price> prices = priceBean.getEntityList();
                    for (Price price : prices) {
                        System.out.println("门票名称：" + price.getTicketName());
                        System.out.println("零售价：" + price.getAmount());
                        System.out.println("建议零售价：" + price.getAmountAdvice());
                    }
                }
            }
            if (content.getLocation() != null) {
                System.out.println("经纬度:" + content.getLocation().toString());
            }
            if (content.getPriceList().size() > 0) {
                System.out.println("图片列表：");
                for (PicList picUrl : content.getPicList()) {
                    System.out.println(picUrl);
                }
            } else {
                System.out.println("没得图片");
            }
        }
    }


}
