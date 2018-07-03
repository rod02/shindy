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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.shindygo.shindy.Api;
import com.shindygo.shindy.R;
import com.shindygo.shindy.interfaces.ClickUser;
import com.shindygo.shindy.model.User;
import com.shindygo.shindy.utils.GlideImage;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Sergey on 24.10.2017.
 */

public class BlockedUserAdapter extends RecyclerView.Adapter<BlockedUserAdapter.BlockedUserHolder> {
    Context context;
    List<User> list = new ArrayList<>();
    String id;
    ClickUser clickUser;
    public BlockedUserAdapter(List<User> friendList, ClickUser clickUser ) {
        this.list = friendList;
        this.clickUser = clickUser;
    }
    @Override
    public BlockedUserAdapter.BlockedUserHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        context=parent.getContext();
        final SharedPreferences sharedPref = context.getSharedPreferences("set", Context.MODE_PRIVATE);
        id = sharedPref.getString("fbid", "");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_block_user,null);

        return new BlockedUserAdapter.BlockedUserHolder(view);
    }

    @Override
    public void onBindViewHolder(final BlockedUserHolder holder, final int position) {
        holder.bindModel(list.get(position),clickUser);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class BlockedUserHolder extends RecyclerView.ViewHolder{
        LinearLayout unblock;
        ImageView avatar;
        TextView name;
        Api api =  new Api(context);

        public BlockedUserHolder(View v) {
            super(v);
            unblock = v.findViewById(R.id.unblock);
            avatar = v.findViewById(R.id.iv_avatar);
            name = v.findViewById(R.id.tv_name_age);

        }
        public void bindModel(final User user, final ClickUser clickUser){
            GlideImage.load(context, user.getPhoto(), avatar);

            //Glide.with(context).load(user.getPhoto()).into(avatar);
            name.setText(user.getFullname());
            unblock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickUser.Click(user);
                }
            });
        }
    }

}
