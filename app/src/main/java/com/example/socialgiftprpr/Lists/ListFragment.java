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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.socialgiftprpr.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListFragment extends Fragment {

    private TextView title;
    // Button to create a task
    private ImageButton addList;
    // Recycler view
    private RecyclerView lists;
    // Adapter
    private ListAdapter adapter;
    // List of tasks
    private List<ListModel> listEvents;
    // Shared preferences
    private SharedPreferences sharedPreferences;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
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
    // TODO: Rename and change types and number of parameters
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
        // Inflate the layout for this fragment
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
        listEvents = new ArrayList<>();

        //getDataFromAPI();
        getAllListsFromAPI();

        //listEvents.add(new ListModel("HEY", "TODAY", "1/6", false));

        return view;
    }

    public void getAllListsFromAPI() {

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MiArchivoPreferencias", Context.MODE_PRIVATE);
        int id = sharedPreferences.getInt("id", 0);

        String url ="https://balandrau.salle.url.edu/i3/socialgift/api/v1/users/"+ id + "/wishlists";

        RequestQueue queue = Volley.newRequestQueue(getContext());
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                System.out.println("RESPONSEE: " + response);
                try{
                    for(int i = 0; i < response.length(); i ++){

                        JSONObject jsonObject = response.getJSONObject(i);
                        int idJSON = jsonObject.getInt("id");
                        String nameJSON = jsonObject.getString("name");
                        String descriptionJSON = jsonObject.getString("description");
                        String emailJSON = jsonObject.getString("end_date");

                        System.out.println(nameJSON + " " + descriptionJSON + " " + emailJSON);
                        //name.setText(fullName);
                        //email.setText(emailJSON);

                        listEvents.add(new ListModel(idJSON, nameJSON, descriptionJSON, emailJSON, false));
                        adapter = new ListAdapter(listEvents, false);

                        adapter.setTasks(listEvents);
                        lists.setAdapter(adapter);
                    }

                } catch(JSONException e){
                    e.printStackTrace();
                }

                Toast.makeText(getContext(), "Data posted successfully", Toast.LENGTH_SHORT).show();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MiArchivoPreferencias", Context.MODE_PRIVATE);
                String apiKey = sharedPreferences.getString("apiKey", null);

                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + apiKey);

                return headers;
            }
        };

        queue.add(request);
    }

    public void getDataFromAPI() {

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MiArchivoPreferencias", Context.MODE_PRIVATE);
        String mail = sharedPreferences.getString("email", null);

        String url = "https://balandrau.salle.url.edu/i3/socialgift/api/v1/users/search?s=" + mail;

        RequestQueue queue = Volley.newRequestQueue(getContext());
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try{
                    JSONObject jsonObject = response.getJSONObject(0);
                    int id = jsonObject.getInt("id");

                    System.out.println("ID " + id);

                    SharedPreferences preferences = requireContext().getSharedPreferences("MiArchivoPreferencias", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putInt("id", id);
                    editor.apply();

                } catch(JSONException e){
                    e.printStackTrace();
                }

                Toast.makeText(getContext(), "Data posted successfully", Toast.LENGTH_SHORT).show();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MiArchivoPreferencias", Context.MODE_PRIVATE);
                String apiKey = sharedPreferences.getString("apiKey", null);

                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + apiKey);

                return headers;
            }
        };

        queue.add(request);
    }


}