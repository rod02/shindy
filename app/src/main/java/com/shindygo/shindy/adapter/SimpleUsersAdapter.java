package com.shindygo.shindy.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shindygo.shindy.R;
import com.shindygo.shindy.model.User;
import com.shindygo.shindy.utils.GlideImage;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SimpleUsersAdapter extends ArrayAdapter {

    private List<User> dataList;
    private Context mContext;
    private int itemLayout;

    private ListFilter listFilter = new ListFilter();
    private List<User> dataListAllItems;
    private static final String TAG = SimpleUsersAdapter.class.getSimpleName();


    public SimpleUsersAdapter(Context context, int resource, List<User> storeDataLst) {
        super(context, resource, storeDataLst);
        dataList = storeDataLst;
        mContext = context;
        itemLayout = resource;
    }


    public SimpleUsersAdapter(Context context, List<User> storeDataLst) {
        this(context, R.layout.item_user_simple, storeDataLst);
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public User getItem(int position) {
        /*Log.d("CustomListAdapter",
                dataList.get(position).getFullname());*/
        return dataList.get(position);
    }

    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {

        if (view == null) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(itemLayout, parent, false);
        }

        TextView tvName = (TextView) view.findViewById(R.id.tv_name_age);
        ImageView avatar = (ImageView) view.findViewById(R.id.iv_avatar);

        User user = getItem(position);
        tvName.setText(mContext.getResources().getString(R.string.name_n_age,
                user.getFullname(), user.getAge()));
        try {
            GlideImage.load(mContext, user.getPhoto(), avatar);
        }catch (Exception e){
            e.printStackTrace();
        }

        return view;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return listFilter;
    }

    public class ListFilter extends Filter {
        private Object lock = new Object();

        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();
            if (dataListAllItems == null) {

                synchronized (lock) {
                    dataListAllItems = new ArrayList<User>(dataList);

                }

            }

            if (prefix == null || prefix.length() == 0) {
                synchronized (lock) {
                    results.values = dataListAllItems;
                    results.count = dataListAllItems.size();
                }
            } else {
                final String searchStrLowerCase = prefix.toString().toLowerCase();

                ArrayList<User> matchValues = new ArrayList<User>();

                for (User dataItem : dataListAllItems) {
                    if (dataItem.getFullname().contains(searchStrLowerCase)) {
                        matchValues.add(dataItem);
                    }
                }

                results.values = matchValues;
                results.count = matchValues.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.values != null) {
                dataList = (ArrayList<User>) results.values;
            } else {
                dataList = null;
            }
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }


    }}
