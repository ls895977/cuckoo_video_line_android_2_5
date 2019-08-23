package com.ailiaovideo.videoline.fragment;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ConvertUtils;
import com.ailiaovideo.videoline.R;
import com.ailiaovideo.videoline.adapter.FragAdapter;
import com.ailiaovideo.videoline.api.Api;
import com.ailiaovideo.videoline.base.BaseFragment;
import com.ailiaovideo.videoline.json.JsonGetIsAuth;
import com.ailiaovideo.videoline.json.JsonRequestBase;
import com.ailiaovideo.videoline.manage.SaveData;
import com.ailiaovideo.videoline.modle.ConfigModel;
import com.ailiaovideo.videoline.modle.StarLevelModel;
import com.ailiaovideo.videoline.ui.PushDynamicActivity;
import com.lzy.okgo.callback.StringCallback;
import com.qmuiteam.qmui.widget.QMUIViewPager;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * 动态
 */
public class DynamicFragment extends BaseFragment {

    private QMUIViewPager rollViewViewpage;
    private TabLayout dynamicTablayout;
    private List fragmentList;
    private List titleList;
    private FragAdapter mDynamicFragAdapter;
    private ImageView dynamicIv;

    @Override
    protected View getBaseView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_dynamic_layout, container, false);
    }

    @Override
    protected void initView(View view) {

        dynamicTablayout = view.findViewById(R.id.tabLayout);
        rollViewViewpage = view.findViewById(R.id.roll_view_viewpage);

        dynamicIv = view.findViewById(R.id.iv_dynamic);

        //发动态
        dynamicIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickPushDynamic();
            }
        });
    }

    @Override
    protected void initDate(View view) {
        fragmentList = new ArrayList();
        fragmentList.add(new DynamicRecommeFragment());
        fragmentList.add(new DynamicAttentionFragment());

        titleList = new ArrayList();
        titleList.add(getString(R.string.dynamic_recome));
        titleList.add(getString(R.string.dynamic_attention));


        ArrayList<StarLevelModel> list = ConfigModel.getInitData().getStar_level_list();
        if (list != null) {
            for (StarLevelModel item : list) {

                StarLevelListFragment levelListFragment = new StarLevelListFragment();
                Bundle intent = new Bundle();
                intent.putString(StarLevelListFragment.LEVEL_ID, item.getId());
                levelListFragment.setArguments(intent);
                fragmentList.add(levelListFragment);
                titleList.add(item.getLevel_name());
            }
        }


    }

    @Override
    protected void initSet(View view) {

        rollViewViewpage.setOffscreenPageLimit(3);

        mDynamicFragAdapter = new FragAdapter(getChildFragmentManager(), fragmentList);
        mDynamicFragAdapter.setTitleList(titleList);
        rollViewViewpage.setAdapter(mDynamicFragAdapter);

        dynamicTablayout.setupWithViewPager(rollViewViewpage);

        //初始化tabbar
        initTabBar();


    }

    private void initTabBar() {

        for (int i = 0; i < mDynamicFragAdapter.getCount(); i++) {
            //获取每一个tab对象
            TabLayout.Tab tabAt = dynamicTablayout.getTabAt(i);
            //将每一个条目设置我们自定义的视图
            tabAt.setCustomView(R.layout.dynamic_text_view_layout);

            //默认选中第一个
            if (i == 0) {

                // 设置第一个tab的TextView是被选择的样式
                tabAt.getCustomView().findViewById(R.id.tv_tab).setSelected(true);//第一个tab被选中
                //设置选中标签的文字大小
                ((TextView) tabAt.getCustomView().findViewById(R.id.tv_tab)).setTextSize(16);
                //控制底部横线
                tabAt.getCustomView().findViewById(R.id.tab_item_indicator).setVisibility(View.VISIBLE);
                //设置加粗
                TextPaint tp = ((TextView) tabAt.getCustomView().findViewById(R.id.tv_tab)).getPaint();
                tp.setFakeBoldText(true);

            } else {
                //通过tab对象找到自定义视图的ID

                ((TextView) tabAt.getCustomView().findViewById(R.id.tv_tab)).setTextSize(16);

                //控制底部横线
                tabAt.getCustomView().findViewById(R.id.tab_item_indicator).setVisibility(View.INVISIBLE);

                //设置加粗
                TextPaint tp = ((TextView) tabAt.getCustomView().findViewById(R.id.tv_tab)).getPaint();
                tp.setFakeBoldText(false);
            }

            TextView textView = (TextView) tabAt.getCustomView().findViewById(R.id.tv_tab);
            textView.setText(titleList.get(i) + "");//设置tab上的文字

        }


        dynamicTablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                updateTabView(tab, true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                updateTabView(tab, false);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    //发布动态
    private void clickPushDynamic() {

        Api.doRequestGetIsAuth(SaveData.getInstance().getId(),SaveData.getInstance().getToken(),new StringCallback(){

            @Override
            public void onSuccess(String s, Call call, Response response) {
                JsonGetIsAuth data = (JsonGetIsAuth) JsonRequestBase.getJsonObj(s,JsonGetIsAuth.class);
                if(data.getCode() == 1){
                    if(data.getIs_auth() == 1){
                        Intent intent = new Intent(getContext(), PushDynamicActivity.class);
                        startActivity(intent);
                    }else{
                        showToastMsg(getContext(),"未认证不能发布动态！");
                    }
                }else{
                    showToastMsg(getContext(),"未认证不能发布动态！");
                }
            }
        });
    }

    /**
     * 用来改变tabLayout选中后的字体大小及颜色
     *
     * @param tab
     * @param isSelect
     */
    private void updateTabView(TabLayout.Tab tab, boolean isSelect) {
        //找到自定义视图的控件ID
        TextView tv_tab = (TextView) tab.getCustomView().findViewById(R.id.tv_tab);
        View tab_item_indicator = (View) tab.getCustomView().findViewById(R.id.tab_item_indicator);
        if (isSelect) {
            //设置标签选中
            tv_tab.setSelected(true);
            //选中后字体变大
            tv_tab.setTextSize(16);
            TextPaint tp = tv_tab.getPaint();
            tp.setFakeBoldText(true);
            tv_tab.setTextColor(getResources().getColor(R.color.black));
            tab_item_indicator.setVisibility(View.VISIBLE);

        } else {
            //设置标签取消选中
            tv_tab.setSelected(false);
            //恢复为默认字体大小
            tv_tab.setTextSize(16);
            TextPaint tp = tv_tab.getPaint();
            tp.setFakeBoldText(false);
            tv_tab.setTextColor(getResources().getColor(R.color.gray));
            tab_item_indicator.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void initDisplayData(View view) {

    }
}
