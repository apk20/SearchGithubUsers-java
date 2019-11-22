package com.akupintar.mobile.searchgithubusers;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MainView {

    @BindView(R.id.pbUser)
    ProgressBar pbUser;
    @BindView(R.id.tvMessage)
    TextView tvMessage;
    @BindView(R.id.rlShow)
    RelativeLayout rlShow;
    @BindView(R.id.etSearchKeyword)
    EditText etSearchKeyword;
    @BindView(R.id.rvUserList)
    RecyclerView rvUserList;

    UserAdapter adapter;
    ArrayList<User> users = new ArrayList<>();
    MainPresenter presenter;
    Integer page = 1;
    Boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ButterKnife.bind(this);
        adapter = new UserAdapter(this,users);
        rvUserList.setAdapter(adapter);
        presenter = new MainPresenter(this,this);
        setViewBehavior();
    }

    void setViewBehavior(){
        etSearchKeyword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                users.clear();
                page = 1;
                presenter.getUser(s.toString(),String.valueOf(page),true);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        rvUserList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading && users.size()%100 == 0) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastVisibleItemPosition() == users.size() - 1) {
                        //bottom of list!
                        page++;
                        isLoading = true;
                        presenter.getUser(etSearchKeyword.getText().toString(),String.valueOf(page),false);
                    }
                }
            }
        });
    }

    @Override
    public void showLoading() {
        rvUserList.setVisibility(View.GONE);
        pbUser.setVisibility(View.VISIBLE);
        tvMessage.setVisibility(View.GONE);
        rlShow.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        rvUserList.setVisibility(View.VISIBLE);
        pbUser.setVisibility(View.GONE);
        tvMessage.setVisibility(View.GONE);
        rlShow.setVisibility(View.GONE);
    }

    @Override
    public void showError() {
        hideLoading();
        Toast.makeText(this,"Search Limit reached, \n search is not allowed for approximately the next 1 minute",Toast.LENGTH_LONG).show();
    }

    @Override
    public void showMessage(String message) {
        rvUserList.setVisibility(View.GONE);
        pbUser.setVisibility(View.GONE);
        tvMessage.setVisibility(View.VISIBLE);
        rlShow.setVisibility(View.VISIBLE);
        tvMessage.setText(message);
    }

    @Override
    public void showUsers(ArrayList<User> data) {
        users.addAll(data);
        adapter.notifyDataSetChanged();
        hideLoading();
        isLoading = false;
    }
}
