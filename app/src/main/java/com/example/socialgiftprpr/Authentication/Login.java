package com.example.socialgiftprpr.Authentication;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.socialgiftprpr.R;
import com.example.socialgiftprpr.MainWindow;

public class Login extends Fragment {

    private Button logIn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_log_in, container, false);
        logIn = view.findViewById(R.id.login_button);

        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainWindow.class);
                startActivity(intent);
            }
        });
        return view;
    }
}