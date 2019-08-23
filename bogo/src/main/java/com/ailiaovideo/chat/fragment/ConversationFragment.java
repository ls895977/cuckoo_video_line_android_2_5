package com.ailiaovideo.chat.fragment;


import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ailiaovideo.chat.adapter.ConversationRvAdapter;
import com.ailiaovideo.videoline.manage.RequestConfig;
import com.ailiaovideo.videoline.modle.ConfigModel;
import com.ailiaovideo.videoline.utils.DialogHelp;
import com.blankj.utilcode.util.LogUtils;
import com.ailiaovideo.chat.adapter.ConversationAdapter;
import com.ailiaovideo.chat.model.Conversation;
import com.ailiaovideo.chat.model.CustomMessage;
import com.ailiaovideo.chat.model.MessageFactory;
import com.ailiaovideo.chat.model.NomalConversation;
import com.ailiaovideo.chat.utils.PushUtil;
import com.ailiaovideo.videoline.MyApplication;
import com.ailiaovideo.videoline.R;
import com.ailiaovideo.videoline.api.Api;
import com.ailiaovideo.videoline.json.JsonRequestGetConversationUserInfo;
import com.ailiaovideo.videoline.modle.UserModel;
import com.ailiaovideo.videoline.utils.Utils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.chad.library.adapter.base.listener.OnItemSwipeListener;
import com.lzy.okgo.callback.StringCallback;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.group.TIMGroupCacheInfo;
import com.tencent.imsdk.ext.group.TIMGroupPendencyItem;
import com.tencent.imsdk.ext.sns.TIMFriendFutureItem;
import com.tencent.qcloud.presentation.presenter.ConversationPresenter;
import com.tencent.qcloud.presentation.presenter.FriendshipManagerPresenter;
import com.tencent.qcloud.presentation.presenter.GroupManagerPresenter;
import com.tencent.qcloud.presentation.viewfeatures.ConversationView;
import com.tencent.qcloud.presentation.viewfeatures.FriendshipMessageView;
import com.tencent.qcloud.presentation.viewfeatures.GroupManageMessageView;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import com.yanzhenjie.recyclerview.swipe.touch.OnItemMoveListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * 会话列表界面
 */
public class ConversationFragment extends Fragment implements ConversationView, FriendshipMessageView, GroupManageMessageView {

    private final String TAG = "ConversationFragment";

    private View view;
    private List<Conversation> conversationList = new LinkedList<>();
    private ConversationRvAdapter adapter;
    private RecyclerView listView;
    private ConversationPresenter presenter;
    private FriendshipManagerPresenter friendshipManagerPresenter;
    private GroupManagerPresenter groupManagerPresenter;
    //    private List<String> groupList;
    //private FriendshipConversation friendshipConversation;
    //private GroupManageConversation groupManageConversation;

    /**
     * 控制拉取数据并刷新数据   新消息刷新
     */
    private boolean hasInfo = false;

    private boolean isRefreshSuccess = true;
    private int requestCode = 0;

    private String nowGetInfoIds = "";

    public ConversationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_conversation, container, false);
            listView = (RecyclerView) view.findViewById(R.id.list);
            listView.setLayoutManager(new LinearLayoutManager(getContext()));
            adapter = new ConversationRvAdapter(getActivity(), R.layout.item_conversation, conversationList);
            listView.setAdapter(adapter);

            friendshipManagerPresenter = new FriendshipManagerPresenter(this);
            groupManagerPresenter = new GroupManagerPresenter(this);
            presenter = new ConversationPresenter(this);
            presenter.getConversation();

            adapter.setOnItemClickListener(new ConversationRvAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    conversationList.get(position).navToDetail(getActivity());
                }
            });

            OnItemSwipeListener onItemSwipeListener = new OnItemSwipeListener() {
                @Override
                public void onItemSwipeStart(RecyclerView.ViewHolder viewHolder, int pos) {
                    LogUtils.i("onItemSwipeStart");
                }

                @Override
                public void clearView(RecyclerView.ViewHolder viewHolder, int position) {
                    LogUtils.i("clearView");

                }

                @Override
                public void onItemSwiped(RecyclerView.ViewHolder viewHolder, int position) {
                    LogUtils.i("onItemSwiped");
                    //此方法在Item在侧滑删除时被调用。
                    // 从数据源移除该Item对应的数据，并刷新Adapter。

                    NomalConversation conversation = (NomalConversation) conversationList.get(position);

                    if (conversation != null) {
                        if (presenter.delConversation(conversation.getType(), conversation.getIdentify())) {
                            conversationList.remove(conversation);
                            adapter = new ConversationRvAdapter(getActivity(), R.layout.item_conversation, conversationList);
                            listView.setAdapter(adapter);
                            adapter.setOnItemClickListener(new ConversationRvAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                    conversationList.get(position).navToDetail(getActivity());
                                }
                            });
                            refresh();
                        }
                    }
                }

                @Override
                public void onItemSwipeMoving(Canvas canvas, RecyclerView.ViewHolder viewHolder, float dX, float dY, boolean isCurrentlyActive) {
                    LogUtils.i("onItemSwipeMoving");

                }
            };

            ItemDragAndSwipeCallback itemDragAndSwipeCallback = new ItemDragAndSwipeCallback(adapter);
            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemDragAndSwipeCallback);
            itemTouchHelper.attachToRecyclerView(listView);

            // 开启滑动删除
            adapter.enableSwipeItem();
            adapter.setOnItemSwipeListener(onItemSwipeListener);


        }


        return view;

    }


    /**
     * 初始化界面或刷新界面
     *
     * @param conversationList //只走一遍
     */
    @Override
    public void initView(List<TIMConversation> conversationList) {

        this.conversationList.clear();

//        groupList = new ArrayList<>();

        for (TIMConversation item : conversationList) {
            switch (item.getType()) {
                case C2C:
                case Group:
                    if (!item.getPeer().equals(RequestConfig.getConfigObj().getGroupId())) {
                        //填充数据
                        this.conversationList.add(new NomalConversation(item));
//                    groupList.add(item.getPeer());
                    }
                    break;
            }
        }

        friendshipManagerPresenter.getFriendshipLastMessage();
        groupManagerPresenter.getGroupManageLastMessage();

        getConversationInfo();
    }

    /**
     * 更新最新消息显示
     *
     * @param message 最后一条消息
     */
    @Override
    public void updateMessage(TIMMessage message) {

        if (message == null) {
            adapter.notifyDataSetChanged();
            return;
        }
        if (message.getConversation().getType() == TIMConversationType.System) {
            groupManagerPresenter.getGroupManageLastMessage();
            return;
        }
        //if (MessageFactory.getMessage(message) instanceof CustomMessage) return;
        NomalConversation conversation = new NomalConversation(message.getConversation());
        Iterator<Conversation> iterator = conversationList.iterator();
        while (iterator.hasNext()) {
            Conversation c = iterator.next();
            if (conversation.equals(c)) {
                conversation = (NomalConversation) c;
                iterator.remove();
                break;
            }
        }
        conversation.setLastMessage(MessageFactory.getMessage(message));
        if (!conversation.getIdentify().equals(RequestConfig.getConfigObj().getGroupId())) {
            conversationList.add(conversation);
        }
        Collections.sort(conversationList);
        refresh();
    }

    /**
     * 更新好友关系链消息
     */
    @Override
    public void updateFriendshipMessage() {
        friendshipManagerPresenter.getFriendshipLastMessage();
    }

    /**
     * 删除会话
     *
     * @param identify
     */
    @Override
    public void removeConversation(String identify) {
        Iterator<Conversation> iterator = conversationList.iterator();
        while (iterator.hasNext()) {
            Conversation conversation = iterator.next();
            if (conversation.getIdentify() != null && conversation.getIdentify().equals(identify)) {
                iterator.remove();
                adapter.notifyDataSetChanged();
                return;
            }
        }
    }

    /**
     * 更新群信息
     *
     * @param info
     */
    @Override
    public void updateGroupInfo(TIMGroupCacheInfo info) {
        for (Conversation conversation : conversationList) {
            if (conversation.getIdentify() != null && conversation.getIdentify().equals(info.getGroupInfo().getGroupId())) {
                adapter.notifyDataSetChanged();
                return;
            }
        }
    }

    /**
     * 刷新
     */
    @Override
    public void refresh() {
//        Collections.sort(conversationList);
//        adapter.notifyDataSetChanged();

        if (isRefreshSuccess) {
            LogUtils.i("刷新列表信息数据false" + isRefreshSuccess);
            isRefreshSuccess = false;

            String groupId = ConfigModel.getInitData().getGroup_id();
            StringBuilder ids = new StringBuilder();
            //更新用户信息
            for (Conversation item : conversationList) {
                NomalConversation nomalConversation = (NomalConversation) item;
                if (!nomalConversation.getName().equals(groupId) && nomalConversation.getName().equals(nomalConversation.getIdentify())) {
                    ids.append(item.getIdentify()).append(",");
                }
            }

            LogUtils.i("nowGetInfoIds: " + nowGetInfoIds + "-----ids: " + ids.toString() + "-----TIME:" + System.currentTimeMillis());
            if (!TextUtils.isEmpty(ids.toString()) && !nowGetInfoIds.equals(ids.toString())) {
                requestCode = 1;
                nowGetInfoIds = ids.toString();
                //获取头像昵称数据  这里用后台接口的是因为腾讯云的里边数据不全
                Api.doRequestConversationUserInfo(ids, new StringCallback() {

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        JsonRequestGetConversationUserInfo jsonObj =
                                (JsonRequestGetConversationUserInfo) JsonRequestGetConversationUserInfo
                                        .getJsonObj(s, JsonRequestGetConversationUserInfo.class);
                        if (jsonObj.getCode() == 1) {

                            for (UserModel userModel : jsonObj.getList()) {
                                for (Conversation conversation : conversationList) {
                                    if (userModel.getId().equals(conversation.getIdentify())) {
                                        conversation.setAvatar(userModel.getAvatar());
                                        conversation.setName(userModel.getUser_nickname());
                                    }
                                }
                            }

                            //刷新
                            //adapter.setData(conversationList);
                            adapter.notifyDataSetChanged();
                        }
                        nowGetInfoIds = "";
                        isRefreshSuccess = true;
                        requestCode = 0;

                        LogUtils.i("刷新列表信息数据true" + isRefreshSuccess);
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        isRefreshSuccess = true;
                        requestCode = 0;
                    }
                });
            } else {
                //adapter.setData(conversationList);
                adapter.notifyDataSetChanged();
                if (requestCode == 0) {
                    isRefreshSuccess = true;
                }
            }

        } else {
            //adapter.setData(conversationList);
            adapter.notifyDataSetChanged();
        }

        //if (getActivity() instanceof  HomeActivity)
        //((HomeActivity) getActivity()).setMsgUnread(getTotalUnreadNum() == 0);
    }


    //只走一遍
    @Override
    public void getConversationInfo() {
        refresh();
    }

    @Override
    public void onResume() {
        super.onResume();
        PushUtil.getInstance().reset();
        refresh();
        LogUtils.i("onResume()调用刷新refresh()");
    }

    /**
     * 获取好友关系链管理系统最后一条消息的回调
     *
     * @param message     最后一条消息
     * @param unreadCount 未读数
     */
    @Override
    public void onGetFriendshipLastMessage(TIMFriendFutureItem message, long unreadCount) {

    }

    /**
     * 获取好友关系链管理最后一条系统消息的回调
     *
     * @param message 消息列表
     */
    @Override
    public void onGetFriendshipMessage(List<TIMFriendFutureItem> message) {
        friendshipManagerPresenter.getFriendshipLastMessage();
    }

    /**
     * 获取群管理最后一条系统消息的回调
     *
     * @param message     最后一条消息
     * @param unreadCount 未读数
     */
    @Override
    public void onGetGroupManageLastMessage(TIMGroupPendencyItem message, long unreadCount) {

    }

    /**
     * 获取群管理系统消息的回调
     *
     * @param message 分页的消息列表
     */
    @Override
    public void onGetGroupManageMessage(List<TIMGroupPendencyItem> message) {

    }

    private long getTotalUnreadNum() {
        long num = 0;
        for (Conversation conversation : conversationList) {
            num += conversation.getUnreadNum();
        }
        return num;
    }

}
