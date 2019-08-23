package com.ailiaovideo.videoline.utils;

import android.webkit.JavascriptInterface;

import com.blankj.utilcode.util.ClipboardUtils;
import com.blankj.utilcode.util.ToastUtils;

public class JavaScriptInterface {

    @JavascriptInterface
    public void copyText(String text){
        ClipboardUtils.copyText(text);
        ToastUtils.showLong("复制成功");
    }
}
