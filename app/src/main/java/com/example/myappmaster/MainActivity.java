package com.example.myappmaster;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout drawerLayout;
    BottomNavigationView bottomNavigationView;
    FragmentManager fragmentManager;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.navigation_drawer);
        navigationView.setNavigationItemSelectedListener(this);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setBackground(null);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId= item.getItemId();
                if(itemId==R.id.nav_profile){
                    openFragment(new ProfileFragment());
                    return true;
                }else if(itemId==R.id.nav_likeddiets){
                    LikedVideoFragment fragment = new LikedVideoFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString(LikedVideoFragment.TYPE, VideoType.DIET.toString());
                    fragment.setArguments(bundle);
                    openFragment(fragment);
                    return true;
                }else if(itemId==R.id.nav_home){
                    openFragment(new DashboardFragment());
                    return true;
                }else if(itemId==R.id.nav_likedworkouts){
                    LikedVideoFragment fragment = new LikedVideoFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString(LikedVideoFragment.TYPE, VideoType.WORKOUT.toString());
                    fragment.setArguments(bundle);
                    openFragment(fragment);
                    return true;
                }else if(itemId==R.id.nav_social){
                    openFragment(new SocialFragment());
                    return true;
                }
                return false;
            }
        });
        fragmentManager = getSupportFragmentManager();
        openFragment(new ProfileFragment());

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, new DashboardFragment());
        transaction.commit();


    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId= item.getItemId();
        if(itemId == R.id.nav_logout){
            openFragment(new LogoutFragment());
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{

            super.onBackPressed();
        }
    }
    private void openFragment(Fragment fragment){
        FragmentTransaction transaction= fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container,fragment);
        transaction.commit();
    }
}
