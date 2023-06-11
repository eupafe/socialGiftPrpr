package com.example.socialgiftprpr.Lists.Gifts;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.socialgiftprpr.Lists.ListModel;
import com.example.socialgiftprpr.MainWindow;
import com.example.socialgiftprpr.R;

import java.util.ArrayList;
import java.util.List;

public class AddGiftActivity extends AppCompatActivity {

    private Button saveButton;
    private EditText name;
    private EditText priority;
    private EditText link;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_gift);

        name = (EditText) findViewById(R.id.gift_name);
        priority = (EditText) findViewById(R.id.gift_priority);
        link = (EditText) findViewById(R.id.gift_link);

        saveButton = (Button) findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                String nameS = name.getText().toString();
                String priorityS = priority.getText().toString();
                String linkS = link.getText().toString();
                List<GiftModel> lists = new ArrayList<>();
                lists.add(new GiftModel(nameS, priorityS, linkS, false));

                Context context = v.getContext();
                Intent intent = new Intent(context, MainWindow.class);
                intent.putExtra("fragment", "giftsFragment");
                context.startActivity(intent);
            }
        });

    }
}