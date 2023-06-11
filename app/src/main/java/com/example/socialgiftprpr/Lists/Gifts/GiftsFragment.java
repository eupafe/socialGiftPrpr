package com.example.socialgiftprpr.Lists.Gifts;

import android.annotation.SuppressLint;
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

import com.example.socialgiftprpr.Lists.AddListActivity;
import com.example.socialgiftprpr.Lists.ListAdapter;
import com.example.socialgiftprpr.Lists.ListModel;
import com.example.socialgiftprpr.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GiftsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GiftsFragment extends Fragment {

    private ImageButton seeGift;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // Button to create a new gift
    private ImageButton addGift;

    // Recycler view
    private RecyclerView gifts;
    // Adapter
    private GiftAdapter adapter;
    // List of tasks
    private List<GiftModel> giftEvents;
    // Shared preferences
    private SharedPreferences sharedPreferences;

    public GiftsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GiftsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GiftsFragment newInstance(String param1, String param2) {
        GiftsFragment fragment = new GiftsFragment();
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
        View view = inflater.inflate(R.layout.fragment_gifts, container, false);

        addGift = (ImageButton) view.findViewById(R.id.addButton);
        addGift.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), AddGiftActivity.class);
                startActivity(intent);
            }
        });


        // Adapter initialization
        gifts = (RecyclerView) view.findViewById(R.id.gifts);
        gifts.setLayoutManager(new LinearLayoutManager(view.getContext()));
        giftEvents = new ArrayList<>();
        giftEvents.add(new GiftModel("TEDDY", "https", false));
        adapter = new GiftAdapter(giftEvents);

        adapter.setTasks(giftEvents);
        gifts.setAdapter(adapter);
        return view;

    }
}