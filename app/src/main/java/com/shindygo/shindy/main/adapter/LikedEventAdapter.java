package com.shindygo.shindy.main.adapter;

/**
 * Created by User on 16.03.2018.
 */

import android.content.Context;
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
import com.shindygo.shindy.R;
import com.shindygo.shindy.interfaces.ClickEvent;
import com.shindygo.shindy.interfaces.ClickUser;
import com.shindygo.shindy.model.Event;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Sergey on 24.10.2017.
 */

public class LikedEventAdapter extends RecyclerView.Adapter<LikedEventAdapter.LikedEventHolder> {
    Context context;
    List<Event> list = new ArrayList<>();
    String id;
    ClickEvent clickEvent;


    public LikedEventAdapter(List<Event> list,ClickEvent clickEvent) {
        this.list = list;
        this.clickEvent = clickEvent;
    }

    @Override
    public LikedEventHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        context = parent.getContext();
        final SharedPreferences sharedPref = context.getSharedPreferences("set", Context.MODE_PRIVATE);
        id = sharedPref.getString("fbid", "");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_choose_event, null);

        return new LikedEventHolder(view);
    }

    @Override
    public void onBindViewHolder(final LikedEventHolder holder, final int position) {
        holder.bindModel(list.get(position),clickEvent);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class LikedEventHolder extends RecyclerView.ViewHolder {
        //        LinearLayout unblock;
//        ImageView avatar;
//        TextView name;
//        Api api =  new Api(context);
        ImageView heart,ivAvatar;
        TextView tvEventName,tvExprires,tvDate;
        View all;
        public LikedEventHolder(View v) {
            super(v);
            all=v;
            heart = v.findViewById(R.id.indicator);
            heart.setImageResource(R.mipmap.heart);
            tvDate = v.findViewById(R.id.tv_date);
            tvEventName = v.findViewById(R.id.tv_eventName);
            tvExprires = v.findViewById(R.id.tv_exprires);
            ivAvatar = v.findViewById(R.id.iv_avatar);
//            unblock = v.findViewById(R.id.unblock);
//            avatar = v.findViewById(R.id.iv_avatar);
//            name = v.findViewById(R.id.tv_name_age);

        }

        public void bindModel(final Event e, final ClickEvent clickEvent) {
            try {
                Glide.with(context).load(e.getImage()).into(ivAvatar);
            }catch (Exception ee){}
            try {
                tvEventName.setText(e.getEventname());
            }catch (Exception eew){}
            tvExprires.setText(MessageFormat.format("Expires: {0}", e.getExpirydate()));
            if (e.getSchedStartdate() != null)
                tvDate.setText(e.getSchedStartdate().toString());
            else
                tvDate.setText("");
            all.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickEvent.openEvent(e);
                }
            });
            heart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickEvent.Click(e);
                }
            });
        }
    }

}
