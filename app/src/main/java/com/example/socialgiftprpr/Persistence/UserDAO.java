package com.example.socialgiftprpr.Persistence;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UserDAO {

    public UserDAO(){
    }

    public interface UserCallback {
        void onSuccess(String accessToken);
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
                        callback.onSuccess(accessToken);

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

    public void signup(String name, String surname, String email, String password, UserCallback callback){

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
                    callback.onSuccess("Sign up successful");

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
                        callback.onSuccess(id);

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

    /*
    public String getUserIdByEmailFromAPI() {

        String[] id = new String[1];

        SharedPreferences sharedPreferences = context.getSharedPreferences("MiArchivoPreferencias", Context.MODE_PRIVATE);
        String mail = sharedPreferences.getString("email", null);

        String url = "https://balandrau.salle.url.edu/i3/socialgift/api/v1/users/search?s=" + mail;

        RequestQueue queue = Volley.newRequestQueue(context);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try{
                    JSONObject jsonObject = response.getJSONObject(0);
                    id[0] = jsonObject.getString("id");

                    System.out.println("ID " + id[0]);

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

        return id[0];
    }

     */

}
