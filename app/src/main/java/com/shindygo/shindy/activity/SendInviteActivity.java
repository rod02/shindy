package com.shindygo.shindy.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.shindygo.shindy.Api;
import com.shindygo.shindy.R;
import com.shindygo.shindy.SearchFilterActivity;
import com.shindygo.shindy.api.EventController;
import com.shindygo.shindy.interfaces.UserControl;
import com.shindygo.shindy.main.UsersFragment;
import com.shindygo.shindy.main.adapter.CircleImageArray;
import com.shindygo.shindy.main.adapter.InviteFriendAdapter;
import com.shindygo.shindy.model.Event;
import com.shindygo.shindy.model.Filter;
import com.shindygo.shindy.model.InviteEvent;
import com.shindygo.shindy.model.Status;
import com.shindygo.shindy.model.User;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.shindygo.shindy.SearchFilterActivity.FILTER;

public class SendInviteActivity extends AppCompatActivity {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.start)
    LinearLayout start;
    @BindView(R.id.tv_eventName)
    TextView tvEventName;
    @BindView(R.id.tv_sold_out)
    TextView tvSoldOut;
    @BindView(R.id.tetx1)
    TextView tetx1;
    @BindView(R.id.text2)
    TextView text2;
    @BindView(R.id.search_user_img)
    ImageView searchUserImg;
    @BindView(R.id.search_user_spinner)
    Spinner searchUserSpinner;
    @BindView(R.id.search_user_filters_txt)
    TextView searchUserFiltersTxt;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.clear)
    ImageView clear;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.rl2)
    RelativeLayout rl2;
    @BindView(R.id.bottom)
    RelativeLayout bottom;
    @BindView(R.id.rv_invite)
    RecyclerView rvInvite;
    @BindView(R.id.search_view)
    LinearLayout searchView;
    LinearLayout llMainUsers;

    TextView invitedC;
    List<User> list = new ArrayList<>();
    CircleImageArray invitedAdapter;
    List<User> invited = new ArrayList<>();
    @BindView(R.id.send)
    ImageView send;
    String event;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.tv_expires)
    TextView tvExpires;
    @BindView(R.id.iv_avatar)
    RoundedImageView ivAvatar;
    @BindView(R.id.man_count)
    TextView manCount;
    @BindView(R.id.women_count)
    TextView womenCount;
    @BindView(R.id.send_invite_email)
    ImageView sendInviteEmail;
    @BindView(R.id.main_layout)
    RelativeLayout mainLayout;
    private InviteFriendAdapter adapter;


    private Filter pFilter;
    private UsersFragment.OnFragmentInteractionListener mListener;

    LinearLayoutManager layoutManager;
    private PopupWindow mPopupWindow, messPopup;
    Event eventFull;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invite_friends);
        ButterKnife.bind(this);
        invitedC = findViewById(R.id.invited_count);
        eventFull = getIntent().getParcelableExtra("event");
        event = eventFull.getEventid();
        if (event == null) {
            Toast.makeText(getApplicationContext(), "No event ID", Toast.LENGTH_SHORT).show();
            finish();
        }
        tvEventName.setText(eventFull.getEventname());
        tvDate.setText(eventFull.getStartTime());
        tvExpires.setText(eventFull.getExpirydate());
        manCount.setText(eventFull.getMax_male());
        womenCount.setText(eventFull.getMax_female());
        Glide.with(getApplicationContext()).load(eventFull.getImage()).into(ivAvatar);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        sendInviteEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createMessage();
            }
        });
        /*
        Api api = new Api(getApplicationContext());
        api.getUsers(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                list = response.body();
                recyclerView.setAdapter(new InviteFriendAdapter(list, new UserControl() {
                    @Override
                    public void addUser(User user) {
                        invited.add(user);
                        invitedAdapter.notifyDataSetChanged();
                        invitedC.setText("+" + invited.size());
                        bottom.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void removeUser(User user) {
                        invited.remove(user);
                        invitedAdapter.notifyDataSetChanged();
                        invitedC.setText("+" + invited.size());
                        if (invited.size() < 1) {
                            bottom.setVisibility(View.GONE);
                        }


                    }
                }));
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                list = new ArrayList<>();
                t.printStackTrace();
            }
        });
*/

        invitedAdapter = new CircleImageArray(invited);
        rvInvite.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        rvInvite.setAdapter(invitedAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        initSeach();
    }

    @OnClick({R.id.back, R.id.title})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                break;
            case R.id.title:
                break;
        }
    }

    @OnClick(R.id.send)
    public void onViewClicked() {
        for (User u : invited) {
            InviteEvent inviteEvent = new InviteEvent(event, u.getFbid(), u.isAnonymous_invite(), u.isOffer_to_pay());
            EventController eventController = new EventController(getApplicationContext());
            eventController.sendinvite(inviteEvent, new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response != null) {
                        Log.v("sendInvite", response.body().toString());
                        finish();

                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    t.printStackTrace();
                    finish();
                }
            });
        }

        /*startActivity(new Intent(SendInviteActivity.this,MyInvitesActivity.class).putExtra("event",eventFull));*/
    }

    Context getContext() {

        return getApplicationContext();
    }


    private void seachF(String text) {
        final Api api = new Api(getContext());
        api.searchUserF(text, pFilter, searchUserSpinner.getSelectedItemPosition(), new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                list.clear();
                if (response.body() != null)
                    list = response.body();
                adapter.notifyDataSetChanged();
                Log.i("Search", response.message());
                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                        layoutManager.getOrientation());
                recyclerView.addItemDecoration(dividerItemDecoration);
                adapter = new InviteFriendAdapter(list, new UserControl() {

                    @Override
                    public void addUser(User user) {
                        invited.add(user);
                        invitedAdapter.notifyDataSetChanged();
                        invitedC.setText("+" + invited.size());
                        bottom.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void removeUser(User user) {
                        invited.remove(user);
                        invitedAdapter.notifyDataSetChanged();
                        invitedC.setText("+" + invited.size());
                        if (invited.size() < 1) {
                            bottom.setVisibility(View.GONE);
                        }
                    }
                });
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                list.clear();
                adapter.notifyDataSetChanged();
                Log.i("123", "324");
            }
        });


    }


    void initSeach() {
        layoutManager = new LinearLayoutManager(getContext());
        searchUserFiltersTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SearchFilterActivity.class);
                if (pFilter != null)
                    intent.putExtra(FILTER, pFilter);
                startActivityForResult(intent, 96);
            }
        });

        adapter = new InviteFriendAdapter(list, new UserControl() {
            @Override
            public void addUser(User user) {

            }

            @Override
            public void removeUser(User user) {

            }
        });
        searchUserImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchView.setVisibility(View.VISIBLE);
                etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                        seachF(etSearch.getText().toString());

                        return true;
                    }
                });
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etSearch.setText("");
            }
        });
        searchUserSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                seachF(etSearch.getText().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 96:
                if (resultCode == Activity.RESULT_OK) {
                    pFilter = data.getParcelableExtra(FILTER);
                    if (!pFilter.isClear()) {
                        searchUserFiltersTxt.setText(getText(R.string.filtersApl));
                        searchUserFiltersTxt.setBackgroundColor(Color.parseColor("#FFA500"));
                    } else {
                        searchUserFiltersTxt.setText("+FILTER");
                        searchUserFiltersTxt.setBackgroundColor(Color.TRANSPARENT);
                    }
                    seachF(etSearch.getText().toString());
                }
                break;

        }

    }

    private void createMessage() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE);

        // Inflate the custom layout/view
        View customView = inflater.inflate(R.layout.popup_invite_by_email, null);
        final TextView textView = customView.findViewById(R.id.textView2);
        final EditText email = customView.findViewById(R.id.editText);
        final EditText note = customView.findViewById(R.id.editText2);
        ImageView imageView = customView.findViewById(R.id.imgEvent);
        Button send = customView.findViewById(R.id.send);
        TextView title,date;
        title = customView.findViewById(R.id.title);
        date = customView.findViewById(R.id.date);
        title.setText(eventFull.getEventname());
        date.setText(eventFull.getCreatedate());
        Glide.with(getApplicationContext()).load(eventFull.getImage()).into(imageView);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(email.getText().toString()))
                new EventController(SendInviteActivity.this).inviteByEmail(eventFull.getEventid(), email.getText().toString(), note.getText().toString(), new Callback<Status>() {
                    @Override
                    public void onResponse(Call<Status> call, Response<Status> response) {
                        Status status = response.body();
                        if(!status.getStatus().equals("success")) {
                            Toast.makeText(SendInviteActivity.this, status.getResult(), Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(SendInviteActivity.this, "Invite sent", Toast.LENGTH_SHORT).show();
                            messPopup.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<Status> call, Throwable t) {

                    }
                });
                else
                    Toast.makeText(SendInviteActivity.this, "Enter correct email", Toast.LENGTH_SHORT).show();
            }
        });
        messPopup = new PopupWindow(
                customView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true
        );
        if (Build.VERSION.SDK_INT >= 21) {
            messPopup.setElevation(5.0f);
        }

        ImageView closeButton = customView.findViewById(R.id.close);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                messPopup.dismiss();
            }
        });

        messPopup.showAtLocation(mainLayout, Gravity.CENTER, 0, 0);
    }
}
