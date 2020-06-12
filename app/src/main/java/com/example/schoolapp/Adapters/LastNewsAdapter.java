package com.example.schoolapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolapp.Models.Entities.LastNews;
import com.example.schoolapp.R;
import com.example.schoolapp.ViewHolders.ViewHolderLastNews;

import java.util.List;

public class LastNewsAdapter extends RecyclerView.Adapter<ViewHolderLastNews> {
    private Context context;
    private List<LastNews> lastNewsList;


    public LastNewsAdapter(Context context, List<LastNews> lastNewsList) {
        this.context = context;
        this.lastNewsList = lastNewsList;
    }



    @NonNull
    @Override
    public ViewHolderLastNews onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_last_news, parent, false);
        return new ViewHolderLastNews(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderLastNews viewHolderLastNews, int position) {

        LastNews lastNews = lastNewsList.get(position);

        viewHolderLastNews.getTxtTitleLastNews().setText(lastNews.getTitle());

        viewHolderLastNews.getTxtContainLastNews().setText(lastNews.getContain());

        viewHolderLastNews.getTxtDateLastNews().setText(lastNews.getDate());


    }


    @Override
    public int getItemCount() {
        return lastNewsList.size();
    }


    public void setData(List<LastNews> lastNews) {
        this.lastNewsList = lastNews;
        notifyDataSetChanged();
    }

}
