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
public class UserOnlineAdapter extends  RecyclerView.Adapter<UserOnlineAdapter.UserOnlineHolder> {

Context context;

    @Override
    public UserOnlineHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context=parent.getContext();

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_active,null);
        return new UserOnlineHolder(view);
    }

    @Override
    public void onBindViewHolder(UserOnlineHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }


    class UserOnlineHolder extends RecyclerView.ViewHolder{

        public UserOnlineHolder(View itemView) {
            super(itemView);
        }
    }
}
