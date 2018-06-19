package com.shindygo.shindy.utils;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

public class ConnectivityChangeReceiver extends BroadcastReceiver {
    Context context;
    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        ComponentName comp = new ComponentName(context.getPackageName(),
                ConnectIntentService.class.getName());
        intent.putExtra("isNetworkConnected",isConnected(context));
        Toast.makeText(context, isConnected(context)?"COnnected":"Disconnected", Toast.LENGTH_SHORT).show();
        context.startService((intent.setComponent(comp)));
    }
    public  boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE));
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        boolean connection = networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
    }
    public void showAlert()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Your internet connection is gone!")
                .setMessage("Check, and press OK!")
                .setCancelable(false)
                .setNegativeButton("ОК",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                isConnected(context);
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
