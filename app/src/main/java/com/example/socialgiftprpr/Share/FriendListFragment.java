package com.example.socialgiftprpr.Share;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.socialgiftprpr.Lists.ListAdapter;
import com.example.socialgiftprpr.Lists.ListModel;
import com.example.socialgiftprpr.Persistence.ListDAO;
import com.example.socialgiftprpr.Persistence.UserDAO;
import com.example.socialgiftprpr.R;

import java.io.IOException;
import java.util.List;

public class FriendListFragment extends Fragment {

    // Variables
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    // UI components
    private TextView titleText;
    // Recycler view
    private RecyclerView friendLists;
    // Adapter
    private ListAdapter adapter;

    public FriendListFragment() {
        // Required empty public constructor
    }

    public static FriendListFragment newInstance(String param1, String param2) {
        FriendListFragment fragment = new FriendListFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_friend_list, container, false);

        titleText = view.findViewById(R.id.listTitle);

        // Adapter initialization
        friendLists = (RecyclerView) view.findViewById(R.id.friendLists);
        friendLists.setLayoutManager(new LinearLayoutManager(view.getContext()));

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("SP", Context.MODE_PRIVATE);
        String apiKey = sharedPreferences.getString("apiKey", null);
        String email = sharedPreferences.getString("friendEmail", null);

        UserDAO userDAO = new UserDAO();
        userDAO.getUserIdFromAPI(email, apiKey, new UserDAO.UserCallback() {
            @Override
            public void onSuccess(String id, String name, String p1) {

                ListDAO listDAO = new ListDAO();
                listDAO.getAllListsFromAPI(id, apiKey, new ListDAO.ListCallback(){
                    @Override
                    public void onSuccess(List<ListModel> list) {

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if(list.size() == 0){

                                    Toast.makeText(getContext(), "THERE ARE NO LISTS AVAILABLE", Toast.LENGTH_SHORT).show();

                                } else{
                                    int counter = 0;
                                    for (int i = 0; i < list.size(); i++) {
                                        System.out.println(counter);
                                        counter = list.get(i).getGifts().size() + counter;
                                    }

                                    SharedPreferences preferences = requireContext().getSharedPreferences("SP", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putString("totalFriendLists", String.valueOf(list.size()));
                                    editor.putString("totalFriendGifts", String.valueOf(counter));
                                    editor.apply();

                                    adapter = new ListAdapter(list, true);
                                    friendLists.setAdapter(adapter);
                                }

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

        return view;
    }
}