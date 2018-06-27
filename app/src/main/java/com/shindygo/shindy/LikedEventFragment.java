package com.shindygo.shindy;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shindygo.shindy.api.EventController;
import com.shindygo.shindy.interfaces.ClickEvent;
import com.shindygo.shindy.main.adapter.ChooseEventAdapter;
import com.shindygo.shindy.main.adapter.LikedEventAdapter;
import com.shindygo.shindy.model.Event;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LikedEventFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LikedEventFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    List<Event> events = new ArrayList<>();
    LikedEventAdapter likedEventAdapter;

    @BindView(R.id.rv_list)
    RecyclerView rvChooseEvent;
    Unbinder unbinder;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public LikedEventFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyShindigsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LikedEventFragment newInstance(String param1, String param2) {
        LikedEventFragment fragment = new LikedEventFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_liked_event, container, false);
        unbinder = ButterKnife.bind(this, view);

        update();

        return view;
    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        update();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
    void update()
    {
        final EventController eventController = new EventController(getContext());
        eventController.getLikedEvents(new Callback<List<Event>>() {
            @Override
            public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {

                try {
                    events = response.body();
                    rvChooseEvent.setLayoutManager(new LinearLayoutManager(getContext()));
                    likedEventAdapter = new LikedEventAdapter(events, new ClickEvent() {
                        @Override
                        public void Click(final Event event) {
                            eventController.unlikeEvent(event.getEventid(), event.getLikecode(), new Callback<Object>() {
                                @Override
                                public void onResponse(Call<Object> call, Response<Object> response) {
                                    events.remove(event);
                                    likedEventAdapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onFailure(Call<Object> call, Throwable t) {

                                }
                            });
                        }

                        @Override
                        public void openEvent(Event event) {
                            Intent intent = new Intent(getContext(),EventDetailActivity.class);
                            intent.putExtra("event", event);
                            startActivity(intent);
                        }
                    }

                    );
                   if(events!=null)
                        rvChooseEvent.setAdapter(likedEventAdapter);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<List<Event>> call, Throwable t) {
                rvChooseEvent.setLayoutManager(new LinearLayoutManager(getContext()));
                likedEventAdapter = new LikedEventAdapter(new ArrayList<Event>(), new ClickEvent() {
                    @Override
                    public void Click(final Event event) {
                        eventController.unlikeEvent(event.getEventid(), event.getLikecode(), new Callback<Object>() {
                            @Override
                            public void onResponse(Call<Object> call, Response<Object> response) {
                                events.remove(event);
                                likedEventAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onFailure(Call<Object> call, Throwable t) {

                            }
                        });
                    }

                    @Override
                    public void openEvent(Event event) {
                        Intent intent = new Intent(getContext(),EventDetailActivity.class);
                        intent.putExtra("event", event);
                        startActivity(intent);
                    }
                }

                );
                // if(events!=null)
                rvChooseEvent.setAdapter(likedEventAdapter);
            }
        });
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
