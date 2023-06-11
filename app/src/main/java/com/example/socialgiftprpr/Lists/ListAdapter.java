package com.example.socialgiftprpr.Lists;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.socialgiftprpr.Lists.Gifts.AddGiftActivity;
import com.example.socialgiftprpr.Lists.Gifts.GiftsFragment;
import com.example.socialgiftprpr.MainWindow;
import com.example.socialgiftprpr.R;

import java.util.ArrayList;
import java.util.List;

public class ListAdapter  extends RecyclerView.Adapter<ListAdapter.ViewHolder>{

    public List<ListModel> listEvents;

    public ListAdapter(List<ListModel> listEvents){
       // this.activity = activity;
        this.listEvents = listEvents;
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
        holder.seeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Context context = v.getContext();
                Intent intent = new Intent(context, MainWindow.class);
                intent.putExtra("fragment", "giftsFragment");
                context.startActivity(intent);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu menu = new PopupMenu(v.getContext(), v);
                menu.getMenu().add("View description");
                menu.getMenu().add("Edit");
                menu.getMenu().add("Delete");
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        if(item.getTitle().equals("View description")){
                            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());

                            TextView titleTextView = new TextView(v.getContext());
                            titleTextView.setText("DESCRIPTION");
                            titleTextView.setGravity(Gravity.CENTER);
                            builder.setCustomTitle(titleTextView);

                            TextView descriptionTextView = new TextView(v.getContext());
                            descriptionTextView.setText("heykdnvvvvvvvvvvvvvvvvvvvf");
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

                            //TODO ARREGLAR EL DISSENY

                        }
                        else if(item.getTitle().equals("Edit")) {
                           //TODO SET EDITTEXTS TO THE TASK THAT WE ARE IN
                            Context context = v.getContext();
                            Intent intent = new Intent(context, AddListActivity.class);
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
        return listEvents.size();
    }

    public void setTasks(List<ListModel> listEvents){
        this.listEvents = new ArrayList<>();
        this.listEvents.addAll(listEvents);
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
