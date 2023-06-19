package com.example.socialgiftprpr.Persistence;

import com.example.socialgiftprpr.Lists.Gifts.GiftModel;
import com.example.socialgiftprpr.Lists.Gifts.ProductModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GiftDAO {

    private ProductModel product;

    public interface GiftCallback {
        void onSuccess(List<GiftModel> giftEvents, String listName, int id);
        void onFailure(IOException e);
    }

    public void addGiftToAPI(String apiKey, int wishlistId, String productUrl, int priority, GiftDAO.GiftCallback callback){

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

    public void getAllGiftsFromAPI(int wishlistId, String apiKey, GiftDAO.GiftCallback callback) {

        OkHttpClient client = new OkHttpClient().newBuilder().build();
        Request request = new Request.Builder()
                .url("https://balandrau.salle.url.edu/i3/socialgift/api/v1/wishlists/" + wishlistId)
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
                        List<GiftModel> gifts = new ArrayList<>();
                        String responseBody = response.body().string();
                        JSONObject jsonObject = new JSONObject(responseBody);

                        int listId = jsonObject.getInt("id");
                        String listName = jsonObject.getString("name");
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

                            boolean finalState = state;
                            gifts.add(new GiftModel(id, wishlistId, productUrl, priority, finalState, product));

                            ProductDAO productDAO = new ProductDAO();

                            int finalJ = j;
                            productDAO.getProductData(productUrl, apiKey, new ProductDAO.ProductCallback() {
                                @Override
                                public void onSuccess(ProductModel productModel) {
                                    //System.out.println("PRODUCT MODEL: " + productModel);
                                   /* GiftModel gift = gifts.get(finalJ);
                                    gift.setProductInfo(productModel);
                                    gifts.set(finalJ, gift);

                                    */
                                    GiftModel gift = gifts.get(finalJ);
                                    gift.setProductInfo(productModel);
                                    gifts.set(finalJ, gift);

                                    // Verificar si todos los regalos han sido actualizados con el ProductModel
                                    if (isAllGiftsUpdated(gifts, jsonArray1.length())) {
                                        // Llamar al método onSuccess del callback con los regalos completos
                                        callback.onSuccess(gifts, listName, listId);
                                    }

                                }

                                @Override
                                public void onFailure(IOException e) {
                                    // Verificar si todos los regalos han sido actualizados con el ProductModel
                                    if (isAllGiftsUpdated(gifts, jsonArray1.length())) {
                                        // Llamar al método onSuccess del callback con los regalos completos
                                        callback.onSuccess(gifts, listName, listId);
                                    }
                                }
                            });

                            //gifts.add(new GiftModel(id, wishlistId, productUrl, priority, finalState, product));
                            System.out.println("ENTRA FOR");
                        }

                       // callback.onSuccess(gifts, listName, listId);

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

    private boolean isAllGiftsUpdated(List<GiftModel> gifts, int expectedSize) {
        for (GiftModel gift : gifts) {
            if (gift.getProductInfo() == null) {
                return false;
            }
        }
        return gifts.size() == expectedSize;
    }

    public void deleteGiftFromAPI(int idGift, String apiKey, GiftDAO.GiftCallback callback){

        OkHttpClient client = new OkHttpClient().newBuilder().build();
        Request request = new Request.Builder()
                .url("https://balandrau.salle.url.edu/i3/socialgift/api/v1/gifts/"+ idGift)
                .method("DELETE", null)
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
                    List<GiftModel> giftEvents = null;
                    String listName = null;
                    int id = 0;
                    callback.onSuccess(giftEvents, listName, id);

                } else {
                    callback.onFailure(new IOException("Gift delete failed"));
                }
            }

        });

    }

    public void editGiftToAPI(String id, String priority, String link, String apiKey, GiftCallback callback) {

        getGiftById(id, apiKey, new GiftDAO.GiftCallback() {
            @Override
            public void onSuccess(List<GiftModel> giftEvents, String string, int num) {
                GiftModel gift = giftEvents.get(0);

                OkHttpClient client = new OkHttpClient().newBuilder().build();
                MediaType mediaType = MediaType.parse("application/json");

                JSONObject jsonBody = new JSONObject();
                try {
                    jsonBody.put("id", gift.getGiftId());
                    jsonBody.put("wishlist_id", gift.getWishlistId());
                    jsonBody.put("product_url", link);
                    jsonBody.put("priority", String.valueOf(priority));
                    jsonBody.put("booked", gift.getSave());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                RequestBody body = RequestBody.create(mediaType, jsonBody.toString());
                Request request = new Request.Builder()
                        .url("https://balandrau.salle.url.edu/i3/socialgift/api/v1/gifts/" + id)
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

                            List<GiftModel> listEvents = null;
                            String listName = null;
                            int id = 0;
                            callback.onSuccess(listEvents, listName, id);

                        } else {
                            callback.onFailure(new IOException("List edited failed"));
                        }
                    }

                });
            }

            @Override
            public void onFailure(IOException e) {

            }
        });
    }

    public void getGiftById(String id, String apiKey, GiftCallback callback){

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

                        ArrayList<GiftModel> gifts = new ArrayList<>();
                        String responseBody = response.body().string();
                        JSONObject jsonObject = new JSONObject(responseBody);

                            int id = jsonObject.getInt("id");
                            int wishlistId = jsonObject.getInt("wishlist_id");
                            String productUrl = jsonObject.getString("product_url");
                            int priority = jsonObject.getInt("priority");
                            int booked = jsonObject.getInt("booked");
                            boolean state = false;
                            if(booked == 1){
                                state = true;
                            }
                            gifts.add(new GiftModel(id, wishlistId, productUrl, priority, state, null));

                        System.out.println("GIFTS: " + gifts);
                        String listName = null;
                        int idcall = 0;
                        callback.onSuccess(gifts, listName, idcall);

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


}
