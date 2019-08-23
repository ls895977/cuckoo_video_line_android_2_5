package com.ailiaovideo.chat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ailiaovideo.chat.model.Message;
import com.ailiaovideo.videoline.R;
import com.tencent.qcloud.ui.CircleImageView;

import java.util.List;

/**
 * 聊天界面adapter
 */
public class ChatAdapter extends ArrayAdapter<Message> {

    private final String TAG = "ChatAdapter";

    private int resourceId;
    private View view;
    private ViewHolder viewHolder;
    private OnChatChildrenItemListen listen;

    /**
     * Constructor
     *
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a TextView to use when
     *                 instantiating views.
     * @param objects  The objects to represent in the ListView.
     */
    public ChatAdapter(Context context, int resource, List<Message> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView != null) {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        } else {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.leftMessage = (RelativeLayout) view.findViewById(R.id.leftMessage);
            viewHolder.rightMessage = (RelativeLayout) view.findViewById(R.id.rightMessage);
            viewHolder.leftPanel = (RelativeLayout) view.findViewById(R.id.leftPanel);
            viewHolder.rightPanel = (RelativeLayout) view.findViewById(R.id.rightPanel);
            viewHolder.sending = (ProgressBar) view.findViewById(R.id.sending);
            viewHolder.error = (ImageView) view.findViewById(R.id.sendError);
            viewHolder.sender = (TextView) view.findViewById(R.id.sender);
            viewHolder.rightDesc = (TextView) view.findViewById(R.id.rightDesc);
            viewHolder.systemMessage = (TextView) view.findViewById(R.id.systemMessage);
            viewHolder.leftAvatar = view.findViewById(R.id.leftAvatar);
            viewHolder.rightAvatar = view.findViewById(R.id.rightAvatar);
            viewHolder.callBack = (ImageView) view.findViewById(R.id.call_back);


            viewHolder.llContent = view.findViewById(R.id.ll_content);
            viewHolder.leftAvatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listen != null) {
                        listen.onChildrenClick(R.id.leftAvatar);
                    }
                }
            });
            viewHolder.rightAvatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listen != null) {
                        listen.onChildrenClick(R.id.rightAvatar);
                    }
                }
            });

            view.setTag(viewHolder);
        }
        if (position < getCount()) {
            final Message data = getItem(position);
            data.showMessage(viewHolder, getContext());
        }
        return view;
    }

    public void setChildrenListen(OnChatChildrenItemListen listen) {
        this.listen = listen;
    }

    public class ViewHolder {
        public RelativeLayout leftMessage;
        public RelativeLayout rightMessage;
        public RelativeLayout leftPanel;
        public RelativeLayout rightPanel;
        public ProgressBar sending;
        public ImageView error;
        public TextView sender;
        public TextView systemMessage;
        public TextView rightDesc;
        public CircleImageView leftAvatar;
        public CircleImageView rightAvatar;
        public LinearLayout llContent;
        public ImageView callBack;

    }

    public interface OnChatChildrenItemListen {
        void onChildrenClick(int id);
    }


}
