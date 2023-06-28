package com.example.socialgiftprpr.Profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.socialgiftprpr.MainWindow;
import com.example.socialgiftprpr.Persistence.UserDAO;
import com.example.socialgiftprpr.R;

import java.io.IOException;

public class EditProfileActivity extends AppCompatActivity {

    // UI components
    private Button confirmButton;
    private EditText name;
    private EditText surname;
    private EditText email;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        name = findViewById(R.id.edit_name);
        surname = findViewById(R.id.edit_surname);
        email = findViewById(R.id.edit_email);
        password = findViewById(R.id.edit_password);

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

                UserDAO userDAO = new UserDAO();

                userDAO.editUserProfileToAPI(email1, enteredName, enteredSurname, enteredEmail, enteredPassword, apiKey,
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
        });
    }
}
