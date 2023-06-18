package com.example.socialgiftprpr.Authentication;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.socialgiftprpr.Persistence.UserDAO;
import com.example.socialgiftprpr.R;
import com.example.socialgiftprpr.MainWindow;
import java.io.IOException;

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

                SharedPreferences preferences = requireContext().getSharedPreferences("SP", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("email", enteredEmail);
                editor.apply();

                /*
                JsonObjectRequest request = postDataToAPI(enteredName, enteredSurname, enteredEmail, enteredPassword);

                Volley.newRequestQueue(getContext()).add(request);

                 */

                UserDAO userDAO = new UserDAO();
                userDAO.signup(enteredName, enteredSurname, enteredEmail, enteredPassword, new UserDAO.UserCallback() {
                    @Override
                    public void onSuccess(String successful, String name) {
                        Intent intent = new Intent(getActivity(), MainWindow.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(IOException e) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(), "Sign up failed. Please, try again", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
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
}