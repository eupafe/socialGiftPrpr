package com.example.socialgiftprpr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.socialgiftprpr.Lists.ListAdapter;
import com.example.socialgiftprpr.Lists.ListModel;
import com.example.socialgiftprpr.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class mainWindow extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_window);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
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

        replaceFragment(new ListFragment());

    }

        /*
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());

        mainBinding.bottomNavigationView.setOnItemSelectedListener(item -> {

            switch(item.getItemId()){

            }
            return true;

        });
*/
    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout, fragment)
                .commit();
    }
}