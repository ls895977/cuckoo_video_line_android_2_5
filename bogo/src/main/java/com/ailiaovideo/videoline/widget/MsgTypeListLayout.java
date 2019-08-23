package com.ailiaovideo.videoline.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.ailiaovideo.videoline.R;
import com.ailiaovideo.videoline.inter.AdapterOnItemClick;
import com.ailiaovideo.videoline.msg.adapter.MsgTypeListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 消息页底部功能消息列表
 * Created by jiahengfei on 2018/1/17 0017.
 */

public class MsgTypeListLayout extends FrameLayout{
    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;
    private MsgTypeListAdapter msgTypeListAdapter;
    private OnItemclick onItemclick;

    private List<Integer> imgs = new ArrayList<>();
    private List<String> strings = new ArrayList<>();

    public MsgTypeListLayout(@NonNull Context context,OnItemclick onItemclick) {
        super(context);
        this.onItemclick = onItemclick;
        init(context);
    }

    public MsgTypeListLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MsgTypeListLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(final Context context){
        LayoutInflater.from(context).inflate(R.layout.view_msglist_function,this);
        recyclerView = findViewById(R.id.listmsg_recycler);

        gridLayoutManager = new GridLayoutManager(context,4);
        initData();
        recyclerView.setLayoutManager(gridLayoutManager);

        msgTypeListAdapter = new MsgTypeListAdapter(imgs,strings);

        msgTypeListAdapter.setOnItemClickListener(new AdapterOnItemClick() {
            @Override
            public void onItemClick(View view, ViewName viewName, int position) {
                onItemclick.onItemClick(view,viewName,position);
            }
        });

        recyclerView.setAdapter(msgTypeListAdapter);
    }

    private void initData() {
        imgs.add(R.drawable.helper_photo_btn);
        imgs.add(R.drawable.helper_video_btn);
        imgs.add(R.drawable.helper_grid_btn);

        strings.add("私照");
        strings.add("视频");
        strings.add("礼物");
    }

    public interface OnItemclick{
        void onItemClick(View view, AdapterOnItemClick.ViewName viewName, int position);
    }
}
