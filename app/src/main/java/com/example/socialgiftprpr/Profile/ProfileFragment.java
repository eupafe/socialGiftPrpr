package com.example.socialgiftprpr.Profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.socialgiftprpr.Lists.ListAdapter;
import com.example.socialgiftprpr.Lists.ListModel;
import com.example.socialgiftprpr.MainActivity;
import com.example.socialgiftprpr.MainWindow;
import com.example.socialgiftprpr.Persistence.ListDAO;
import com.example.socialgiftprpr.Persistence.UserDAO;
import com.example.socialgiftprpr.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    ImageButton editProfileButton;

    ImageButton viewReservedButton;

    private TextView totalLists;
    private TextView totalGifts;
    private TextView totalFriends;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String apiKey;

    private TextView name;
    private TextView email;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("SP", Context.MODE_PRIVATE);
        String apiKey = sharedPreferences.getString("apiKey", null);
        String emailUser = sharedPreferences.getString("email", null);

        name = view.findViewById(R.id.profile_name);
        email = view.findViewById(R.id.profile_email);
        totalLists = view.findViewById(R.id.total_lists_counter);
        totalGifts = view.findViewById(R.id.total_gifts_counter);
        totalFriends = view.findViewById(R.id.total_friends_counter);

        email.setText(emailUser);
        /*
        totalLists.setText();
        totalGifts.setText();
        totalFriends.setText(0);

         */

        UserDAO userDAO = new UserDAO();
        userDAO.getUserIdFromAPI(emailUser, apiKey, new UserDAO.UserCallback() {
            @Override
            public void onSuccess(String id, String nameUser) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        name.setText(nameUser);
                    }
                });

            }
            @Override
            public void onFailure(IOException e) {
                Toast.makeText(getContext(), "ERROR, cannot connect to the server", Toast.LENGTH_SHORT).show();
            }
        });

        ImageButton logoutButton = view.findViewById(R.id.log_out_button);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go back to log in / sign up page
                Intent intent1 = new Intent(getActivity(), MainActivity.class);
                startActivity(intent1);
                getActivity().finish();
            }
        });

        editProfileButton = (ImageButton) view.findViewById(R.id.edit_profile_button);
        editProfileButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent2 = new Intent(v.getContext(), EditProfileActivity.class);
                startActivity(intent2);
            }
        });

        viewReservedButton = (ImageButton) view.findViewById(R.id.view_reserved_button);
        viewReservedButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, MainWindow.class);
                intent.putExtra("fragment", "FriendListFragment");
                //intent.putExtra("title", "RESERVED GIFTS");
                context.startActivity(intent);

            }
        });

        return view;
    }
    /*
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
                    String nameJSON = jsonObject.getString("name");
                    String lastNameJSON = jsonObject.getString("last_name");
                    String emailJSON = jsonObject.getString("email");

                    String fullName = nameJSON + " " + lastNameJSON;
                    name.setText(fullName);
                    email.setText(emailJSON);

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

     */

}