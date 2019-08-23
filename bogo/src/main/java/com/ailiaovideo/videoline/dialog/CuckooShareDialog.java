package com.ailiaovideo.videoline.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.ClipboardManager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.ailiaovideo.videoline.R;
import com.ailiaovideo.videoline.modle.ConfigModel;
import com.ailiaovideo.videoline.utils.ImageUtils;
import com.ailiaovideo.videoline.utils.QRCodeUtil;
import com.ailiaovideo.videoline.utils.StringUtils;
import com.ailiaovideo.videoline.widget.CuckooShareDialogView;

import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * @author 魏鹏
 * email 1403102936@qq.com
 * 山东布谷鸟网络科技有限公司著
 * @dw 分享Dialog
 */
public class CuckooShareDialog extends Dialog implements CuckooShareDialogView.CuckooShareDialogViewCallback {

    private CuckooShareDialogView view;
    private String shareUrl;
    private String img;
    private Context context;

    public CuckooShareDialog(@NonNull Context context) {
        super(context, R.style.share_dialog);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        view = new CuckooShareDialogView(getContext());
        //view.setLayoutParams(new WindowManager.LayoutParams(ScreenUtils.getScreenWidth(), ViewGroup.LayoutParams.WRAP_CONTENT));
        setContentView(view);//这行一定要写在前面
        setCancelable(true);//点击外部不可dismiss
        setCanceledOnTouchOutside(true);
        Window window = this.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.getDecorView().setPadding(0, 0, 0, 0); //消除边距
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);

        view.setCallback(this);

        ConfigModel configModel = ConfigModel.getInitData();
        view.setShowItem(StringUtils.toInt(configModel.getOpen_login_qq()) == 1, StringUtils.toInt(configModel.getOpen_login_wx()) == 1, true,true);

    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    @Override
    public void onClickWeChat() {
        showShare(Wechat.NAME);
    }

    @Override
    public void onClickPyq() {

        showShare(WechatMoments.NAME);
    }

    @Override
    public void copyUrl() {


        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        // 将文本内容放到系统剪贴板里。
        cm.setText(shareUrl);
        ToastUtils.showLong("复制成功，可以发给朋友们了。");
    }


    @Override
    public void onClickQQ() {
        showShare(QQ.NAME);
    }

    @Override
    public void onClickQrcode() {

        final Bitmap bitmap = QRCodeUtil.createQRCodeBitmap(shareUrl, ConvertUtils.dp2px(200), ConvertUtils.dp2px(250));
        Dialog dialog = new Dialog(getContext(), R.style.dialogBase);

        View view = View.inflate(getContext(), R.layout.dialog_qrcode, null);
        ImageView codeImg = view.findViewById(R.id.iv_qrcode);
        codeImg.setImageBitmap(bitmap);
        view.findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ImageUtils.saveImageToGallery(getContext(), bitmap);
                //MediaStore.Images.Media.insertImage(getContext().getContentResolver(), bitmap, "邀请二维码", "description");
                ToastUtils.showLong("保存成功！");
            }
        });

        dialog.setContentView(view);
        dialog.show();
    }

    private void showShare(String platform) {
        OnekeyShare oks = new OnekeyShare();
        //指定分享的平台，如果为空，还是会调用九宫格的平台列表界面
        if (platform != null) {
            oks.setPlatform(platform);
        }
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(ConfigModel.getInitData().getShare_title());
        // titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
        oks.setTitleUrl(shareUrl);
        // text是分享文本，所有平台都需要这个字段
        oks.setText(ConfigModel.getInitData().getShare_content());
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        //oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath(img);//确保SDcard下面存在此张图片
        oks.setImageUrl(img);
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(shareUrl);
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        //oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        //oks.setSite("ShareSDK");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        //oks.setSiteUrl("http://sharesdk.cn");

        //启动分享
        oks.show(getContext());
    }
}