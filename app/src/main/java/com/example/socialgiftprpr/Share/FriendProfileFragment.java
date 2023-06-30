package com.example.socialgiftprpr.Share;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.socialgiftprpr.Lists.ListModel;
import com.example.socialgiftprpr.MainWindow;
import com.example.socialgiftprpr.Persistence.ListDAO;
import com.example.socialgiftprpr.Persistence.UserDAO;
import com.example.socialgiftprpr.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

public class FriendProfileFragment extends Fragment {

    // Variables
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    // UI components
    private ImageButton viewList;
    private ImageView image;
    private TextView name;
    private TextView email;
    private TextView totalLists;
    private TextView totalGifts;

    public FriendProfileFragment() {
        // Required empty public constructor
    }

    public static FriendProfileFragment newInstance(String param1, String param2) {
        FriendProfileFragment fragment = new FriendProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend_profile, container, false);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("SP", Context.MODE_PRIVATE);
        String apiKey = sharedPreferences.getString("apiKey", null);
        String friendEmail = sharedPreferences.getString("friendEmail", null);

        name = view.findViewById(R.id.profile_name);
        email = view.findViewById(R.id.profile_email);
        image = view.findViewById(R.id.profile_image);
        email.setText(friendEmail);

        UserDAO userDAO = new UserDAO();
        userDAO.getUserIdFromAPI(friendEmail, apiKey, new UserDAO.UserCallback() {
            @Override
            public void onSuccess(String id, String name, String p1) {

                ListDAO listDAO = new ListDAO();
                listDAO.getAllListsFromAPI(id, apiKey, new ListDAO.ListCallback() {
                    @Override
                    public void onSuccess(List<ListModel> list) {

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                totalLists = view.findViewById(R.id.total_lists_counter);
                                totalLists.setText(String.valueOf(list.size()));

                                int counter = 0;
                                for (int i = 0; i < list.size(); i++) {
                                    counter = list.get(i).getGifts().size() + counter;
                                }

                                totalGifts = view.findViewById(R.id.total_gifts_counter);
                                totalGifts.setText(String.valueOf(counter));

                            }
                        });
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(getContext(), "ERROR, cannot connect to the server", Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onFailure(IOException e) {
                Toast.makeText(getContext(), "ERROR, cannot connect to the server", Toast.LENGTH_SHORT).show();
            }
        });


        userDAO.getUserIdFromAPI(friendEmail, apiKey, new UserDAO.UserCallback() {
            @Override
            public void onSuccess(String id, String nameUser, String imageProfile) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        name.setText(nameUser);
                        if (imageProfile != null && !imageProfile.isEmpty()) {
                            Picasso.get().load(imageProfile).into(image);
                        }
                    }
                });

            }
            @Override
            public void onFailure(IOException e) {
                Toast.makeText(getContext(), "ERROR, cannot connect to the server", Toast.LENGTH_SHORT).show();
            }
        });


        viewList = (ImageButton) view.findViewById(R.id.viewListButton);
        viewList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, MainWindow.class);
                intent.putExtra("fragment", "FriendListFragment");
                context.startActivity(intent);
            }
        });

        return view;
    }
}