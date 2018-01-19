package com.xiaoshq.database;

/**
 * Created by ivan on 2018/1/19.
 */

public class User
{
    public int userId;
    public String userName;
    public String password;

    public User(int uId, String name, String pwd) {
        this.userId = uId;
        this.userName = name;
        this.password = pwd;
    }
}
