package com.example.socialgiftprpr.Profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.socialgiftprpr.MainWindow;
import com.example.socialgiftprpr.R;

public class EditProfileActivity extends AppCompatActivity {

    Button confirmButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        confirmButton = (Button) findViewById(R.id.save_button);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                /*
                SharedPreferences preferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                String list = preferences.getString("myTasks", "");
                Gson gson = new Gson();
                List<ToDoModel> tasks = gson.fromJson(list, new TypeToken<List<ToDoModel>>(){}.getType());

                String nameS = name.getText().toString();
                String descriptionS = description.getText().toString();
                String deadlineS = deadline.getText().toString();
                List<ListModel> lists = new ArrayList<>();
                lists.add(new ListModel(nameS, descriptionS, deadlineS, false));

                Gson ngson = new Gson();
                String listOfTasks = ngson.toJson(tasks);
                editor.putString("myTasks", listOfTasks);
                editor.apply();
                */

                Context context = v.getContext();
                Intent intent = new Intent(context, MainWindow.class);
                intent.putExtra("fragment", "profileFragment");
                context.startActivity(intent);
            }
        });

    }
}
