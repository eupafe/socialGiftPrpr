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
import android.widget.TextView;
import android.widget.Toast;

import com.example.socialgiftprpr.MainWindow;
import com.example.socialgiftprpr.Persistence.GiftDAO;
import com.example.socialgiftprpr.R;

import java.io.IOException;
import java.util.List;

public class AddGiftActivity extends AppCompatActivity {

    // UI components
    private Button saveButton;
    private EditText priority;
    private EditText link;
    private TextView title;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_gift);

        int id = 0;
        String listName = null;
        Bundle extras = getIntent().getExtras();
        if(extras != null && extras.containsKey("wishlistId")){
            id = extras.getInt("wishlistId");
            listName = extras.getString("wishlistName");
        }

        String t = getIntent().getStringExtra("edit_gift_activity");

        title = (TextView) findViewById(R.id.title);

        if (t != null) {
            title.setText(t);
        }

        priority = (EditText) findViewById(R.id.gift_priority);
        link = (EditText) findViewById(R.id.gift_link);

        saveButton = (Button) findViewById(R.id.save_button);
        int finalId = id;
        String finalListName = listName;

        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("SP", Context.MODE_PRIVATE);
                String apiKey = sharedPreferences.getString("apiKey", null);

                String enteredPriority = priority.getText().toString();
                String enteredLink = link.getText().toString();

                int priorityValue = 0;

                try {
                    priorityValue = Integer.parseInt(enteredPriority);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                if(t != null){

                    String value = getIntent().getStringExtra("gift_id");

                    GiftDAO giftDAO = new GiftDAO();
                    giftDAO.editGiftToAPI(value, enteredPriority, enteredLink, apiKey, new GiftDAO.GiftCallback() {
                        @Override
                        public void onSuccess(List<GiftModel> listEvents, String string, int id) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(v.getContext(), MainWindow.class);
                                    intent.putExtra("fragment", "giftsFragment");
                                    intent.putExtra("listId", finalId);
                                    intent.putExtra("listName", finalListName);
                                    startActivity(intent);
                                }
                            });

                        }

                        @Override
                        public void onFailure(IOException e) {

                        }
                    });

                } else {

                    GiftDAO giftDAO = new GiftDAO();
                    giftDAO.addGiftToAPI(apiKey, finalId, enteredLink, priorityValue, new GiftDAO.GiftCallback() {
                        @Override
                        public void onSuccess(List<GiftModel> gifts, String list, int id) {

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
                }

                Intent intent = new Intent(v.getContext(), MainWindow.class);
                intent.putExtra("fragment", "giftsFragment");
                intent.putExtra("listId", finalId);
                intent.putExtra("listName", finalListName);
                startActivity(intent);

            }
        });

    }
}