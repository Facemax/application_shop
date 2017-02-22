package com.example.face.application_shoptwo.Model;


import cn.bmob.v3.BmobObject;

/**
 * Created by Face on 2016/12/21.
 */

public class Bug_Bmob extends BmobObject{
    private String type;
    private String context;
    private String username;
    private String useremail;

    public String getUseremail() {
        return useremail;
    }

    public void setUseremail(String useremail) {
        this.useremail = useremail;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
