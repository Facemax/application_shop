package com.example.face.application_shoptwo.Model;

import cn.bmob.v3.BmobObject;

/**
 * Created by Face on 2016/11/11.
 */

public class History_Bmob extends BmobObject{
    private String username;
    private String mode;
    private String shopname;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }
}
