package com.example.socialgiftprpr.Lists.Gifts;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.socialgiftprpr.Lists.ListAdapter;
import com.example.socialgiftprpr.Lists.ListModel;
import com.example.socialgiftprpr.Persistence.GiftDAO;
import com.example.socialgiftprpr.Persistence.ListDAO;
import com.example.socialgiftprpr.Persistence.ProductDAO;
import com.example.socialgiftprpr.Persistence.UserDAO;
import com.example.socialgiftprpr.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GiftsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GiftsFragment extends Fragment{

    private ImageButton seeGift;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String mParam3;

    private TextView listTitle;
    // Button to create a new gift
    private ImageButton addGift;

    private Spinner spinner;

    // Recycler view
    private RecyclerView gifts;
    // Adapter
    private GiftAdapter adapter;
    // List of tasks
    private List<GiftModel> giftEvents;

    private int idList;

    // Shared preferences
    private SharedPreferences sharedPreferences;

    private String listName;
    private int id;

    public GiftsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param listName Parameter 1.
     * @return A new instance of fragment GiftsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GiftsFragment newInstance(String listName, int id) {
        GiftsFragment fragment = new GiftsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, listName);
        args.putSerializable(ARG_PARAM2, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            listName = getArguments().getString(ARG_PARAM1);
            id = getArguments().getInt(ARG_PARAM2);
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gifts, container, false);

        listTitle = view.findViewById(R.id.giftsListTitle);
        listTitle.setText(listName);

        addGift = (ImageButton) view.findViewById(R.id.addButton);
        addGift.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), AddGiftActivity.class);
                intent.putExtra("wishlistId", id);
                intent.putExtra("wishlistName", listName);
                startActivity(intent);
            }
        });

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
                // No se seleccionó ningún elemento
            }
        });

        return view;

    }
    private void sortBy(String sortBy){

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("SP", Context.MODE_PRIVATE);
        String apiKey = sharedPreferences.getString("apiKey", null);
        String email = sharedPreferences.getString("email", null);

        GiftDAO giftDAO = new GiftDAO();
        giftDAO.getAllGiftsFromAPI(id, apiKey, new GiftDAO.GiftCallback() {
            @Override
            public void onSuccess(List<GiftModel> allGifts, String listName, int id) {
                System.out.println("ALL GIFTS: " + allGifts);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("SORT BY: " + sortBy);

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

                        adapter = new GiftAdapter(allGifts, false, listName, apiKey);
                        gifts.setAdapter(adapter);
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