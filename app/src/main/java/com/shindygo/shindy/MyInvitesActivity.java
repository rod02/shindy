package com.shindygo.shindy;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.shindygo.shindy.api.EventController;
import com.shindygo.shindy.interfaces.ClickUninviteUser;
import com.shindygo.shindy.main.adapter.MyInvitesAdapter;
import com.shindygo.shindy.model.Event;
import com.shindygo.shindy.model.MyInvites;
import com.shindygo.shindy.model.MyInvitesUser;
import com.shindygo.shindy.model.Status;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyInvitesActivity extends AppCompatActivity {
    EventController eventController;
    Event event;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.top)
    LinearLayout top;
    @BindView(R.id.iv_avatar)
    RoundedImageView ivAvatar;
    @BindView(R.id.tv_expires)
    TextView tvExpires;
    @BindView(R.id.start)
    LinearLayout start;
    @BindView(R.id.tv_eventName)
    TextView tvEventName;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.middle)
    LinearLayout middle;
    @BindView(R.id.line)
    View line;
    @BindView(R.id.man_count)
    TextView manCount;
    @BindView(R.id.women_count)
    TextView womenCount;
    @BindView(R.id.tv_sold_out)
    TextView tvSoldOut;
    @BindView(R.id.rl)
    RelativeLayout rl;
    @BindView(R.id.list_item_profile)
    TextView listItemProfile;
    @BindView(R.id.ll_profile)
    LinearLayout llProfile;
    @BindView(R.id.star_favorite)
    ImageView starFavorite;
    @BindView(R.id.list_item_favorite)
    TextView listItemFavorite;
    @BindView(R.id.ll_favorite)
    LinearLayout llFavorite;
    @BindView(R.id.list_item_invite)
    TextView listItemInvite;
    @BindView(R.id.list_item_message)
    TextView listItemMessage;
    @BindView(R.id.ll_message)
    LinearLayout llMessage;
    @BindView(R.id.ll_detail)
    LinearLayout llDetail;
    @BindView(R.id.bar)
    LinearLayout bar;
    @BindView(R.id.linearLayout2)
    LinearLayout linearLayout2;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.recycler_view2)
    RecyclerView recyclerView2;
    List<MyInvitesUser> userList = new ArrayList<>();
    List<MyInvitesUser> userList2 = new ArrayList<>();
    MyInvitesAdapter myInvitesAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_invites_fragment);
        ButterKnife.bind(this);

        event = getIntent().getParcelableExtra("event");
        eventController = new EventController(this);

        tvEventName.setText(event.getEventname());
        tvDate.setText(event.getStartTime());
        tvExpires.setText(event.getExpirydate());
        manCount.setText(event.getMax_male());
        womenCount.setText(event.getMax_female());
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Glide.with(this).load(event.getImage()).into(ivAvatar);


        eventController.getWhoIsInvited_Event(event.getEventid(), new Callback<MyInvites>() {
            @Override
            public void onResponse(Call<MyInvites> call, Response<MyInvites> response) {



                if (response.body() != null) {
                    userList = response.body().myInvites;
                    userList2 = response.body().othersInvite;
                    recyclerView.setLayoutManager(new LinearLayoutManager(MyInvitesActivity.this));
                    recyclerView2.setLayoutManager(new LinearLayoutManager(MyInvitesActivity.this));
                    myInvitesAdapter = new MyInvitesAdapter(userList, false, new ClickUninviteUser() {
                        @Override
                        public void uninvite(final MyInvitesUser myInvitesUser) {
                            new EventController(MyInvitesActivity.this).cancelInvite(myInvitesUser.getEventid(),myInvitesUser.getFbid(), new Callback<Status>() {
                                @Override
                                public void onResponse(Call<Status> call, Response<Status> response) {
                                    Status status = response.body();
                                    if(status.getStatus().equals("success")) {
                                        Toast.makeText(MyInvitesActivity.this, "User uninvite", Toast.LENGTH_SHORT).show();
                                        userList.remove(myInvitesUser);
                                        myInvitesAdapter.notifyDataSetChanged();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Status> call, Throwable t) {

                                }
                            });
                        }
                    });
                        recyclerView.setAdapter(myInvitesAdapter);
                        recyclerView2.setAdapter(new MyInvitesAdapter(userList2, true, new ClickUninviteUser() {
                            @Override
                            public void uninvite(MyInvitesUser myInvitesUser) {

                            }
                        }));


                            recyclerView.setNestedScrollingEnabled(false);
                    recyclerView2.setNestedScrollingEnabled(false);
                }


            }

            @Override
            public void onFailure(Call<MyInvites> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
