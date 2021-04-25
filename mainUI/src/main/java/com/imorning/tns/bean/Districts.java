/**
  * Copyright 2021 json.cn 
  */
package com.imorning.tns.bean;
import java.util.List;

/**
 * Auto-generated: 2021-04-25 21:47:37
 *
 * @author json.cn (i@json.cn)
 * @website http://www.json.cn/java2pojo/
 */
public class Districts {

    private String citycode;
    private String adcode;
    private String name;
    private String center;
    private String level;
    private List<Districts> districts;
    public void setCitycode(String citycode) {
         this.citycode = citycode;
     }
     public String getCitycode() {
         return citycode;
     }

    public void setAdcode(String adcode) {
         this.adcode = adcode;
     }
     public String getAdcode() {
         return adcode;
     }

    public void setName(String name) {
         this.name = name;
     }
     public String getName() {
         return name;
     }

    public void setCenter(String center) {
         this.center = center;
     }
     public String getCenter() {
         return center;
     }

    public void setLevel(String level) {
         this.level = level;
     }
     public String getLevel() {
         return level;
     }

    public void setDistricts(List<Districts> districts) {
         this.districts = districts;
     }
     public List<Districts> getDistricts() {
         return districts;
     }

}