package com.xiaoshq.database;

/**
 * Created by ivan on 2018/1/3.
 */

public class Ctrl {
    public int ctrlId;
    public int userId;
    public String ctrlName;
    public String detail;

    public Ctrl(int cId, int uId, String name, String detail) {
        this.ctrlId = cId;
        this.userId = uId;
        this.ctrlName = name;
        this.detail = detail;
    }
}

