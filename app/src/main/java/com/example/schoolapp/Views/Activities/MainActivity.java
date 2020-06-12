package com.example.schoolapp.Views.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.schoolapp.Adapters.CourseAdapter;
import com.example.schoolapp.Adapters.SubjectAdapter;
import com.example.schoolapp.Models.Entities.Course;
import com.example.schoolapp.R;
import com.example.schoolapp.Models.Entities.Subject;
import com.example.schoolapp.SendNotification.Token;
import com.example.schoolapp.Views.Fragments.ContactFragment;
import com.example.schoolapp.Views.Fragments.HomeFragment;
import com.example.schoolapp.Views.Fragments.LecturesFragment;
import com.example.schoolapp.Views.Fragments.MessageFragment;
import com.example.schoolapp.Views.Fragments.TestsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;

import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.schoolapp.Views.Activities.GroupChatActivity.X;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference reference;

    private String currentUserID;


    private AppBarConfiguration mAppBarConfiguration;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser != null)
            currentUserID = firebaseAuth.getCurrentUser().getUid();


        reference = FirebaseDatabase.getInstance().getReference();


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);


        navigationView.setItemIconTintList(null);


        //  onNavigationItemSelected(navigationView.getMenu().getItem(0));

        //  getSupportActionBar().setTitle(R.style.FontFamily);

        //   drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_noun_hamburger_menu_2832820);


        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);


        if (X == true) {
            Fragment selectedFragment = new MessageFragment();
            bottomNav.setSelectedItemId(R.id.item4);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();

        } else if (X == false) {
            Fragment selectedFragment = new HomeFragment();
            bottomNav.setSelectedItemId(R.id.item1);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
        }


        if (ProfilePersonActivity.state.equals("Friend")) {
            Fragment selectedFragment = new MessageFragment();
            bottomNav.setSelectedItemId(R.id.item4);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();

        }

        if (ProfilePersonActivity.state.equals("Groups")) {
            Fragment selectedFragment = new MessageFragment();
            bottomNav.setSelectedItemId(R.id.item4);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();

        }


        if (ProfilePersonActivity.state.equals("Chat")) {

            Fragment selectedFragment = new MessageFragment();
            bottomNav.setSelectedItemId(R.id.item4);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();

        }

        if (ProfilePersonActivity.state.equals("Test")) {

            Fragment selectedFragment = new TestsFragment();
            bottomNav.setSelectedItemId(R.id.item2);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();

        }


        if (ProfilePersonActivity.state.equals("RetriveLectures")) {

            Fragment selectedFragment = new LecturesFragment();
            bottomNav.setSelectedItemId(R.id.item3);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();

        }


    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem Item) {
            Fragment selectedFragment = null;
            switch (Item.getItemId()) {
                case R.id.item1:
                    selectedFragment = new HomeFragment();
                    break;
                case R.id.item2:
                    selectedFragment = new TestsFragment();
                    break;
                case R.id.item3:
                    selectedFragment = new LecturesFragment();
                    break;
                case R.id.item4:
                    selectedFragment = new MessageFragment();
                    break;

            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            return true;
        }

    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem item = menu.findItem(R.id.menu_notification);

        MenuItem item2 = menu.findItem(R.id.menu_image_profile);

        View v2 = item2.getActionView();
        CircleImageView cr = v2.findViewById(R.id.crcProfile);

        RetriveUserInfo(cr);

        reference.child("Users").child(currentUserID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


        View v = item.getActionView();
        ImageView tv = v.findViewById(R.id.imgNotification);

        reference.child("Users").child(currentUserID).child("notification")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue().toString().equals("online"))
                            tv.setImageResource(R.drawable.ic_notification);
                        else
                            tv.setImageResource(R.drawable.ic_bell);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                reference.child("Users").child(currentUserID).child("notification")
                        .setValue("offline");

                Intent intent = new Intent(MainActivity.this, LastNewsActivity.class);
                startActivity(intent);

            }
        });


        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        int id = menuItem.getItemId();


        if (id == R.id.nav_upload) {
            Intent intent = new Intent(this, UploadLecturesActivity.class);
            this.startActivity(intent);
        } else if (id == R.id.nav_insert_subject) {
            Intent intent = new Intent(this, InsertSubjectsActivity.class);
            this.startActivity(intent);
        } else if (id == R.id.nav_logout) {
            updateUserLastSeen("offline");
            firebaseAuth.signOut();
            Intent intent = new Intent(this, SigninActivity.class);
            this.startActivity(intent);
        } else if (id == R.id.nav_setting) {
            Intent intent = new Intent(this, SettingActivity.class);
            this.startActivity(intent);
        } else if (id == R.id.nav_insert_test) {
            Intent intent = new Intent(this, InsertNearlyTestSubjectActivity.class);
            this.startActivity(intent);
        } else if (id == R.id.nav_insert_mark) {
            Intent intent = new Intent(this, InsertMarksSubjectActivity.class);
            this.startActivity(intent);
        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();


        firebaseUser = firebaseAuth.getCurrentUser();

        updateToken();

        if (firebaseUser == null) {
            Intent intent = new Intent(this, SigninActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();

        } else {

            //     updateUserLastSeen("online");
            VerifyUserExistance();
        }
    }


    @Override
    protected void onStop() {
        super.onStop();

        firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser != null) {
            //updateUserLastSeen("offline");
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser != null) {
            // updateUserLastSeen("offline");
        }

    }

    private void VerifyUserExistance() {
        currentUserID = firebaseAuth.getCurrentUser().getUid();
        reference.child("Users").child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if ((!dataSnapshot.child("name").getValue().toString().equals(""))) {
                    //       Toast.makeText(MainActivity.this, "Welcome", Toast.LENGTH_SHORT).show();
                } else {
                    toSetting();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void toSetting() {
        Intent intent = new Intent(this, SettingActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }


    private void RetriveUserInfo(CircleImageView circleImageView) {
        reference.child("Users").child(currentUserID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if ((dataSnapshot.exists()) && (dataSnapshot.hasChild("image"))) {

                            String retriveprofileImage = dataSnapshot.child("image").getValue().toString();


                            if (!retriveprofileImage.equals(""))
                                Picasso.get().load(retriveprofileImage).into(circleImageView);


                        } else {

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
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


    public void updateToken() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String refreshToken = FirebaseInstanceId.getInstance().getToken();
        Token token = new Token(refreshToken);
        FirebaseDatabase.getInstance().getReference("Tokens").child(firebaseUser.getUid()).setValue(token);

    }


}
