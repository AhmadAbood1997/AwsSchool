package com.example.schoolapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolapp.Models.Entities.User;
import com.example.schoolapp.R;
import com.example.schoolapp.ViewHolders.ViewHolderFindFriend;
import com.example.schoolapp.ViewHolders.ViewHolderUsers;
import com.example.schoolapp.Views.Activities.ProfilePersonActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FindFriendAdapter extends RecyclerView.Adapter<ViewHolderFindFriend> {
    private Context context;
    private List<User> users;


    public FindFriendAdapter(Context context, List<User> users) {
        this.context = context;
        this.users = users;
    }


    @NonNull
    @Override
    public ViewHolderFindFriend onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_image_find_friend, parent, false);
        return new ViewHolderFindFriend(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderFindFriend viewHolderFindFriend, int position) {

        final User user = users.get(position);

        viewHolderFindFriend.getTxtFindFriendName().setText(user.getName());
        viewHolderFindFriend.getTxtFindFriendStatus().setText(user.getStatus());

        if (!user.getImage().equals(""))
        Picasso.get().load(user.getImage()).into(viewHolderFindFriend.getImgFindFriendProfile());

        viewHolderFindFriend.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProfilePersonActivity.state = "FindFriend";
                Intent intent = new Intent(context, ProfilePersonActivity.class);
                intent.putExtra("name",user.getName());
                intent.putExtra("status",user.getStatus());
                intent.putExtra("image",user.getImage());
                intent.putExtra("userid",user.getUid());
                intent.putExtra("token",user.getDevice_token());
                context.startActivity(intent);
            }
        });

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
