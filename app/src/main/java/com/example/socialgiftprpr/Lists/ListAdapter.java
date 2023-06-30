package com.example.socialgiftprpr.Lists;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.socialgiftprpr.MainWindow;
import com.example.socialgiftprpr.Persistence.ListDAO;
import com.example.socialgiftprpr.R;

import java.util.ArrayList;
import java.util.List;

public class ListAdapter  extends RecyclerView.Adapter<ListAdapter.ViewHolder>{

    // Variables
    public List<ListModel> listEvents;
    private boolean friend;

    public ListAdapter(List<ListModel> listEvents, boolean friend){
        this.listEvents = listEvents;
        this.friend = friend;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_list, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ListAdapter.ViewHolder holder, int position){
        int pos = holder.getAdapterPosition();
        ListModel list = listEvents.get(pos);

        holder.name.setText(list.getName());
        //holder.description.setText(list.getDescription());
        holder.deadline.setText(list.getDeadline());

        if (!friend) {
            holder.seeButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    Context context = v.getContext();
                    Intent intent = new Intent(context, MainWindow.class);
                    intent.putExtra("fragment", "giftsFragment");
                    intent.putExtra("listName", list.getName());
                    intent.putExtra("listGifts", list.getGifts());
                    intent.putExtra("listId", list.getId());
                    context.startActivity(intent);

                }
            });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    SharedPreferences sharedPreferences = v.getContext().getSharedPreferences("SP", Context.MODE_PRIVATE);
                    String apiKey = sharedPreferences.getString("apiKey", null);

                    PopupMenu menu = new PopupMenu(v.getContext(), v);
                    menu.getMenu().add("View description");
                    menu.getMenu().add("Edit");
                    menu.getMenu().add("Delete");
                    menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            if (item.getTitle().equals("View description")) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());

                                TextView titleTextView = new TextView(v.getContext());
                                titleTextView.setText(R.string.pop_up_description);
                                titleTextView.setGravity(Gravity.CENTER);
                                builder.setCustomTitle(titleTextView);

                                TextView descriptionTextView = new TextView(v.getContext());
                                descriptionTextView.setText(list.getDescription());
                                descriptionTextView.setGravity(Gravity.CENTER);
                                builder.setView(descriptionTextView);


                                builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });

                                AlertDialog dialog = builder.create();
                                dialog.show();

                            } else if (item.getTitle().equals("Edit")) {

                                Context context = v.getContext();
                                Intent intent = new Intent(context, AddListActivity.class);
                                intent.putExtra("edit_list_activity", "EDIT LIST");
                                intent.putExtra("edit_value", String.valueOf(list.getId()));
                                context.startActivity(intent);

                            } else if (item.getTitle().equals("Delete")) {

                                ListDAO listDAO = new ListDAO();
                                listDAO.deleteListFromAPI(list.getId(), apiKey, new ListDAO.ListCallback() {
                                    @Override
                                    public void onSuccess(List<ListModel> lists) {

                                        Context context = v.getContext();
                                        Intent intent = new Intent(context, MainWindow.class);
                                        context.startActivity(intent);

                                    }

                                    @Override
                                    public void onFailure(Exception e) {
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
        else{
            holder.seeButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    Context context = v.getContext();
                    Intent intent = new Intent(context, MainWindow.class);
                    intent.putExtra("fragment", "FriendGiftFragment");
                    intent.putExtra("listName", list.getName());
                    intent.putExtra("listGifts", list.getGifts());
                    intent.putExtra("listId", list.getId());
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return listEvents.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageButton seeButton;
        public TextView name;
        public TextView deadline;

        ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nameText);
            deadline = itemView.findViewById(R.id.deadlineText);
            seeButton = itemView.findViewById(R.id.seeButton);
        }
    }

}
