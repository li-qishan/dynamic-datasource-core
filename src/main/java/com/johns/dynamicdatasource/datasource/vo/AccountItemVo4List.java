package com.johns.dynamicdatasource.datasource.vo;

import com.johns.dynamicdatasource.datasource.entities.AccountItem;

public class AccountItemVo4List extends AccountItem {

    private String accountName;

    private String inOutItemName;

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getInOutItemName() {
        return inOutItemName;
    }

    public void setInOutItemName(String inOutItemName) {
        this.inOutItemName = inOutItemName;
    }
}