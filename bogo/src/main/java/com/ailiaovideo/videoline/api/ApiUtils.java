package com.ailiaovideo.videoline.api;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.ailiaovideo.videoline.helper.ImageUtil;
import com.ailiaovideo.videoline.manage.AppConfig;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.BitmapCallback;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

import static io.agora.rtc.internal.AudioRoutingController.TAG;

/**
 * api工具类
 * Created by jiahengfei on 2018/1/20 0020.
 */

public class ApiUtils {
    public static Bitmap bitMap = null;
    public static File file  = null;

    /**
     * 判断是否是url
     * @param url
     * @return
     */
    public static boolean isTrueUrl(String url){
        if (url==null){
            return false;
        }else{
            if (url.length()==0){
                return false;
            }else{
                if ("".equals(url.trim())){
                    return false;
                }else{
                    return true;
                }
            }
        }
    }

    /**
     * 判断是否是全地址
     * @param url
     * @return
     */
    public static String isHttpString(String url){
        Log.d(TAG, "isHttpString: 地址打印::"+url);
        if (url!=null && url.length() > 0){
            if (url.substring(0,4).equals("http")){
                Log.d(TAG, "isHttpString: 地址正确,不需要拼接!!");
                return url;
            }else {
                Log.d(TAG, "isHttpString: 拼接之后的地址"+AppConfig.MAIN_URL+url);
                return AppConfig.MAIN_URL+url;
            }
        }else{
            Log.d(TAG, "isHttpString: 拼接之后的地址"+"---地址为空,无法拼接");
            return url;
        }
    }

    /**
     * 获取解析的map集合
     * @param jsonStr
     * @return
     */
    public static Map<String,Object> getJsonObj(String jsonStr){
        Map<String,Object> map = new HashMap<>();
        map = JSON.parseObject(jsonStr,new TypeReference<HashMap<String,Object>>(){});
        return map;
    }

    /**
     * 获取解析的对象
     * @param jsonStr
     * @return
     */
    public static JSONObject getJsonObj2(String jsonStr){
        JSONObject jsonObject = JSON.parseObject(jsonStr);
        return jsonObject;
    }

    /**
     * 获取网络图片之后执行的方法接口
     */
    public interface GetUrlBitMap{
        void doThenByBitmap(Bitmap bitmap);
    }

    /**
     * 网络获取bitmap图片
     * @param url
     * @return
     */
    public static void getUrlBitmap(String url, final GetUrlBitMap getUrlBitMap){
        OkGo.get(isHttpString(url))
                .tag("getRecommendUserList")
                .cacheMode(CacheMode.DEFAULT)
                .execute(new BitmapCallback() {
                    @Override
                    public void onSuccess(Bitmap bitmap, Call call, Response response) {
                        getUrlBitMap.doThenByBitmap(bitmap);
                    }
                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                    }
                });
    }

    /**
     * 获取网络图片
     * @param url
     * @return
     */
    public static Bitmap getURLimage(String url) {
        Bitmap bmp = null;
        try {
            URL myurl = new URL(isHttpString(url));
            // 获得连接
            HttpURLConnection conn = (HttpURLConnection) myurl.openConnection();
            conn.setConnectTimeout(6000);//设置超时
            conn.setDoInput(true);
            conn.setUseCaches(false);//不缓存
            conn.connect();
            InputStream is = conn.getInputStream();//获得图片的数据流
            bmp = BitmapFactory.decodeStream(is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bmp;
    }

    /**
     * 获取网络图片(批量+获取之后的监听)
     * @param urls
     * @return
     */
    public static void getURLimages(final List<String> urls, final GetUrlBitMaps getUrlBitMaps) {
        final ArrayList<Bitmap> bmp = new ArrayList<>();
        new Thread(){
            @Override
            public void run() {
                try {
                    for (String url:urls) {
                        URL myurl = new URL(isHttpString(url));
                        // 获得连接
                        HttpURLConnection conn = (HttpURLConnection) myurl.openConnection();
                        conn.setConnectTimeout(6000);//设置超时
                        conn.setDoInput(true);
                        conn.setUseCaches(false);//不缓存
                        conn.connect();
                        InputStream is = conn.getInputStream();//获得图片的数据流
                        bmp.add(BitmapFactory.decodeStream(is));
                        is.close();
                    }
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            getUrlBitMaps.doThenByRefresh(bmp);
                        }
                    });
                }catch (Exception e){
                    Log.d(TAG, "run<getURLimages>: "+e);
                }
            }
        }.start();
    }
    public interface GetUrlBitMaps{
        void doThenByRefresh(ArrayList<Bitmap> bmp);
    }

    /**
     * 设置网络获取bitmap图片
     * @param url
     * @return
     */
    public static void setUrlBitmapToFile(String url, final ImageView imgView){
        OkGo.get(isHttpString(url))
                .tag("getRecommendUserList")
                .cacheMode(CacheMode.DEFAULT)
                .execute(new BitmapCallback() {
                    @Override
                    public void onSuccess(Bitmap bitmap, Call call, Response response) {
                        imgView.setImageBitmap(bitmap);
                        setFiles(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/headImage.jpg"));
                    }
                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                    }
                });
    }

    public static void setFiles(File files){
        file = files;
    }

    /**
     * 设置网络获取bitmap图片
     * @param url
     * @return
     */
    /*public static void setUrlBitmap(String url, final ImageView imgView){
        OkGo.get(isHttpString(url))
                .tag("getRecommendUserList")
                .cacheMode(CacheMode.DEFAULT)
                .execute(new BitmapCallback() {
                    @Override
                    public void onSuccess(Bitmap bitmap, Call call, Response response) {
                        imgView.setImageBitmap(bitmap);
                    }
                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                    }
                });
    }*/

    /**
     * 获取stringuri地址
     * @param stringUri 例"/api/index.php/index/Smallvideo/index
     * @return
     */
    public static Uri getUriByString(String stringUri){
       //return Uri.parse(AppConfig.MAIN_URL+stringUri);
       return Uri.parse(stringUri);
    }

    /**
     * 网络获取bitmap图片
     * @param url
     * @return
     */
    public static void getUrlBitmapToSD(String url, final String filename){
        OkGo.get(isHttpString(url))
                .tag("getRecommendUserList")
                .cacheMode(CacheMode.DEFAULT)
                .execute(new BitmapCallback() {
                    @Override
                    public void onSuccess(Bitmap bitmap, Call call, Response response) {
                        ImageUtil.getSaveFile(bitmap,filename);
                    }
                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                    }
                });
    }

    /**
     * 视频类型参数
     */
    public static class VideoType{
        //推荐reference
        public static final String reference = "reference";
        //最新latest
        public static final String latest = "latest";
        //关注attention
        public static final String attention = "attention";
        //附近near
        public static final String near = "near";
    }

}
