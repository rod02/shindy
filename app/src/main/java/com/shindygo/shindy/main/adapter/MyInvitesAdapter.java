package com.shindygo.shindy.main.adapter;

/**
 * Created by User on 16.03.2018.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shindygo.shindy.Api;
import com.shindygo.shindy.R;
import com.shindygo.shindy.interfaces.ClickUninviteUser;
import com.shindygo.shindy.model.MyInvitesUser;

import java.util.List;

public class MyInvitesAdapter extends RecyclerView.Adapter<MyInvitesAdapter.MyInvitesHolder> {
    Context context;
    List<MyInvitesUser> list;
    String id;
    ClickUninviteUser clickUser;
    boolean other;
    public MyInvitesAdapter(List<MyInvitesUser> list,boolean other, ClickUninviteUser clickUser) {
        this.list = list;
        this.clickUser = clickUser;
        this.other = other;
    }

    @Override
    public MyInvitesAdapter.MyInvitesHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        context=parent.getContext();
        final SharedPreferences sharedPref = context.getSharedPreferences("set", Context.MODE_PRIVATE);
        id = sharedPref.getString("fbid", "");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_invites,null);

        return new MyInvitesAdapter.MyInvitesHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyInvitesHolder holder, final int position) {
        holder.bindModel(list.get(position),clickUser);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyInvitesHolder extends RecyclerView.ViewHolder{
       ImageView confirm;
        ImageView avatar;
        TextView name;

        Api api =  new Api(context);

        public MyInvitesHolder(View v) {
            super(v);
            confirm = v.findViewById(R.id.iv_confirm);
            avatar = v.findViewById(R.id.iv_avatar);
            name = v.findViewById(R.id.tv_name_age);

        }
        public void bindModel(final MyInvitesUser myInvitesUser, final ClickUninviteUser clickUninviteUser){
            Glide.with(context).load(myInvitesUser.getPhoto()).into(avatar);
            name.setText(myInvitesUser.getFullname());
            if(!other) {
                if (myInvitesUser.getAttendingstatus() != null)
                    confirm.setImageResource(myInvitesUser.getAttendingstatus().equals("1") ? R.drawable.confirmed : R.drawable.uninvite);
            }
            else
            {
                if (myInvitesUser.getAttendingstatus() != null)
                    if(myInvitesUser.getAttendingstatus().equals("1"))
                        confirm.setImageResource(R.drawable.confirmed );
                    else
                        confirm.setVisibility(View.GONE);
            }
            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!other) {
                        if(myInvitesUser.getAttendingstatus().equals("0"))
                        {
                            clickUninviteUser.uninvite(myInvitesUser);
                        }
                    }
                }
            });
        }
    }

}
