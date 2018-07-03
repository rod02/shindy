package com.shindygo.shindy;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shindygo.shindy.adapter.MyCreatedEventsAdapter;
import com.shindygo.shindy.api.EventController;
import com.shindygo.shindy.interfaces.Click;
import com.shindygo.shindy.model.Event;
import com.shindygo.shindy.model.Event;
import com.shindygo.shindy.model.User;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CreatedEventsFragment extends Fragment {

    private static final String TAG = CreatedEventsFragment.class.getSimpleName();
    RecyclerView rv;/*
    @BindView(R.id.tv_invite)
    TextView tvInvite;
    @BindView(R.id.linearLayout)
    LinearLayout linearLayout;
    @BindView(R.id.linearLayout2)
    LinearLayout linearLayout2;*/
   /* @BindView(R.id.list_item_profile)
    TextView listItemProfile;
    @BindView(R.id.ll_details)
    LinearLayout llDetails;
    @BindView(R.id.star_favorite)
    ImageView starFavorite;
    @BindView(R.id.list_item_favorite)
    TextView listItemFavorite;*/
/*    @BindView(R.id.ll_invited)
    LinearLayout llInvited;
    @BindView(R.id.list_item_invite)
    TextView listItemInvite;
    @BindView(R.id.list_item_message)
    TextView listItemMessage;
    @BindView(R.id.ll_invite)
    LinearLayout llInvite;
    @BindView(R.id.bar)
    LinearLayout bar;*/

    SwipeRefreshLayout mSwipeRefreshLayout;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CreatedEventsFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static CreatedEventsFragment newInstance() {
        CreatedEventsFragment fragment = new CreatedEventsFragment();
       /* Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);*/
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_created_events, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            rv = (RecyclerView) view;
            rv.setLayoutManager(new LinearLayoutManager(context));
            rv.setHasFixedSize(true);//every item of the RecyclerView has a fix size

        }
        loadRecyclerViewData();
        return view;
    }


    private void loadRecyclerViewData() {
        // Showing refresh animation before making http call
       // mSwipeRefreshLayout.setRefreshing(true);

        final EventController api = new EventController(getContext());
        api.fetchCreatedEvents(User.getCurrentUserId(),new Callback<List<Event>>() {
            @Override
            public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {

                List<Event> eventsList = new ArrayList<>();
                Log.v(TAG, response.toString());

                if (response.body()!=null)
                    eventsList = response.body();

                Log.i(TAG, response.message());
                Log.v(TAG, "list size; "+ eventsList.size());
                try {

                    listEvents(eventsList, rv);
                }catch (NullPointerException e){
                  //  e.printStackTrace();
                    Log.d(TAG, "fetch on response");
                }

            }

            @Override
            public void onFailure(Call<List<Event>> call, Throwable t) {
                /*users.clear();
                adapter.notifyDataSetChanged();*/
                Log.e(TAG, "failed");
                t.printStackTrace();
            }
        });


    }

    private void listEvents(List<Event> eventsList, RecyclerView rv) {

        MyCreatedEventsAdapter adapter = new MyCreatedEventsAdapter(eventsList, rv,
                new Click<Event>() {
            @Override
            public void onClick(int id, View view, Event event) {
                switch (id) {
                    case R.id.ll_details: {
                        openEventEditor(event);
                        break;
                    }
                    case R.id.ll_invite: {


                        break;
                    }
                    case R.id.ll_invited: {


                        break;
                    }
                    case R.id.lay_edit: {

                        openEventEditor(event);
                        break;
                    }
                }
            } });
        rv.setAdapter(adapter);


    }

    private void openEventEditor(Event event) {
        String json = event.toJSON();
        Log.v(TAG,json );
        Bundle args = new Bundle();
        args.putString(EventActivity.EXTRA_MODEL, json);
        args.putString(EventActivity.EXTRA_EVENT_ID, event.getEventid());

        FragmentManager fm = getActivity().getSupportFragmentManager();
        Fragment fragment = new EventActivity();
        fragment.setArguments(args);
        fm.beginTransaction()
                .replace(R.id.frame, fragment)
                .addToBackStack("my_fragment")
                .commit();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }



        @Override
        public void onDetach() {
            super.onDetach();
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();
        }
}
