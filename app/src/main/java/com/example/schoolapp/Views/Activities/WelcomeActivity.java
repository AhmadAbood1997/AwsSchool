package com.example.schoolapp.Views.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.example.schoolapp.Adapters.ViewPagerWelcomeAdabter;
import com.example.schoolapp.R;
import com.example.schoolapp.Models.Entities.WelcomeItem;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class WelcomeActivity extends AppCompatActivity {
    private ViewPager screenPager;
    private ViewPagerWelcomeAdabter ViewPagerWelcomeAdabter;
    private List<WelcomeItem> welcomeItems;
    private TabLayout tabLayout;
    private Button btnGetStarted;
    private int postition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (restorePrefData()) {
            Intent intent = new Intent(getApplicationContext(), SigninActivity.class);
            startActivity(intent);
            finish();
        }


        setContentView(R.layout.activity_welcome);


        tabLayout = findViewById(R.id.tabForWelcome);
        btnGetStarted = findViewById(R.id.btnGetStarted);

        String a =  getString(R.string.we_have_a_lot_of_training_courses);
        String b =  getString(R.string.you_can_study_every_thing_from_your_house);
        String c =  getString(R.string.we_will_help_you_in_all_subjects);

        welcomeItems = new ArrayList<>();
        welcomeItems.add(new WelcomeItem(a, R.drawable.education2));
        welcomeItems.add(new WelcomeItem(b, R.drawable.education3));
        welcomeItems.add(new WelcomeItem(c, R.drawable.education4));


        screenPager = findViewById(R.id.viewPagerFoeWelcome);
        ViewPagerWelcomeAdabter = new ViewPagerWelcomeAdabter(this, welcomeItems);
        screenPager.setAdapter(ViewPagerWelcomeAdabter);


        tabLayout.setupWithViewPager(screenPager);

        btnGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postition = screenPager.getCurrentItem();

                if (postition < welcomeItems.size()) {
                    postition++;
                    screenPager.setCurrentItem(postition);
                }

                if (postition == welcomeItems.size() - 1) {
                    btnGetStarted.setText(R.string.lets_go);
                }

                if (postition == 3) {
                    Intent intent = new Intent(WelcomeActivity.this, SigninActivity.class);
                    startActivity(intent);
                    savePrefsData();
                    finish();
                }

            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == welcomeItems.size() - 1) {
                    btnGetStarted.setText(R.string.lets_go);
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }

    private boolean restorePrefData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myprefs", MODE_PRIVATE);
        Boolean isSignActivityOpned = pref.getBoolean("SignInOpend", false);
        return isSignActivityOpned;
    }

    private void savePrefsData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myprefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("SignInOpend", true);
        editor.commit();
    }
}
