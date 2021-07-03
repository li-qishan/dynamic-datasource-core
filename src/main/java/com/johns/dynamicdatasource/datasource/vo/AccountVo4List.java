package com.johns.dynamicdatasource.datasource.vo;

import com.johns.dynamicdatasource.datasource.entities.Account;

public class AccountVo4List extends Account {

    private String thisMonthAmount;

    public String getThisMonthAmount() {
        return thisMonthAmount;
    }

    public void setThisMonthAmount(String thisMonthAmount) {
        this.thisMonthAmount = thisMonthAmount;
    }
}