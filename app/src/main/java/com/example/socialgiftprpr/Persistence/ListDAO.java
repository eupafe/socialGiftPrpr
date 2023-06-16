package com.example.socialgiftprpr.Persistence;
import com.example.socialgiftprpr.Lists.Gifts.GiftModel;
import com.example.socialgiftprpr.Lists.ListModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.*;

public class ListDAO {

    public ListDAO(){

    }

    public interface ListCallback {
        void onSuccess(List<ListModel> listEvents);
        void onFailure(Exception e);
    }
    public void getAllListsFromAPI(String id, String apiKey, ListCallback callback) {

        OkHttpClient client = new OkHttpClient().newBuilder().build();
        Request request = new Request.Builder()
                .url("https://balandrau.salle.url.edu/i3/socialgift/api/v1/users/" + id + "/wishlists")
                .method("GET", null)
                .addHeader("Authorization", "Bearer " + apiKey)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                callback.onFailure(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        List<ListModel> listEvents = new ArrayList<>();
                        String responseBody = response.body().string();
                        JSONArray jsonArray = new JSONArray(responseBody);

                        for(int i = 0; i < jsonArray.length(); i ++) {

                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            int idJSON = jsonObject.getInt("id");
                            String nameJSON = jsonObject.getString("name");
                            String descriptionJSON = jsonObject.getString("description");
                            String deadlineJSON = jsonObject.getString("end_date");
                            JSONArray jsonArray1 = jsonObject.getJSONArray("gifts");

                            ArrayList<GiftModel> gifts = new ArrayList<>();

                            for (int j = 0; j < jsonArray1.length(); j++) {

                                JSONObject jsonObject1 = jsonArray1.getJSONObject(j);
                                int id = jsonObject1.getInt("id");
                                int wishlistId = jsonObject1.getInt("wishlist_id");
                                String productUrl = jsonObject1.getString("product_url");
                                int priority = jsonObject1.getInt("priority");
                                int booked = jsonObject1.getInt("booked");
                                boolean state = false;
                                if(booked == 1){
                                    state = true;
                                }
                                gifts.add(new GiftModel(id, wishlistId, productUrl, priority, state));
                            }

                            listEvents.add(new ListModel(idJSON, nameJSON, descriptionJSON, deadlineJSON, gifts));
                        }
                        callback.onSuccess(listEvents);

                    } catch (JSONException e) {
                        //e.printStackTrace();
                        //callback.onFailure(e);
                    }
                } else {
                    callback.onFailure(new IOException("Login failed"));
                }
            }

        });
    }

    public void addListToAPI(String apiKey, String name, String description, String endDate, ListCallback callback){

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(endDate, dateFormatter);

        DateTimeFormatter iso8601Formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
        String deadline = dateTime.format(iso8601Formatter);

        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("application/json");

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("name", name);
            jsonBody.put("description", description);
            jsonBody.put("end_date", deadline);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(mediaType, jsonBody.toString());
        Request request = new Request.Builder()
                .url("https://balandrau.salle.url.edu/i3/socialgift/api/v1/wishlists")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + apiKey)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                callback.onFailure(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    callback.onFailure(new IOException("Add list failed"));
                }
            }

        });
    }

    /*
    public List<ListModel> getAllListsFromAPI(String id) {

        List<ListModel> listEvents = new ArrayList<ListModel>();

        String url ="https://balandrau.salle.url.edu/i3/socialgift/api/v1/users/"+ 752 + "/wishlists";

        RequestQueue queue = Volley.newRequestQueue(context);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                System.out.println("RESPONSEE: " + response);
                try{
                    for(int i = 0; i < response.length(); i ++){

                        JSONObject jsonObject = response.getJSONObject(i);
                        int idJSON = jsonObject.getInt("id");
                        String nameJSON = jsonObject.getString("name");
                        String descriptionJSON = jsonObject.getString("description");
                        String emailJSON = jsonObject.getString("end_date");

                        System.out.println(nameJSON + " " + descriptionJSON + " " + emailJSON);
                        //name.setText(fullName);
                        //email.setText(emailJSON);

                        listEvents.add(new ListModel(idJSON, nameJSON, descriptionJSON, emailJSON, false));
                    }

                } catch(JSONException e){
                    e.printStackTrace();
                }

                Toast.makeText(context, "Data posted successfully", Toast.LENGTH_SHORT).show();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                SharedPreferences sharedPreferences = context.getSharedPreferences("MiArchivoPreferencias", Context.MODE_PRIVATE);
                String apiKey = sharedPreferences.getString("apiKey", null);

                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + apiKey);

                return headers;
            }
        };

        queue.add(request);

        return listEvents;
    }

     */
}