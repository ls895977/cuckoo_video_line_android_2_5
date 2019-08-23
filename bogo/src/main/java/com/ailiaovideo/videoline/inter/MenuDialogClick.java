package com.ailiaovideo.videoline.inter;

import android.content.DialogInterface;

/**
 * 菜单dialog监听接口
 * Created by fly on 2018/1/3 0003.
 */

public interface MenuDialogClick {
    /**
     * 列表点击监听
     * @param dialog dialog对象
     * @param which 选项索引,从0开始
     */
    void OnMenuItemClick(DialogInterface dialog, int which);
}
