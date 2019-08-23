package com.ailiaovideo.videoline.utils;

/**
 * Created by Administrator on 2017/12/23 0023.
 */

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.ailiaovideo.videoline.LiveConstant;
import com.ailiaovideo.videoline.R;
import com.ailiaovideo.videoline.inter.MsgDialogClick;
import com.ailiaovideo.videoline.manage.AppConfig;
import com.ailiaovideo.videoline.MyApplication;
import com.ailiaovideo.videoline.modle.ConfigModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jp.wasabeef.glide.transformations.BlurTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

/**
 * 项目工具类
 */
public class Utils {

    public static boolean isMainProcess(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return context.getApplicationInfo().packageName.equals(appProcess.processName);
            }
        }
        return false;
    }

    /**
     * 显示一条toast
     *
     * @param msg
     */
    public static void showToastMsg(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * Activity跳转方法
     *
     * @param context
     * @param cls
     * @return 返回Activity对象
     */
    public static Activity goActivity(Context context, Class cls) {
        Intent intent = new Intent(context, cls);
        context.startActivity(intent);
        return (Activity) context;
    }

    /**
     * Activity跳转方法
     *
     * @param view
     * @param cls
     * @return 返回Activity对象
     */
    public static Activity goActivity(View view, Class cls) {
        Intent intent = new Intent(view.getContext(), cls);
        view.getContext().startActivity(intent);
        return (Activity) view.getContext();
    }

    /**
     * 获得指定数目的UUID
     *
     * @param number int 需要获得的UUID数量
     * @return String[] UUID数组
     */
    public static String[] getUUID(int number) {
        if (number < 1) {
            return null;
        }
        String[] retArray = new String[number];
        for (int i = 0; i < number; i++) {
            retArray[i] = getUUID();
        }
        return retArray;
    }

    /**
     * 获得一个UUID
     *
     * @return String UUID
     */
    public static String getUUID() {
        String uuid = UUID.randomUUID().toString();
        //去掉“-”符号
        return uuid.replaceAll("-", "");
    }

    /**
     * 获取性别
     *
     * @param sex int性别
     * @return 字符串性别
     */
    public static String getSex(int sex) {
        if (sex == 0) {
            return "保密";
        } else if (sex == 1) {
            return "男";
        } else if (sex == 2) {
            return "女";
        } else {
            return null;
        }
    }

    /**
     * 显示一个信息提示框,自行执行了show方法,内置监听自带dismiss方法
     *
     * @param msg            提示消息
     * @param msgDialogClick 点击回调监听
     * @return MessageDialogBuilder实例
     */
    public static QMUIDialog.MessageDialogBuilder showMsgDialog(Context context, String title, String msg, final MsgDialogClick msgDialogClick) {
        QMUIDialog.MessageDialogBuilder messageDialogBuilder = new QMUIDialog.MessageDialogBuilder(context);
        messageDialogBuilder.setTitle(title)
                .setMessage(msg)
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        msgDialogClick.doNo(dialog, index);
                        dialog.dismiss();
                    }
                })
                .addAction("确定", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        msgDialogClick.doYes(dialog, index);
                        dialog.dismiss();
                    }
                })
                .show();
        return messageDialogBuilder;
    }

    /*
     * 获得一个完整的图片url地址
     * */
    public static String getCompleteImgUrl(String imgUrl) {

        if (imgUrl != null && !(imgUrl.contains("http://") || imgUrl.contains("https://"))) {
            return AppConfig.MAIN_URL + imgUrl;
        }

        return imgUrl;
    }

    /*
     * 加载图片
     * */
    public static void loadHttpImg(Context context, String url, ImageView img) {

        Glide.with(MyApplication.getInstances()).load(url).into(img);

    }

    /*
     * 加载图片
     * */
    public static void loadHeadHttpImg(Context context, String url, ImageView img) {

        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.icon_logo_logo);

        Glide.with(MyApplication.getInstances()).load(url).apply(options).into(img);

    }

    /*
     * 加载图片
     * */
    public static void loadHttpImg(Context context, String url, ImageView img, int def) {

        RequestOptions options = new RequestOptions()
                .placeholder(R.mipmap.no_bac);

        Glide.with(MyApplication.getInstances()).load(url).apply(options).into(img);

    }

    /*
     * 加载图片
     * */
    public static void loadHttpImgBlue(Context context, String url, ImageView img, int def) {

        //加载模糊图片
        Glide.with(MyApplication.getInstances()).load(Utils.getCompleteImgUrl(url))
                .apply(bitmapTransform(new BlurTransformation(25, 3)))
                .into((ImageView) img);

    }

    /*
     * 加载图片
     * */
    public static void loadHttpImg(String url, ImageView img) {

        Glide.with(MyApplication.getInstances()).load(url).into(img);

    }


    //敏感词过滤
    public static boolean dirtyWordFilter(String text) {

        String dirtWord = ConfigModel.getInitData().getDirty_word();
        String[] sourceStrArray = dirtWord.split(",");
        for (int i = 0; i < sourceStrArray.length; i++) {
            if (text.contains(sourceStrArray[i])) {
                return false;
            }
        }

        return true;
    }

    //打开外部浏览器
    public static void openWeb(Context context, String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(intent);
    }

    /**
     * 验证手机格式
     */
    public static boolean isMobile(String number) {
        //"[1]"代表第1位为数字1，"[34578]"代表第二位可以为3、4、5、7、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        String num = "[1][34578]\\d{9}";
        if (TextUtils.isEmpty(number)) {
            return false;
        } else {
            //matches():字符串是否在给定的正则表达式匹配
            return number.matches(num);
        }
    }

    //是否还有下一页
    public static boolean isHasNextPage(int size) {
        return (size % LiveConstant.DATA_DEFINE.PAGE_COUNT) == 0;
    }

    public static void getSign(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    "com.ailiaovideo.videoline",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }


    public static void loadHttpIconImg(Context mContext, String url, ImageView img, int i) {

        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.head_other);

        Glide.with(mContext).load(url).apply(options).into(img);
    }

    public static boolean isBackground() {
        return !CuckooLifecycleHandler.isApplicationInForeground();
    }

    /**
     * Return pseudo unique ID
     * @return ID
     */public static String getUniquePsuedoID() {
        String android_id = Settings.Secure.getString(MyApplication.getInstances().getContentResolver(), Settings.Secure.ANDROID_ID);
        return android_id;

    }
}
