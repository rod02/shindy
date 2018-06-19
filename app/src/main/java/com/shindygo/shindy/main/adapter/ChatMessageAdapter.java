package com.shindygo.shindy.main.adapter;

/**
 * Created by User on 27.03.2018.
 */

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.shindygo.shindy.R;
import com.shindygo.shindy.interfaces.ClickCard;

public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.ChatMessageHolder> {
    Context context;
    int a = 0;

//    public ChatAdapter(ClickCard clickCard) {
//        this.clickCard = clickCard;
//    }

    @Override
    public ChatMessageHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        context = parent.getContext();
        if (a % 2 == 0) {
            View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_chat, null);
            a++;
            return new ChatMessageHolder(view1);
        } else{
            a++;
            View view2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_chat_my, null);
            return new ChatMessageHolder(view2);
        }
    }

    @Override
    public void onBindViewHolder(final ChatMessageHolder holder, final int position) {
        holder.bindModel();
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class ChatMessageHolder extends RecyclerView.ViewHolder {

        public ChatMessageHolder(View v) {
            super(v);


        }

        public void bindModel() {

        }
    }

}



