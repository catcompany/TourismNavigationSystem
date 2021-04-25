/**
 * Copyright 2021 json.cn
 */
package com.imorning.senseinfohelper.bean.scenedata;

import java.util.Date;

/**
 * @author iMorning
 */
public class Price {
    /**
     * 门票名称
     */
    private String TicketName;
    /**
     * 零售价
     */
    private String Amount;
    private Date EndDate;
    private Date BeginDate;
    private String PriceName;
    /**
     * 建议零售价
     */
    private String AmountAdvice;
    private long TicketTypeId;
    private long PriceId;
    private int PriceInSceneryId;

    public String getTicketName() {
        return TicketName;
    }

    public void setTicketName(String TicketName) {
        this.TicketName = TicketName;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String Amount) {
        this.Amount = Amount;
    }

    public Date getEndDate() {
        return EndDate;
    }

    public void setEndDate(Date EndDate) {
        this.EndDate = EndDate;
    }

    public Date getBeginDate() {
        return BeginDate;
    }

    public void setBeginDate(Date BeginDate) {
        this.BeginDate = BeginDate;
    }

    public String getPriceName() {
        return PriceName;
    }

    public void setPriceName(String PriceName) {
        this.PriceName = PriceName;
    }

    public String getAmountAdvice() {
        return AmountAdvice;
    }

    public void setAmountAdvice(String AmountAdvice) {
        this.AmountAdvice = AmountAdvice;
    }

    public long getTicketTypeId() {
        return TicketTypeId;
    }

    public void setTicketTypeId(long TicketTypeId) {
        this.TicketTypeId = TicketTypeId;
    }

    public long getPriceId() {
        return PriceId;
    }

    public void setPriceId(long PriceId) {
        this.PriceId = PriceId;
    }

    public int getPriceInSceneryId() {
        return PriceInSceneryId;
    }

    public void setPriceInSceneryId(int PriceInSceneryId) {
        this.PriceInSceneryId = PriceInSceneryId;
    }

}