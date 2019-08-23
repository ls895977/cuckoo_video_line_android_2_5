package com.ailiaovideo.videoline.modle;

/**
 * Created by weipeng on 2018/2/28.
 */

public class UserImgModel {
    private String id;
    private String img;
    private int isLocal;

    public int getIsLocal() {
        return isLocal;
    }

    public void setIsLocal(int isLocal) {
        this.isLocal = isLocal;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
