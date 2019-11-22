package com.akupintar.mobile.searchgithubusers;

import java.util.ArrayList;

public interface MainView {
    void showLoading();
    void hideLoading();
    void showError();
    void showMessage(String message);
    void showUsers(ArrayList<User> data);
}
