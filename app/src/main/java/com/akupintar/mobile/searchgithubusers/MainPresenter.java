package com.akupintar.mobile.searchgithubusers;

import android.content.Context;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainPresenter {
    private static final String TAG = "MainPresenter";
    Context context;
    MainView view;

    public MainPresenter(Context context, MainView view) {
        this.context = context;
        this.view = view;
    }

    void getUser(String keyword, String page, Boolean loading){
        if (loading) view.showLoading();
        AndroidNetworking.get("https://api.github.com/search/users")
                .addQueryParameter("q", keyword)
                .addQueryParameter("page", page)
                .addQueryParameter("per_page", "100")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "onResponse: " + response);
                        if (response.has("items")){
                            Log.d(TAG, "onResponse: in");
                            Gson gson = new Gson();
                            try {
                                JSONArray items = response.getJSONArray("items");
                                ArrayList<User> users =gson.fromJson(items.toString(), new TypeToken<ArrayList<User>>() {
                                }.getType());
                                if (users.size() == 0)view.showMessage("No search result found");
                                else view.showUsers(users);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            view.showError();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        Log.e(TAG, "onError: ",error );
                        view.showError();
                    }
                });
    }
}
