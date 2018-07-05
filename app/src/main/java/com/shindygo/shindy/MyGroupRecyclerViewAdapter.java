package com.shindygo.shindy;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shindygo.shindy.GroupListFragment.OnListFragmentInteractionListener;
import com.shindygo.shindy.dummy.DummyContent;
import com.shindygo.shindy.dummy.DummyContent.DummyItem;
import com.shindygo.shindy.utils.GlideImage;

import java.util.List;

public class MyGroupRecyclerViewAdapter extends RecyclerView.Adapter<MyGroupRecyclerViewAdapter.ViewHolder> {

    private final List<DummyItem> mValues;
    private final OnListFragmentInteractionListener mListener;
    Context context;

    public MyGroupRecyclerViewAdapter(List<DummyItem> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_group_join, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.bindView(mValues.get(position),position);

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView tvName;
        public final ImageView ivChoose;
        public DummyItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            tvName = (TextView) view.findViewById(R.id.tv_name);
            ivChoose = (ImageView) view.findViewById(R.id.iv_choose);
        }


        public void bindView(DummyItem dummyItem, final int position) {

            mItem = mValues.get(position);
            tvName.setText(mValues.get(position).id);

            View.OnClickListener onClickListener =new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int  id = v.getId();
                    if (null != mListener) {
                        int action = 0;
                        switch (v.getId()){
                            case R.id.iv_choose:
                                action = GroupListFragment.ACTION_DELETE;
                                break;
                            case R.id.ll_item_group:
                                action = GroupListFragment.ACTION_VIEW;

                                break;

                        }
                        mListener.onListFragmentInteraction(mItem, action, position, id );
                    }
                }
            };
            mView.setOnClickListener(onClickListener);
            ivChoose.setOnClickListener(onClickListener);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + tvName.getText() + "'";
        }

    }
}
