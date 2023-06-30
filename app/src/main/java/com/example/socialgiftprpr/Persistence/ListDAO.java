package com.example.socialgiftprpr.Persistence;
import com.example.socialgiftprpr.Lists.Gifts.GiftModel;
import com.example.socialgiftprpr.Lists.Gifts.ProductModel;
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
                callback.onFailure(new IOException("Server error"));
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
                            int idUserJSON = jsonObject.getInt("user_id");
                            String nameJSON = jsonObject.getString("name");
                            String descriptionJSON = jsonObject.getString("description");
                            String creationDateJSON = jsonObject.getString("creation_date");
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

                                boolean finalState = state;
                                ProductModel product = null;
                                gifts.add(new GiftModel(id, wishlistId, productUrl, priority, finalState, product));

                            }

                            listEvents.add(new ListModel(idJSON,idUserJSON, nameJSON, descriptionJSON,creationDateJSON, deadlineJSON, gifts));
                        }
                        callback.onSuccess(listEvents);

                    } catch (JSONException e) {
                        callback.onFailure(new IOException("Server error"));
                    }
                } else {
                    callback.onFailure(new IOException("Get all lists failed"));
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
                callback.onFailure(new IOException("Server error"));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    callback.onFailure(new IOException("Add list failed"));
                }
            }

        });
    }

    public void deleteListFromAPI(int idList, String apiKey, ListDAO.ListCallback callback){

        OkHttpClient client = new OkHttpClient().newBuilder().build();
        Request request = new Request.Builder()
                .url("https://balandrau.salle.url.edu/i3/socialgift/api/v1/wishlists/"+ idList)
                .method("DELETE", null)
                .addHeader("Authorization", "Bearer " + apiKey)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(new IOException("Server error"));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    List<ListModel> listEvents = null;
                    callback.onSuccess(listEvents);

                } else {
                    callback.onFailure(new IOException("Gift delete failed"));
                }
            }

        });

    }

    public void editListToAPI(String id, String name, String description, String deadline, String apiKey, ListCallback callback) {

        getWishlistById(id, apiKey, new ListCallback() {
            @Override
            public void onSuccess(List<ListModel> listEvents) {
                ListModel list = listEvents.get(0);

                OkHttpClient client = new OkHttpClient().newBuilder().build();
                MediaType mediaType = MediaType.parse("application/json");

                JSONObject jsonBody = new JSONObject();
                try {

                    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
                    LocalDateTime dateTime = LocalDateTime.parse(deadline, dateFormatter);

                    DateTimeFormatter iso8601Formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
                    String endDate = dateTime.format(iso8601Formatter);

                    jsonBody.put("id", list.getId());
                    jsonBody.put("name", name);
                    jsonBody.put("description", description);
                    jsonBody.put("user_id", list.getUserId());
                    jsonBody.put("gifts", list.getGifts());
                    jsonBody.put("creation_date", list.getCreationDate());
                    jsonBody.put("end_date", endDate);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                RequestBody body = RequestBody.create(mediaType, jsonBody.toString());
                Request request = new Request.Builder()
                        .url("https://balandrau.salle.url.edu/i3/socialgift/api/v1/wishlists/" + id)
                        .method("PUT", body)
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Authorization", "Bearer " + apiKey)
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        callback.onFailure(new IOException("Server error"));
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {

                            List<ListModel> listEvents = null;
                            callback.onSuccess(listEvents);

                        } else {
                            callback.onFailure(new IOException("List edited failed"));
                        }
                    }

                });
            }

            @Override
            public void onFailure(Exception e) {

            }
        });


    }

    public void getWishlistById(String wishlistId, String apiKey, ListCallback callback) {

        OkHttpClient client = new OkHttpClient().newBuilder().build();
        Request request = new Request.Builder()
                .url("https://balandrau.salle.url.edu/i3/socialgift/api/v1/wishlists/" + wishlistId)
                .method("GET", null)
                .addHeader("Authorization", "Bearer " + apiKey)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(new IOException("Server error"));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {

                        List<ListModel> listModel = new ArrayList<>();
                        ArrayList<GiftModel> gifts = new ArrayList<>();
                        String responseBody = response.body().string();
                        JSONObject jsonObject = new JSONObject(responseBody);

                        int listId = jsonObject.getInt("id");
                        int userId = jsonObject.getInt("user_id");
                        String listName = jsonObject.getString("name");
                        String description = jsonObject.getString("description");
                        String creationDate = jsonObject.getString("creation_date");
                        String endDate = jsonObject.getString("end_date");
                        JSONArray jsonArray1 = jsonObject.getJSONArray("gifts");

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
                            gifts.add(new GiftModel(id, wishlistId, productUrl, priority, state, null));
                        }

                        listModel.add(new ListModel(listId, userId, listName, description, creationDate, endDate, gifts));

                        callback.onSuccess(listModel);

                    } catch (JSONException e) {
                        callback.onFailure(new IOException("Server Error"));
                    }
                } else {
                    callback.onFailure(new IOException("Login failed"));
                }
            }

        });
    }
}
