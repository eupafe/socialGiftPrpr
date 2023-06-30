package com.example.socialgiftprpr.Authentication;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.provider.MediaStore;
import android.text.Editable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.socialgiftprpr.Persistence.UserDAO;
import com.example.socialgiftprpr.R;
import com.example.socialgiftprpr.MainWindow;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class SignUp extends Fragment {

    // UI components
    private ImageView imageView;
    private Editable image;
    private EditText name;
    private EditText surname;
    private EditText email;
    private EditText password;
    private Button signUp;

    // Other variables
    private Uri imagePath;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        imageView = view.findViewById(R.id.profileImage);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                TextView titleTextView = new TextView(getContext());
                titleTextView.setText(R.string.sign_up_image);
                titleTextView.setGravity(Gravity.CENTER);
                builder.setCustomTitle(titleTextView);

                EditText link = new EditText(getContext());
                link.setHint(R.string.image_link);
                link.setGravity(Gravity.CENTER);
                builder.setView(link);

                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        image = link.getText();
                        if (image != null && !image.equals("") && !image.toString().isEmpty()) {

                            try {
                                URL url = new URL(String.valueOf(image));
                                Picasso.get().load(String.valueOf(image)).into(imageView);
                            } catch (MalformedURLException e) {
                                imageView.setImageResource(R.drawable.no_photo);
                            }

                        } else{
                            imageView.setImageResource(R.drawable.no_photo);
                        }
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });

        name = view.findViewById(R.id.signup_name);
        surname = view.findViewById(R.id.signup_surname);
        password = view.findViewById(R.id.signup_password);
        email = view.findViewById(R.id.signup_email);

        signUp = view.findViewById(R.id.signup_button);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String enteredName = name.getText().toString();
                String enteredSurname = surname.getText().toString();
                String enteredEmail = email.getText().toString();
                String enteredPassword = password.getText().toString();

                if(enteredName.isEmpty()|| enteredSurname.isEmpty() || enteredEmail.isEmpty() || enteredPassword.isEmpty()){
                    Toast.makeText(getContext(), "Some fields are missing!", Toast.LENGTH_SHORT).show();
                } else{
                    SharedPreferences preferences = requireContext().getSharedPreferences("SP", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("email", enteredEmail);
                    editor.apply();

                    UserDAO userDAO = new UserDAO();
                    userDAO.signup(enteredName, enteredSurname, enteredEmail, enteredPassword, String.valueOf(image), new UserDAO.UserCallback() {
                        @Override
                        public void onSuccess(String successful, String name, String p1) {
                            Intent intent = new Intent(getActivity(), MainWindow.class);
                            startActivity(intent);
                        }

                        @Override
                        public void onFailure(IOException e) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    String string = null;

                                    if(e.getMessage().equals("Sign up failed")){
                                        string = "Sign up failed. Please, try again";
                                    } else if (e.getMessage().equals("Server error")){
                                        string = "Server connection failed";
                                    }
                                    String finalString = string;

                                    Toast.makeText(getContext(), finalString, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

}