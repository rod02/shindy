package com.shindygo.shindy.main.adapter;

import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.shindygo.shindy.R;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

public class ArtistViewHolder extends ChildViewHolder {

    private TextView childTextView1;
    private TextView childTextView2;
    private TextView childTextView3;
    private TextView childTextView4;

    public ArtistViewHolder(View itemView) {
        super(itemView);
        childTextView1 = (TextView) itemView.findViewById(R.id.list_item_profile);
        childTextView2 = (TextView) itemView.findViewById(R.id.list_item_favorite);
        childTextView3 = (TextView) itemView.findViewById(R.id.list_item_invite);
        childTextView4 = (TextView) itemView.findViewById(R.id.list_item_message);
    }

    public void setArtistName(String name) {
        childTextView1.setText(R.string.profile);
        childTextView2.setText(R.string.favorite);
        childTextView3.setText(R.string.invite);
        childTextView4.setText(R.string.message);
    }
}