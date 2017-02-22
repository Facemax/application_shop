package com.example.face.application_shoptwo.Model;

/**
 * Created by Face on 2016/10/7.
 */

public class Complex_Shop {
    public String spname;//商品名字
    public String sppic;//商品图片地址
    public String spurl;//商品url地址
    public String spprice;//商品价格
    public String className;//分类名称

    public String brandName;//品牌名称
    public String siteName;//商城名称
    public String commentUrl;//评论地址
    public String commentCount;//评论数量
    public String TitleHighLighter;//标题
    public String ziying;//是否自营，1表示自营，2表示第三方

    public Complex_Shop(String spname, String ziying, String titleHighLighter, String commentCount, String commentUrl, String siteName, String brandName, String className, String spprice, String spurl, String sppic) {
        this.spname = spname;
        this.ziying = ziying;
        this.TitleHighLighter = titleHighLighter;
        this.commentCount = commentCount;
        this.commentUrl = commentUrl;
        this.siteName = siteName;
        this.brandName = brandName;
        this.className = className;
        this.spprice = spprice;
        this.spurl = spurl;
        this.sppic = sppic;
    }
}
