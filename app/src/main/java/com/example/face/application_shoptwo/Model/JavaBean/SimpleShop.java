package com.example.face.application_shoptwo.Model.JavaBean;

import java.util.List;

/**
 * Created by Face on 2016/10/7.
 */

public class SimpleShop {
    private String error_code;
    private String reason;
    private List<Result> result;
    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public List<Result> getResult() {
        return result;
    }

    public void setResult(List<Result> result) {
        this.result = result;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
    public static class Result{
        private String sppic;
        private String siteName;
        private String spurl;
        private String spname;
        private String spprice;
        
        public String getSppic() {
            return sppic;
        }

        public void setSppic(String sppic) {
            this.sppic = sppic;
        }

        public String getSiteName() {
            return siteName;
        }

        public void setSiteName(String siteName) {
            this.siteName = siteName;
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
}
