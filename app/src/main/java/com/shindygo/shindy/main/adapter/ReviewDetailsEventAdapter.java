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
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.shindygo.shindy.R;
import com.shindygo.shindy.interfaces.ClickUser;
import com.shindygo.shindy.model.Rating;
import com.shindygo.shindy.utils.DateUtils;
import com.shindygo.shindy.utils.TextUtils;

import java.text.ParseException;
import java.util.List;


/**
 * Created by Sergey on 24.10.2017.
 */

public class ReviewDetailsEventAdapter extends RecyclerView.Adapter<ReviewDetailsEventAdapter.LikedEventHolder> {
    Context context;
    List<Rating> list;


    public ReviewDetailsEventAdapter(List<Rating> list) {
        this.list = list;
    }

    ClickUser clickUser;

    //    public ChooseEventAdapter(List<User> friendList, ClickUser clickEvent ) {
//        this.list = friendList;
//        this.clickEvent = clickEvent;
//    }
    @Override
    public LikedEventHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        context = parent.getContext();


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review, null);

        return new LikedEventHolder(view);
    }

    @Override
    public void onBindViewHolder(final LikedEventHolder holder, final int position) {
        holder.bindModel(
                list.get(position)
        );
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class LikedEventHolder extends RecyclerView.ViewHolder {

        RatingBar ratingBar;
        TextView rating;
        TextView tvName;
        TextView tvDate;
        TextView tvText;


        public LikedEventHolder(View v) {
            super(v);
            tvText=v.findViewById(R.id.tv_text);
            tvDate=v.findViewById(R.id.tv_date);
            tvName=v.findViewById(R.id.tv_name);
            rating=v.findViewById(R.id.rating);
            ratingBar=v.findViewById(R.id.rating_bar);
        }

        public void bindModel(
                Rating r
        ) {

            tvName.setText(r.getFullname());
            try {
                tvDate.setText(DateUtils.getTimeAgo(r.getRateDate(), TextUtils.SDF_5));
            } catch (ParseException e) {
                e.printStackTrace();
                tvDate.setText(r.getRateDate());
            }
            tvText.setText(r.getFeedback());
            rating.setText(r.getRating());
            ratingBar.setRating(Float.parseFloat(r.getRating()));


        }
    }

}
