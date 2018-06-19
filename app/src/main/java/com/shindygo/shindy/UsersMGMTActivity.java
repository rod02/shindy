package com.shindygo.shindy;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shindygo.shindy.interfaces.ClickUser;
import com.shindygo.shindy.main.adapter.BlockedUserAdapter;
import com.shindygo.shindy.model.Filter;
import com.shindygo.shindy.model.User;
import com.shindygo.shindy.utils.FontUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.shindygo.shindy.SearchFilterActivity.FILTER;

public class UsersMGMTActivity extends Fragment {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.top)
    LinearLayout top;
    @BindView(R.id.desc)
    RelativeLayout desc;

    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.qwert)
    RelativeLayout qwert;
    @BindView(R.id.searchpanel)
    RelativeLayout searchpanel;
    @BindView(R.id.tv_filter)
    TextView tvFilter;
    @BindView(R.id.clear)
    ImageView clear;
    @BindView(R.id.rv_blocked)
    RecyclerView rvBlocked;
    private RecyclerView recyclerView;
    Filter filter;
    List<User> users = new ArrayList<>();
    BlockedUserAdapter blockedUserAdapter;
    String id;
    Api api;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_users_mgmt, container, false);
        ButterKnife.bind(this, view);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etSearch.setText("");
                seachF();
            }
        });
      api = new Api(getContext());
       seachF();
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                seachF( );
                return true;
            }
        });
        FontUtils.setFont(title, FontUtils.Be_Bright);
        recyclerView = view.findViewById(R.id.rv_blocked);
        tvFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SearchFilterActivity.class);
                if (filter != null)
                    intent.putExtra("filter", filter);
                startActivityForResult(intent, 69);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).openedDrawer();
            }
        });
        return view;
    }

    private void seachF( ) {
        api.searchBlocedUserF(etSearch.getText().toString(),filter, new Callback<List<User>>() {
            //api.getBlockedUsers(id, new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.body()!=null)
                    users = response.body();
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                blockedUserAdapter = new BlockedUserAdapter(users, new ClickUser() {
                    @Override
                    public void Click(final User user) {
                        api.unblockUser(id, user.getFbid(), new Callback<Object>() {
                            @Override
                            public void onResponse(Call<Object> call, Response<Object> response) {
                                Toast.makeText(getContext(), "User unblocked!", Toast.LENGTH_SHORT).show();
                                users.remove(user);
                                blockedUserAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onFailure(Call<Object> call, Throwable t) {

                            }
                        });
                    }
                });
                recyclerView.setAdapter(blockedUserAdapter);
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                users.clear();
                if (blockedUserAdapter!=null)
                blockedUserAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 69) {
            if (resultCode == Activity.RESULT_OK) {

                filter = data.getParcelableExtra("filter");
                if (resultCode== Activity.RESULT_OK) {
                    filter = data.getParcelableExtra(FILTER);
                    if (!filter.isClear()) {
                      tvFilter.setText(getText(R.string.filtersApl));
                       tvFilter.setBackgroundColor(Color.parseColor("#FFA500"));
                    }
                    else
                    {
                     tvFilter.setText("+FILTER");
                       tvFilter.setBackgroundColor(Color.TRANSPARENT);
                    }

                  seachF( );
                }
                Log.v("blocked", filter.toString());
            }
        }
    }




}
