package com.ailiaovideo.videoline.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.ailiaovideo.videoline.R;
import com.ailiaovideo.videoline.adapter.CuckooRewardCoinAdapter;
import com.ailiaovideo.videoline.api.Api;
import com.ailiaovideo.videoline.api.ApiUtils;
import com.ailiaovideo.videoline.json.JsonRequestBase;
import com.ailiaovideo.videoline.json.JsonRequestGetRewardRule;
import com.ailiaovideo.videoline.manage.SaveData;
import com.ailiaovideo.videoline.modle.ConfigModel;
import com.ailiaovideo.videoline.modle.RewardCoinModel;
import com.ailiaovideo.videoline.utils.DialogHelp;
import com.ailiaovideo.videoline.utils.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * @dw 打赏虚拟币Dialog
 * @author 魏鹏
 * email 1403102936@qq.com
 * 山东布谷鸟网络科技有限公司著
 * */
public class CuckooRewardCoinDialog extends BottomSheetDialog implements BaseQuickAdapter.OnItemClickListener {

    private RecyclerView rvContentList;
    private List<RewardCoinModel> listData = new ArrayList<>();
    private CuckooRewardCoinAdapter cuckooRewardCoinAdapter;


    public CuckooRewardCoinDialog(@NonNull Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_reward_coin,null);
        setContentView(view,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ConvertUtils.dp2px(200)));

        initView();
        initData();
    }

    private void initData() {
        requestGetRewardRule();
    }

    private void initView() {
        rvContentList = findViewById(R.id.rv_content_list);
        rvContentList.setLayoutManager(new GridLayoutManager(getContext(),3));

        cuckooRewardCoinAdapter = new CuckooRewardCoinAdapter(listData);
        rvContentList.setAdapter(cuckooRewardCoinAdapter);
        cuckooRewardCoinAdapter.setOnItemClickListener(this);

    }


    private void requestGetRewardRule() {

        Api.doRequestGetRewardCoinRule(new StringCallback(){

            @Override
            public void onSuccess(String s, Call call, Response response) {

                JsonRequestGetRewardRule data = (JsonRequestGetRewardRule) JsonRequestBase.getJsonObj(s,JsonRequestGetRewardRule.class);
                if(StringUtils.toInt(data.getCode()) == 1){
                    listData.clear();
                    listData.addAll(data.getList());
                    cuckooRewardCoinAdapter.refresh();
                }else{
                    ToastUtils.showLong("请求错误！");
                }
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);

                ToastUtils.showLong("请求错误！");
            }
        });
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        final RewardCoinModel model = listData.get(position);

        DialogHelp.getConfirmDialog(getContext(), "打赏" + model.getReward_coin_num() + ConfigModel.getInitData().getCurrency_name(), new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                clickReward(model.getId());
            }
        }).show();
    }

    private void clickReward(String id) {

        Api.doRewardCoin(SaveData.getInstance().getId(),SaveData.getInstance().getToken(),id,new StringCallback(){

            @Override
            public void onSuccess(String s, Call call, Response response) {

                JsonRequestBase data = JsonRequestBase.getJsonObj(s,JsonRequestBase.class);
                if(StringUtils.toInt(data.getCode()) == 1){
                    ToastUtils.showLong("打赏成功！");
                }else{
                    ToastUtils.showLong(data.getMsg());
                }
            }
        });
    }
}
