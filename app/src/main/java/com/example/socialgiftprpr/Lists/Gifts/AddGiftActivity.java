package com.example.socialgiftprpr.Lists.Gifts;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.socialgiftprpr.Lists.ListAdapter;
import com.example.socialgiftprpr.Lists.ListModel;
import com.example.socialgiftprpr.MainWindow;
import com.example.socialgiftprpr.Persistence.GiftDAO;
import com.example.socialgiftprpr.Persistence.ListDAO;
import com.example.socialgiftprpr.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AddGiftActivity extends AppCompatActivity {

    private Button saveButton;
    private EditText priority;
    private EditText link;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_gift);

        priority = (EditText) findViewById(R.id.gift_priority);
        link = (EditText) findViewById(R.id.gift_link);

        saveButton = (Button) findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String enteredPriority = priority.getText().toString();
                String enteredLink = link.getText().toString();

                int priorityValue = 0;

                try {
                    priorityValue = Integer.parseInt(enteredPriority);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                String id = getIntent().getStringExtra("wishlistId");
                System.out.println("ID IS : " + id);

                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("SP", Context.MODE_PRIVATE);
                String apiKey = sharedPreferences.getString("apiKey", null);

                GiftDAO giftDAO = new GiftDAO();
                giftDAO.addGiftToAPI(apiKey, id, enteredLink, priorityValue, new GiftDAO.GiftCallback() {
                    @Override
                    public void onSuccess(List<GiftModel> giftEvents) {
                        Toast.makeText(getApplicationContext(), "Gift successfully added!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "ERROR, cannot connect to the server", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });

                Intent intent = new Intent(v.getContext(), MainWindow.class);
                intent.putExtra("fragment", "giftsFragment");
                startActivity(intent);
            }
        });

    }
}