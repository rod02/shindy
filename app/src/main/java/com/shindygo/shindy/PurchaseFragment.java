package com.shindygo.shindy;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.shindygo.shindy.api.EventController;
import com.shindygo.shindy.api.OpenEdge;
import com.shindygo.shindy.model.Event;
import com.shindygo.shindy.model.OpenedgeSetupResponse;
import com.shindygo.shindy.model.Status;
import com.shindygo.shindy.model.User;
import com.shindygo.shindy.utils.GlideImage;
import com.shindygo.shindy.utils.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.MessageFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PurchaseFragment extends Fragment {

    private static final int RQ_PAY = 44;
    private static final String TAG = PurchaseFragment.class.getSimpleName();
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.top)
    LinearLayout top;
    @BindView(R.id.start)
    LinearLayout start;
    @BindView(R.id.middle)
    LinearLayout middle;


    @BindView(R.id.btn_submit)
    Button btnSubmit;

    Unbinder unbinder;
    Event event;


    @BindView(R.id.iv_avatar)
    RoundedImageView ivAvatar;

    @BindView(R.id.tv_eventName)
    TextView tvEventName;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.tv_invited_by)
    TextView tvInvitedBy;

    @BindView(R.id.tv_local_tax)
    TextView tvLocalTax;
    @BindView(R.id.tv_ticket_price)
    TextView tvTicketPrice;
    @BindView(R.id.tv_price_total)
    TextView tvPriceTotal;
    @BindView(R.id.btn_checkout)
    Button btnCheckout;

    @BindView(R.id.lay_progress)
    FrameLayout layProgress;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_purchase, container, false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        try {
            event = ((EventDetailActivity) this.getActivity()).getEvent();

            GlideImage.load(getContext(), event.getImage(), ivAvatar);

            tvEventName.setText(event.getEventname());
            tvInvitedBy.setText(getString(R.string.invited_by_n, event.getInvitedby()));
            tvDate.setText(event.getEventSched());

            tvLocalTax.setText(event.getCustomPrice() + "$");
            tvTicketPrice.setText(event.getTicketprice() + "$");
            try {
                double v = Double.parseDouble(event.getTicketprice()) + Double.parseDouble(event.getCustomPrice());
                tvPriceTotal.setText(v + "$");
            }catch (Exception e){}
            // tvExpires.setText(MessageFormat.format("Expires: {0}", event.getExpirydate()));
            if (event.getSchedStartdate() != null)
                tvDate.setText(event.getSchedStartdate().toString());
            else
                tvDate.setText("");
            btnCheckout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    layProgress.setVisibility(View.VISIBLE);
                    Log.d(TAG, "onClick id: "+generateOrderId()
                            + " \nticket: " +event.getTicketprice()
                            + " \nfbid: " +User.getCurrentUserId());
                    setupTransaction();

                }
            });
        }catch (NullPointerException e){
            e.printStackTrace();
        }

    }

    void setupTransaction(){
        EventController eventController = new EventController(getContext());
        eventController.checkout(User.getCurrentUserId(), event.getEventid(), event.getTicketprice(),
                new Callback<OpenedgeSetupResponse>() {
                    @Override
                    public void onResponse(Call<OpenedgeSetupResponse> call, Response<OpenedgeSetupResponse> response) {
                        try {
                            if (response.message().equalsIgnoreCase("ok")){
                                    OpenedgeSetupResponse setupResponse = response.body();
                                    try {
                                        if(setupResponse.paid.equalsIgnoreCase("paid")) {
                                            join(event);
                                            return;
                                        }
                                    }catch (NullPointerException e){

                                    }
                                    getPayPage(setupResponse);

                            }
                        }catch (NullPointerException e){

                        }

                    }

                    @Override
                    public void onFailure(Call<OpenedgeSetupResponse> call, Throwable t) {
                        Log.d(TAG, t.getLocalizedMessage());
                        layProgress.setVisibility(View.GONE);

                    }
                });
    }

    void oldSetupTransaction(){

        OpenEdge.getInstance().setupRequest(generateOrderId(), event.getTicketprice(), User.getCurrentUserId(),
                new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        Log.d(TAG, "onResponse "+response.toString());
                        try {
                            if(response.message().equalsIgnoreCase("ok")){
                                ResponseBody osr = response.body();
                                String sealedSetupParameters ="";
                                try {
                                    String stringRes = osr.string();
                                    Log.d(TAG, "onResponse "+stringRes);
                                    JSONObject json = new JSONObject(stringRes);
                                    String errorMsg = json.optString("errorMessage",null);
                                    if (errorMsg!=null){
                                        Toast.makeText(getContext(), errorMsg, Toast.LENGTH_LONG).show();
                                        layProgress.setVisibility(View.GONE);
                                        return;
                                    }
                                    sealedSetupParameters = json.optString("sealedSetupParameters","");

                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                Log.d(TAG, "onResponse "+sealedSetupParameters);


                            }
                        }catch (NullPointerException e){

                        }

                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.d(TAG, "onFailure "+t.getLocalizedMessage());

                        layProgress.setVisibility(View.GONE);

                    }
                });

    }


    void getPayPage(OpenedgeSetupResponse setupResponse){
        OpenEdge.getInstance().paypage(setupResponse.sealedSetupParameters,
                new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        Log.d(TAG, "onResponse "+response.toString());
                        try {
                            if(response.message().equalsIgnoreCase("ok")){
                                try {
                                    String stringHtmls =  response.body().string();
                                    //  Log.d(TAG, "onResponse "+stringHtmls);
                                    openPayPage(stringHtmls);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                            layProgress.setVisibility(View.GONE);
                        }catch (NullPointerException  e){

                        }


                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.d(TAG, "onFailure "+t.getLocalizedMessage());
                        layProgress.setVisibility(View.GONE);

                    }
                });
    }


    void openPayPage(String stringHtmls){
        layProgress.setVisibility(View.GONE);

        Intent intent = new Intent(getContext(), PaymentPageActivity.class);

        intent.putExtra(PaymentPageActivity.HTML_DATA,stringHtmls);
        startActivityForResult(intent,RQ_PAY);
    }


    private String generateOrderId() {
        return event.getEventid()+  User.getCurrentUserId();
    }

    private void join(Event event){

        new EventController(getContext()).joinIamInEvent(event.getEventid(),event.getInvitecode(), new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                try {
                    Status status = response.body();
                    if(status.getStatus().equals("success")) {
                        ((EventDetailActivity) getActivity()).setEventAttandeeStatus();
                        Toast.makeText(getContext(), "You are in", Toast.LENGTH_LONG).show();
                        getActivity().onBackPressed();
                    }
                    else
                        Toast.makeText(getContext(), status.getResult(), Toast.LENGTH_LONG).show();
                    layProgress.setVisibility(View.GONE);
                }catch (NullPointerException e){

                }

            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                try {
                    layProgress.setVisibility(View.GONE);

                    getActivity().onBackPressed();
                }catch (NullPointerException e){

                }


            }
        });

    }


    @Override
    public void onActivityResult ( int requestCode, int resultCode, Intent data){
        switch (requestCode){
            case RQ_PAY:
                if(resultCode== Activity.RESULT_OK){
                    join(event);
                }

                break;
        }

    }
}
