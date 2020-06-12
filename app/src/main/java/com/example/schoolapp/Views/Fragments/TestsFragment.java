package com.example.schoolapp.Views.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.schoolapp.Adapters.SectionPagerAdapter;
import com.example.schoolapp.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class TestsFragment extends Fragment {

    ViewPager viewPagerTests;
    TabLayout tabForTest;

    CircleImageView imgFrgUserImage;
    TextView txtFrgTestUserName;

    private DatabaseReference UserRef;

    private String currentUserID;
    private FirebaseAuth firebaseAuth;

    public TestsFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tests, container, false);


        firebaseAuth = FirebaseAuth.getInstance();
        currentUserID = firebaseAuth.getCurrentUser().getUid();


        viewPagerTests = view.findViewById(R.id.viewPagerTests);
        tabForTest = view.findViewById(R.id.tabForTest);

        imgFrgUserImage = view.findViewById(R.id.imgFrgUserImage);
        txtFrgTestUserName = view.findViewById(R.id.txtFrgTestUserName);

        UserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID);

        UserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(!dataSnapshot.child("image").getValue().toString().equals(""))
                Picasso.get().load(dataSnapshot.child("image").getValue().toString()).into(imgFrgUserImage);
                txtFrgTestUserName.setText(dataSnapshot.child("name").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setUpViewPager(viewPagerTests);
        tabForTest.setupWithViewPager(viewPagerTests);

    }

    private void setUpViewPager(ViewPager viewPagerTests) {
        SectionPagerAdapter adapter = new SectionPagerAdapter(getChildFragmentManager());

        String a = getString(R.string.nearly_test);
        String b = getString(R.string.marks);



        adapter.addFragment(new NearlyTestsFragment(), a);
        adapter.addFragment(new MarksFragment(), b);


        viewPagerTests.setAdapter(adapter);

    }
}
