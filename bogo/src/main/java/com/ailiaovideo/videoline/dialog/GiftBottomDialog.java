package com.ailiaovideo.videoline.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.ailiaovideo.videoline.adapter.BannerAdaper;
import com.ailiaovideo.videoline.adapter.recycler.GiftCountAdaper;
import com.ailiaovideo.videoline.ui.RechargeActivity;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ailiaovideo.videoline.R;
import com.ailiaovideo.videoline.adapter.recycler.RecycleViewGiftItemAdapter;
import com.ailiaovideo.videoline.adapter.recycler.RecyclerGiftCount;
import com.ailiaovideo.videoline.api.Api;
import com.ailiaovideo.videoline.json.JsonRequestBase;
import com.ailiaovideo.videoline.json.JsonRequestDoPrivateSendGif;
import com.ailiaovideo.videoline.json.JsonRequestGetGiftList;
import com.ailiaovideo.videoline.manage.SaveData;
import com.ailiaovideo.videoline.modle.GiftModel;
import com.example.zhouwei.library.CustomPopWindow;
import com.lzy.okgo.callback.StringCallback;
import com.qmuiteam.qmui.widget.QMUIPagerAdapter;
import com.qmuiteam.qmui.widget.QMUIViewPager;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by 魏鹏 on 2018/3/6.
 * email:1403102936@qq.com
 * 山东布谷鸟网络科技有限公司著
 */

public class GiftBottomDialog extends BottomSheetDialog implements View.OnClickListener {

    private QMUIViewPager qmuiViewPager, qmuiViewPagerBp;
    private QMUIPagerAdapter qmuiPagerAdapter;
    private RecyclerView mRvGiftCountList;
    private RecyclerGiftCount mGiftCountAdapter;
    private RelativeLayout rl_menu;

    private List<String> mGiftCountList = new ArrayList<>();
    private List<GiftModel> giftModelList = new ArrayList<>();

    private List<GiftModel> myGiftModelList = new ArrayList<>();
    private List<View> giftPageItemList = new ArrayList<>();

    private DoSendGiftListen doSendGiftListen;
    //0 正常聊天赠送礼物 1 一对一视频赠送礼物
    private int type = 0;
    private String chanelId = "";


    public static final String TO_USER_ID = "TO_USER_ID";
    public static final String TYPE = "TYPE";

    private String giftId;
    private String toUserId;
    private Button giftCount;
    private Context context;
    private RecyclerView giftCountPopRv;
    private Button qmuiRoundButton;
    private int giftCountNum = 1;
    private TextView getCoin;
    private TextView tvGift;
    private TextView tvBackpack;
    private boolean isSelectGift = true;

    public GiftBottomDialog(@NonNull Context context, String toUserId) {
        super(context, R.style.dialogGift);
        this.context = context;
        this.toUserId = toUserId;
        init();
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_gift, null);
        setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        //BGViewUtil.setBackgroundDrawable(getContentView(), new BGDrawable().color(Color.parseColor("#50FFFFFF")));

        initView();
        initData();
    }

    private void initData() {

        requestGetGift();
    }

    private void initView() {
        rl_menu = findViewById(R.id.rl_menu);
        tvGift = findViewById(R.id.tv_btn_gift);
        tvBackpack = findViewById(R.id.tv_btn_backpack);

        //礼物数量
        mRvGiftCountList = findViewById(R.id.rv_count_list);
        getCoin = findViewById(R.id.tv_get_money);
        qmuiRoundButton = findViewById(R.id.btn_send);
        giftCount = findViewById(R.id.btn_gift_count);
        qmuiRoundButton.setOnClickListener(this);
        giftCount.setOnClickListener(this);
        getCoin.setOnClickListener(this);
        LinearLayoutManager countLayoutManage = new LinearLayoutManager(getContext());
        countLayoutManage.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRvGiftCountList.setLayoutManager(countLayoutManage);

        mGiftCountList.add("1");
        mGiftCountList.add("5");
        mGiftCountList.add("10");
        mGiftCountList.add("15");
        mGiftCountList.add("20");
        mGiftCountList.add("30");

        mGiftCountAdapter = new RecyclerGiftCount(getContext(), mGiftCountList);
        mRvGiftCountList.setAdapter(mGiftCountAdapter);


        qmuiViewPager = findViewById(R.id.view_page);
        qmuiViewPagerBp = findViewById(R.id.view_page_backpack);

        newAdaper();

        qmuiViewPager.setAdapter(qmuiPagerAdapter);

        selectItem(15, true, R.color.admin_color, 13, false, R.color.white);

        tvBackpack.setOnClickListener(this);
        tvGift.setOnClickListener(this);
    }

    public void hideMenu(){
        rl_menu.setVisibility(View.GONE);
    }

    private QMUIPagerAdapter newAdaper() {


        qmuiPagerAdapter = new QMUIPagerAdapter() {
            @Override
            protected Object hydrate(ViewGroup container, int position) {
                return giftPageItemList.get(position);
            }

            @Override
            protected void populate(ViewGroup container, Object item, int position) {
                View v = giftPageItemList.get(position);
                ViewGroup parent = (ViewGroup) v.getParent();
                if (parent != null) {
                    parent.removeAllViews();
                }
                container.addView(giftPageItemList.get(position));
            }

            @Override
            protected void destroy(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }

            @Override
            public int getCount() {
                return giftPageItemList.size();
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                return view == object;
            }
        };

        return qmuiPagerAdapter;
    }


    private void selectItem(int infoTvSize, boolean infoTvStyle, int black, int videoTvSize, boolean videoTvStyle, int admin_color) {
        tvGift.setTextSize(infoTvSize);
        TextPaint tp1 = tvGift.getPaint();
        tp1.setFakeBoldText(infoTvStyle);
        tvGift.setTextColor(context.getResources().getColor(black));

        tvBackpack.setTextSize(videoTvSize);
        TextPaint tpv1 = tvBackpack.getPaint();
        tpv1.setFakeBoldText(videoTvStyle);
        tvBackpack.setTextColor(context.getResources().getColor(admin_color));
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setChanelId(String chanelId) {
        this.chanelId = chanelId;
    }

    //直接获取礼物列表
    private void requestGetGift() {

        Api.doRequestGetGift(new StringCallback() {

            @Override
            public void onSuccess(String s, Call call, Response response) {

                JsonRequestGetGiftList jsonObj = (JsonRequestGetGiftList) JsonRequestBase.getJsonObj(s, JsonRequestGetGiftList.class);
                if (jsonObj.getCode() == 1) {
                    giftModelList.clear();
                    giftModelList.addAll(jsonObj.getList());

                    refreshGiftAdapter(1, giftModelList);
                } else {
                    ToastUtils.showLong(jsonObj.getMsg());
                }
            }
        });
    }

    //获取背包礼物列表
    private void requestGetMyGift() {

        String uid = SaveData.getInstance().getId();
        String token = SaveData.getInstance().getToken();

        Api.doRequestGetMyGift(uid, token, new StringCallback() {

            @Override
            public void onSuccess(String s, Call call, Response response) {

                JsonRequestGetGiftList jsonObj = (JsonRequestGetGiftList) JsonRequestBase.getJsonObj(s, JsonRequestGetGiftList.class);
                if (jsonObj.getCode() == 1) {
                    myGiftModelList.clear();
                    if (jsonObj.getList() != null && jsonObj.getList().size() > 0) {
                        myGiftModelList.addAll(jsonObj.getList());
                    }

                    refreshGiftAdapter(2, myGiftModelList);
                } else {
                    ToastUtils.showLong(jsonObj.getMsg());
                }
            }
        });
    }

    //刷新礼物列表
    private void refreshGiftAdapter(int i, final List<GiftModel> list) {

        //  1==1 礼物弹窗  i==2背包
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_gift_recycleview, null);
        RecyclerView giftListView = view.findViewById(R.id.rv_content_list);
        final RecycleViewGiftItemAdapter giftItemAdapter = new RecycleViewGiftItemAdapter(getContext(), list, i);
        giftListView.setAdapter(giftItemAdapter);
        giftListView.setLayoutManager(new GridLayoutManager(getContext(), 4));
        giftItemAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                giftCount.setVisibility(View.VISIBLE);
                qmuiRoundButton.setBackgroundResource(R.drawable.gift_send_drawable);


                giftItemAdapter.setSelected(position);

                giftId = list.get(position).getId();
                mGiftCountAdapter.setSelected(-1);
            }
        });

        giftPageItemList.clear();
        giftPageItemList.add(view);


        qmuiPagerAdapter = newAdaper();

        if (i == 1) {
            qmuiViewPager.setAdapter(qmuiPagerAdapter);
        } else {
            qmuiViewPagerBp.setAdapter(qmuiPagerAdapter);

        }
        qmuiPagerAdapter.notifyDataSetChanged();
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_send:
                if (isSelectGift) {
                    Log.e("i", "gift");
                    doSendGift("gift");
                } else {
                    Log.e("i", "backpack");
                    doSendGift("backpack");
                }


                break;

            case R.id.btn_gift_count:
                showPopTop();
                break;

            case R.id.tv_get_money:
                RechargeActivity.startRechargeActivity(context);
                break;

            case R.id.tv_btn_gift:
                selectItem(15, true, R.color.admin_color, 13, false, R.color.white);
                qmuiViewPager.setVisibility(View.VISIBLE);
                qmuiViewPagerBp.setVisibility(View.GONE);
                isSelectGift = true;

                //防止切换后前台没显示选中后台数据已经被选中
                restarLayout();

                requestGetGift();
                break;
            case R.id.tv_btn_backpack:

                selectItem(13, false, R.color.white, 15, true, R.color.admin_color);
                qmuiViewPager.setVisibility(View.GONE);
                qmuiViewPagerBp.setVisibility(View.VISIBLE);
                isSelectGift = false;

                //防止切换后前台没显示选中后台数据已经被选中
                restarLayout();

                requestGetMyGift();
                break;
            default:
                break;
        }
    }

    private void restarLayout() {
        qmuiRoundButton.setBackgroundResource(R.drawable.gift_sendgift_normal_drawable);
        giftCount.setVisibility(View.GONE);
        giftCountNum = 1;
        giftId = "0";
    }


    private void showPopTop() {
        View contentView = LayoutInflater.from(context).inflate(R.layout.gift_count_pop_layout, null);

        final CustomPopWindow popWindow = new CustomPopWindow.PopupWindowBuilder(context)
                .setView(contentView)
                .create();
        popWindow.showAsDropDown(giftCount, giftCount.getHeight() / 7, -(giftCount.getHeight() + popWindow.getHeight() + 10));

        giftCountPopRv = contentView.findViewById(R.id.gift_count_pop_rv);
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        giftCountPopRv.setLayoutManager(linearLayoutManager);

        GiftCountAdaper mAdapter = new GiftCountAdaper(getContext(), mGiftCountList);
        giftCountPopRv.setAdapter(mAdapter);

        mAdapter.setClickItemListener(new GiftCountAdaper.ClickItemListener() {
            @Override
            public void onItemClickListener(int posi) {
//                ToastUtils.showLong(posi + "");
                popWindow.dissmiss();
                giftCountNum = Integer.parseInt(mGiftCountList.get(posi));
                giftCount.setText("x" + giftCountNum);
            }
        });
    }

    public void setDoSendGiftListen(DoSendGiftListen doSendGiftListent) {
        this.doSendGiftListen = doSendGiftListent;
    }


    private void doSendGift(final String where) {

        String uid = SaveData.getInstance().getId();
        String token = SaveData.getInstance().getToken();

        String count = "";
        count = giftCountNum + "";

        String getUrl = "";
        if ("gift".equals(where)) {
            getUrl = "/deal_api/send_gift";
        } else {
            getUrl = "/deal_api/send_bag_gift";
        }

        Api.doSendGift(uid, token, count, toUserId, giftId, chanelId, getUrl, new StringCallback() {

            @Override
            public void onSuccess(String s, Call call, Response response) {

                JsonRequestDoPrivateSendGif jsonObj = (JsonRequestDoPrivateSendGif)
                        JsonRequestBase.getJsonObj(s, JsonRequestDoPrivateSendGif.class);
                if (jsonObj.getCode() == 1) {

                    if (doSendGiftListen != null) {
                        doSendGiftListen.onSuccess(jsonObj);
                    }

                    //背包送完礼物后要刷新
                    if (!"gift".equals(where)) {
                        //初始化布局，防止未选中却能送情况
                        restarLayout();
                        //送完重新拉区数据
                        requestGetMyGift();
                    }

                } else {

                    ToastUtils.showLong(jsonObj.getMsg());
                }

            }
        });
    }

    public interface DoSendGiftListen {

        void onSuccess(JsonRequestDoPrivateSendGif sendGif);
    }
}
