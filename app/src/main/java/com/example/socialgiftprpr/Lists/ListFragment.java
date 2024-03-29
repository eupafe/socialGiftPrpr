package com.example.socialgiftprpr.Lists;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.socialgiftprpr.Persistence.ListDAO;
import com.example.socialgiftprpr.Persistence.UserDAO;
import com.example.socialgiftprpr.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListFragment extends Fragment{

    // Variables
    private ImageButton addList;
    private RecyclerView lists;
    private ListAdapter adapter;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public ListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListFragment.
     */

    public static ListFragment newInstance(String param1, String param2) {
        ListFragment fragment = new ListFragment();
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
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        addList = (ImageButton) view.findViewById(R.id.addButton);
        addList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AddListActivity.class);
                startActivity(intent);
            }
        });

        // Adapter initialization
        lists = (RecyclerView) view.findViewById(R.id.lists);
        lists.setLayoutManager(new LinearLayoutManager(view.getContext()));

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("SP", Context.MODE_PRIVATE);
        String apiKey = sharedPreferences.getString("apiKey", null);
        String email = sharedPreferences.getString("email", null);

        UserDAO userDAO = new UserDAO();
        userDAO.getUserIdFromAPI(email, apiKey, new UserDAO.UserCallback() {
            @Override
            public void onSuccess(String id, String name, String p1) {

                ListDAO listDAO = new ListDAO();
                listDAO.getAllListsFromAPI(id, apiKey, new ListDAO.ListCallback() {
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
                                        counter = list.get(i).getGifts().size() + counter;
                                    }

                                    adapter = new ListAdapter(list, false);
                                    lists.setAdapter(adapter);
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