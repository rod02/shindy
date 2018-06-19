package com.shindygo.shindy.main.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shindygo.shindy.R;
import com.shindygo.shindy.model.User;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Anton Kyrychenko on 005 05.04.18.
 */
public class CircleImageArray extends RecyclerView.Adapter<CircleImageArray.Holder> {
    Context context;
    List<User> list;

    public CircleImageArray(List<User> list) {
        this.list = list;
    }

    @Override
    public CircleImageArray.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_c_image, parent, false);
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
    class Holder extends RecyclerView.ViewHolder{
        @BindView(R.id.iv_avatar)
       public CircleImageView ivAvatar;


        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        void bind(User u){
            try {
                if (u.getPhoto()==null||u.getPhoto().equals(""))
                    ivAvatar.setImageResource(R.drawable.greycircle);
                else
                    Glide.with(context).load(u.getPhoto()).into(ivAvatar);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
