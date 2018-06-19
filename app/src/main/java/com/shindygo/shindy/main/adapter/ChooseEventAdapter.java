package com.shindygo.shindy.main.adapter;

/**
 * Created by User on 16.03.2018.
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.shindygo.shindy.EventDetailActivity;
import com.shindygo.shindy.MyInvitesActivity;
import com.shindygo.shindy.R;
import com.shindygo.shindy.interfaces.ClickEvent;
import com.shindygo.shindy.model.Event;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sergey on 24.10.2017.
 */

public class ChooseEventAdapter extends RecyclerView.Adapter<ChooseEventAdapter.ChooseEventHolder> {
    Context context;
    List<Event> list = new ArrayList<>();
    String id;


    public ChooseEventAdapter(List<Event> friendList) {
        this.list = friendList;
    }

    @Override
    public ChooseEventHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        context = parent.getContext();
        final SharedPreferences sharedPref = context.getSharedPreferences("set", Context.MODE_PRIVATE);
        id = sharedPref.getString("fbid", "");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_choose_event, null);

        return new ChooseEventHolder(view);
    }

    @Override
    public void onBindViewHolder(final ChooseEventHolder holder, final int position) {
        holder.bindModel(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ChooseEventHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_avatar)
        RoundedImageView ivAvatar;
        @BindView(R.id.tv_exprires)
        TextView tvExprires;
        @BindView(R.id.start)
        LinearLayout start;
        @BindView(R.id.tv_eventName)
        TextView tvEventName;
        @BindView(R.id.tv_host)
        TextView tvHost;
        @BindView(R.id.tv_invited)
        TextView tvInvited;
        @BindView(R.id.tv_offer)
        TextView tvOffer;
        @BindView(R.id.tv_date)
        TextView tvDate;
        @BindView(R.id.middle)
        LinearLayout middle;
        @BindView(R.id.indicator)
        ImageView indicator;
        @BindView(R.id.text)
        TextView text;
        @BindView(R.id.cv_main)
        CardView cvMain;

        public ChooseEventHolder(View v) {
            super(v);
            ButterKnife.bind(this,v);

        }

        public void bindModel(final Event event) {
            tvEventName.setText(event.getEventname());
            Glide.with(context).load(event.getImage()).into(ivAvatar);
            tvDate.setText(event.getCreatedate());
            tvExprires.setText(event.getExpirydate());

            cvMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context,MyInvitesActivity.class).putExtra("event",event));
                }
            });
        }
    }

}
