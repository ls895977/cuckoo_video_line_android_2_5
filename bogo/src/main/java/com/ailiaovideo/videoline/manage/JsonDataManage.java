package com.ailiaovideo.videoline.manage;

import com.ailiaovideo.videoline.MyApplication;
import com.ailiaovideo.videoline.dao.JsonDataDao;
import com.ailiaovideo.videoline.modle.JsonData;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * Created by weipeng on 2018/2/10.
 */

public class JsonDataManage {


    public static JsonDataManage instance;

    public JsonDataDao jsonDataDao;

    /**
     * 获取当前单例
     * @return SaveData
     */
    public static JsonDataManage getInstance(){
        if (instance == null){
            instance = new JsonDataManage();
        }
        return instance;
    }


    /**
     * 初始化方法
     * @param context NimApplication对象
     */
    public static void init(MyApplication context){
        getInstance().initData(context);
    }

    /**
     * 初始化数据
     */
    private void initData(MyApplication context){
        jsonDataDao = context.getDaoSession().getJsonDataDao();

    }

    /**
     * 保存信息
     * @param data
     */
    public void saveData(JsonData data) {

        //查询是否存在该条记录，如果存在进行覆盖存储，不存在插入
        QueryBuilder builder = jsonDataDao.queryBuilder();
        List<JsonData> list = builder.where(JsonDataDao.Properties.Key.eq(data.getKey())).build().list();
        if(list.size() > 0){
            long id = list.get(0).getId();
            data.setId(id);
        }
        jsonDataDao.insertOrReplace(data);
    }

    /**
    * @dw 获取数据
    * */
    public JsonData getData(String key){
        QueryBuilder builder = jsonDataDao.queryBuilder();
        List<JsonData> list = builder.where(JsonDataDao.Properties.Key.eq(key)).build().list();
        if(list.size() > 0) {
            return list.get(0);
        }

        return null;
    }

    /**
    * @dw 删除数据
    * @param key 要删除的数据key
    * */
    public boolean delete(String key){

        QueryBuilder builder = jsonDataDao.queryBuilder();
        List<JsonData> list = builder.where(JsonDataDao.Properties.Key.eq(key)).build().list();
        if(list.size() > 0) {
            jsonDataDao.deleteByKey(list.get(0).getId());
            return true;
        }

        return false;
    }

}
