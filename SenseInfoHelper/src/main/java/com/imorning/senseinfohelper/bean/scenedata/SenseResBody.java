/**
 * Copyright 2021 json.cn
 */
package com.imorning.senseinfohelper.bean.scenedata;


/**
 * @author iMorning
 *
 */
public class SenseResBody {

    private int ret_code;
    private String remark;
    private PageBean pagebean;

    public int getRet_code() {
        return ret_code;
    }

    public void setRet_code(int ret_code) {
        this.ret_code = ret_code;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public PageBean getPagebean() {
        return pagebean;
    }

    public void setPagebean(PageBean pagebean) {
        this.pagebean = pagebean;
    }

}
