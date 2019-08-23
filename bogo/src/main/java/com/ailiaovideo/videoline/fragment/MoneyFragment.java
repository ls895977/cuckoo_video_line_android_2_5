package com.ailiaovideo.videoline.fragment;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.ailiaovideo.videoline.ui.common.Common;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ailiaovideo.videoline.R;
import com.ailiaovideo.videoline.adapter.recycler.RecyclerRankingAdapter;
import com.ailiaovideo.videoline.api.Api;
import com.ailiaovideo.videoline.api.ApiUtils;
import com.ailiaovideo.videoline.base.BaseFragment;
import com.ailiaovideo.videoline.helper.SelectResHelper;
import com.ailiaovideo.videoline.inter.JsonCallback;
import com.ailiaovideo.videoline.json.JsonRequestRank;
import com.ailiaovideo.videoline.modle.RankModel;
import com.ailiaovideo.videoline.ui.HomePageActivity;
import com.ailiaovideo.videoline.utils.StringUtils;
import com.ailiaovideo.videoline.utils.Utils;
import com.ailiaovideo.videoline.widget.GradeShowLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 财气
 * Created by fly on 2017/12/28 0028.
 */
public class MoneyFragment extends BaseFragment implements RadioGroup.OnCheckedChangeListener{
    private RadioGroup radioGroup;//按钮组容器
    private RadioButton radioDays,radioWeeks,radioRalls;//单选按钮组
    private TextView textMyRanking;//我的排行
    private SwipeRefreshLayout rankingFresh;//刷新组件
    private RecyclerView rankingRecycler;//数据列表
    private View headView;//头部信息
    //适配器
    private RecyclerRankingAdapter recyclerRankingAdapter;
    //数据
    private List<RankModel> charmDatas = new ArrayList<>();

    //头部数据
    private CircleImageView headImg1;
    private TextView nickName1;
    private TextView money1;
    private TextView location1;
    private ImageView isOnline1;
    private TextView tv_level1;
    private LinearLayout llRank1;
    private RankModel charmData1;


    //////////////////////////////////////////初始化操作//////////////////////////////////////////////
    @Override
    protected View getBaseView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_money_list, container,false);
    }

    @Override
    protected void initView(View view) {
        //主页信息
        radioGroup = view.findViewById(R.id.ranking_radio_group);
        textMyRanking = view.findViewById(R.id.text_my_ranking);
        rankingFresh  =view.findViewById(R.id.ranking_fresh);
        rankingRecycler = view.findViewById(R.id.ranking_recyclerview);
        radioDays = view.findViewById(R.id.radio_days);
        radioWeeks = view.findViewById(R.id.radio_weeks);
        radioRalls = view.findViewById(R.id.radio_alls);

        if(headView == null){

            //引入头部信息
            headView = LayoutInflater.from(getContext()).inflate(R.layout.view_money_one,null);

            headImg1 = headView.findViewById(R.id.ranking_one_headimg);
            nickName1 = headView.findViewById(R.id.rankinghead_bar_title);
            money1 = headView.findViewById(R.id.left_text);
            location1 = headView.findViewById(R.id.right_text);
            isOnline1 = headView.findViewById(R.id.rankinghead_bar_isonLine);
            llRank1 = headView.findViewById(R.id.number_one);
            tv_level1 = headView.findViewById(R.id.tv_level);
        }

    }

    @Override
    protected void initDate(View view) {

    }

    @Override
    public void fetchData() {

        //加载假数据源
        requestMoneyData("");//加载排行榜数据
    }

    @Override
    protected void initSet(View view) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        rankingRecycler.setHasFixedSize(true);
        rankingRecycler.setLayoutManager(linearLayoutManager);
        //选择监听
        radioGroup.setOnCheckedChangeListener(this);
        //刷新监听
        rankingFresh.setOnRefreshListener(this);

        if(recyclerRankingAdapter != null){
            recyclerRankingAdapter.removeAllHeaderView();
        }
        //创建并设置Adapter
        recyclerRankingAdapter = new RecyclerRankingAdapter(charmDatas,getContext(),1);
        recyclerRankingAdapter.addHeaderView(headView);
        recyclerRankingAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                Common.jumpUserPage(getContext(),charmDatas.get(position).getId());
            }
        });
        //设置添加适配器
        rankingRecycler.setAdapter(recyclerRankingAdapter);
        refreshAdapter();//刷新适配器
    }

    @Override
    protected void initDisplayData(View view) {
    }


    ////////////////////////////////////////////监听方法处理//////////////////////////////////////////
    @Override
    public void onClick(View v) {

    }

    @Override
    public void onRefresh() {
        // 刷新数据-初始位置/刷新数量
        recyclerRankingAdapter.notifyItemRangeInserted(recyclerRankingAdapter.getItemCount(),charmDatas.size());
        rankingFresh.setRefreshing(false);
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == R.id.radio_days){
            //日榜
            charmDatas.clear();
            requestMoneyData("day");
        }else if (checkedId == R.id.radio_weeks){
            //周榜
            charmDatas.clear();
            requestMoneyData("week");
        }else if (checkedId == R.id.radio_month){
            //周榜
            charmDatas.clear();
            requestMoneyData("month");
        }else{
            //总榜
            charmDatas.clear();
            requestMoneyData("");
        }
    }

    ////////////////////////////////////////////本地工具方法//////////////////////////////////////////

    /**
     * 加载财气排行榜数据
     */
    private void requestMoneyData(String type) {
        Api.getMoneyListData(
                uId,
                uToken,
                type,
                0,
                new JsonCallback() {
                    @Override
                    public Context getContextToJson() {
                        return getContext();
                    }
                    @Override
                    public void onSuccess(String s, Call call, Response response) {

                        JsonRequestRank requestObj = JsonRequestRank.getJsonObj(s);
                        if (requestObj.getCode() == 1){
                            List<RankModel> charmArray = requestObj.getList();
                            textMyRanking.setText(String.format(Locale.CHINA,"我的排名：%s",requestObj.getOrder_num()));
                            charmDatas.clear();
                            charmDatas.addAll(charmArray);

                            if (charmDatas.size() > 0){
                                //设置1
                                charmData1 = charmDatas.get(0);
                                setRes(charmData1,headImg1,nickName1,money1,location1,isOnline1,tv_level1);
                                charmDatas.remove(charmData1);
                                llRank1.setVisibility(View.VISIBLE);

                            }else{
                                llRank1.setVisibility(View.GONE);
                            }

                            //设置其他
                            refreshAdapter();
                        }
                    }
                }
        );
    }

    /**
     * 刷新适配器
     */
    private void refreshAdapter() {
        recyclerRankingAdapter.notifyDataSetChanged();
        LogUtils.i("设置添加适配器" + getClass().getName());
    }

    /**
     * 设置资源
     * @param charmData 资源对象
     * @param headImg 头像
     * @param nickname 昵称
     * @param money 钱数
     * @param location 地址
     * @param isOnline 是否在线
     * @param grid 等级
     */
    private void setRes(final RankModel charmData, CircleImageView headImg, TextView nickname, TextView money, TextView location, ImageView isOnline, TextView grid){
        if (ApiUtils.isTrueUrl(charmData.getAvatar())){
            Utils.loadHttpImg(getContext(),Utils.getCompleteImgUrl(charmData.getAvatar()),headImg);
        }
        nickname.setText(charmData.getUser_nickname());
        money.setText(charmData.getTotal());
        location.setText(charmData.getAddress());
        //设置跳转监听
        headImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Common.jumpUserPage(getContext(),charmData.getId());
            }
        });
        isOnline.setBackgroundResource(SelectResHelper.getOnLineRes(StringUtils.toInt(charmData.getIs_online())));

        grid.setBackgroundResource(R.drawable.bg_org_num);
        grid.setText("V " + charmData.getLevel());
    }
}
