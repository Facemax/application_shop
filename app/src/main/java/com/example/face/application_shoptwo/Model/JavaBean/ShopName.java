package com.example.face.application_shoptwo.Model.JavaBean;

import java.util.List;

/**
 * Created by Face on 2016/10/5.
 */

public class ShopName {

    public List<Result> result;

    public List<Result> getResult() {
        return result;
    }

    public void setResult(List<Result> result) {
        this.result = result;
    }

    public static class Result{
            private String Id;
            private String Site;

        public String getId() {
            return Id;
        }

        public void setId(String id) {
            Id = id;
        }

        public String getSite() {
            return Site;
        }

        public void setSite(String site) {
            Site = site;
        }
    }
}
