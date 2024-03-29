package com.example.socialgiftprpr.Share;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.socialgiftprpr.Persistence.FriendDAO;
import com.example.socialgiftprpr.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class ShareFragment extends Fragment {

    // Variables
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    // Recycler view
    private RecyclerView users;
    // Adapter
    private UserAdapter adapter;
    // UI components
    private SearchView searchView;

    public ShareFragment() {
        // Required empty public constructor
    }

    public static ShareFragment newInstance(String param1, String param2) {
        ShareFragment fragment = new ShareFragment();
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

        View view = inflater.inflate(R.layout.fragment_share, container, false);

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("SP", Context.MODE_PRIVATE);
        String apiKey = sharedPreferences.getString("apiKey", null);

        searchView = view.findViewById(R.id.searchView);

        // Adapter initialization
        users = (RecyclerView) view.findViewById(R.id.users);
        users.setLayoutManager(new LinearLayoutManager(view.getContext()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {

                String searchInput = searchView.getQuery().toString();

                if(searchInput != null || !searchInput.equals("")){

                    FriendDAO friendDAO = new FriendDAO();
                    friendDAO.getFriendIdFromAPI(searchInput, apiKey, new FriendDAO.FriendCallback() {
                        @Override
                        public void onSuccess(List<UserModel> usersList) {

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    if(usersList.size() == 0){
                                        Toast.makeText(getContext(), "THERE ARE NO USERS WITH THIS NAME", Toast.LENGTH_SHORT).show();

                                    } else{
                                        Gson gson = new Gson();
                                        String userListJson = gson.toJson(usersList);

                                        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("SP", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();

                                        Type userListType = new TypeToken<List<UserModel>>() {}.getType();
                                        List<UserModel> userList = gson.fromJson(userListJson, userListType);

                                        editor.putString("usersList", userListJson);
                                        editor.apply();

                                        adapter = new UserAdapter(userList);
                                        users.setAdapter(adapter);
                                    }

                                }
                            });

                        }
                        @Override
                        public void onFailure(IOException e) {
                            Toast.makeText(getContext(), "ERROR, cannot connect to the server", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return view;
    }
}