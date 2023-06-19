package com.example.socialgiftprpr.Persistence;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.socialgiftprpr.Lists.Gifts.ProductModel;
import com.example.socialgiftprpr.Share.UserModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ProductDAO {

    public ProductDAO(){
    }

    public interface ProductCallback {
        void onSuccess(ProductModel productModel);
        void onFailure(IOException e);
    }

    public void getProductData(String urlProduct, String apiKey, ProductCallback callback) {

        OkHttpClient client = new OkHttpClient().newBuilder().build();
        Request request = new Request.Builder()
                .url(urlProduct)
                .method("GET", null)
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

                        JSONObject jsonObject = new JSONObject(responseBody);
                        int id = jsonObject.getInt("id");
                        String name = jsonObject.getString("name");
                        String description = jsonObject.getString("description");
                        String link = jsonObject.getString("link");
                        String photo = jsonObject.getString("photo");
                        double price = jsonObject.getDouble("price");

                        callback.onSuccess(new ProductModel(id, name, description, link, photo, price));

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

}
