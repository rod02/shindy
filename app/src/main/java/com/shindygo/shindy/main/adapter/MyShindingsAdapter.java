package com.shindygo.shindy.main.adapter;

/**
 * Created by User on 16.03.2018.
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.shindygo.shindy.EventDetailActivity;
import com.shindygo.shindy.MyInvitesActivity;
import com.shindygo.shindy.R;
import com.shindygo.shindy.activity.SendInviteActivity;
import com.shindygo.shindy.interfaces.ClickEvent;
import com.shindygo.shindy.model.Event;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sergey on 24.10.2017.
 */

public class MyShindingsAdapter extends RecyclerView.Adapter<MyShindingsAdapter.MyShindingsHolder> {
    Context context;
    List<Event> list;
    String id;
    int size;
    ClickEvent clickEvent;



    public MyShindingsAdapter(List<Event> events, ClickEvent clickEvent) {
        this.list = events;
        this.clickEvent = clickEvent;
    }

    @Override
    public MyShindingsHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        context = parent.getContext();
        final SharedPreferences sharedPref = context.getSharedPreferences("set", Context.MODE_PRIVATE);
        id = sharedPref.getString("fbid", "");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_shindings, null);

        return new MyShindingsHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyShindingsHolder holder, final int position) {
        holder.bindModel(list.get(position), clickEvent);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyShindingsHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_avatar)
        RoundedImageView ivAvatar;
        @BindView(R.id.tv_expires)
        TextView tvExpires;
        @BindView(R.id.start)
        LinearLayout start;
        @BindView(R.id.tv_eventName)
        TextView tvEventName;
        @BindView(R.id.tv_date)
        TextView tvDate;
        @BindView(R.id.middle)
        LinearLayout middle;
        @BindView(R.id.line)
        View line;
        @BindView(R.id.tv_sold_out)
        TextView tvSoldOut;
        @BindView(R.id.bar)
        LinearLayout bar;
        @BindView(R.id.rl)
        RelativeLayout rl;
        @BindView(R.id.ll_profile)
        LinearLayout llProfile;
        @BindView(R.id.ll_favorite)
        LinearLayout llFavorite;
        @BindView(R.id.ll_message)
        LinearLayout llMessage;
        @BindView(R.id.man_count)
        TextView manCount;
        @BindView(R.id.women_count)
        TextView womenCount;
        @BindView(R.id.tv_private_host)
        TextView tvPrivateHost;
        @BindView(R.id.tv_invited_by)
        TextView tvInvitedBy;
        @BindView(R.id.tv_offer)
        TextView tvOffer;

        public MyShindingsHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);

        }

        public void bindModel(final Event event, final ClickEvent clickEvent) {


            try {
                if (event.getPrivate_host()!=null||!event.getPrivate_host().equals("")){
                tvPrivateHost.setVisibility(View.VISIBLE);
                tvPrivateHost.setText("Private host: "+event.getPrivate_host());
                }
                else
                    tvPrivateHost.setVisibility(View.GONE);

                if (event.getInvitedby().toString().length()>0){
                    tvInvitedBy.setVisibility(View.VISIBLE);
                    tvInvitedBy.setText("Invited by: "+event.getInvitedby());
                }
                else
                    tvInvitedBy.setVisibility(View.GONE);

                    tvOffer.setVisibility(event.getOffer_to_pay().equals("1")?View.VISIBLE:View.GONE);

                tvDate.setText(event.getSchedStartdate());
                tvExpires.setText(event.getExpirydate());
                manCount.setText(event.getMax_male());
                womenCount.setText(event.getMax_female());
                tvEventName.setText(event.getEventname());
                ivAvatar.setImageResource(android.R.color.transparent);
                Glide.with(context).load(event.getImage()).into(ivAvatar);
                rl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bar.setVisibility(bar.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                    }
                });
                llProfile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, EventDetailActivity.class);
                        intent.putExtra("event", event);
                        context.startActivity(intent);
                    }
                });
                llFavorite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        context.startActivity(new Intent(context, MyInvitesActivity.class).putExtra("event", event));
                    }
                });
                llMessage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        context.startActivity(new Intent(context, SendInviteActivity.class).putExtra("event", event));
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

}
