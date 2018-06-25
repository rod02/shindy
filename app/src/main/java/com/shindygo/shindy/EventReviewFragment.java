package com.shindygo.shindy;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.shindygo.shindy.api.EventController;
import com.shindygo.shindy.interfaces.ClickEvent;
import com.shindygo.shindy.main.adapter.ReviewDetailsEventAdapter;
import com.shindygo.shindy.main.adapter.ReviewEventAdapter;
import com.shindygo.shindy.model.Event;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EventReviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventReviewFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    EventController eventController;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    List<Event> events = new ArrayList<>();
    ReviewEventAdapter adapter;

    private OnFragmentInteractionListener mListener;
    private PopupWindow mPopupWindow;
    private LinearLayout mLinearLayout;

    public EventReviewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EventReviewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EventReviewFragment newInstance(String param1, String param2) {
        EventReviewFragment fragment = new EventReviewFragment();
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
        View view = inflater.inflate(R.layout.fragment_event_review, container, false);
        ButterKnife.bind(this, view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mLinearLayout = view.findViewById(R.id.ll);


        eventController = new EventController(getContext());
        eventController.get_attending_event(new Callback<List<Event>>() {
            @Override
            public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                if (response!=null)
                events = response.body();
                adapter= new ReviewEventAdapter(events, new ClickEvent() {
                    @Override
                    public void Click(final Event event) {
                        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE);

                        // Inflate the custom layout/view
                        View customView = inflater.inflate(R.layout.event_review_popup, null);
                        Button submit = customView.findViewById(R.id.submit);
                        final EditText feedback = customView.findViewById(R.id.et_comment);
                        final RatingBar ratingBarEvent = customView.findViewById(R.id.rating_bar_event);
                        ratingBarEvent.setNumStars(5);
                        final RatingBar ratingBarHost = customView.findViewById(R.id.rating_bar_host);
                        ratingBarHost.setNumStars(5);
                        ratingBarEvent.setMax(5);
                        ratingBarHost.setMax(5);
                        mPopupWindow = new PopupWindow(
                                customView,
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                true
                        );
                        if (Build.VERSION.SDK_INT >= 21) {
                            mPopupWindow.setElevation(5.0f);
                        }

                        // Get a reference for the custom view close button
                        ImageView closeButton = customView.findViewById(R.id.close);

                        // Set a click listener for the popup window close button
                        closeButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Dismiss the popup window
                                mPopupWindow.dismiss();
                            }
                        });
                        submit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String rating_event = String.valueOf(ratingBarEvent.getRating());
                                String rating_host = String.valueOf(ratingBarHost.getRating());
                                eventController.rateEvent(event.getEventid(), rating_event, rating_host, feedback.getText().toString(), new Callback<Object>() {
                                    @Override
                                    public void onResponse(Call<Object> call, Response<Object> response) {
                                        mPopupWindow.dismiss();
                                        Toast.makeText(getContext(), "Thank you for review)", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onFailure(Call<Object> call, Throwable t) {

                                    }
                                });
                            }
                        });

                        mPopupWindow.showAtLocation(mLinearLayout, Gravity.CENTER, 0, 0);
                    }

                    @Override
                    public void openEvent(Event event) {
                        Intent intent = new Intent(getContext(),EventDetailActivity.class);
                        intent.putExtra("event", event);
                        startActivity(intent);

                    }
                });
                if(events!=null)
                recyclerView.setAdapter(adapter);


            }

            @Override
            public void onFailure(Call<List<Event>> call, Throwable t) {
                Log.e("Events",t.getMessage());

                try {
                    events.clear();
                    adapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });



        return view;
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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

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
