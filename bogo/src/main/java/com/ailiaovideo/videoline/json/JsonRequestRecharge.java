package com.ailiaovideo.videoline.json;

/**
 * Created by 魏鹏 on 2018/3/28.
 * email:1403102936@qq.com
 * 山东布谷鸟网络科技有限公司著
 */
public class JsonRequestRecharge extends JsonRequestBase {

    /**
     * pay : {"post_url":"http://www.qianyingnet.com/pay/?uid=3002&type=101&m=1&orderid=1522205059123321&callbackurl=http://127.0.0.1/pay/Receive.aspx&sign=f0c24d10102e8f997a221b81ad5d7051&gofalse=http://www.qianyingnet.com/pay&gotrue=http:/www.qianyingnet.com/&charset=utf-8&token=wu"}
     */

    private PayBean pay;

    public PayBean getPay() {
        return pay;
    }

    public void setPay(PayBean pay) {
        this.pay = pay;
    }

    public static class PayBean {
        /**
         * post_url : http://www.qianyingnet.com/pay/?uid=3002&type=101&m=1&orderid=1522205059123321&callbackurl=http://127.0.0.1/pay/Receive.aspx&sign=f0c24d10102e8f997a221b81ad5d7051&gofalse=http://www.qianyingnet.com/pay&gotrue=http:/www.qianyingnet.com/&charset=utf-8&token=wu
         */

        private String post_url;
        private String is_wap;
        private String type;
        private String pay_info;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getPay_info() {
            return pay_info;
        }

        public void setPay_info(String pay_info) {
            this.pay_info = pay_info;
        }

        public String getIs_wap() {
            return is_wap;
        }

        public void setIs_wap(String is_wap) {
            this.is_wap = is_wap;
        }

        public String getPost_url() {
            return post_url;
        }

        public void setPost_url(String post_url) {
            this.post_url = post_url;
        }
    }
}
