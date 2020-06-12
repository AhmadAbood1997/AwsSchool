package com.example.schoolapp.Views.Fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.schoolapp.Adapters.SectionPagerAdapter;
import com.example.schoolapp.Adapters.SubjectAdapter;
import com.example.schoolapp.Adapters.UsersAdapter;
import com.example.schoolapp.Models.Entities.Subject;
import com.example.schoolapp.Models.Entities.User;
import com.example.schoolapp.R;
import com.example.schoolapp.Views.Activities.ProfilePersonActivity;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class MessageFragment extends Fragment {

    ViewPager viewPagerMessage;
    TabLayout tabForMessage;


    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference reference;

    private String currentUserID;


    public MessageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_message, container, false);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference();

        if (firebaseUser != null)
            currentUserID = firebaseAuth.getCurrentUser().getUid();



        viewPagerMessage = view.findViewById(R.id.viewPagerMessage);
        tabForMessage = view.findViewById(R.id.tabForMessage);

        return view;

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setUpViewPager(viewPagerMessage);
        tabForMessage.setupWithViewPager(viewPagerMessage);


        if (ProfilePersonActivity.state.equals("Groups")) {
            TabLayout.Tab tab = tabForMessage.getTabAt(1);
            tab.select();
        }

        if (ProfilePersonActivity.state.equals("Friend")) {
            TabLayout.Tab tab = tabForMessage.getTabAt(2);
            tab.select();
        }


        setupTabIcons();


    }


    private void setupTabIcons() {


        for (int i = 0; i < tabForMessage.getTabCount(); i++) {
            TabLayout.Tab tab = tabForMessage.getTabAt(i);
            switch (i) {
                case 0:
                    if (tab != null) {
                        tab.setIcon(R.drawable.ic_messagechat);
                    }
                    break;
                case 1:
                    if (tab != null) {
                        tab.setIcon(R.drawable.ic_group);
                    }
                    break;
                case 2:
                    if (tab != null) {
                        tab.setIcon(R.drawable.ic_friends);
                    }
                    break;
                case 3:
                    if (tab != null) {
                        tab.setIcon(R.drawable.ic_friendrequest);
                    }
                    break;
            }
            if (tab != null) tab.setCustomView(R.layout.custom_tab);
        }


    }

    private void setUpViewPager(ViewPager viewPagerTests) {
        SectionPagerAdapter adapter = new SectionPagerAdapter(getChildFragmentManager());


        adapter.addFragment(new ChatFragment(), "");
        adapter.addFragment(new GroupFragment(), "");
        adapter.addFragment(new ContactFragment(), "");
        adapter.addFragment(new RequestFragment(), "");


        viewPagerTests.setAdapter(adapter);

    }


    @Override
    public void onStart() {
        super.onStart();

        updateUserLastSeen("online");

    }


    @Override
    public void onStop() {
        super.onStop();

        firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser != null) {
            updateUserLastSeen("offline");
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();


        firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser != null) {
            updateUserLastSeen("offline");
        }
    }

    private void updateUserLastSeen(String state) {

        String saveCurrentTime;
        String saveCurrentDate;

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
        saveCurrentTime = currentTime.format(calendar.getTime());


        HashMap<String, Object> onlineState = new HashMap<>();
        onlineState.put("time", saveCurrentTime);
        onlineState.put("date", saveCurrentDate);
        onlineState.put("state", state);

        reference.child("Users").child(currentUserID).child("userState")
                .updateChildren(onlineState);

    }


}
