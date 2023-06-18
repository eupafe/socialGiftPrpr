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
import okhttp3.OkHttpClient;
import okhttp3.Request;
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
                e.printStackTrace();
                callback.onFailure(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {

                        String responseBody = response.body().string();
                        JSONArray jsonArray = new JSONArray(responseBody);

                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        String id = jsonObject.getString("id");
                        String name = jsonObject.getString("name");
                        String surname = jsonObject.getString("last_name");
                        String fullName = name.concat(" ").concat(surname);
                        String email = jsonObject.getString("email");
                        String image = jsonObject.getString("image");

                        List<UserModel> user = new ArrayList<>();
                        user.add(new UserModel(id, name, surname, email, image));
                        callback.onSuccess(user);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        //callback.onFailure(e);
                    }
                } else {
                    callback.onFailure(new IOException("Login failed"));
                }
            }
        });
    }

    public void markAsFriend(String id, String apiKey, FriendCallback callback){


    }

    public void getAllFriends(String apiKey, FriendCallback callback) {

        OkHttpClient client = new OkHttpClient().newBuilder().build();
        Request request = new Request.Builder()
                .url("https://balandrau.salle.url.edu/i3/socialgift/api/v1/friends")
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
                        // TODO: check all this. WRONG! Do smth similar to lists /gifts
                        String responseBody = response.body().string();
                        JSONArray jsonArray = new JSONArray(responseBody);

                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        String id = jsonObject.getString("id");
                        String name = jsonObject.getString("name");
                        String surname = jsonObject.getString("last_name");
                        String fullName = name.concat(" ").concat(surname);
                        String email = jsonObject.getString("email");
                        String image = jsonObject.getString("image");

                        List<UserModel> user = new ArrayList<>();
                        user.add(new UserModel(id, name, surname, email, image));
                        callback.onSuccess(user);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        //callback.onFailure(e);
                    }
                } else {
                    callback.onFailure(new IOException("Login failed"));
                }
            }
        });
    }

    public void reserveGift(int id, String apiKey, FriendCallback callback) {

        OkHttpClient client = new OkHttpClient().newBuilder().build();

        Request request = new Request.Builder()
                .url("https://balandrau.salle.url.edu/i3/socialgift/api/v1/gifts/"+ id + "/book")
                .method("POST", null)
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


}
