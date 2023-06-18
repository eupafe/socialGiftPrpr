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

import com.example.socialgiftprpr.MainWindow;
import com.example.socialgiftprpr.Persistence.UserDAO;
import com.example.socialgiftprpr.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FriendProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FriendProfileFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private ImageButton viewList;
    private ImageView image;
    private TextView name;
    private TextView email;
    private TextView totalLists;
    private TextView totalGifts;
    private TextView totalFriends;

    public FriendProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FriendProfileFragment.
     */

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
        //String emailUser = sharedPreferences.getString("email", null);
        String listCounter = sharedPreferences.getString("totalFriendLists", null);
        String giftCounter = sharedPreferences.getString("totalFriendGifts", null);
        String friendEmail = sharedPreferences.getString("friendEmail", null);

        name = view.findViewById(R.id.profile_name);
        email = view.findViewById(R.id.profile_email);
        image = view.findViewById(R.id.profile_image);
        email.setText(friendEmail);

        totalLists = view.findViewById(R.id.total_lists_counter);
        totalLists.setText(listCounter);

        totalGifts = view.findViewById(R.id.total_gifts_counter);
        totalGifts.setText(giftCounter);

        totalFriends = view.findViewById(R.id.total_friends_counter);
        //totalGifts.setText("");

        UserDAO userDAO = new UserDAO();
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