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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.socialgiftprpr.Lists.ListModel;
import com.example.socialgiftprpr.MainActivity;
import com.example.socialgiftprpr.Persistence.ListDAO;
import com.example.socialgiftprpr.Persistence.UserDAO;
import com.example.socialgiftprpr.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // UI components
    ImageButton editProfileButton;
    private TextView totalLists;
    private TextView totalGifts;
    private TextView name;
    private TextView email;
    private ImageView image;

    // Variables
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

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

        image = view.findViewById(R.id.profile_image);

        UserDAO userDAO = new UserDAO();
        userDAO.getUserIdFromAPI(emailUser, apiKey, new UserDAO.UserCallback() {
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
                                    System.out.println(counter);
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

        email.setText(emailUser);

        userDAO.getUserIdFromAPI(emailUser, apiKey, new UserDAO.UserCallback() {
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


        return view;
    }

}