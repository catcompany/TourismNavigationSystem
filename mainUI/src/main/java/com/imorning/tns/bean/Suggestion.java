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
public class Suggestion {

    private List<String> keywords;
    private List<String> cities;
    public void setKeywords(List<String> keywords) {
         this.keywords = keywords;
     }
     public List<String> getKeywords() {
         return keywords;
     }

    public void setCities(List<String> cities) {
         this.cities = cities;
     }
     public List<String> getCities() {
         return cities;
     }

}