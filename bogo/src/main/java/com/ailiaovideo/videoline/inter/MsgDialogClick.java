package com.ailiaovideo.videoline.inter;

import com.qmuiteam.qmui.widget.dialog.QMUIDialog;

/**
 * 消息Dialog监听接口
 * Created by fly on 2017/12/27 0027.
 */

public interface MsgDialogClick {

    /**
     * 点击确定选项
     * @param dialog
     * @param index
     */
    void doYes(QMUIDialog dialog, int index);

    /**
     * 点击取消操作
     * @param dialog
     * @param index
     */
    void doNo(QMUIDialog dialog, int index);

}
