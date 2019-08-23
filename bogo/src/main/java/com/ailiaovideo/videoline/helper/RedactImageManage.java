package com.ailiaovideo.videoline.helper;

import android.graphics.Bitmap;

import java.io.File;
import java.util.ArrayList;

/**
 * 编辑资料页图片管理类
 * Created by jiahengfei on 2018/1/26 0026.
 */

public class RedactImageManage {
    private ArrayList<File> files = new ArrayList<>();//保存新选择的图片
    private ArrayList<File> jadis = new ArrayList<>();//之前的图片


    private File headImg;//头像图片

    public static RedactImageManage imageManage;
    /**
     * 本类实例
     * @return RedactImageManage
     */
    public static RedactImageManage getImageManage(){
        if (imageManage == null){
            return new RedactImageManage();
        }else{
            return imageManage;
        }
    }

    /**
     * 初始化方法
     * @param bitmaps
     */
    private void init(ArrayList<Bitmap> bitmaps){
        //转换图片
        int i = 0;
        for (Bitmap bitmap:bitmaps) {
            jadis.add(ImageUtil.getSaveFile(bitmap,"img"+i));
            i++;
        }
    }

    /**
     * 新获取的文件
     * @param strings
     */
    private void toGetFile(ArrayList<String> strings){
        for (String string:strings) {
            files.add(new File(string));
        }
    }

    public ArrayList<File> getFiles() {
        return files;
    }

    public ArrayList<File> getJadis() {
        return jadis;
    }

    public File getHeadImg() {
        return headImg;
    }

    public void setFiles(ArrayList<File> files) {

        this.files = files;
    }

    public void setJadis(ArrayList<File> jadis) {
        this.jadis = jadis;
    }

    public void setHeadImg(File headImg) {
        this.headImg = headImg;
    }
//    for (String filepath:filePaths) {
//        ApiUtils.getUrlBitmap(filepath, new ApiUtils.GetUrlBitMap() {
//            @Override
//            public void doThenByBitmap(Bitmap bitmap) {
//
//            }
//        });
//    }
}
