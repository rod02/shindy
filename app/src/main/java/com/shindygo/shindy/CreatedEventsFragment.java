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


public class CreatedEventsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = CreatedEventsFragment.class.getSimpleName();
    RecyclerView rv;
    SwipeRefreshLayout mSwipeRefreshLayout;


    public CreatedEventsFragment() {
    }

    public static CreatedEventsFragment newInstance() {
        CreatedEventsFragment fragment = new CreatedEventsFragment();
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
        Context context = view.getContext();
        rv = (RecyclerView) view.findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(context));
        rv.setHasFixedSize(true);//every item of the RecyclerView has a fix size
        // SwipeRefreshLayout
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.lay_swipe);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        /**
         * Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used
         */
        mSwipeRefreshLayout.post(new Runnable() {

            @Override
            public void run() {

                mSwipeRefreshLayout.setRefreshing(true);

                // Fetching data from server
                try {
                    loadRecyclerViewData();
                }catch (NullPointerException e){

                }


            }
        });
       // loadRecyclerViewData();
        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition() == 0)
                    mSwipeRefreshLayout.setEnabled(true);
                else
                    mSwipeRefreshLayout.setEnabled(false);
            }
        });

        return view;
    }


    private void loadRecyclerViewData() {
        // Showing refresh animation before making http call
        mSwipeRefreshLayout.setRefreshing(true);

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
                    mSwipeRefreshLayout.setRefreshing(false);
                    mSwipeRefreshLayout.setEnabled(true);
                    hideRefreshProgressBar();
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
    // Call when the a network service is done. We should re-enable
    // swipe-to-refresh as now we allow user to refresh it.
    public void hideRefreshProgressBar() {
        if (mSwipeRefreshLayout != null &&
                mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    mSwipeRefreshLayout.setRefreshing(false);
                    mSwipeRefreshLayout.setEnabled(true);
                }
            });
        }
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

    @Override
    public void onRefresh() {
        loadRecyclerViewData();
    }
}
