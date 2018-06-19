package com.shindygo.shindy;


import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shindygo.shindy.api.EventController;
import com.shindygo.shindy.main.adapter.ChooseEventAdapter;
import com.shindygo.shindy.model.Event;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChooseEventFragment extends Fragment {

    List<Event> events = new ArrayList<>();
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.top)
    LinearLayout top;
    @BindView(R.id.rv_choose_event)
    RecyclerView rvChooseEvent;
    Unbinder unbinder;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_choose_event, container, false);
        unbinder = ButterKnife.bind(this, view);
        new EventController(getContext()).getEventListCreatedByUser(new Callback<List<Event>>() {
            @Override
            public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                events = response.body();
                rvChooseEvent.setLayoutManager(new LinearLayoutManager(getContext()));
                rvChooseEvent.setAdapter(new ChooseEventAdapter(events));
            }

            @Override
            public void onFailure(Call<List<Event>> call, Throwable t) {

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).openedDrawer();
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
