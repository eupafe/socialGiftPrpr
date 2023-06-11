package com.example.socialgiftprpr.Lists.Gifts;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.socialgiftprpr.Lists.Gifts.AddGiftActivity;
import com.example.socialgiftprpr.Lists.Gifts.GiftModel;
import com.example.socialgiftprpr.R;

import java.util.ArrayList;
import java.util.List;

public class GiftAdapter extends RecyclerView.Adapter<GiftAdapter.ViewHolder>{

    public List<GiftModel> giftEvents;

    public GiftAdapter(List<GiftModel> giftEvents){
        // this.activity = activity;
        this.giftEvents = giftEvents;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_gift, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(GiftAdapter.ViewHolder holder, int position){
        int pos = holder.getAdapterPosition();
        GiftModel gift = giftEvents.get(pos);
        holder.name.setText(gift.getName());
        //holder.description.setText(list.getDescription());
        holder.link.setText(gift.getLink());
        /*
        holder.reservedCheckBox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Context context = v.getContext();
                Intent intent = new Intent(context, AddGiftActivity.class);
                context.startActivity(intent);
            }
        });

         */
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu menu = new PopupMenu(v.getContext(), v);
                menu.getMenu().add("Edit");
                menu.getMenu().add("Delete");
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        if(item.getTitle().equals("Edit")) {
                            //TODO SET EDITTEXTS TO THE TASK THAT WE ARE IN
                            Context context = v.getContext();
                            Intent intent = new Intent(context, AddGiftActivity.class);
                            context.startActivity(intent);

                        } else if (item.getTitle().equals("Delete")) {

                            //TODO DELETE THE LIST
                        }
                        return true;
                    }
                });
                menu.show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return giftEvents.size();
    }

    public void setTasks(List<GiftModel> listEvents){
        this.giftEvents = new ArrayList<>();
        this.giftEvents.addAll(listEvents);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageButton reservedCheckBox;
        public TextView name;
        public TextView link;

        ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nameGiftText);
            link = itemView.findViewById(R.id.linkText);
           // reservedCheckBox = itemView.findViewById(R.id.checkBox);
        }
    }

}

