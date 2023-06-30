package com.example.socialgiftprpr.Profile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.socialgiftprpr.MainWindow;
import com.example.socialgiftprpr.Persistence.UserDAO;
import com.example.socialgiftprpr.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class EditProfileActivity extends AppCompatActivity {

    // UI components
    private Button confirmButton;
    private EditText name;
    private EditText surname;
    private EditText email;
    private EditText password;
    private ImageView imageView;
    private Editable image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        name = findViewById(R.id.edit_name);
        surname = findViewById(R.id.edit_surname);
        email = findViewById(R.id.edit_email);
        password = findViewById(R.id.edit_password);

        imageView = findViewById(R.id.profileImage);
        imageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

                TextView titleTextView = new TextView(view.getContext());
                titleTextView.setText(R.string.sign_up_image);
                titleTextView.setGravity(Gravity.CENTER);
                builder.setCustomTitle(titleTextView);

                EditText link = new EditText(view.getContext());
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

        confirmButton = (Button) findViewById(R.id.confirm_button);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("SP", Context.MODE_PRIVATE);
                String apiKey = sharedPreferences.getString("apiKey", null);
                String email1 = sharedPreferences.getString("email", null);

                String enteredName = name.getText().toString();
                String enteredSurname = surname.getText().toString();
                String enteredEmail = email.getText().toString();
                String enteredPassword = password.getText().toString();

                boolean valid = true;
                if(enteredName.isEmpty() || enteredSurname.isEmpty() || enteredEmail.isEmpty() || enteredPassword.isEmpty()){
                    valid = false;
                    Toast.makeText(getApplicationContext(), "Missing fields!", Toast.LENGTH_SHORT).show();
                } else{

                    // Check that the image is available
                    try {
                        URL url = new URL(String.valueOf(image));

                    } catch (MalformedURLException e) {
                        valid = false;
                        Toast.makeText(getApplicationContext(), "URL not valid", Toast.LENGTH_SHORT).show();
                    }
                }

                if(valid){
                    UserDAO userDAO = new UserDAO();

                    userDAO.editUserProfileToAPI(email1, enteredName, enteredSurname, enteredEmail, enteredPassword, String.valueOf(image), apiKey,
                            new UserDAO.UserCallback() {
                                @Override
                                public void onSuccess(String email, String name, String p1) {

                                    SharedPreferences preferences = v.getContext().getSharedPreferences("SP", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putString("email", email);
                                    editor.apply();

                                    Intent intent = new Intent(getApplicationContext(), MainWindow.class);
                                    intent.putExtra("fragment", "profileFragment");
                                    startActivity(intent);

                                }

                                @Override
                                public void onFailure(IOException e) {

                                }
                            });
                }
            }
        });
    }
}
