package com.example.socialgiftprpr.Authentication;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.socialgiftprpr.R;
import com.example.socialgiftprpr.MainWindow;

import org.json.JSONException;
import org.json.JSONObject;

public class Login extends Fragment {

    private EditText email;
    private EditText password;
    private Button logIn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_log_in, container, false);

        email = view.findViewById(R.id.login_email);
        password = view.findViewById(R.id.login_password);

        logIn = view.findViewById(R.id.login_button);
        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String enteredEmail = email.getText().toString();
                String enteredPassword = password.getText().toString();

                JsonObjectRequest request = postDataToAPI(enteredEmail, enteredPassword);

                Volley.newRequestQueue(getContext()).add(request);
            }
        });
        return view;
    }

    public JsonObjectRequest postDataToAPI(String email, String password) {

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("email", email);
            jsonBody.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = "https://balandrau.salle.url.edu/i3/socialgift/api/v1/users/login";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    String accessToken = response.getString("accessToken");
                    System.out.println("Access Token: " + accessToken);

                    Intent intent = new Intent(getActivity(), MainWindow.class);
                    intent.putExtra("access_token", accessToken);
                    startActivity(intent);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Toast.makeText(getContext(), "Data posted successfully", Toast.LENGTH_SHORT).show();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Wrong credentials. Please, try again", Toast.LENGTH_SHORT).show();
                        //error.printStackTrace();
                    }
                }
        );

        return request;
    }
}