package com.ailiaovideo.videoline.json;

import com.alibaba.fastjson.JSON;

import java.util.List;

public class RecommendBean extends JsonRequestBase {

    private int code;
    private String msg;

    @Override
    public String getMsg() {
        return msg;
    }

    @Override
    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public void setCode(int code) {
        this.code = code;
    }

    private List<RecommendChildBean> list;

    public List<RecommendChildBean> getList() {
        return list;
    }

    public void setList(List<RecommendChildBean> list) {
        this.list = list;
    }

    public static RecommendBean getJsonObj(String json) {
        return JSON.parseObject(json, RecommendBean.class);
    }

    public RecommendBean() {
        super();
    }

    public class RecommendChildBean {


        /**
         * user_nickname : 花幺不妖
         * avatar : http://videoline.qiniu.bugukj.com/'user/20181121/19052d0e772d496455724f26a35f001b.jpg'
         * id : 100206
         * sex : 2
         */

        private String user_nickname;
        private String avatar;
        private int id;
        private int sex;
        private boolean isChecked = true;

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }

        public String getUser_nickname() {
            return user_nickname;
        }

        public void setUser_nickname(String user_nickname) {
            this.user_nickname = user_nickname;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }
    }
}
