package com.example.socialgiftprpr.Persistence;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UserDAO {

    public UserDAO(){
    }

    public interface UserCallback {
        void onSuccess(String accessToken, String name, String image);
        void onFailure(IOException e);
    }

    public void login(String email, String password, UserCallback callback){

        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("application/json");

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("email", email);
            jsonBody.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(mediaType, jsonBody.toString());
        Request request = new Request.Builder()
                .url("https://balandrau.salle.url.edu/i3/socialgift/api/v1/users/login")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
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
                        JSONObject jsonResponse = new JSONObject(response.body().string());
                        String accessToken = jsonResponse.getString("accessToken");
                        System.out.println("Access Token: " + accessToken);
                        callback.onSuccess(accessToken, "", "");

                    } catch (JSONException e) {
                        //e.printStackTrace();
                        //callback.onFailure(new IOException("Server Error"));
                    }
                } else {
                    callback.onFailure(new IOException("Login failed"));
                }
            }
        });
    }

    public void signup(String name, String surname, String email, String password, String image, UserCallback callback){

        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("application/json");

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("name", name);
            jsonBody.put("last_name", surname);
            jsonBody.put("email", email);
            jsonBody.put("password", password);
            jsonBody.put("image", image);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(mediaType, jsonBody.toString());
        Request request = new Request.Builder()
                .url("https://balandrau.salle.url.edu/i3/socialgift/api/v1/users")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
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
                    callback.onSuccess("Sign up successful", "", "");

                } else{
                    callback.onFailure(new IOException("Sign up failed"));
                }
            }
        });
    }

    public void getUserIdFromAPI(String email, String apiKey, UserCallback callback){

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

                        callback.onSuccess(id, fullName, image);

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

    public void editUserProfileToAPI(String emailUser, String name, String surname, String email, String password, String apiKey, UserCallback callback) {

        getUserIdFromAPI(emailUser, apiKey, new UserDAO.UserCallback() {
            @Override
            public void onSuccess(String id, String string, String p1) {

                OkHttpClient client = new OkHttpClient().newBuilder().build();
                MediaType mediaType = MediaType.parse("application/json");

                JSONObject jsonBody = new JSONObject();
                try {
                    jsonBody.put("name", name);
                    jsonBody.put("last_name", surname);
                    jsonBody.put("email", email);
                    jsonBody.put("password", password);
                    jsonBody.put("image", "https://balandrau.salle.url.edu/i3/repositoryimages/photo/47601a8b-dc7f-41a2-a53b-19d2e8f54cd0.png");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                RequestBody body = RequestBody.create(mediaType, jsonBody.toString());
                Request request = new Request.Builder()
                        .url("https://balandrau.salle.url.edu/i3/socialgift/api/v1/users")
                        .method("PUT", body)
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
                        if (response.isSuccessful()) {

                            callback.onSuccess(email, "", "");

                        } else {
                            callback.onFailure(new IOException("User edited failed"));
                        }
                    }

                });
            }

            @Override
            public void onFailure(IOException e) {

            }
        });

    }

}
