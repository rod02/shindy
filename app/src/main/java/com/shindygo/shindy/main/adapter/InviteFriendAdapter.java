package com.shindygo.shindy.main.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shindygo.shindy.EventDetailActivity;
import com.shindygo.shindy.R;
import com.shindygo.shindy.interfaces.ClickUser;
import com.shindygo.shindy.interfaces.UserControl;
import com.shindygo.shindy.model.User;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Anton Kyrychenko on 005 05.04.18.
 */
public class InviteFriendAdapter extends RecyclerView.Adapter<InviteFriendAdapter.Holder> {
    List<User> list=new ArrayList<>();
    ClickUser clickUser;
    UserControl userControl;
    Context context;

    public InviteFriendAdapter(List<User> list,   UserControl userControl) {
        this.list = list;

        this.userControl = userControl;
    }



    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_invite, parent, false);

        context=parent.getContext();
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class Holder extends RecyclerView.ViewHolder  {
        @BindView(R.id.iv_avatar)
        CircleImageView ivAvatar;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.doller)
        ImageView doller;
        @BindView(R.id.anonim)
        ImageView anonim;

        ImageView check;
         Holder(View v) {
            super(v);
            ButterKnife.bind(this, v);
            check=v.findViewById(R.id.check);

        }
        public void bind(final User u){
             tvName.setText(u.getFullname());
            Glide.with(context).load(u.getPhoto()).into(ivAvatar);



            if (u.checked){
                check.setImageResource(R.drawable.accept_arrow);
                anonim.setVisibility(View.VISIBLE);
                doller.setVisibility(View.VISIBLE);
            }
            else {
                check.setImageResource(R.drawable.greycircle);
                anonim.setVisibility(View.INVISIBLE);
                doller.setVisibility(View.INVISIBLE);
            }

            if (u.isAnonymous_inviteB()){
                anonim.setColorFilter(ContextCompat.getColor(context, R.color.fb_blue));
            }
            else {
                anonim.setColorFilter(ContextCompat.getColor(context, R.color.gray_tint));
            }

            anonim.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    u.setAnonymous_invite(!u.isAnonymous_inviteB());
                    if (u.isAnonymous_inviteB()){
                        anonim.setColorFilter(ContextCompat.getColor(context, R.color.fb_blue));
                    }
                    else {
                        anonim.setColorFilter(ContextCompat.getColor(context, R.color.gray_tint));
                    }


                }
            });
            if (u.isOffer_to_payB()){
                doller.setColorFilter(ContextCompat.getColor(context, R.color.fb_blue));
            }
            else {
                doller.setColorFilter(ContextCompat.getColor(context, R.color.gray_tint));
            }
            doller.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View view) {
                                              u.setOffer_to_pay(!u.isOffer_to_payB());
                                              if (u.isOffer_to_payB()){
                                                  doller.setColorFilter(ContextCompat.getColor(context, R.color.fb_blue));
                                              }
                                              else {
                                                  doller.setColorFilter(ContextCompat.getColor(context, R.color.gray_tint));
                                              }
                                          }
                                      }
            );
             check.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {
                          u.checked=!u.checked;
                          if (u.checked){
                              check.setImageResource(R.drawable.accept_arrow);
                              anonim.setVisibility(View.VISIBLE);
                              doller.setVisibility(View.VISIBLE);
                              userControl.addUser(u);
                          }
                          else {
                              check.setImageResource(R.drawable.greycircle);
                              anonim.setVisibility(View.INVISIBLE);
                              doller.setVisibility(View.INVISIBLE);
                              userControl.removeUser(u);
                          }
                          notifyDataSetChanged();
                 }
             });



        }
    }

}
