package com.example.socialgiftprpr.Lists.Gifts;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.socialgiftprpr.MainWindow;
import com.example.socialgiftprpr.Persistence.FriendDAO;
import com.example.socialgiftprpr.Persistence.GiftDAO;
import com.example.socialgiftprpr.R;
import com.example.socialgiftprpr.Share.UserModel;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GiftAdapter extends RecyclerView.Adapter<GiftAdapter.ViewHolder>{

    // Variables
    public List<GiftModel> giftEvents;
    private boolean friend;
    private String listName;
    private String apiKey;

    public GiftAdapter(List<GiftModel> giftEvents,  boolean friend, String listName, String apiKey){
        this.giftEvents = giftEvents;
        this.friend = friend;
        this.listName = listName;
        this.apiKey = apiKey;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_gift, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        int pos = holder.getAdapterPosition();
        GiftModel gift = giftEvents.get(pos);

        holder.name.setText(gift.getProductInfo().getName());
        holder.link.setText(gift.getProductUrl());

        String photo = gift.getProductInfo().getPhoto();
        if (photo != null && !photo.isEmpty()) {
            Picasso.get().load(photo).into(holder.image);
        }

        if(giftEvents.get(pos).getSave()){
            holder.reservedCheckBox.setChecked(true);
        } else{
            holder.reservedCheckBox.setChecked(false);
        }

        if (!friend) {

            holder.reservedCheckBox.setEnabled(false);

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

                                Context context = v.getContext();
                                Intent intent = new Intent(context, AddGiftActivity.class);
                                intent.putExtra("edit_gift_activity", "EDIT GIFT");
                                intent.putExtra("gift_id", String.valueOf(gift.getGiftId()));
                                intent.putExtra("wishlistId", gift.getWishlistId());
                                intent.putExtra("wishlistName", listName);
                                context.startActivity(intent);

                            } else if (item.getTitle().equals("Delete")) {

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
                                    }
                                });
                            }
                            return true;
                        }
                    });
                    menu.show();

                }
            });
        } else{

            holder.reservedCheckBox.setEnabled(true);
            holder.reservedCheckBox.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    int id = gift.getGiftId();

                    SharedPreferences sharedPreferences = v.getContext().getSharedPreferences("SP", Context.MODE_PRIVATE);
                    String apiKey = sharedPreferences.getString("apiKey", null);

                    FriendDAO friendDAO = new FriendDAO();

                    CheckBox checkBox = (CheckBox) v;
                    boolean isChecked = checkBox.isChecked();

                    if(isChecked){

                        friendDAO.reserveGift(id, apiKey,new FriendDAO.FriendCallback() {
                            @Override
                            public void onSuccess(List<UserModel> users) {

                            }

                            @Override
                            public void onFailure(IOException e) {

                            }
                        });
                    } else{

                        friendDAO.deleteReservationGift(id, apiKey,new FriendDAO.FriendCallback() {
                            @Override
                            public void onSuccess(List<UserModel> users) {

                            }

                            @Override
                            public void onFailure(IOException e) {

                            }
                        });

                    }
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return giftEvents.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public CheckBox reservedCheckBox;
        public TextView name;
        public TextView link;
        public ImageView image;

        ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.NameText);
            link = itemView.findViewById(R.id.linkText);
            reservedCheckBox = itemView.findViewById(R.id.checkBox);
            image = itemView.findViewById(R.id.giftImage);

        }
    }

}

