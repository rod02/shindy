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
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.shindygo.shindy.api.EventController;
import com.shindygo.shindy.interfaces.ClickEvent;
import com.shindygo.shindy.main.adapter.BlockedEventAdapter;
import com.shindygo.shindy.main.adapter.LikedEventAdapter;
import com.shindygo.shindy.main.model.Respo;
import com.shindygo.shindy.model.Event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BlockedEventFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BlockedEventFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    EventController eventController;
    List<Event> events = new ArrayList<>();
    @BindView(R.id.rv_choose_event)
    RecyclerView rvChooseEvent;
    Unbinder unbinder;
    BlockedEventAdapter blockedEventAdapter;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public BlockedEventFragment() {
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
    public static BlockedEventFragment newInstance(String param1, String param2) {
        BlockedEventFragment fragment = new BlockedEventFragment();
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
        View view = inflater.inflate(R.layout.fragment_liked, container, false);
        unbinder = ButterKnife.bind(this, view);
        eventController = new EventController(getContext());

        rvChooseEvent.setLayoutManager(new LinearLayoutManager(getContext()));
        rvChooseEvent.setAdapter(blockedEventAdapter);
        getBlockedEvents();
        return view;
    }

    void getBlockedEvents(){

        try {
            eventController.getBlockedEvents(new Callback<List<Event>>() {
                @Override
                public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {



                    events =  response.body();
                    blockedEventAdapter = new BlockedEventAdapter(events, new ClickEvent() {
                        @Override
                        public void Click(Event event) {
                            events.remove(event);
                            blockedEventAdapter.notifyDataSetChanged();
                            eventController.unblockEvent(event.getEventid(), event.getBlockCode(), new Callback<Respo>() {
                                @Override
                                public void onResponse(Call<Respo> call, Response<Respo> response) {
                                    try {
                                        if(response.body().isSuccses())
                                            Toast.makeText(getContext(),"Event Unblocked",Toast.LENGTH_SHORT).show();
                                        else
                                            Toast.makeText(getContext(),"You can`t unblock event",Toast.LENGTH_SHORT).show();


                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        Toast.makeText(getContext(),"You can`t unblock event",Toast.LENGTH_SHORT).show();
                                    }

                                }

                                @Override
                                public void onFailure(Call<Respo> call, Throwable t) {
                                    t.printStackTrace();

                                }
                            });
                        }

                        @Override
                        public void openEvent(Event event) {
                            Intent intent = new Intent(getContext(),EventDetailActivity.class);
                            intent.putExtra("event", event);
                            startActivity(intent);
                        }
                    });
                    if (events!=null)
                        rvChooseEvent.setAdapter(blockedEventAdapter);
                }

                @Override
                public void onFailure(Call<List<Event>> call, Throwable t) {
                    t.printStackTrace();
                    events.clear();
                    if (blockedEventAdapter!=null)
                        blockedEventAdapter.notifyDataSetChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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

    @Override
    public void onResume() {
        super.onResume();

        getBlockedEvents();


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
