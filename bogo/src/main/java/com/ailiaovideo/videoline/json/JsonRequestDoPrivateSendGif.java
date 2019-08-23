package com.ailiaovideo.videoline.json;

/**
 * Created by 魏鹏 on 2018/3/11.
 * email:1403102936@qq.com
 * 山东布谷鸟网络科技有限公司著
 */

public class JsonRequestDoPrivateSendGif extends JsonRequestBase {

    /**
     * send : {"from_msg":"送给你一个小幽灵","from_score":"你的经验值+200","to_ticket":200,"to_diamonds":"200","to_user_id":100115,"prop_icon":"http://p4ulgsz1p.bkt.clouddn.com/'admin/20180307/ca4bde3fff7cd169fec65ed16ff17dbe.png'","status":1,"prop_id":1,"total_ticket":0}
     */

    private SendBean send;

    public SendBean getSend() {
        return send;
    }

    public void setSend(SendBean send) {
        this.send = send;
    }

    public static class SendBean {
        /**
         * from_msg : 送给你一个小幽灵
         * from_score : 你的经验值+200
         * to_ticket : 200
         * to_diamonds : 200
         * to_user_id : 100115
         * prop_icon : http://p4ulgsz1p.bkt.clouddn.com/'admin/20180307/ca4bde3fff7cd169fec65ed16ff17dbe.png'
         * status : 1
         * prop_id : 1
         * total_ticket : 0
         */

        private String from_msg;
        private String from_score;
        private String to_ticket;
        private String to_diamonds;
        private String to_user_id;
        private String prop_icon;
        private String status;
        private String prop_id;
        private String to_msg;
        private String total_ticket;

        public String getTo_msg() {
            return to_msg;
        }

        public void setTo_msg(String to_msg) {
            this.to_msg = to_msg;
        }

        public String getFrom_msg() {
            return from_msg;
        }

        public void setFrom_msg(String from_msg) {
            this.from_msg = from_msg;
        }

        public String getFrom_score() {
            return from_score;
        }

        public void setFrom_score(String from_score) {
            this.from_score = from_score;
        }

        public String getTo_ticket() {
            return to_ticket;
        }

        public void setTo_ticket(String to_ticket) {
            this.to_ticket = to_ticket;
        }

        public String getTo_diamonds() {
            return to_diamonds;
        }

        public void setTo_diamonds(String to_diamonds) {
            this.to_diamonds = to_diamonds;
        }

        public String getTo_user_id() {
            return to_user_id;
        }

        public void setTo_user_id(String to_user_id) {
            this.to_user_id = to_user_id;
        }

        public String getProp_icon() {
            return prop_icon;
        }

        public void setProp_icon(String prop_icon) {
            this.prop_icon = prop_icon;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getProp_id() {
            return prop_id;
        }

        public void setProp_id(String prop_id) {
            this.prop_id = prop_id;
        }

        public String getTotal_ticket() {
            return total_ticket;
        }

        public void setTotal_ticket(String total_ticket) {
            this.total_ticket = total_ticket;
        }
    }
}
