package com.shindygo.shindy.adapter;

import android.content.Context;
import android.nfc.Tag;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.aakira.expandablelayout.ExpandableLinearLayout;
import com.shindygo.shindy.Api;
import com.shindygo.shindy.R;
import com.shindygo.shindy.model.UserAvailability;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserAvailabilityAdapter extends RecyclerView.Adapter<UserAvailabilityAdapter.MyViewHolder> {

    private static final String TAG = UserAvailabilityAdapter.class.getSimpleName();

    List<UserAvailability> list = new ArrayList<>();
    Context context;

    public UserAvailabilityAdapter(List<UserAvailability> userAvailabilities) {
        this.list = userAvailabilities;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_not_available_time,null));
    }

    @Override
    public void onBindViewHolder(UserAvailabilityAdapter.MyViewHolder holder, int position) {
        holder.bindModel(list.get(position),position);

    }

    @Override
    public int getItemCount() {
        try {
            return list.size();
        }catch (NullPointerException e){
            Log.d(TAG, "null list");
            return 0;
        }

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_delete)
        ImageView ivDelete;
        @BindView(R.id.tv_day)
        TextView tvDay;
        @BindView(R.id.tv_time)
        TextView tvTime;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        public void bindModel(final UserAvailability av, final int position){
            try{
                Log.d(TAG, "day "+av.getDay());
                if(av.getDayString()!=null)
                     tvDay.setText(av.getDayString());
                if(av.getTime()!=null)
                    tvTime.setText(av.getTime());
                ivDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Api.getInstance().deleteUserAvailableTime(av.getFbid(), av.getId(), new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                try {
                                    /*Log.d(TAG,"onResponse");
                                    Log.d(TAG,"onResponse " + response.toString());
                                    try {
                                        Log.d(TAG,"onResponse " + response.body().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }*/
                                    if(response.message().equalsIgnoreCase("ok")){
                                        list.remove(position);
                                        notifyItemRemoved(position);
                                        notifyItemRangeChanged(position, getItemCount());
                                    }

                                }catch (NullPointerException e){
                                    Log.d(TAG,"onResponse " + "null views");

                                }

                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Log.d(TAG,"onFailure");


                            }
                        });
                    }
                });


            }catch (Exception e){
                Log.d(TAG, e.getLocalizedMessage());
                e.printStackTrace();
            }

        }


        @Override
        public String toString() {
            return super.toString() + " '" + tvDay.getText().toString() + " "+ tvTime + "'";
        }

    }


}
