package com.example.socialgiftprpr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;

import com.example.socialgiftprpr.Lists.Gifts.GiftModel;
import com.example.socialgiftprpr.Lists.Gifts.GiftsFragment;
import com.example.socialgiftprpr.Lists.ListFragment;
import com.example.socialgiftprpr.Persistence.ListDAO;
import com.example.socialgiftprpr.Share.FriendGiftFragment;
import com.example.socialgiftprpr.Share.FriendListFragment;
import com.example.socialgiftprpr.Share.FriendProfileFragment;
import com.example.socialgiftprpr.Share.ShareFragment;
import com.example.socialgiftprpr.Profile.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainWindow extends AppCompatActivity{

    // Bottom navigation bar
    BottomNavigationView bottomNavigationView;

    private int totalLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_window);

        Bundle extras = getIntent().getExtras();
        if(extras != null && extras.containsKey("access_token")){

            SharedPreferences sharedPreferences = getSharedPreferences("SP", Context.MODE_PRIVATE);
            String apiKey = extras.getString("access_token");
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("apiKey", apiKey);
            editor.apply();
        }
        if (extras != null && extras.containsKey("fragment")) {
            String fragmentType = extras.getString("fragment");
            if (fragmentType.equals("giftsFragment")) {

                String listName = extras.getString("listName");
                System.out.println("LIST NAME : " + listName);

                int id = extras.getInt("listId");
                System.out.println("LIST id : " + id);

                GiftsFragment giftsFragment = GiftsFragment.newInstance(listName, id);
                replaceFragment(giftsFragment);


            } else if(fragmentType.equals("profileFragment")){

                ProfileFragment profileFragment = new ProfileFragment();
                replaceFragment(profileFragment);

            }
            else if(fragmentType.equals("friendProfileFragment")){
                replaceFragment(new FriendProfileFragment());

            }
            else if(fragmentType.equals("FriendListFragment")){

                /*
                FriendListFragment friendListFragment = (FriendListFragment) getSupportFragmentManager().findFragmentByTag("friendListFragment");

                if (friendListFragment != null) {
                    if (extras.containsKey("title")) {
                        String title = extras.getString("title");
                        friendListFragment.setTitle(title);
                    }
                } else {
                    // El fragmento no está agregado actualmente, crea una nueva instancia y pásale los argumentos si es necesario
                    friendListFragment = new FriendListFragment();
                    friendListFragment.setArguments(getIntent().getExtras());
                }*/

                replaceFragment(new FriendListFragment());

                /*
                FriendListFragment friendListFragment = new FriendListFragment();

                if (extras.containsKey("title")){
                    String title = extras.getString("title");
                    friendListFragment.setTitle(title);

                }
                replaceFragment(friendListFragment);*/

            }
            else if(fragmentType.equals("FriendGiftFragment")){
                replaceFragment(new FriendGiftFragment());

            }

            bottomNavigationView = findViewById(R.id.bottom_navigation);
        }
        else{
            ListFragment listFragment = new ListFragment();
            replaceFragment(listFragment);
        }

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.list:
                    replaceFragment(new ListFragment());
                    return true;
                case R.id.share:
                    replaceFragment(new ShareFragment());
                    return true;
                case R.id.profile:
                    replaceFragment(new ProfileFragment());
                    return true;
            }
            return false;
        });
    }

    private void replaceFragment(Fragment fragment) {

        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, fragment).commit();
    }
}