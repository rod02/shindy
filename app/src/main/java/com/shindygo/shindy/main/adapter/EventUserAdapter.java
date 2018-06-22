package com.shindygo.shindy.main.adapter;

/**
 * Created by User on 16.03.2018.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shindygo.shindy.R;
import com.shindygo.shindy.interfaces.ClickEventCard;
import com.shindygo.shindy.model.Event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Sergey on 24.10.2017.
 */

public class EventUserAdapter extends RecyclerView.Adapter<EventUserAdapter.EventUserHolder> {
    Context context;
    ClickEventCard clickCard;
    List<Event> list;
    private HashMap<Integer, Event> storeChecked = new HashMap<>();
    private boolean isMultiselect = true;
    private int itemSelected;

    public EventUserAdapter(ClickEventCard clickCard, List<Event> list) {
        this.clickCard = clickCard;
        if (list==null)
            list=new ArrayList<>();
        this.list = list;
    }

    @Override
    public EventUserAdapter.EventUserHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        context=parent.getContext();

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event_user,null);

        return new EventUserAdapter.EventUserHolder(view);
    }

    @Override
    public void onBindViewHolder(final EventUserHolder holder, final int position) {
        holder.bindModel(list.get(position),clickCard, position);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class EventUserHolder extends RecyclerView.ViewHolder{
        ImageView imageView,arrow;
        TextView textView;

        public EventUserHolder(View v) {
            super(v);
        imageView = v.findViewById(R.id.image);

        }
        public void bindModel(final Event event, final ClickEventCard clickCard, final int position){
            if (event.getImages().size()>0)
                Glide.with(context).load(event.getImage()).into(imageView);
            else
            {
                imageView.setImageResource(R.mipmap.no_image);
            }
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickCard.Click(true,event.getEventid());
                    imageView.setSelected(true);
                    if(isMultiselect){
                        boolean tof = storeChecked.get(position)!=null;

                        if (tof){
                           // triggerOnItemClickListener(--itemSelected, view); // transfer position to update unselected
                            storeChecked.remove(position);// delete position of unselected position in the fragment
                        }else {
                            //triggerOnItemClickListener(++itemSelected, view);
                            // transfer position to update selected  position in the fragment
                            storeChecked.put(position,event);
                        }
                    }


                }
            });

            imageView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(!isMultiselect){
                        storeChecked.put(getAdapterPosition(), event);
                        //notifyDataSetChanged(getAdapterPosition());
                        //triggerOnLongClickListener(++itemSelected); // using listerner i've transfer position to fragment for actionmode selected count

                    }
                    return false;
                }
            });

        }
    }

    //clear on actionmode close
    public void exitMultiselectMode() {
        isMultiselect = false;
        itemSelected = 0;
        storeChecked.clear();
        notifyDataSetChanged();
    }

    // get all selected position
    public List<Event> getSelectedItems() {
        List<Event> items = new ArrayList<>(storeChecked.size());
        if(storeChecked.size()>0){
            Object[] objects = storeChecked.values().toArray();

            for (int i = 0; i < storeChecked.size(); i++) {
                items.add((Event) objects[i]);

            }
        }
      /*  for (int i = 0; i < storeChecked.size(); ++i) {
            items.add(events[i]);
        }*/
        return items;
    }

}
