package com.example.socialgiftprpr.Persistence;

import com.example.socialgiftprpr.Lists.Gifts.GiftModel;
import com.example.socialgiftprpr.Lists.ListModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GiftDAO {

    public interface GiftCallback {
        void onSuccess(List<GiftModel> giftEvents);
        void onFailure(IOException e);
    }

    public void getAllGiftsFromAPI(String id, String apiKey, GiftCallback callback) {

        OkHttpClient client = new OkHttpClient().newBuilder().build();
        Request request = new Request.Builder()
                .url("https://balandrau.salle.url.edu/i3/socialgift/api/v1/gifts/" + id)
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
                        List<GiftModel> giftEvents = new ArrayList<>();
                        String responseBody = response.body().string();
                        JSONArray jsonArray = new JSONArray(responseBody);

                        for(int i = 0; i < jsonArray.length(); i ++) {

                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            int idJSON = jsonObject.getInt("id");
                            int wishlistIdJSON = jsonObject.getInt("wishlist_id");
                            String productUrlJSON = jsonObject.getString("product_url");
                            int priorityJSON = jsonObject.getInt("priority");
                            boolean bookedJSON = jsonObject.getBoolean("booked");

                            giftEvents.add(new GiftModel(idJSON, wishlistIdJSON, productUrlJSON, priorityJSON, bookedJSON));
                        }
                        callback.onSuccess(giftEvents);

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
    public void addGiftToAPI(String apiKey, String wishlistId, String productUrl, int priority, GiftDAO.GiftCallback callback){

        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("application/json");

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("wishlist_id", wishlistId);
            jsonBody.put("product_url", productUrl);
            jsonBody.put("priority", priority);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println(jsonBody);

        RequestBody body = RequestBody.create(mediaType, jsonBody.toString());
        Request request = new Request.Builder()
                .url("https://balandrau.salle.url.edu/i3/socialgift/api/v1/gifts")
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
                    callback.onFailure(new IOException("Add gift failed"));
                }
            }

        });
    }

}
