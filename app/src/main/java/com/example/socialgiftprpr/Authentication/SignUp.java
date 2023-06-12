package com.example.socialgiftprpr.Authentication;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.socialgiftprpr.R;
import com.example.socialgiftprpr.MainWindow;

import java.io.IOException;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class SignUp extends Fragment {

    private ImageView imageView;
    private Uri imagePath;

    private EditText name;
    private EditText surname;
    private EditText email;
    private EditText password;
    private Button signUp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        name = view.findViewById(R.id.signup_name);
        surname = view.findViewById(R.id.signup_surname);
        password = view.findViewById(R.id.signup_password);
        email = view.findViewById(R.id.signup_email);

        imageView = view.findViewById(R.id.profileImage);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select picture"), 1);
            }
        });

        signUp = view.findViewById(R.id.signup_button);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String enteredName = name.getText().toString();
                String enteredSurname = surname.getText().toString();
                String enteredEmail = email.getText().toString();
                String enteredPassword = password.getText().toString();

                JsonObjectRequest request = postDataToAPI(enteredName, enteredSurname, enteredEmail, enteredPassword);

                Volley.newRequestQueue(getContext()).add(request);

                if (true) {
                    Intent intent = new Intent(getActivity(), MainWindow.class);
                    startActivity(intent);
                } else{
                    Toast.makeText(getActivity(), "Wrong credentials. Please, try again", Toast.LENGTH_SHORT).show();
                }

            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data!=null){
            imagePath = data.getData();
            getImageInImageView();
        }
    }

    private void getImageInImageView(){

        Bitmap bitmap = null;
        try{
            bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(),imagePath);

        }catch (IOException e){
            e.printStackTrace();
        }
        imageView.setImageBitmap(bitmap);

    }

    public JsonObjectRequest postDataToAPI(String name, String surname, String email, String password) {

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("name", name);
            jsonBody.put("last_name", surname);
            jsonBody.put("email", email);
            jsonBody.put("password", password);
            jsonBody.put("image", "https://balandrau.salle.url.edu/i3/repositoryimages/photo/47601a8b-dc7f-41a2-a53b-19d2e8f54cd0.png");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = "https://balandrau.salle.url.edu/i3/socialgift/api/v1/users";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        String responseBody = response.toString();
                        System.out.println(responseBody);
                        Toast.makeText(getContext(), "Data posted successfully", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        error.printStackTrace();
                    }
                }
        );

        return request;
    }

}