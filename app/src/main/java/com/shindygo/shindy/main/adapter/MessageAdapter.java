package com.shindygo.shindy.main.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shindygo.shindy.R;

/**
 * Created by Anton Kyrychenko on 012 12.04.18.
 */
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageHolder>{
    Context context;

    @Override
    public MessageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context=parent.getContext();

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_menu,null);

        return new MessageHolder(view);
    }

    @Override
    public void onBindViewHolder(MessageHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 12;
    }

    class MessageHolder extends RecyclerView.ViewHolder{

        public MessageHolder(View itemView) {
            super(itemView);
        }
    }
}
