package com.shindygo.shindy.main.adapter;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.shindygo.shindy.R;
import com.shindygo.shindy.interfaces.ClickCard;
import com.shindygo.shindy.interfaces.ClickShowPopup;
import com.shindygo.shindy.main.model.TouristSpot;
import com.yuyakaido.android.cardstackview.SwipeDirection;

import java.util.List;

public class TouristSpotCardAdapter extends ArrayAdapter<TouristSpot> {
    ClickCard clickCard;
    ClickShowPopup clickShowPopup;
    public TouristSpotCardAdapter(Context context, ClickCard clickCard,ClickShowPopup clickShowPopup) {
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

        TouristSpot spot = getItem(position);

//        holder.name.setText(spot.name);
//        holder.city.setText(spot.city);
//        Glide.with(getContext()).load(spot.url).into(holder.image);

        return contentView;
    }

    private static class ViewHolder {
        ImageView button;

        public ViewHolder(View view, final ClickCard clickCard, final ClickShowPopup clickShowPopup) {
        ImageView target = view.findViewById(R.id.btn_ok);
        button = view.findViewById(R.id.btn_preview);
        target.setOnClickListener(new View.OnClickListener() {
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
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickShowPopup.Show();
                }
            });
        }
    }

}
