package com.shindygo.shindy.main.adapter;

/**
 * Created by User on 16.03.2018.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.shindygo.shindy.R;
import com.shindygo.shindy.interfaces.ClickEvent;
import com.shindygo.shindy.interfaces.ClickUser;
import com.shindygo.shindy.model.Event;
import com.shindygo.shindy.model.User;
import com.shindygo.shindy.utils.GlideImage;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sergey on 24.10.2017.
 */

public class BlockedEventAdapter extends RecyclerView.Adapter<BlockedEventAdapter.LikedEventHolder> {
    Context context;
    List<Event> list;
    String id;
    private ClickEvent clickEvent;
    public BlockedEventAdapter(List<Event> friendList, ClickEvent clickEvent ) {
        this.list = friendList;
        this.clickEvent = clickEvent;
    }
    @Override
    public LikedEventHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        context=parent.getContext();
        final SharedPreferences sharedPref = context.getSharedPreferences("set", Context.MODE_PRIVATE);
        id = sharedPref.getString("fbid", "");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_choose_event,null);

        return new LikedEventHolder(view);
    }

    @Override
    public void onBindViewHolder(final LikedEventHolder holder, final int position) {
        holder.bindModel(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class LikedEventHolder extends RecyclerView.ViewHolder{
//        LinearLayout unblock;
//        ImageView avatar;
//        TextView name;
//        Api api =  new Api(context);
        ImageView block;
        TextView textView;



        @BindView(R.id.iv_avatar)
        RoundedImageView ivAvatar;
        @BindView(R.id.tv_exprires)
        TextView tvExprires;
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
        @BindView(R.id.indicator)
        ImageView indicator;
        @BindView(R.id.text)
        TextView text;
        @BindView(R.id.cv_main)
        CardView cvMain;


        public void bindModel(final Event e) {

            //GlideImage.load(e.getImage(), ivAvatar);

            //Glide.with(context).load(e.getImage()).into(ivAvatar);
            tvEventName.setText(e.getEventname());
            tvExprires.setText(MessageFormat.format("Expires: {0}", e.getExpirydate()));
            if (e.getSchedStartdate() != null)
                tvDate.setText(e.getSchedStartdate());
            else
                tvDate.setText("");
            cvMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickEvent.openEvent(e);
                }
            });
            block.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickEvent.Click(e);
                }
            });
            try {
                if (e.getPrivate_host().toString().length()>0){
                    tvHost.setVisibility(View.VISIBLE);
                    tvHost.setText("Private host: "+e.getPrivate_host());
                }
                else
                    tvHost.setVisibility(View.GONE);

                if (e.getInvitedby().toString().length()>0){
                    tvInvited.setVisibility(View.VISIBLE);
                    tvInvited.setText("Invited by: "+e.getInvitedby());
                }
                else
                    tvInvited.setVisibility(View.GONE);

                tvOffer.setVisibility(e.getOffer_to_pay().equals("1")?View.VISIBLE:View.GONE);

                tvDate.setText(e.getSchedStartdate());
                tvExprires.setText(e.getExpirydate());
                tvEventName.setText(e.getEventname());
                //ivAvatar.setImageResource(android.R.color.transparent);
                //Glide.with(context).load(e.getImage()).into(ivAvatar);
                GlideImage.load(context, e.getImage(), ivAvatar);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        LikedEventHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);

            block = v.findViewById(R.id.indicator);
            block.setImageResource(R.drawable.stop);
            block.setColorFilter(Color.RED);

            textView= v.findViewById(R.id.text);
            textView.setVisibility(View.VISIBLE);

//            unblock = v.findViewById(R.id.unblock);
//            avatar = v.findViewById(R.id.iv_avatar);
//            name = v.findViewById(R.id.tv_name_age);

        }

    }

}
