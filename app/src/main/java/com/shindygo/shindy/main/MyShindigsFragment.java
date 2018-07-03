package com.shindygo.shindy.main;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shindygo.shindy.MyInvitesActivity;
import com.shindygo.shindy.R;
import com.shindygo.shindy.api.EventController;
import com.shindygo.shindy.interfaces.ClickEvent;
import com.shindygo.shindy.main.adapter.MyShindingsAdapter;
import com.shindygo.shindy.model.Event;
import com.shindygo.shindy.model.Status;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyShindigsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyShindigsFragment extends Fragment {

    private static final String TAG = MyShindingsAdapter.class.getSimpleName();


    @BindView(R.id.rv_shindings_attending)
    RecyclerView rvShindingsAttending;
    @BindView(R.id.rv_shindings_invited)
    RecyclerView rvShindingsInvited;
    Unbinder unbinder;
    @BindView(R.id.tv_invite)
    TextView tvInvite;
    @BindView(R.id.linearLayout)
    LinearLayout linearLayout;
    @BindView(R.id.linearLayout2)
    LinearLayout linearLayout2;




    private OnFragmentInteractionListener mListener;
    private PopupWindow mPopupWindow;
    private RelativeLayout mRelativeLayout;
    List<Event> invited_list = new ArrayList<>();
    List<Event> attending_list = new ArrayList<>();
    EventController eventController;
    public MyShindigsFragment() {
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
    public static MyShindigsFragment newInstance(String param1, String param2) {
        MyShindigsFragment fragment = new MyShindigsFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        update();
    }
    public void update() {
        eventController.getAttendingEvent(new Callback<List<Event>>() {
            @Override
            public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                try {

                    attending_list = response.body();
                    rvShindingsAttending.setLayoutManager(new LinearLayoutManager(getActivity()));
                    if (attending_list!=null)
                        rvShindingsAttending.setAdapter(new MyShindingsAdapter(attending_list, new ClickEvent() {
                            @Override
                            public void Click(Event event) {

                            }

                            @Override
                            public void openEvent(Event event) {

                            }
                        }));
                   // rvShindingsAttending.setNestedScrollingEnabled(true);
                }catch (NullPointerException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<List<Event>> call, Throwable t) {
                t.printStackTrace();
            }
        });
        eventController.getInvitedEvent(new Callback<List<Event>>() {
            @Override
            public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                try {
                    invited_list = response.body();
                    rvShindingsInvited.setLayoutManager(new LinearLayoutManager(getActivity()));
                    if (invited_list != null)
                        rvShindingsInvited.setAdapter(new MyShindingsAdapter(invited_list, new ClickEvent() {
                            @Override
                            public void Click(Event event) {
                                startActivity(new Intent(getContext(), MyInvitesActivity.class).putExtra("event", event));
                            }

                            @Override
                            public void openEvent(Event event) {

                            }
                        }));
                    rvShindingsInvited.setNestedScrollingEnabled(false);
                }catch (NullPointerException e){

                }

                }

            @Override
            public void onFailure(Call<List<Event>> call, Throwable t) {

            }
        });
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_my_shindigs, container, false);
        unbinder = ButterKnife.bind(this, view);
        eventController = new EventController(getContext());

        update();
        tvInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE);

                // Inflate the custom layout/view
                View customView = inflater.inflate(R.layout.popup_invite, null);
                final EditText editText = customView.findViewById(R.id.editText);
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
                Button ok = customView.findViewById(R.id.btn_ok);
                mRelativeLayout = view.findViewById(R.id.rl);
                // Set a click listener for the popup window close button
                closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Dismiss the popup window
                        mPopupWindow.dismiss();
                    }
                });
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (editText.getText().toString().length()>0)
                            eventController.EnterInviteCode(editText.getText().toString(), new Callback<Status>() {
                                @Override
                                public void onResponse(Call<Status> call, Response<Status> response) {
                                    Log.d(TAG, response.toString());
                                    Status status = response.body();
                                    Toast.makeText(getContext(), status.getStatus().equals("success")?"You are attend for this event!":"Invalid invite code", Toast.LENGTH_SHORT).show();
                                    update();
                                    mPopupWindow.dismiss();

                                }

                                @Override
                                public void onFailure(Call<Status> call, Throwable t) {

                                }
                            });
                    }
                });
                mPopupWindow.showAtLocation(mRelativeLayout, Gravity.CENTER, 0, 0);
            }
        });
        return view;
    }


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
        void onFragmentInteraction(Uri uri);
    }
}
