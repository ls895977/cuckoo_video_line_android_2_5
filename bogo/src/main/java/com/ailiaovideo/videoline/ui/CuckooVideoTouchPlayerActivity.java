package com.ailiaovideo.videoline.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.ailiaovideo.videoline.MyApplication;
import com.ailiaovideo.videoline.R;
import com.ailiaovideo.videoline.adapter.VideoPlayerAdapter;
import com.ailiaovideo.videoline.api.Api;
import com.ailiaovideo.videoline.api.ApiUtils;
import com.ailiaovideo.videoline.base.BaseActivity;
import com.ailiaovideo.videoline.event.CuckooOnTouchShortVideoChangeEvent;
import com.ailiaovideo.videoline.inter.JsonCallback;
import com.ailiaovideo.videoline.json.JsonRequestsVideo;
import com.ailiaovideo.videoline.json.jsonmodle.VideoModel;
import com.ailiaovideo.videoline.utils.StringUtils;
import com.lzy.okgo.callback.StringCallback;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import fr.castorflex.android.verticalviewpager.VerticalViewPager;
import okhttp3.Call;
import okhttp3.Response;

/**
 * @author 魏鹏
 * email 1403102936@qq.com
 * 山东布谷鸟网络科技有限公司著
 * @dw 短视频
 */
public class CuckooVideoTouchPlayerActivity extends BaseActivity {

    public static final String VIDEO_LIST = "VIDEO_LIST";
    public static final String VIDEO_POS = "VIDEO_POS";
    public static final String VIDEO_LIST_PAGE = "VIDEO_LIST_PAGE";
    public static final String VIDEO_TYPE = "VIDEO_TYPE";

    @BindView(R.id.vertical_view_page)
    VerticalViewPager vertical_view_page;

    private VideoPlayerAdapter videoPlayerAdapter;

    private List<VideoModel> videos = new ArrayList<>();
    public static int select_video_id = 0;
    private int videoPage = 1;
    private String requestType = "";


    @Override
    protected Context getNowContext() {
        return this;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_cuckoo_video_touch_player;
    }

    @Override
    protected void initView() {
        QMUIStatusBarHelper.translucent(this); // 沉浸式状态栏
//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        videoPlayerAdapter = new VideoPlayerAdapter(getSupportFragmentManager(), videos);
        vertical_view_page.setAdapter(videoPlayerAdapter);
        vertical_view_page.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                select_video_id = StringUtils.toInt(videos.get(position).getId());
                CuckooOnTouchShortVideoChangeEvent event = new CuckooOnTouchShortVideoChangeEvent();
                EventBus.getDefault().post(event);

                if (position == videos.size() - 1) {
                    requestData(false);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void initSet() {

    }

    @Override
    protected void initData() {
        videos.addAll(getIntent().<VideoModel>getParcelableArrayListExtra(VIDEO_LIST));
        int selectPos = getIntent().getIntExtra(VIDEO_POS, 0);
        videoPage = getIntent().getIntExtra(VIDEO_LIST_PAGE, 1);
        requestType = getIntent().getStringExtra(VIDEO_TYPE);
        //默认第一个选中播放
        if (videos.size() > 0) {
            select_video_id = StringUtils.toInt(videos.get(selectPos).getId());
        }
        vertical_view_page.setCurrentItem(selectPos);
        vertical_view_page.setOffscreenPageLimit(1);
        videoPlayerAdapter.notifyDataSetChanged();
    }

    @Override
    protected void initPlayerDisplayData() {

    }

    private void requestData(boolean b) {

        videoPage++;
        Api.getVideoPageList(
                uId,
                uToken,
                requestType,
                videoPage,
                MyApplication.getInstances().getLocation().get("lat"),
                MyApplication.getInstances().getLocation().get("lng"),
                new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {

                        JsonRequestsVideo requestObj = JsonRequestsVideo.getJsonObj(s);
                        if (requestObj.getCode() == 1) {
                            videos.addAll(requestObj.getData());
                            videoPlayerAdapter.notifyDataSetChanged();
                        }
                    }
                }
        );
    }

    public static void startCuckooVideo() {

    }
}
