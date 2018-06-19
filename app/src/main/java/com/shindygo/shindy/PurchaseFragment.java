package com.shindygo.shindy;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.shindygo.shindy.api.EventController;
import com.shindygo.shindy.model.Event;
import com.shindygo.shindy.model.Status;

import java.text.MessageFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PurchaseFragment extends Fragment {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.top)
    LinearLayout top;
    @BindView(R.id.start)
    LinearLayout start;
    @BindView(R.id.tv_eventName)
    TextView tvEventName;
    @BindView(R.id.middle)
    LinearLayout middle;
    @BindView(R.id.line)
    View line;
    @BindView(R.id.tv_sold_out)
    TextView tvSoldOut;
    @BindView(R.id.textView4)
    TextView textView4;
    @BindView(R.id.editText)
    EditText editText;
    @BindView(R.id.btn_clear)
    Button btnClear;
    @BindView(R.id.desc)
    RelativeLayout desc;
    Unbinder unbinder;
    Event event;
    @BindView(R.id.iv_avatar)
    RoundedImageView ivAvatar;
    @BindView(R.id.tv_expires)
    TextView tvExpires;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.tv_local_tax)
    TextView tvLocalTax;
    @BindView(R.id.tv_ticket_price)
    TextView tvTicketPrice;
    @BindView(R.id.tv_price_total)
    TextView tvPriceTotal;
    @BindView(R.id.btn_checkout)
    Button btnCheckout;

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
        event = ((EventDetailActivity) this.getActivity()).getEvent();

        Glide.with(this).load(event.getImage()).into(ivAvatar);
        tvEventName.setText(event.getEventname());
        tvLocalTax.setText(event.getCustomPrice() + "$");
        tvTicketPrice.setText(event.getTicketprice() + "$");
        try {
            double v = Double.parseDouble(event.getTicketprice()) + Double.parseDouble(event.getCustomPrice());
            tvPriceTotal.setText(v + "$");
        }catch (Exception e){}
        tvExpires.setText(MessageFormat.format("Expires: {0}", event.getExpirydate()));
        if (event.getSchedStartdate() != null)
            tvDate.setText(event.getSchedStartdate().toString());
        else
            tvDate.setText("");
        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new EventController(getContext()).joinIamInEvent(event.getEventid(),event.getInvitecode(), new Callback<Status>() {
                    @Override
                    public void onResponse(Call<Status> call, Response<Status> response) {
                        Status status = response.body();
                        if(status.getStatus().equals("success")) {
                            ((EventDetailActivity) getActivity()).setEventAttandeeStatus();
                            Toast.makeText(getContext(), "You are in", Toast.LENGTH_LONG).show();
                            getActivity().onBackPressed();
                        }
                        else
                            Toast.makeText(getContext(), status.getResult(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call<Status> call, Throwable t) {
                        getActivity().onBackPressed();
                    }
                });

            }
        });
    }
}
