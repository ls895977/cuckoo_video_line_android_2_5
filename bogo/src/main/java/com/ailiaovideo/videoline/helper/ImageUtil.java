package com.ailiaovideo.videoline.helper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Environment;

import com.ailiaovideo.videoline.MyApplication;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * 图片操作工具类
 * Created by jiahengfei on 2018/1/19 0019.
 */

public class ImageUtil {
    //宽
    public static float DISPLAY_WIDTH = 200;
    //高
    public static float DISPLAY_HEIGHT = 200;


    // 默认存放图片的路径
    public final static String DEFAULT_SAVE_IMAGE_PATH = Environment
            .getExternalStorageDirectory()
            + File.separator
            + MyApplication.getInstances().getPackageName()
            + File.separator + "live_img" + File.separator;


    public static File getSaveFile(Bitmap bitmap,String filename){

        File dir = new File(DEFAULT_SAVE_IMAGE_PATH);
        if(!dir.exists()){
            dir.mkdirs();
        }

        String fileName = DEFAULT_SAVE_IMAGE_PATH+"/"+filename+".jpg";
        try {
            File file = new File(fileName);
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new File(fileName);
    }

    /**
     * 字节数组转化为BitMap
     * @param b 字节数组
     * @return Bitmap
     */
    public static Bitmap getBitmapByByte(byte[] b){
        return BitmapFactory.decodeByteArray(b, 0, b.length);
    }

    /**
     * BitMap转化为byte[]
     * @param b Bitmap
     * @return byte[]
     */
    public static byte[] getBitmapByByte(Bitmap b){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return  baos.toByteArray();
    }

    /**
     * byte[]转化为String
     * @param b byte[]
     * @return String
     */
    public static String getStringByByte(byte[] b){
        return new String(b);
    }

    /**
     * String转化为byte[]
     * @param str String
     * @return byte[]
     */
    public static byte[] getByteByString(String str){
        return str.getBytes();
    }

    /**
     * 二进制字符串转化为BitMap
     * @param str 由图片byte[]转化来的String
     * @return Bitmap
     */
    public static Bitmap getBitMapByString(String str){
        byte[] b = getByteByString(str);
        return getBitmapByByte(b);
    }

    /**
     * Bitmap对象保存Bitmap文件
     * @param bitmap 位图文件
     * @return 文件对象
     */
    public static File saveBitmapFile(Bitmap bitmap,String filename) {
        String FILE_NAME = filename+".jpg";
        //定义主要外部存储目录。
        String filePath = "/sdcard" + FILE_NAME;
        File myfile = new File(FILE_NAME);
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myfile));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return myfile;
    }


    /**
     * 图片缩放
     * @param bitmap 对象
     * @param w 要缩放的宽度
     * @param h 要缩放的高度
     * @return newBmp 新 Bitmap对象
     */
    public static Bitmap zoomBitmap(Bitmap bitmap, int w, int h){
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) w / width);
        float scaleHeight = ((float) h / height);
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newBmp = Bitmap.createBitmap(bitmap, 0, 0, width, height,
                matrix, true);
        return newBmp;
    }

    /**
     * 从path中获取图片信息
     * @param path
     * @return
     */
    private static Bitmap decodeBitmap(String path){
        BitmapFactory.Options op = new BitmapFactory.Options();
        //inJustDecodeBounds
        //If set to true, the decoder will return null (no bitmap), but the out…
        op.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(path, op); //获取尺寸信息
        //获取比例大小
        int wRatio = (int)Math.ceil(op.outWidth/DISPLAY_WIDTH);
        int hRatio = (int)Math.ceil(op.outHeight/DISPLAY_HEIGHT);
        //如果超出指定大小，则缩小相应的比例
        if(wRatio > 1 && hRatio > 1){
            if(wRatio > hRatio){
                op.inSampleSize = wRatio;
            }else{
                op.inSampleSize = hRatio;
            }
        }
        op.inJustDecodeBounds = false;
        bmp = BitmapFactory.decodeFile(path, op);
        return bmp;
    }

    /**
     * 获取bitmap
     *
     * @param filePath
     * @return
     */
    public static Bitmap getBitmapByPath(String filePath) {
        return getBitmapByPath(filePath, null);
    }
    public static Bitmap getBitmapByPath(String filePath,BitmapFactory.Options opts) {
        FileInputStream fis = null;
        Bitmap bitmap = null;
        try {
            File file = new File(filePath);
            fis = new FileInputStream(file);
            bitmap = BitmapFactory.decodeStream(fis, null, opts);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        } finally {
            try {
                fis.close();
            } catch (Exception e) {
            }
        }
        return bitmap;
    }

    /**
     * 使用当前时间戳拼接一个唯一的文件名
     *
     * @param
     * @return
     */
    public static String getTempFileName() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss_SS");
        String fileName = format.format(new Timestamp(System
                .currentTimeMillis()));
        return fileName;
    }

    /**
     * 获取照相机使用的目录
     *
     * @return
     */
    public static String getCamerPath() {
        return Environment.getExternalStorageDirectory() + File.separator
                + "FounderNews" + File.separator;
    }



}
