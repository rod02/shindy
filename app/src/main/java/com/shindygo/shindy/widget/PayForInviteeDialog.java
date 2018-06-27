package com.shindygo.shindy.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.shindygo.shindy.R;

public class PayForInviteeDialog extends AlertDialog {


    public PayForInviteeDialog(Context context) {
        this(context, R.layout.alert_pay_for_invitee);
    }

    protected PayForInviteeDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    protected PayForInviteeDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public void setAgreeClickListener(View.OnClickListener onClickListener){
        findViewById(R.id.btn_ok).setOnClickListener(onClickListener);
    }
    public void setCancelClickListener(View.OnClickListener onClickListener){
        findViewById(R.id.btn_cancel).setOnClickListener(onClickListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = getLayoutInflater().inflate(R.layout.alert_pay_for_invitee, null);
        setContentView(R.layout.alert_pay_for_invitee);
        View.OnClickListener onClickListenerCancel = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        };
        findViewById(R.id.close).setOnClickListener(onClickListenerCancel);
    }
}
