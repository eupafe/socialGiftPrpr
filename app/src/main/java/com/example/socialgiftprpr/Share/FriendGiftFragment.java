package com.example.socialgiftprpr.Share;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.socialgiftprpr.Lists.Gifts.GiftAdapter;
import com.example.socialgiftprpr.Lists.Gifts.GiftModel;
import com.example.socialgiftprpr.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FriendGiftFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FriendGiftFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Spinner spinner;

    // Recycler view
    private RecyclerView gifts;
    // Adapter
    private GiftAdapter adapter;
    // List of tasks
    private List<GiftModel> giftEvents;
    public FriendGiftFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FriendGiftFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FriendGiftFragment newInstance(String param1, String param2) {
        FriendGiftFragment fragment = new FriendGiftFragment();
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
        View view = inflater.inflate(R.layout.fragment_friend_gift, container, false);

        spinner = view.findViewById(R.id.sortSpinner);
        List<String> items = Arrays.asList("Price","Priority");

        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, items);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterSpinner);


        // Adapter initialization
        gifts = (RecyclerView) view.findViewById(R.id.gifts);
        gifts.setLayoutManager(new LinearLayoutManager(view.getContext()));
        giftEvents = new ArrayList<>();
        giftEvents.add(new GiftModel("Bear", "https", "https", false));

        adapter = new GiftAdapter(giftEvents, true);
        adapter.setTasks(giftEvents);
        gifts.setAdapter(adapter);
        return view;
    }
}