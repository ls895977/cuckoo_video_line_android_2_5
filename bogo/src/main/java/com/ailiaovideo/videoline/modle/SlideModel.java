package com.ailiaovideo.videoline.modle;

public class SlideModel {

    /**
     * id : 3
     * image : http://p4ulgsz1p.bkt.clouddn.com/'admin/20180315/7f4e06e9f6722d6727549cc050ad1dc9.jpg'
     * title : 再来一张
     * url : www.baidu.com
     */

    private int id;
    private String image;
    private String title;
    private String url;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
