package com.ailiaovideo.videoline.json.jsonmodle;

/**
 * Image信息
 * Created by jiahengfei on 2018/1/26 0026.
 */

public class ImageData {
    private String id;
    private String image;//路径
    private String title;
    private String url;

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

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {

        return id;
    }

    public ImageData() {
        super();
    }

    public ImageData(String id, String img) {
        this.id = id;
        this.image = img;
    }

    @Override
    public String toString() {
        return "ImageData{" +
                "id='" + id + '\'' +
                ", img='" + image + '\'' +
                '}';
    }
}
