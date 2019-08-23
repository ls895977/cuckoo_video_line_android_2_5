package com.ailiaovideo.videoline.modle;


import java.util.HashMap;
import java.util.Map;

/**
 * 全局单例 保存数据
 */

public class GlobalMassage {
    private static GlobalMassage globalMassage;

    public static GlobalMassage getInStance() {
        if (globalMassage == null) {

            globalMassage = new GlobalMassage();
        }

        return globalMassage;
    }

    //聊天界面对话框弹出控制
    public boolean isCanShowDialog = true;


}
