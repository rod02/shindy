package com.shindygo.shindy.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shindygo.shindy.R;
import com.shindygo.shindy.main.adapter.ChatMessageAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.top)
    RelativeLayout top;
    @BindView(R.id.rv_chat)
    RecyclerView rvChat;
    @BindView(R.id.iv_avatar)
    CircleImageView ivAvatar;
    @BindView(R.id.et_message)
    EditText etMessage;
    @BindView(R.id.send)
    TextView send;
    @BindView(R.id.send_message)
    LinearLayout sendMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        rvChat.setLayoutManager(new LinearLayoutManager(this));
        rvChat.setAdapter(new ChatMessageAdapter());
    }
}
