package com.example.face.application_shoptwo.Model;

/**
 * Created by Face on 2016/11/8.
 */

public class Shop_Collect {
    public String username;
    public String sppic;
    public String sitename;
    public String spurl;
    public String spname;
    public String spprice;
    public String objectid;

    public Shop_Collect(String objectid,String username, String spprice, String spname, String spurl, String sitename, String sppic) {
        this.username = username;
        this.objectid = objectid;
        this.spprice = spprice;
        this.spname = spname;
        this.spurl = spurl;
        this.sitename = sitename;
        this.sppic = sppic;
    }
}
