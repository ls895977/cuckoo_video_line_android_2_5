package com.ailiaovideo.videoline.modle;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by weipeng on 2018/2/10.
 */
@Entity
public class JsonData {

    @Id(autoincrement = true)
    private Long id;

    private String key;

    private String val;

    @Generated(hash = 1431223176)
    public JsonData(Long id, String key, String val) {
        this.id = id;
        this.key = key;
        this.val = val;
    }

    @Generated(hash = 1032513285)
    public JsonData() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }
}
