package com.shindygo.shindy.main;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.shindygo.shindy.Api;
import com.shindygo.shindy.MainActivity;
import com.shindygo.shindy.R;
import com.shindygo.shindy.adapter.NewUserCardAdapter;
import com.shindygo.shindy.api.EventController;
import com.shindygo.shindy.interfaces.ClickCard;
import com.shindygo.shindy.interfaces.ClickEventCard;
import com.shindygo.shindy.interfaces.OnClickShow;
import com.shindygo.shindy.main.adapter.EventUserAdapter;
import com.shindygo.shindy.model.Event;
import com.shindygo.shindy.model.InviteEvent;
import com.shindygo.shindy.model.User;
import com.shindygo.shindy.utils.Cache;
import com.shindygo.shindy.utils.MySharedPref;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.SwipeDirection;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static android.view.View.GONE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewUsersFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewUsersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewUsersFragment extends Fragment {

    private static final String TAG = "NewUsersFragment";


    private String mParam1;
    private String mParam2;
    private ProgressBar progressBar;
    private CardStackView cardStackView;
    NewUserCardAdapter adapter;
    private OnFragmentInteractionListener mListener;
    FrameLayout mRelativeLayout;

    List<User> users = new ArrayList<>();
    List<Event> eventList;
    private String choosenEvent;
    private int isAnon=0;
    private int isPay=0;


    public NewUsersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewUsersFragment.
     */
    public static NewUsersFragment newInstance(String param1, String param2) {
        NewUsersFragment fragment = new NewUsersFragment();
        Bundle args = new Bundle();/*
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);*/
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            /*mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);*/
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_users, container, false);
//        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager);
//        viewPager.setAdapter();
        mRelativeLayout = view.findViewById(R.id.rl);
        setup(view);
        reload();
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

    private void setup(View view) {
        progressBar = (ProgressBar) view.findViewById(R.id.activity_main_progress_bar);

        cardStackView = (CardStackView) view.findViewById(R.id.activity_main_card_stack_view);

        cardStackView.setCardEventListener(new CardStackView.CardEventListener() {
            @Override
            public void onCardDragging(float percentX, float percentY) {
                Log.d("CardStackView", "onCardDragging");
            }

            @Override
            public void onCardSwiped(SwipeDirection direction) {
                Log.d("CardStackView", "onCardSwiped: " + direction.toString());
                Log.d("CardStackView", "topIndex: " + cardStackView.getTopIndex());
                if(direction.toString().equals("Left")){
                   /* new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            removeFirst();
                        }
                    }, 1000);*/
                    // removeFirst();
                  //  Log.v(TAG, "size: "+adapter.getCount()+ " cards: "+cardStackView.getTopIndex());
                    int i = adapter.getCount() - cardStackView.getTopIndex();
                  //  Log.v(TAG, "pos: "+i);

                    setTabBadgeText(i);
                }else  if(direction.toString().equals("Right")) {
                    addToLike(getTopUser());
                    setTabBadgeText(adapter.getCount() - cardStackView.getTopIndex());

                }
                /*if (cardStackView.getTopIndex() == adapter.getCount() - 5) {
                    Log.d("CardStackView", "Paginate: " + cardStackView.getTopIndex());
                    paginate();
                }*/
            }

            @Override
            public void onCardReversed() {
                Log.d("CardStackView", "onCardReversed");
            }

            @Override
            public void onCardMovedToOrigin() {
                Log.d("CardStackView", "onCardMovedToOrigin");
            }

            @Override
            public void onCardClicked(int index) {
                Log.d("CardStackView", "onCardClicked: " + index);
            }
        });
    }

    private void reload() {
        cardStackView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        Api api = Api.getInstance();
        Log.v(TAG, "fetchNewUsers "+User.getCurrentUserId());
        api.fetchNewUsers(User.getCurrentUserId(), new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                users.clear();
                Log.v(TAG, "resp "+response.message());
                try {
                    if (response.body()!=null)
                        users = response.body();
                        Cache.setNewUsers(users);
                        setUpCardViews(users);

                    progressBar.setVisibility(View.GONE);
                    setTabBadgeText(users.size());

                }catch (NullPointerException e){
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.e(TAG, " fetchNewUsers failed");
                progressBar.setVisibility(View.GONE);
                try {
                    Snackbar.make(getView().findViewById(R.id.rl), R.string.list_empty,
                            Snackbar.LENGTH_SHORT)
                            .show();
                }catch (NullPointerException e){
                    e.printStackTrace();
                }

                setTabBadgeText(users.size());


            }
        });
        /*
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter = createNewUserCardAdapter();
                cardStackView.setAdapter(adapter);
                cardStackView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        }, 1000);*/
    }

    private void setUpCardViews(List<User> users) {
        if(users != null){
            Log.v(TAG, "usersSize "+users.size());
            setTabBadgeText(2, users.size());
            MySharedPref.setNewUsersCount(users.size());
            adapter = createNewUserCardAdapter(users);
            cardStackView.setAdapter(adapter);
            cardStackView.setVisibility(View.VISIBLE);
            adapter.notifyDataSetChanged();

        }else {
            Log.v(TAG, "users null ");
        }
    }

    private void setTabBadgeText(int i, int size) {
        if(size>0){
            ((MainActivity)getActivity()).setTabBadgeText(i, size+"");
        }else{
            ((MainActivity)getActivity()).setTabBadgeText(i, null);
            Snackbar.make(getView().findViewById(R.id.rl), R.string.list_empty,
                    Snackbar.LENGTH_SHORT)
                    .show();
        }
    }

    private LinkedList<User> extractRemainingCard() {
        LinkedList<User> users = new LinkedList<>();
        for (int i = cardStackView.getTopIndex(); i < adapter.getCount(); i++) {
            users.add(adapter.getItem(i));
        }
        return users;
    }
/*
    private void addFirst() {
        LinkedList<User> users = extractRemainingCard();
        spots.addFirst(createNewUser());
        adapter.clear();
        adapter.addAll(spots);
        adapter.notifyDataSetChanged();
    }

    private void addLast() {
        LinkedList<User> card = extractRemainingCard();
        spots.addLast(createNewUser());
        adapter.clear();
        adapter.addAll(spots);
        adapter.notifyDataSetChanged();
    }*/

    private User getTopUser(){
        return adapter.getItem(cardStackView.getTopIndex()-1);
    }
    private void removeFirst() {

        LinkedList<User> users  = extractRemainingCard();

        if (users.isEmpty()) {
            return ;
        }

        //User user = users.removeFirst();
        adapter.clear();
        adapter.addAll(users);
        adapter.notifyDataSetChanged();
        setTabBadgeText(users.size());
        //Log.v(TAG,"remaining: " +users.size());



    }

    private void setTabBadgeText(int value) {
        setTabBadgeText(2,value);
    }

    private void removeLast() {
        LinkedList<User> users = extractRemainingCard();
        if (users.isEmpty()) {
            return;
        }

        users.removeLast();
        adapter.clear();
        adapter.addAll(users);
        adapter.notifyDataSetChanged();
        setTabBadgeText(users.size());

    }

    private void paginate() {
        cardStackView.setPaginationReserved();
        // adapter.addAll(users);
        // adapter.notifyDataSetChanged();
        setTabBadgeText(adapter.getCount());

    }

    public void swipeLeft() {
        List<User> users = extractRemainingCard();
        if (users.isEmpty()) {
            return;
        }

        View target = cardStackView.getTopView();

        ValueAnimator rotation = ObjectAnimator.ofPropertyValuesHolder(
                target, PropertyValuesHolder.ofFloat("rotation", -10f));
        rotation.setDuration(200);
        ValueAnimator translateX = ObjectAnimator.ofPropertyValuesHolder(
                target, PropertyValuesHolder.ofFloat("translationX", 0f, -2000f));
        ValueAnimator translateY = ObjectAnimator.ofPropertyValuesHolder(
                target, PropertyValuesHolder.ofFloat("translationY", 0f, 500f));
        translateX.setStartDelay(100);
        translateY.setStartDelay(100);
        translateX.setDuration(500);
        translateY.setDuration(500);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(rotation, translateX, translateY);

        cardStackView.swipe(SwipeDirection.Left, set);


    }

    public void swipeRight() {
        List<User> user = extractRemainingCard();
        if (user.isEmpty()) {
            return;
        }

        View target = cardStackView.getTopView();

        ValueAnimator rotation = ObjectAnimator.ofPropertyValuesHolder(
                target, PropertyValuesHolder.ofFloat("rotation", 10f));
        rotation.setDuration(200);
        ValueAnimator translateX = ObjectAnimator.ofPropertyValuesHolder(
                target, PropertyValuesHolder.ofFloat("translationX", 0f, 2000f));
        ValueAnimator translateY = ObjectAnimator.ofPropertyValuesHolder(
                target, PropertyValuesHolder.ofFloat("translationY", 0f, 500f));
        translateX.setStartDelay(100);
        translateY.setStartDelay(100);
        translateX.setDuration(500);
        translateY.setDuration(500);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(rotation, translateX, translateY);

        cardStackView.swipe(SwipeDirection.Right, set);
        // addToLike(users.get(0));
        //removeFirst();
    }

    private void addToLike(User user){

        Api api = Api.getInstance();
        final String name = user.getFullname();
        Log.v(TAG, "addToLike: "+ name );
        api.likeUserToGroup(User.getCurrentUserId(), user.getFbid(), new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                if (response.body()==null) return;
                JSONObject r = response.body();
                if(r.optString("status","failed").equals("success")){
                    //removeFirst();
                    // adapter.notifyDataSetChanged();

                    Log.v(TAG, "addToLike: "+ name);
                    int n = MySharedPref.getNewUsersCount();
                    MySharedPref.setNewUsersCount(--n);

                }
            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {
                Log.e(TAG, call.toString());
                reload();
            }
        });
    }

    private void reverse() {
        cardStackView.reverse();
    }


    private NewUserCardAdapter createNewUserCardAdapter(List<User> userList) {
        final NewUserCardAdapter adapter = new NewUserCardAdapter(getContext(), new ClickCard() {
            @Override
            public void Click(boolean b) {
                if (b)
                    swipeRight();
                else
                    swipeLeft();
            }
        }, createPopUp());
        adapter.addAll(userList);
        return adapter;
    }



    private OnClickShow createPopUp() {
        return new OnClickShow<User>() {
            public PopupWindow mPopupWindow;

            @Override
            public void Show(final User user) {
                new EventController(getContext()).getEventListCreatedByUser(new Callback<List<Event>>() {
                    @Override
                    public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                        if (response!=null)
                            eventList = response.body();

                        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                        String[] religions = getResources().getStringArray(R.array.religion);
                        String[] prefs = getResources().getStringArray(R.array.gender_preference);
                        final SharedPreferences sharedPref = getContext().getSharedPreferences("set", Context.MODE_PRIVATE);
                        final String id = sharedPref.getString("fbid", "");

                        // Inflate the custom layout/view
                        View customView = inflater.inflate(R.layout.profile_popup, null);
                        ImageView imageView = customView.findViewById(R.id.imageView2);
                        final ImageView star = customView.findViewById(R.id.iv_star);
                        Glide.with(getContext()).load(user.getPhoto()).into(imageView);
                        TextView name = customView.findViewById(R.id.tv_name);
                        TextView about = customView.findViewById(R.id.tv_desc);
                        about.setText(user.getAbout());
                        TextView city = customView.findViewById(R.id.tv_city);
                        city.setText(user.getAddress());
                        TextView religion = customView.findViewById(R.id.tv_religon);
                        religion.setText(religions[Integer.parseInt(user.getReligion())]);
                        TextView pref = customView.findViewById(R.id.tv_pref);
                        pref.setText("Preference: " + prefs[Integer.parseInt(user.getGenderPref())]);
                        name.setText(user.getFullname());
                        final ImageView arrow = customView.findViewById(R.id.iv_arrow);
                        final ImageView pay = customView.findViewById(R.id.pay);
                        final ImageView anonim = customView.findViewById(R.id.anonim);
                        final TextView textView = customView.findViewById(R.id.tv_Bam);
                        final LinearLayout invite_view = customView.findViewById(R.id.ll_invite_view);
                        final RecyclerView recycler = customView.findViewById(R.id.rv_event_user);
                        recycler.setLayoutManager(new LinearLayoutManager(getContext(), 0, false));
                        recycler.setAdapter(new EventUserAdapter(new ClickEventCard() {
                            @Override
                            public void Click(boolean b, String eventId) {
                                choosenEvent = eventId;
                                arrow.setVisibility(View.VISIBLE);
                            }
                        }, eventList));
                        LinearLayout invite = customView.findViewById(R.id.ll_invite);
                        invite.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                recycler.setAdapter(new EventUserAdapter(new ClickEventCard() {
                                    @Override
                                    public void Click(boolean b, String eventId) {
                                        //choosenEvent = eventId;
                                        arrow.setVisibility(View.VISIBLE);
                                    }
                                }, eventList));
                                invite_view.setVisibility(invite_view.getVisibility()==View.VISIBLE?GONE:View.VISIBLE);
                            }
                        });
                        pay.setColorFilter(ContextCompat.getColor(getContext(), R.color.gray_tint));
                        pay.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (isPay==1){
                                    isPay=0;
                                    pay.setColorFilter(ContextCompat.getColor(getContext(), R.color.fb_blue));
                                }
                                else {
                                    isPay=1;
                                    pay.setColorFilter(ContextCompat.getColor(getContext(), R.color.gray_tint));
                                }

                            }

                        });
                        anonim.setColorFilter(ContextCompat.getColor(getContext(), R.color.gray_tint));
                        anonim.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (isAnon==1){
                                    isAnon=0;
                                    anonim.setColorFilter(ContextCompat.getColor(getContext(), R.color.fb_blue));
                                }
                                else {
                                    isAnon=1;
                                    anonim.setColorFilter(ContextCompat.getColor(getContext(), R.color.gray_tint));
                                }


                            }
                        });
                        arrow.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                textView.setVisibility(View.VISIBLE);
                                //todo send invite
                                EventUserAdapter adapter = (EventUserAdapter) recycler.getAdapter();
                                EventController eventController = new EventController(getContext());
                                List<Event> events = adapter.getSelectedItems();
                                for (int i = 0; i < events.size(); i++) {
                                    eventController.sendinvite(new InviteEvent(events.get(i).getEventid(), user.getFbid(), isAnon, isPay), new Callback<ResponseBody>() {
                                        @Override
                                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                            if (response != null) {
                                                Log.v("sendInvite", response.body().toString());
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                                            t.printStackTrace();
                                        }
                                    });


                                }

                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        textView.setVisibility(GONE);

                                    }
                                }, 2000);

                            }
                        });
                        final Api api = Api.getInstance();
                        LinearLayout linearLayout = customView.findViewById(R.id.ll_block);
                        linearLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                api.blockUser(id, user.getFbid(), new Callback<Object>() {
                                    @Override
                                    public void onResponse(Call<Object> call, Response<Object> response) {
                                        Toast.makeText(getContext(), "User blocked succesfuly!", Toast.LENGTH_SHORT).show();
                                        mPopupWindow.dismiss();
                                    }

                                    @Override
                                    public void onFailure(Call<Object> call, Throwable t) {
                                        Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                                        mPopupWindow.dismiss();
                                    }
                                });
                            }
                        });

                               /* LinearLayout ll_message = customView.findViewById(R.id.ll_message);
                                ll_message.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                       // createMessage();
                                    }
                                });*/
                        LinearLayout favorite = customView.findViewById(R.id.ll_favorite);
                        favorite.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (user.getMarkasfavorite().equals("0")) {
                                    star.setColorFilter(ContextCompat.getColor(getContext(), R.color.navigation_notification_yellow));
                                    api.addFavoriteUser(id, user.getFbid(), new Callback<Object>() {
                                        @Override
                                        public void onResponse(Call<Object> call, Response<Object> response) {
                                            Toast.makeText(getContext(), "User favorite!", Toast.LENGTH_SHORT).show();
                                            users.get(users.indexOf(user)).setMarkasfavorite("1");
                                            user.setMarkasfavorite("1");
                                            adapter.notifyDataSetChanged();
                                            //   mPopupWindow.dismiss();
                                        }

                                        @Override
                                        public void onFailure(Call<Object> call, Throwable t) {
                                            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                                            mPopupWindow.dismiss();
                                        }
                                    });
                                } else {
                                    star.setColorFilter(null);
                                    api.RemoveFavoriteUser(id, user.getFbid(), new Callback<Object>() {
                                        @Override
                                        public void onResponse(Call<Object> call, Response<Object> response) {
                                            Toast.makeText(getContext(), "User remove from favorite!", Toast.LENGTH_SHORT).show();
                                            users.get(users.indexOf(user)).setMarkasfavorite("0");
                                            user.setMarkasfavorite("0");
                                            adapter.notifyDataSetChanged();
                                            //   mPopupWindow.dismiss();
                                        }

                                        @Override
                                        public void onFailure(Call<Object> call, Throwable t) {
                                            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                                            mPopupWindow.dismiss();
                                        }
                                    });
                                }
                            }
                        });
                        if (user.isMarkAsFavorite()) {
                            star.setColorFilter(ContextCompat.getColor(getContext(), R.color.navigation_notification_yellow));
                        } else
                            star.setColorFilter(null);
                        mPopupWindow = new PopupWindow(
                                customView,
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        );
                        if (Build.VERSION.SDK_INT >= 21) {
                            mPopupWindow.setElevation(5.0f);
                        }

                        // Get a reference for the custom view close button
                        ImageView closeButton = customView.findViewById(R.id.back);
                        // Set a click listener for the popup window close button
                        closeButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Dismiss the popup window
                                mPopupWindow.dismiss();
                            }
                        });

                        mPopupWindow.showAtLocation(mRelativeLayout, Gravity.CENTER, 0, 0);
                        /*
                        adapter = new UsersAdapter(users,eventList, new ClickUser() {
                            @Override
                            public void Click(final User user) {

                            }
                        }, new ClickCard() {
                            @Override
                            public void Click(boolean b) {
                               // createMessage();
                            }
                        });
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setAdapter(adapter);*/
                    }

                    @Override
                    public void onFailure(Call<List<Event>> call, Throwable t) {
                        eventList=new ArrayList<>();
                        t.printStackTrace();
                    }
                });


                /*
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE);

                // Inflate the custom layout/view
                View customView = inflater.inflate(R.layout.profile_popup, null);

                ImageView imageView = customView.findViewById(R.id.imageView2);
                Glide.with(getContext()).load(user.getPhoto()).into(imageView);
                TextView name = customView.findViewById(R.id.tv_name);
                name.setText(getString(R.string.name_n_age, user.getFullname(), user.getAge()));
                TextView about = customView.findViewById(R.id.tv_desc);
                about.setText(user.getAbout());
                TextView city = customView.findViewById(R.id.tv_city);
                city.setText(user.getAddress());
                TextView pref = customView.findViewById(R.id.tv_pref);
                pref.setText(user.getGenderPrefAsText());
                TextView religion = customView.findViewById(R.id.tv_religon);
                religion.setText(user.getReligonAsText());

*//*
                LinearLayout menubar = customView.findViewById(R.id.menubar);
                menubar.setVisibility(View.GONE);*//*


                mPopupWindow = new PopupWindow(
                        customView,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                if (Build.VERSION.SDK_INT >= 21) {
                    mPopupWindow.setElevation(5.0f);
                }

                // Get a reference for the custom view close button
                ImageView closeButton = customView.findViewById(R.id.back);

                // Set a click listener for the popup window close button
                closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Dismiss the popup window
                        mPopupWindow.dismiss();
                    }
                });

                mPopupWindow.showAtLocation(mRelativeLayout, Gravity.CENTER, 0, 0);
            */

            }
        };
    }


}
