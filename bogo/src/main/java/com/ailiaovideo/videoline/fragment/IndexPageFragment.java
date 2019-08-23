package com.ailiaovideo.videoline.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;


import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.ailiaovideo.videoline.R;
import com.ailiaovideo.videoline.adapter.FragAdapter;
import com.ailiaovideo.videoline.base.BaseFragment;
import com.ailiaovideo.videoline.modle.ConfigModel;
import com.ailiaovideo.videoline.modle.StarLevelModel;
import com.ailiaovideo.videoline.ui.CuckooSearchActivity;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITabSegment;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.QMUIViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;

/**
 * 排行-推荐 页
 * Created by 魏鹏 on 2017/12/27 0027.
 */

public class IndexPageFragment extends BaseFragment {
    //功能
    private QMUIViewPager rollViewViewpage;
    private QMUITopBar rollTopBar;
    private QMUITabSegment rollTabSegment;
    private View topBarView;

    private List fragmentList;
    private List titleList;

    private FragAdapter mFragAdapter;
    private TabLayout tablayout;

    @Override
    protected View getBaseView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_index_roll, container, false);
    }

    @Override
    protected void initView(View view) {

//        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        rollTopBar = view.findViewById(R.id.roll_tab_segment);
        rollViewViewpage = view.findViewById(R.id.roll_view_viewpage);

        //顶部bar布局
        topBarView = LayoutInflater.from(getContext()).inflate(R.layout.view_top_page, null);
        rollTabSegment = topBarView.findViewById(R.id.mQMUITabSegment);


        //new tablayout
        tablayout = view.findViewById(R.id.tabLayout);

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
    protected void initDate(View view) {
        fragmentList = new ArrayList();
        fragmentList.add(new IndexTabAttentionFragment());
        fragmentList.add(new RecommendFragment());
        fragmentList.add(new NewPeopleFragment());
        //fragmentList.add(new CharmFragment());
        //fragmentList.add(new MoneyFragment());
        //fragmentList.add(new SameCityFragment());
        fragmentList.add(new OnlineFragment());

        titleList = new ArrayList();
        titleList.add(getString(R.string.follow));
        titleList.add(getString(R.string.recommend));
        titleList.add(getString(R.string.novice));
        titleList.add(getString(R.string.on_line));
        //titleList.add("魅力");
        //titleList.add("财气");
        //titleList.add("同城");


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
        rollTopBar.setCenterView(topBarView);
        rollViewViewpage.setOffscreenPageLimit(1);
        mFragAdapter = new FragAdapter(getChildFragmentManager(), fragmentList);
        mFragAdapter.setTitleList(titleList);
        rollViewViewpage.setAdapter(mFragAdapter);

        tablayout.setupWithViewPager(rollViewViewpage);


        for (int i = 0; i < mFragAdapter.getCount(); i++) {
            //获取每一个tab对象
            TabLayout.Tab tabAt = tablayout.getTabAt(i);
            //将每一个条目设置我们自定义的视图
            tabAt.setCustomView(R.layout.home_textz_view_layout);
            //默认选中第一个
            if (i == 1) {
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


        tablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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

        //设置字体大小
        rollTabSegment.setTabTextSize(ConvertUtils.dp2px(15));
        //设置 Tab 选中状态下的颜色
        rollTabSegment.setDefaultSelectedColor(getResources().getColor(R.color.admin_color));
        rollTabSegment.setDefaultNormalColor(getResources().getColor(R.color.gray));
        //关联viewPage
        rollTabSegment.setupWithViewPager(rollViewViewpage, true, false);
        rollViewViewpage.setCurrentItem(1);
    }

    @Override
    protected void initDisplayData(View view) {

    }

    @OnClick({R.id.iv_search})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_search:
                clickSearch();
                break;

            default:
                break;
        }
    }

    private void clickSearch() {
        Intent intent = new Intent(getContext(), CuckooSearchActivity.class);
        startActivity(intent);
    }

}
