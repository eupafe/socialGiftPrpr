package com.example.socialgiftprpr.Lists;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.socialgiftprpr.Persistence.ListDAO;
import com.example.socialgiftprpr.R;
import com.example.socialgiftprpr.MainWindow;

import org.json.JSONException;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class AddListActivity extends AppCompatActivity {

    // UI components
    private TextView title;
    private EditText name;
    private EditText description;
    private EditText deadline;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_list);

        title = (TextView) findViewById(R.id.title);
        name = (EditText) findViewById(R.id.list_name);
        description = (EditText) findViewById(R.id.list_description);
        deadline = (EditText) findViewById(R.id.list_deadline);

        String t = getIntent().getStringExtra("edit_list_activity");

        if (t != null) {
            title.setText(t);
        }

        saveButton = (Button) findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String enteredName = name.getText().toString();
                String enteredDescription = description.getText().toString();
                String enteredDeadline = deadline.getText().toString();

                boolean valid = true;
                if(enteredName.isEmpty() || enteredDescription.isEmpty() || enteredDeadline.isEmpty()){
                    valid = false;
                    Toast.makeText(getApplicationContext(), "Missing fields!", Toast.LENGTH_SHORT).show();
                } else {

                    // Check that the deadline is in the right format
                    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
                    LocalDateTime dateTime;

                    try {
                        dateTime = LocalDateTime.parse(enteredDeadline, dateFormatter);
                    } catch (DateTimeParseException e) {
                        valid = false;
                        Toast.makeText(getApplicationContext(), "Deadline format must be:\n dd/MM/yyyy HH:mm:ss", Toast.LENGTH_SHORT).show();

                    }

                }

                if(valid){
                    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("SP", Context.MODE_PRIVATE);
                    String apiKey = sharedPreferences.getString("apiKey", null);

                    if(t != null){

                        String value = getIntent().getStringExtra("edit_value");

                        ListDAO listDAO = new ListDAO();
                        listDAO.editListToAPI(value, enteredName, enteredDescription, enteredDeadline, apiKey, new ListDAO.ListCallback() {
                            @Override
                            public void onSuccess(List<ListModel> listEvents) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent(v.getContext(), MainWindow.class);
                                        startActivity(intent);
                                    }
                                });
                            }

                            @Override
                            public void onFailure(Exception e) {

                            }
                        });

                    } else{

                        ListDAO listDAO = new ListDAO();
                        listDAO.addListToAPI(apiKey, enteredName, enteredDescription, enteredDeadline, new ListDAO.ListCallback() {
                            @Override
                            public void onSuccess(List<ListModel> listEvents) {

                            }

                            @Override
                            public void onFailure(Exception e) {
                                Toast.makeText(getApplicationContext(), "ERROR, cannot connect to the server", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }

                    Intent intent = new Intent(v.getContext(), MainWindow.class);
                    startActivity(intent);
                }
            }
        });
    }
}