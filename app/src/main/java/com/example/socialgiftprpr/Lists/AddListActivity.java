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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.socialgiftprpr.R;
import com.example.socialgiftprpr.MainWindow;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddListActivity extends AppCompatActivity {

    private TextView title;
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

                /*
                SharedPreferences preferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                String list = preferences.getString("myTasks", "");
                Gson gson = new Gson();
                List<ToDoModel> tasks = gson.fromJson(list, new TypeToken<List<ToDoModel>>(){}.getType());*/

                String enteredName = name.getText().toString();
                String enteredDescription = description.getText().toString();
                String enteredDeadline = deadline.getText().toString();
                //List<ListModel> lists = new ArrayList<>();
                //lists.add(new ListModel(ente, descriptionS, deadlineS, false));
                /*
                Gson ngson = new Gson();
                String listOfTasks = ngson.toJson(tasks);
                editor.putString("myTasks", listOfTasks);
                editor.apply();*/

                if(t != null){

                    String value = getIntent().getStringExtra("edit_activity");
                    if(value != null){
                        JsonObjectRequest request = putDataToAPI(value, enteredName, enteredDescription, enteredDeadline);
                    }

                } else{

                    JsonObjectRequest request = postDataToAPI(enteredName, enteredDescription, enteredDeadline);
                    Volley.newRequestQueue(getApplicationContext()).add(request);
                }

                Intent intent = new Intent(v.getContext(), MainWindow.class);
                startActivity(intent);
            }
        });
    }

    public JsonObjectRequest postDataToAPI(String name, String description, String deadline) {


        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(deadline, dateFormatter);

        DateTimeFormatter iso8601Formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
        String iso8601String = dateTime.format(iso8601Formatter);

        System.out.println(iso8601String);

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("name", name);
            jsonBody.put("description", description);
            jsonBody.put("end_date", iso8601String);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = "https://balandrau.salle.url.edu/i3/socialgift/api/v1/wishlists";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                String responseBody = response.toString();
                System.out.println(responseBody);
                Toast.makeText(getApplicationContext(), "Data posted successfully", Toast.LENGTH_SHORT).show();

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        error.printStackTrace();
                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                SharedPreferences sharedPreferences = getSharedPreferences("MiArchivoPreferencias", Context.MODE_PRIVATE);
                String apiKey = sharedPreferences.getString("apiKey", null);

                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + apiKey);

                return headers;
            }
        };

        return request;
    }

    public JsonObjectRequest putDataToAPI(String value, String name, String description, String deadline) {

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("name", name);
            jsonBody.put("description", description);
            jsonBody.put("end_date", deadline);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = "https://balandrau.salle.url.edu/i3/socialgift/api/v1/wishlists/" + value;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, jsonBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                String responseBody = response.toString();
                System.out.println(responseBody);

                Context context = getApplicationContext();
                Intent intent = new Intent(context, MainWindow.class);
                intent.putExtra("fragment", "profileFragment");
                startActivity(intent);

                Toast.makeText(getApplicationContext(), "Data successfully edited", Toast.LENGTH_SHORT).show();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("ERROR");
                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                SharedPreferences sharedPreferences = getSharedPreferences("MiArchivoPreferencias", Context.MODE_PRIVATE);
                String apiKey = sharedPreferences.getString("apiKey", null);

                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + apiKey);

                return headers;
            }
        };

        return request;
    }

}