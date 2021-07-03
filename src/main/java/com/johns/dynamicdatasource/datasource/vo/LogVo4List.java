package com.johns.dynamicdatasource.datasource.vo;

import com.johns.dynamicdatasource.datasource.entities.Log;

public class LogVo4List extends Log {

    private String userName;

    private String createTimeStr;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCreateTimeStr() {
        return createTimeStr;
    }

    public void setCreateTimeStr(String createTimeStr) {
        this.createTimeStr = createTimeStr;
    }
}