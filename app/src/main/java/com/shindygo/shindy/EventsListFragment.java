package com.shindygo.shindy;

import android.app.Activity;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shindygo.shindy.adapter.MyCreatedEventsAdapter;
import com.shindygo.shindy.api.EventController;
import com.shindygo.shindy.interfaces.Click;
import com.shindygo.shindy.interfaces.ClickEvent;
import com.shindygo.shindy.main.adapter.MyShindingsAdapter;
import com.shindygo.shindy.model.Event;
import com.shindygo.shindy.model.User;
import com.shindygo.shindy.utils.FontUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EventsListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = EventsListFragment.class.getSimpleName();
    RecyclerView rv;
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.top)
    LinearLayout top;

    public EventsListFragment() {
    }

    public static EventsListFragment newInstance() {
        EventsListFragment fragment = new EventsListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events_list, container, false);
        ButterKnife.bind(this, view);

        FontUtils.setFont(title, FontUtils.Be_Bright);
        Activity mActivity = getActivity();
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

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity mActivity = getActivity();
                if(mActivity instanceof MainActivity){
                     ((MainActivity) getActivity()).openedDrawer();
                }else{
                    getFragmentManager().popBackStack();
                }
            }
        });
        return view;
    }


    private void loadRecyclerViewData() {
        // Showing refresh animation before making http call
        mSwipeRefreshLayout.setRefreshing(true);

        final EventController api = new EventController(getContext());
        api.fetchEvents(User.getCurrentUserId(),new Callback<List<Event>>() {
            @Override
            public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {

                List<Event> eventsList = new ArrayList<>();
                Log.v(TAG, response.toString());

                if (response.body()!=null)
                    eventsList = response.body();

                Log.i(TAG, response.message());
                Log.v(TAG, "list size; "+ eventsList.size());
                try {
                    removeEventsCreatedBy(eventsList, User.getCurrentUserId());
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

    private void removeEventsCreatedBy(List<Event> eventsList, String currentUserId) {
        for (Event e : eventsList) {
            try {
                if(e.getCreatedby().equalsIgnoreCase(currentUserId)) eventsList.remove(e);

            }catch (NullPointerException ex){

            }
        }
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

        MyShindingsAdapter adapter = new MyShindingsAdapter(eventsList, new ClickEvent() {
            @Override
            public void Click(Event event) {

            }

            @Override
            public void openEvent(Event event) {

            }
        });
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
