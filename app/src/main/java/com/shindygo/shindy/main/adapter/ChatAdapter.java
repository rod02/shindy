package com.shindygo.shindy.main.adapter;

import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
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

import com.bumptech.glide.Glide;
import com.shindygo.shindy.Api;
import com.shindygo.shindy.R;
import com.shindygo.shindy.api.EventController;
import com.shindygo.shindy.interfaces.DiscussionClick;
import com.shindygo.shindy.model.Discussion;
import com.shindygo.shindy.model.Reply;
import com.shindygo.shindy.model.Status;
import com.shindygo.shindy.model.User;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static com.shindygo.shindy.Api.getContext;

/**
 * Created by User on 16.03.2018.
 */


public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatHolder> {
    Context context;
    DiscussionClick interf;
    List<Discussion> list;



    public ChatAdapter(List<Discussion> list, DiscussionClick interf) {
        this.interf = interf;
        this.list = list;

    }
    //    public ChatAdapter(ClickCard clickCard) {
//        this.clickCard = clickCard;
//    }

    @Override
    public ChatHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        context = parent.getContext();

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, null);

        return new ChatHolder(view);
    }

    @Override
    public void onBindViewHolder(final ChatHolder holder, final int position) {
        holder.bindModel(list.get(position));


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ChatHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.rl_like)
        RelativeLayout rlLike;
        @BindView(R.id.iv_avatar)
        ImageView ivAvatar;
        @BindView(R.id.rv_name)
        TextView rvName;
        @BindView(R.id.tv_text)
        TextView tvText;
        @BindView(R.id.reply)
        TextView reply;
        @BindView(R.id.count)
        TextView count;
        @BindView(R.id.like)
        ImageView like;
        @BindView(R.id.ll_reply)
        LinearLayout llReply;
        @BindView(R.id.linearLayout3)
        LinearLayout linearLayout3;
        @BindView(R.id.rv_chat_comment)
        RecyclerView rvChatComment;
        @BindView(R.id.tv_comment_date)
        TextView tvCommentDate;
        @BindView(R.id.rl_reply)
        RelativeLayout rlReply;
        @BindView(R.id.et_reply)
        EditText etReply;
        @BindView(R.id.sent_reply)
        Button sentReply;
        View all;
        boolean liked = false;
        boolean replyVis = false;

        public ChatHolder(View v) {
            super(v);
            all = v;
            ButterKnife.bind(this, v);

        }

        public void bindModel(final Discussion d) {


            //TODO like dislike check normal must be
            try {
                count.setText(d.getNum_likes());
                rvChatComment.setVisibility(View.VISIBLE);
                rvChatComment.setLayoutManager(new LinearLayoutManager(context));
                rvChatComment.setAdapter(new ChatCommentAdapter(d.getReply()));
                tvText.setText(d.getComment());
                tvCommentDate.setText(d.getCommentDate());
                rvName.setText(d.getFullname());
                ivAvatar.setImageResource(android.R.color.transparent);
                if (d.getPhoto() != null)
                    Glide.with(context).load(d.getPhoto()).into(ivAvatar);
            } catch (Exception e) {
                e.printStackTrace();
            }
            reply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    interf.clickReply(d, "");
                   // createMessage(d);
                  /*  replyVis=!replyVis;

                    rlReply.setVisibility(replyVis?View.VISIBLE:View.GONE);*/
                }
            });
            sentReply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    etReply.getText().toString().trim();
                    //todo sent
                    if (etReply.getText().toString().trim().length() > 1)
                        interf.clickReply(d, etReply.getText().toString().trim());
                    etReply.setText("");

                }
            });
            liked = d.isLiked();
            if (d.isLiked()) {
                like.setColorFilter(ContextCompat.getColor(context, R.color.navigation_notification_red));
            }
            LinearLayout msg = itemView.findViewById(R.id.ll_reply);
            msg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (!liked) {
                        liked = !liked;
                        new EventController(context).likeDiscussElement(d.getDiscussionId(), new Callback<Object>() {
                            @Override
                            public void onResponse(Call<Object> call, Response<Object> response) {
                                like.setColorFilter(ContextCompat.getColor(context, R.color.navigation_notification_red));
                                d.setNum_likes(String.valueOf(Integer.parseInt(d.getNum_likes()) + 1));
                                d.setLike_status("0");
                                count.setText(String.valueOf(Integer.parseInt(d.getNum_likes())));
                            }

                            @Override
                            public void onFailure(Call<Object> call, Throwable t) {

                            }
                        });
                    } else {
                        liked = !liked;
                        new EventController(context).unlikeDiscussElement(d.getDiscussionId(), new Callback<Object>() {
                            @Override
                            public void onResponse(Call<Object> call, Response<Object> response) {
                                like.setColorFilter(null);
                                d.setLike_status("1");
                                d.setNum_likes(String.valueOf(Integer.parseInt(d.getNum_likes()) - 1));
                                count.setText(d.getNum_likes());
                            }

                            @Override
                            public void onFailure(Call<Object> call, Throwable t) {

                            }
                        });
                    }
                }
            });
        }

        private void createMessage(final Discussion d) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            final PopupWindow messPopup;
            // Inflate the custom layout/view
            View customView = inflater.inflate(R.layout.message_popup, null);
            final TextView textView = customView.findViewById(R.id.textView2);
            final EditText editText = customView.findViewById(R.id.editText);
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    textView.setText(300 - editable.length() + " charecters left");
                }
            });
            messPopup = new PopupWindow(
                    customView,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    true
            );
            if (Build.VERSION.SDK_INT >= 21) {
                messPopup.setElevation(5.0f);
            }

            ImageView closeButton = customView.findViewById(R.id.close);
            closeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    messPopup.dismiss();
                }
            });
            Button send = customView.findViewById(R.id.send);
            send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        final String s = editText.getText().toString();
                        if(s.trim().length()>1){
                            EventController eventController = new EventController(context);
                            eventController.postReply(d.getEventid(), d.getDiscussionId(), s, new Callback<Status>() {
                                @Override
                                public void onResponse(Call<Status> call, Response<Status> response) {
                                    if (response.isSuccessful()){
                                        User currentuser = User.getCurrentUser();
                                        d.reply.add(new Reply(s, currentuser.getFullname(),currentuser.getPhoto()));
                                        notifyDataSetChanged();
                                        editText.setText("");
                                        messPopup.dismiss();
                                        rvChatComment.getAdapter().notifyDataSetChanged();
                                        Toast.makeText(Api.getContext(),"Success",Toast.LENGTH_SHORT).show();
                                    }
                                    else  Toast.makeText(Api.getContext(),"Error",Toast.LENGTH_SHORT).show();

                                }

                                @Override
                                public void onFailure(Call<Status> call, Throwable t) {
                                    t.printStackTrace();
                                }
                            });
                        }


                    }catch (NullPointerException e){
                        e.printStackTrace();
                    }

                    /*
                    if (editText.getText().toString().trim().length() > 1)
                        interf.clickReply(d, editText.getText().toString().trim());
                    editText.setText("");
                    messPopup.dismiss();*/
                }
            });

            messPopup.showAtLocation(all, Gravity.CENTER, 0, 0);
        }
    }


}

