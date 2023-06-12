package com.example.socialgiftprpr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import com.example.socialgiftprpr.Lists.Gifts.GiftsFragment;
import com.example.socialgiftprpr.databinding.ActivityMainBinding;
import com.example.socialgiftprpr.Profile.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainWindow extends AppCompatActivity{

    ActivityMainBinding binding;
    ListFragment listFragment;
    ShareFragment shareFragment;
    ProfileFragment profileFragment;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_window);

        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey("fragment")) {
            String fragmentType = extras.getString("fragment");
            if (fragmentType.equals("giftsFragment")) {
                replaceFragment(new GiftsFragment());

            } else if(fragmentType.equals("profileFragment")){
                replaceFragment(new ProfileFragment());

            }

            bottomNavigationView = findViewById(R.id.bottom_navigation);
        }
        else{
            replaceFragment(new ListFragment());

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
        /*
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout, fragment)
                .commit();

         */
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}