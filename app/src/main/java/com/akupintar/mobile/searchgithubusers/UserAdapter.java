package com.akupintar.mobile.searchgithubusers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<User> githubUsers;
    Context context;

    UserAdapter(Context context, ArrayList<User> githubUsers){
        this.githubUsers = githubUsers;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_user, parent, false);
        vh = new UserViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        User githubUser = githubUsers.get(position);
        UserViewHolder uvh = (UserViewHolder) viewHolder;
        uvh.tvUserName.setText(githubUser.getLogin());
        Glide.with(context).load(githubUser.getAvatarUrl()).into(uvh.ivAvatar);
    }

    @Override
    public int getItemCount() {
        return githubUsers.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivAvatar) ImageView ivAvatar;
        @BindView(R.id.tvUserName) TextView tvUserName;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setClickable(true);
        }
    }
}
