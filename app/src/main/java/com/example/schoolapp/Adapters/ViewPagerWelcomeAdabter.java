package com.example.schoolapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.schoolapp.R;
import com.example.schoolapp.Models.Entities.WelcomeItem;

import java.util.List;

public class ViewPagerWelcomeAdabter extends PagerAdapter {


    Context context;
    List<WelcomeItem> welcomeItems;


    public ViewPagerWelcomeAdabter(Context context, List<WelcomeItem> welcomeItems) {
        this.context = context;
        this.welcomeItems = welcomeItems;
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View layoutScreen = inflater.inflate(R.layout.item_welcome, null);
        ImageView imageView = layoutScreen.findViewById(R.id.ImageItemWelcome);
        TextView description = layoutScreen.findViewById(R.id.DescriptionItemWelcome);

        description.setText(welcomeItems.get(position).getDescription());
        imageView.setImageResource(welcomeItems.get(position).getImage());




        container.addView(layoutScreen);
        return  layoutScreen;


    }

    @Override
    public int getCount() {
        return welcomeItems.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
