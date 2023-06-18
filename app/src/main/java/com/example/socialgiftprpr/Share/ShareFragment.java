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
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShareFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShareFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    // Recycler view
    private RecyclerView users;
    // Adapter
    private UserAdapter adapter;
    // List of tasks
    private List<UserModel> userEvents;

    private SearchView searchView;

    public ShareFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShareFragment.
     */

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
        userEvents = new ArrayList<>();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {

                String searchInput = searchView.getQuery().toString();

                SharedPreferences sharedPreferences = requireContext().getSharedPreferences("SP", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("friendEmail", searchInput);
                editor.apply();

                if(searchInput != null || !searchInput.equals("")){

                    FriendDAO friendDAO = new FriendDAO();
                    friendDAO.getFriendIdFromAPI(searchInput, apiKey, new FriendDAO.FriendCallback() {
                        @Override
                        public void onSuccess(List<UserModel> usersList) {

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    // TODO: el onSucess li hauria de retornar el user sencer (fem que retorni una llista de 1 només). Llavors li fem el mètode PUT
                                    // TODO: i marquem com que es un amic. Després cridem a una funció que sigui GET ALL FRIENDS
                                    // TODO: que el onSucess retorni també una llista de tots els friends. Fem update a shared preferences de la size
                                    // TODO: i que la passi a l'adapter. TENIR EN COMPTE QUE HEM D'ESBORRAR TOT EL QUE POSEM A SHARED PREFERENCES CADA
                                    // TODO: COP QUE FEM EDIT (O ALTRES CASOS, MIRAR-HO BÉ)

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