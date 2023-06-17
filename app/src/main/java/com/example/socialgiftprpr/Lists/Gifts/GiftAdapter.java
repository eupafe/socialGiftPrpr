package com.example.socialgiftprpr.Lists.Gifts;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.socialgiftprpr.MainWindow;
import com.example.socialgiftprpr.Persistence.GiftDAO;
import com.example.socialgiftprpr.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GiftAdapter extends RecyclerView.Adapter<GiftAdapter.ViewHolder>{

    public List<GiftModel> giftEvents;

    private boolean friend;
    private String listName;

    public GiftAdapter(List<GiftModel> giftEvents,  boolean friend, String listName){
        // this.activity = activity;
        this.giftEvents = giftEvents;
        this.friend = friend;
        this.listName = listName;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_gift, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(GiftAdapter.ViewHolder holder, int position){
        int pos = holder.getAdapterPosition();
        GiftModel gift = giftEvents.get(pos);
        //holder.description.setText(list.getDescription());
        holder.link.setText(gift.getProductUrl());
        /*
        holder.reservedCheckBox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Context context = v.getContext();
                Intent intent = new Intent(context, AddGiftActivity.class);
                context.startActivity(intent);
            }
        });

         */
        if (!friend) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    SharedPreferences sharedPreferences = v.getContext().getSharedPreferences("SP", Context.MODE_PRIVATE);
                    String apiKey = sharedPreferences.getString("apiKey", null);

                    PopupMenu menu = new PopupMenu(v.getContext(), v);
                    menu.getMenu().add("Edit");
                    menu.getMenu().add("Delete");
                    menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            if (item.getTitle().equals("Edit")) {
                                //TODO SET EDITTEXTS TO THE TASK THAT WE ARE IN
                                Context context = v.getContext();
                                Intent intent = new Intent(context, AddGiftActivity.class);
                                intent.putExtra("edit_gift_activity", "EDIT GIFT");
                                intent.putExtra("gift_id", String.valueOf(gift.getGiftId()));
                                context.startActivity(intent);

                            } else if (item.getTitle().equals("Delete")) {

                                //TODO DELETE THE LIST
                                GiftDAO giftDAO = new GiftDAO();
                                giftDAO.deleteGiftFromAPI(gift.getGiftId(), apiKey, new GiftDAO.GiftCallback() {
                                    @Override
                                    public void onSuccess(List<GiftModel> gifts, String list, int id) {

                                        Context context = v.getContext();
                                        Intent intent = new Intent(context, MainWindow.class);
                                        intent.putExtra("fragment", "giftsFragment");
                                        intent.putExtra("listId", gift.getWishlistId());
                                        intent.putExtra("listName", listName);
                                        context.startActivity(intent);

                                    }

                                    @Override
                                    public void onFailure(IOException e) {
                                        //Toast.makeText(v.getContext(), "ERROR: gift NOT deleted", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            return true;
                        }
                    });
                    menu.show();

                }
            });
        }
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
            //name = itemView.findViewById(R.id.nameGiftText);
            link = itemView.findViewById(R.id.linkText);
           // reservedCheckBox = itemView.findViewById(R.id.checkBox);
        }
    }

}

