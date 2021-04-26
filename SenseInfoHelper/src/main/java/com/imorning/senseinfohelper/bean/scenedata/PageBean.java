/**
 * Copyright 2021 json.cn
 */
package com.imorning.senseinfohelper.bean.scenedata;

import java.util.List;


/**
 * Auto-generated: 2021-04-24 16:59:43
 *
 * @author iMorning
 */
public class PageBean {

    private int allPages;
    private List<SenseInfoContentList> contentlist;
    private int currentPage;
    private int allNum;
    private int maxResult;

    public int getAllPages() {
        return allPages;
    }

    public void setAllPages(int allPages) {
        this.allPages = allPages;
    }

    /**
     * 获取景点信息
     *
     * @return {@link SenseInfoContentList}
     */
    public List<SenseInfoContentList> getContentList() {
        return contentlist;
    }

    public void setContentList(List<SenseInfoContentList> contentlist) {
        this.contentlist = contentlist;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getAllNum() {
        return allNum;
    }

    public void setAllNum(int allNum) {
        this.allNum = allNum;
    }

    public int getMaxResult() {
        return maxResult;
    }

    public void setMaxResult(int maxResult) {
        this.maxResult = maxResult;
    }

}
