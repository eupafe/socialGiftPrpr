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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.socialgiftprpr.Lists.Gifts.GiftAdapter;
import com.example.socialgiftprpr.Lists.Gifts.GiftModel;
import com.example.socialgiftprpr.Persistence.GiftDAO;
import com.example.socialgiftprpr.R;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FriendGiftFragment extends Fragment {

    // Variables
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private int mParam2;

    // UI components
    private TextView listTitle;
    private Spinner spinner;

    // Recycler view
    private RecyclerView gifts;
    // Adapter
    private GiftAdapter adapter;

    public FriendGiftFragment() {
        // Required empty public constructor
    }

    public static FriendGiftFragment newInstance(String param1, int param2) {
        FriendGiftFragment fragment = new FriendGiftFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putInt(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getInt(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend_gift, container, false);

        listTitle = view.findViewById(R.id.giftsListTitle);
        listTitle.setText(mParam1);

        spinner = view.findViewById(R.id.sortSpinner);
        List<String> items = Arrays.asList("Price","Priority");

        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, items);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterSpinner);

        // Adapter initialization
        gifts = (RecyclerView) view.findViewById(R.id.gifts);
        gifts.setLayoutManager(new LinearLayoutManager(view.getContext()));

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();

                if (selectedItem.equals("Price")) {
                    sortBy("Price");

                } else if (selectedItem.equals("Priority")) {
                    sortBy("Priority");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }

    private void sortBy(String sortBy){

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("SP", Context.MODE_PRIVATE);
        String apiKey = sharedPreferences.getString("apiKey", null);

        GiftDAO giftDAO = new GiftDAO();
        giftDAO.getAllGiftsFromAPI(mParam2, apiKey, new GiftDAO.GiftCallback() {
            @Override
            public void onSuccess(List<GiftModel> allGifts, String listName, int id) {

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if(allGifts.size() == 0){
                            Toast.makeText(getContext(), "THERE ARE NO GIFTS IN THIS LIST", Toast.LENGTH_SHORT).show();
                        } else{

                            if(sortBy.equals("Priority")) {
                                Collections.sort(allGifts, new Comparator<GiftModel>() {
                                    @Override
                                    public int compare(GiftModel gift1, GiftModel gift2) {

                                        return Integer.compare(gift1.getPriority(), gift2.getPriority());
                                    }
                                });
                            } else {

                                Collections.sort(allGifts, new Comparator<GiftModel>() {
                                    @Override
                                    public int compare(GiftModel gift1, GiftModel gift2) {

                                        return Double.compare(gift1.getProductInfo().getPrice(), gift2.getProductInfo().getPrice());
                                    }
                                });

                            }

                            adapter = new GiftAdapter(allGifts, true, listName, apiKey);
                            gifts.setAdapter(adapter);
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
}