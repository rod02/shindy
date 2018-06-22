package com.shindygo.shindy.main.adapter;

/**
 * Created by User on 27.03.2018.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shindygo.shindy.R;
import com.shindygo.shindy.model.Image;
import com.shindygo.shindy.model.Reply;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChatCommentAdapter extends RecyclerView.Adapter<ChatCommentAdapter.ChatCommentHolder> {
    Context context;
    List<Reply> list;



//    public ChatAdapter(ClickCard clickCard) {
//        this.clickCard = clickCard;
//    }


    public ChatCommentAdapter(List<Reply> list) {
        this.list = list;
    }

    @Override
    public ChatCommentHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        context = parent.getContext();

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reply, null);

        return new ChatCommentHolder(view);
    }

    @Override
    public void onBindViewHolder(final ChatCommentHolder holder, final int position) {
        holder.bindModel(list.get(position), position);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ChatCommentHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.lay_reply_content)
        RelativeLayout rlReplyContent;
        @BindView(R.id.tvPreviousReply)
        TextView tvPreviousReply;
        @BindView(R.id.iv_avatar)
        ImageView ivAvatar;
        @BindView(R.id.tv_name)
        TextView rvName;
        @BindView(R.id.content)
        TextView tvText;
        @BindView(R.id.tv_date_created)
        TextView tvCommentDate;

        public ChatCommentHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }

        public void bindModel(Reply r, int position) {
            //rlReplyContent.setVisibility(View.GONE);
            Glide.with(context).load(r.getPhoto()).into(ivAvatar);
            rvName.setText(r.getFullname());
            tvText.setText(r.getReplyComment());
            tvCommentDate.setText(r.getReplyDate());
        }
    }
}


