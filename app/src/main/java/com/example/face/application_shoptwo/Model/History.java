package com.example.face.application_shoptwo.Model;

/**
 * Created by Face on 2016/11/11.
 */

public class History {
    public String username;
    public String mode;
    public String shopname;
    public String data;

    public History(String username, String data, String shopname, String mode) {
        this.username = username;
        this.data = data;
        this.shopname = shopname;
        this.mode = mode;
    }
}
