/**
  * Copyright 2021 json.cn 
  */
package com.imorning.senseinfohelper.bean.citydata.area;
import java.util.List;

/**
 * Auto-generated: 2021-04-25 10:56:22
 *
 * @author iMorning
 *
 */
public class AreaResBody {

    private int ret_code;
    private List<AreaList> list;
    public void setRet_code(int ret_code) {
         this.ret_code = ret_code;
     }
     public int getRet_code() {
         return ret_code;
     }

    public void setList(List<AreaList> list) {
         this.list = list;
     }
     public List<AreaList> getList() {
         return list;
     }

}