package com.example.socialgiftprpr.Persistence;

import com.example.socialgiftprpr.Share.UserModel;

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

public class FriendDAO {

    public FriendDAO(){
    }

    public interface FriendCallback {
        void onSuccess(List<UserModel> users);
        void onFailure(IOException e);
    }

    public void getFriendIdFromAPI(String email, String apiKey, FriendDAO.FriendCallback callback){

        OkHttpClient client = new OkHttpClient().newBuilder().build();
        Request request = new Request.Builder()
                .url("https://balandrau.salle.url.edu/i3/socialgift/api/v1/users/search?s=" + email)
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

                        String responseBody = response.body().string();
                        JSONArray jsonArray = new JSONArray(responseBody);

                        List<UserModel> user = new ArrayList<>();

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String id = jsonObject.getString("id");
                            String name = jsonObject.getString("name");
                            String surname = jsonObject.getString("last_name");
                            String fullName = name.concat(" ").concat(surname);
                            String email = jsonObject.getString("email");
                            String image = jsonObject.getString("image");

                            user.add(new UserModel(id, name, surname, email, image));
                        }

                        callback.onSuccess(user);

                    } catch (JSONException e) {
                        callback.onFailure(new IOException("Server error"));
                    }
                } else {
                    callback.onFailure(new IOException("Friend error"));
                }
            }
        });
    }


    public void reserveGift(int id, String apiKey, FriendCallback callback) {

        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("application/json");

        JSONObject jsonBody = new JSONObject();

        RequestBody body = RequestBody.create(mediaType, jsonBody.toString());
        Request request = new Request.Builder()
                .url("https://balandrau.salle.url.edu/i3/socialgift/api/v1/gifts/"+ id +"/book")
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
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful()) {
                    List<UserModel> users = new ArrayList<>();
                    callback.onSuccess(users);

                } else{
                    callback.onFailure(new IOException("Reservation failed"));
                }
            }
        });
    }

    public void deleteReservationGift(int id, String apiKey, FriendCallback callback) {

        OkHttpClient client = new OkHttpClient().newBuilder().build();
        Request request = new Request.Builder()
                .url("https://balandrau.salle.url.edu/i3/socialgift/api/v1/gifts/" + id + "/book")
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
                    List<UserModel> listEvents = null;
                    callback.onSuccess(listEvents);

                } else {
                    callback.onFailure(new IOException("Gift delete failed"));
                }
            }

        });

    }
}
