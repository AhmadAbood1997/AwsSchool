package com.example.schoolapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolapp.Models.Entities.User;
import com.example.schoolapp.R;
import com.example.schoolapp.ViewHolders.ViewHolderUsers;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<ViewHolderUsers> {
    private Context context;
    private List<User> users;


    public UsersAdapter(Context context, List<User> users) {
        this.context = context;
        this.users = users;
    }


    @NonNull
    @Override
    public ViewHolderUsers onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_image_chat, parent, false);
        return new ViewHolderUsers(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderUsers viewHolderUsers, int position) {

        final User user = users.get(position);

        viewHolderUsers.getTxtNameUser().setText(user.getImage());

    }


    @Override
    public int getItemCount() {
        return users.size();
    }


    public void setData(List<User> users) {
        this.users = users;
        notifyDataSetChanged();
    }

}
