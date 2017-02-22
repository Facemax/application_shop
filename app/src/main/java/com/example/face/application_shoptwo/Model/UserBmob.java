package com.example.face.application_shoptwo.Model;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by Face on 2016/10/23.
 */

public class UserBmob extends BmobUser {
    private String message;
    private BmobFile headphoto;


    public BmobFile getHeadphoto() {
        return headphoto;
    }

    public void setHeadphoto(BmobFile headphoto) {
        this.headphoto = headphoto;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
