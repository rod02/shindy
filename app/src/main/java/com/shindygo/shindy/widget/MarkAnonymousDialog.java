package com.shindygo.shindy.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.shindygo.shindy.R;

public class MarkAnonymousDialog extends AlertDialog {


    public MarkAnonymousDialog(Context context) {
        this(context, R.layout.alert_mark_anonymous);
    }

    protected MarkAnonymousDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    protected MarkAnonymousDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public void setAgreeClickListener(View.OnClickListener onClickListener){
        findViewById(R.id.btn_ok).setOnClickListener(onClickListener);
    }
    public void setOnCheckListener(CompoundButton.OnCheckedChangeListener listener){
        ((CheckBox) findViewById(R.id.checkbox)).setOnCheckedChangeListener(listener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View.OnClickListener onClickListenerCancel = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        };

    }

    public boolean isChecked(){
        return ((CheckBox)findViewById(R.id.checkbox)).isChecked();
    }
}
