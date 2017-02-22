package com.example.face.application_shoptwo.Model.JavaBean;

import java.util.List;


/**
 * Created by Face on 2016/10/7.
 */

public class ComplexShop {
    private String error_code;//返回码
    private String reason;//返回说明
    public Result result;//结果

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public static class Result{
        private int SearchItemsCount;//返回记录总数
        public List<SearchResultList> SearchResultList;//返回结果集

        public int getSearchItemsCount() {
            return SearchItemsCount;
        }

        public void setSearchItemsCount(int searchItemsCount) {
            SearchItemsCount = searchItemsCount;
        }

        public List<SearchResultList> getSearchResultList() {
            return SearchResultList;
        }

        public void setSearchResultList(List<SearchResultList> searchResultList) {
            this.SearchResultList = searchResultList;
        }

        public static class SearchResultList{
            private String spname;//商品名字
            private String sppic;//商品图片地址
            private String spurl;//商品url地址
            private String spprice;//商品价格
            private String className;//分类名称
            private String brandName;//品牌名称
            private String siteName;//商城名称
            private String commentUrl;//评论地址
            private String commentCount;//评论数量
            private String TitleHighLighter;//标题
            private String ziying;//是否自营，1表示自营，2表示第三方
            private String siteid;
            private String id;

            public String getSpname() {
                return spname;
            }

            public void setSpname(String spname) {
                this.spname = spname;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getSiteid() {
                return siteid;
            }

            public void setSiteid(String siteid) {
                this.siteid = siteid;
            }

            public String getCommentCount() {
                return commentCount;
            }

            public void setCommentCount(String commentCount) {
                this.commentCount = commentCount;
            }

            public String getBrandName() {
                return brandName;
            }

            public void setBrandName(String brandName) {
                this.brandName = brandName;
            }

            public String getSpurl() {
                return spurl;
            }

            public void setSpurl(String spurl) {
                this.spurl = spurl;
            }

            public String getSppic() {
                return sppic;
            }

            public void setSppic(String sppic) {
                this.sppic = sppic;
            }

            public String getClassName() {
                return className;
            }

            public void setClassName(String className) {
                this.className = className;
            }

            public String getSpprice() {
                return spprice;
            }

            public void setSpprice(String spprice) {
                this.spprice = spprice;
            }

            public String getCommentUrl() {
                return commentUrl;
            }

            public void setCommentUrl(String commentUrl) {
                this.commentUrl = commentUrl;
            }

            public String getSiteName() {
                return siteName;
            }

            public void setSiteName(String siteName) {
                this.siteName = siteName;
            }

            public String getTitleHighLighter() {
                return TitleHighLighter;
            }

            public void setTitleHighLighter(String titleHighLighter) {
                TitleHighLighter = titleHighLighter;
            }

            public String getZiying() {
                return ziying;
            }

            public void setZiying(String ziying) {
                this.ziying = ziying;
            }
        }
    }
/*private Result result;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }
    public static class Result{

        private List<SearchResultList>SearchResultList;

        public List<Result.SearchResultList> getSearchResultList() {
            return SearchResultList;
        }

        public void setSearchResultList(List<Result.SearchResultList> searchResultList) {
            SearchResultList = searchResultList;
        }

        public static class SearchResultList{
            private String spname;
            private String sppic;
            private String spurl;
            private String spprice;
            private String className;
            private String brandName;
            private String siteName;
            private String commentUrl;
            private String commentCount;
            private String TitleHighLighter;
            private String  ziying;
            private String siteid;
            private String  id;

            public String getSpname() {
                return spname;
            }

            public void setSpname(String spname) {
                this.spname = spname;
            }

            public String getSppic() {
                return sppic;
            }

            public void setSppic(String sppic) {
                this.sppic = sppic;
            }

            public String getSpurl() {
                return spurl;
            }

            public void setSpurl(String spurl) {
                this.spurl = spurl;
            }

            public String getSpprice() {
                return spprice;
            }

            public void setSpprice(String spprice) {
                this.spprice = spprice;
            }

            public String getClassName() {
                return className;
            }

            public void setClassName(String className) {
                this.className = className;
            }

            public String getBrandName() {
                return brandName;
            }

            public void setBrandName(String brandName) {
                this.brandName = brandName;
            }

            public String getSiteName() {
                return siteName;
            }

            public void setSiteName(String siteName) {
                this.siteName = siteName;
            }

            public String getCommentCount() {
                return commentCount;
            }

            public void setCommentCount(String commentCount) {
                this.commentCount = commentCount;
            }

            public String getCommentUrl() {
                return commentUrl;
            }

            public void setCommentUrl(String commentUrl) {
                this.commentUrl = commentUrl;
            }

            public String getTitleHighLighter() {
                return TitleHighLighter;
            }

            public void setTitleHighLighter(String titleHighLighter) {
                TitleHighLighter = titleHighLighter;
            }

            public String getZiying() {
                return ziying;
            }

            public void setZiying(String ziying) {
                this.ziying = ziying;
            }

            public String getSiteid() {
                return siteid;
            }

            public void setSiteid(String siteid) {
                this.siteid = siteid;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }
        }
    }*/
}
