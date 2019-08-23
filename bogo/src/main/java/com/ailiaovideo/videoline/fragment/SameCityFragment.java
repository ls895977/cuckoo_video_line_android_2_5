package com.ailiaovideo.videoline.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ailiaovideo.videoline.R;
import com.ailiaovideo.videoline.base.BaseFragment;

/**
 * 同城
 * Created by fly on 2017/12/28 0028.
 */
public class SameCityFragment extends BaseFragment{
    //功能
    private LinearLayout sameCityIsNull;
    private TextView sameCityBtn;

    //////////////////////////////////////////初始化操作//////////////////////////////////////////////
    @Override
    protected View getBaseView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_same_city, container,false);
    }

    @Override
    protected void initView(View view) {
        sameCityIsNull = view.findViewById(R.id.same_city_isNull);
        sameCityBtn = view.findViewById(R.id.same_city_btn);
    }

    @Override
    protected void initDate(View view) {

    }

    @Override
    protected void initSet(View view) {
        sameCityBtn.setOnClickListener(this);
    }

    @Override
    protected void initDisplayData(View view) {

    }


    ////////////////////////////////////////////监听方法处理//////////////////////////////////////////
    @Override
    public void onClick(View v) {
        //跳转页面
        showToastMsg(getContext(),"升级功能维护中!");
    }
}
