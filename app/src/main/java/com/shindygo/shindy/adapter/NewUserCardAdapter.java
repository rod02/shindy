package com.shindygo.shindy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shindygo.shindy.R;
import com.shindygo.shindy.interfaces.ClickCard;
import com.shindygo.shindy.interfaces.ClickShowPopup;
import com.shindygo.shindy.interfaces.OnClickShow;
import com.shindygo.shindy.model.User;

public class NewUserCardAdapter extends ArrayAdapter<User> {
    ClickCard clickCard;
    OnClickShow<User> clickShowPopup;
    public NewUserCardAdapter(Context context, ClickCard clickCard, OnClickShow<User> clickShowPopup) {
        super(context, 0);
        this.clickCard = clickCard;
        this.clickShowPopup = clickShowPopup;
    }

    @Override
    public View getView(int position, View contentView, ViewGroup parent) {
        ViewHolder holder;

        if (contentView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            contentView = inflater.inflate(R.layout.user_view, parent, false);
            holder = new ViewHolder(contentView,clickCard,clickShowPopup);
            contentView.setTag(holder);
        } else {
            holder = (ViewHolder) contentView.getTag();
        }

        User user = getItem(position);

        holder.name.setText(user.getFullname()+ ", "+user.getAge());
        //holder.city.setText(user.getAddress());
        Glide.with(getContext()).load(user.getPhoto()).into(holder.avatar);
        holder.ivPreview.setTag(user);
        return contentView;
    }

    private static class ViewHolder {
        ImageView ivPreview;
        TextView name;
        ImageView avatar;

        public ViewHolder(View view, final ClickCard clickCard, final OnClickShow<User> clickShowPopup) {
        ImageView ivCheck = view.findViewById(R.id.btn_ok);
        ivPreview = view.findViewById(R.id.btn_preview);
        name = view.findViewById(R.id.user_name);
        avatar = view.findViewById(R.id.user_image);
        ivCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickCard.Click(true);
            }
        });
            ImageView skip = view.findViewById(R.id.btn_skip);
            skip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickCard.Click(false);
                }
            });
            ivPreview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickShowPopup.Show((User) view.getTag());
                }
            });
        }
    }


}
