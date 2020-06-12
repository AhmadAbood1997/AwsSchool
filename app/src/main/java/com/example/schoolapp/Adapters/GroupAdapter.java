package com.example.schoolapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolapp.Models.Entities.Group;
import com.example.schoolapp.Models.Entities.User;
import com.example.schoolapp.R;
import com.example.schoolapp.ViewHolders.ViewHolderGroups;
import com.example.schoolapp.ViewHolders.ViewHolderUsers;
import com.example.schoolapp.Views.Activities.GroupChatActivity;
import com.example.schoolapp.Views.Activities.ProfilePersonActivity;
import com.example.schoolapp.Views.Fragments.GroupFragment;

import java.util.List;

public class GroupAdapter extends RecyclerView.Adapter<ViewHolderGroups> {
    private Context context;
    private List<Group> groups;


    public GroupAdapter(Context context, List<Group> groups) {
        this.context = context;
        this.groups = groups;
    }
    @NonNull
    @Override
    public ViewHolderGroups onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_image_group, parent, false);
        return new ViewHolderGroups(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderGroups viewHolderGroups, int position) {

        final Group group = groups.get(position);

        viewHolderGroups.geTxtGroupName().setText(group.getNameGroup());
        viewHolderGroups.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               ProfilePersonActivity.state = "Groups";
               Intent intent = new Intent(context, GroupChatActivity.class);
                intent.putExtra("groupName",group.getNameGroup());
                context.startActivity(intent);
            }
        });

    }
    @Override
    public int getItemCount() {
        return groups.size();
    }


    public void setData(List<Group> groups) {
        this.groups = groups;
        notifyDataSetChanged();
    }

}
