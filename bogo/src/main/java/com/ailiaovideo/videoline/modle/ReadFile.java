package com.ailiaovideo.videoline.modle;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 *
 * Created by jiahengfei on 2018/1/16 0016.
 */
@Entity
public class ReadFile {

    @Id(autoincrement = true)
    private Long id;
    private String fileName;
    private String filePath;
    private String fileSize;
    public String getFileSize() {
        return this.fileSize;
    }
    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }
    public String getFilePath() {
        return this.filePath;
    }
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    public String getFileName() {
        return this.fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    @Generated(hash = 1898072982)
    public ReadFile(Long id, String fileName, String filePath, String fileSize) {
        this.id = id;
        this.fileName = fileName;
        this.filePath = filePath;
        this.fileSize = fileSize;
    }
    @Generated(hash = 1743920990)
    public ReadFile() {
    }
}
