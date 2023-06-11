package com.example.socialgiftprpr.Lists;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.socialgiftprpr.R;
import com.example.socialgiftprpr.MainWindow;

import java.util.ArrayList;
import java.util.List;

public class AddListActivity extends AppCompatActivity {


    // Box to input the name of the list
    private EditText name;
    // Box to input the description of the list
    private EditText description;
    // Box to input the deadline of the list
    private EditText deadline;
    // Button to save the list
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_list);


        name = (EditText) findViewById(R.id.nameBox);
        description = (EditText) findViewById(R.id.descriptionBox);
        deadline = (EditText) findViewById(R.id.deadlineBox);

        saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                /*
                SharedPreferences preferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                String list = preferences.getString("myTasks", "");
                Gson gson = new Gson();
                List<ToDoModel> tasks = gson.fromJson(list, new TypeToken<List<ToDoModel>>(){}.getType());*/

                String nameS = name.getText().toString();
                String descriptionS = description.getText().toString();
                String deadlineS = deadline.getText().toString();
                List<ListModel> lists = new ArrayList<>();
                lists.add(new ListModel(nameS, descriptionS, deadlineS, false));
                /*
                Gson ngson = new Gson();
                String listOfTasks = ngson.toJson(tasks);
                editor.putString("myTasks", listOfTasks);
                editor.apply();*/

                Intent intent = new Intent(v.getContext(), MainWindow.class);
                startActivity(intent);
            }
        });


    }
}