package com.example.face.application_shoptwo.Model;

import cn.bmob.v3.BmobObject;

/**
 * Created by Face on 2016/11/8.
 */

public class Collect_Bmob extends BmobObject {
    private String username;
    private String sppic;
    private String sitename;
    private String spurl;
    private String spname;
    private String spprice;

    public String getUsername() {
        return username;}
    public void setUsername(String username) {
        this.username = username;
    }

    public String getSppic() {
        return sppic;
    }

    public void setSppic(String sppic) {
        this.sppic = sppic;
    }

    public String getSitename() {
        return sitename;
    }

    public void setSitename(String sitename) {
        this.sitename = sitename;
    }

    public String getSpurl() {
        return spurl;
    }

    public void setSpurl(String spurl) {
        this.spurl = spurl;
    }

    public String getSpname() {
        return spname;
    }

    public void setSpname(String spname) {
        this.spname = spname;
    }

    public String getSpprice() {
        return spprice;
    }

    public void setSpprice(String spprice) {
        this.spprice = spprice;
    }
}
