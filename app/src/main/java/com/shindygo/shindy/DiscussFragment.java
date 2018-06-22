package com.shindygo.shindy;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shindygo.shindy.api.EventController;
import com.shindygo.shindy.interfaces.DiscussionClick;
import com.shindygo.shindy.main.adapter.ChatAdapter;
import com.shindygo.shindy.model.Discussion;
import com.shindygo.shindy.model.Event;
import com.shindygo.shindy.model.Reply;
import com.shindygo.shindy.model.Status;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by User on 27.03.2018.
 */

public class DiscussFragment extends Fragment {


    private static final String TAG = "DiscussFragment";

    @BindView(R.id.rv_chat)
    RecyclerView rvChat;
    Unbinder unbinder;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Event event;
    List<Discussion>  listOfDisc;
    /*
    @BindView(R.id.et_message)
    EditText etMessage;
    @BindView(R.id.send)
    TextView send;
*/
    static
    LinearLayout sendMessage;
    private String mParam1;
    private String mParam2;
    String myName,myPhoto;
    EventController eventController;
    ChatAdapter chatAdapter;
    private OnFragmentInteractionListener mListener;

    public DiscussFragment() {
        // Required empty public constructor
    }

    public static void changeVisibility()
    {
        sendMessage.setVisibility(sendMessage.getVisibility()==View.VISIBLE?View.GONE:View.VISIBLE);
    }

    public static DiscussFragment newInstance(String param1, String param2) {
        DiscussFragment fragment = new DiscussFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_discuss, container, false);
        unbinder = ButterKnife.bind(this, view);
        final EventDetailActivity activity = (EventDetailActivity) getActivity();
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvChat.setLayoutManager(linearLayoutManager);
//        rvChat.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                if(linearLayoutManager.findFirstVisibleItemPosition()==0)
//                {
//                    activity.show();
//                    rvChat.scrollToPosition(listOfDisc.size()-1);
//                }
//                else
//                    activity.hide();
//                super.onScrolled(recyclerView, dx, dy);
//            }
//        });
      //  sendMessage = view.findViewById(R.id.send_message);
        final SharedPreferences sharedPref = getContext().getSharedPreferences("set", Context.MODE_PRIVATE);
        myPhoto = sharedPref.getString("url", "");
        myName =sharedPref.getString("name", "");
        /*send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();

            }
        });
        etMessage.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                sendMessage();
              try {
                  rvChat.scrollToPosition(listOfDisc.size());
              }catch (NullPointerException e){
                  e.printStackTrace();
              }
                return false;
            }
        });*/
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        event = ((EventDetailActivity) this.getActivity()).getEvent();
        getDiscuss();
        try {
            rvChat.scrollToPosition(listOfDisc.size()-1);

        }catch (NullPointerException e){
            e.printStackTrace();
        }
        //TODO add image user
       // Glide.with(getContext()).load(event.getImage()).into(ivAvatar);

    }

    public void sendMessage(String message) {

        if (listOfDisc != null)
            rvChat.scrollToPosition(listOfDisc.size()-1);
        eventController.addDiscussElement(event.getEventid(),message, new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.d(TAG, "sendDiscussResponse: "+ response.toString());
                if(response.message().equalsIgnoreCase("ok")){
                    Toast.makeText(getContext(), "Send", Toast.LENGTH_SHORT).show();

                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getView().getWindowToken(),0);

                }
                //etMessage.setText("");
                getDiscuss();

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });
    }

    void sendMessage()
    {/*
        if (listOfDisc != null)
            rvChat.scrollToPosition(listOfDisc.size()-1);
        eventController.addDiscussElement(event.getEventid(),etMessage.getText().toString(), new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Toast.makeText(getContext(), "Send", Toast.LENGTH_SHORT).show();
                etMessage.setText("");
                getDiscuss();

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });*/
    }
    void getDiscuss()
    {
        eventController = new EventController(getContext());
        eventController.getDiscussEvents(event.getEventid(), new Callback<List<Discussion>>() {
            @Override
            public void onResponse(Call<List<Discussion>> call, Response<List<Discussion>> response) {
                   listOfDisc=response.body();

                chatAdapter =   new ChatAdapter(listOfDisc, new DiscussionClick() {


                    @Override
                    public void clickReply(final Discussion d, final String s) {
                        try{
                            getEventDetailActivity().getCommentBox().setTag(d);
                            getEventDetailActivity().getCommentBox().requestFocus();
                            getEventDetailActivity().getSendButton().setTag(EventDetailActivity.REPLY);
                            getEventDetailActivity().hideKeyboard();
                            getEventDetailActivity().showKeyBoard();
                        }catch (NullPointerException e){
                            e.printStackTrace();
                        }



/*
                        eventController.postReply(d.getEventid(), d.getDiscussionId(), s, new Callback<Status>() {
                            @Override
                            public void onResponse(Call<Status> call, Response<Status> response) {
                                if (response.isSuccessful()){
                                    listOfDisc.get(listOfDisc.indexOf(d)).reply.add(new Reply(s,myName,myPhoto));
                                    chatAdapter.notifyDataSetChanged();

                                    Toast.makeText(getContext(),"Success",Toast.LENGTH_SHORT).show();
                                }
                                else  Toast.makeText(getContext(),"Error",Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void onFailure(Call<Status> call, Throwable t) {
                                t.printStackTrace();
                            }
                        });*/
                    }
                });
                rvChat.setAdapter(chatAdapter);
                rvChat.scrollToPosition(chatAdapter.getItemCount() - 1);
            }

            @Override
            public void onFailure(Call<List<Discussion>> call, Throwable t) {
                t.printStackTrace();
                //TODO need clear if here
            }
        });
    }

    EventDetailActivity getEventDetailActivity(){
        return (EventDetailActivity) getActivity();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
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

    public void sendDiscussionReply(final Discussion discussion, final String reply) {
        eventController.postReply(discussion.getEventid(), discussion.getDiscussionId(), reply, new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                if (response.isSuccessful()){
                    listOfDisc.get(listOfDisc.indexOf(discussion)).reply.add(new Reply(reply,myName,myPhoto));
                    chatAdapter.notifyDataSetChanged();

                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getView().getWindowToken(),0);

                    Toast.makeText(getContext(),"Success",Toast.LENGTH_SHORT).show();
                }
                else  Toast.makeText(getContext(),"Error",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                t.printStackTrace();
            }
        });
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
