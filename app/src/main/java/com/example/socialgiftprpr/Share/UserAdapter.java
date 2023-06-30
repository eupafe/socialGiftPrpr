package com.example.socialgiftprpr.Share;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.socialgiftprpr.MainWindow;
import com.example.socialgiftprpr.R;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{

    // Variables
    public List<UserModel> userEvents;

    public UserAdapter(List<UserModel> userEvents){
        this.userEvents = userEvents;
    }

    public UserAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_friend, parent, false);
        return new UserAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(UserAdapter.ViewHolder holder, int position){
        int pos = holder.getAdapterPosition();
        UserModel user = userEvents.get(pos);

        holder.name.setText(user.getName() + " " + user.getSurname());
        holder.seeProfile.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                SharedPreferences sharedPreferences = v.getContext().getSharedPreferences("SP", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("friendEmail", user.getEmail());
                editor.apply();

                Context context = v.getContext();
                Intent intent = new Intent(context, MainWindow.class);
                intent.putExtra("fragment", "friendProfileFragment");

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userEvents.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView name;
        public ImageButton seeProfile;

        ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nameText);
            seeProfile = itemView.findViewById(R.id.seeButton);
        }
    }

}