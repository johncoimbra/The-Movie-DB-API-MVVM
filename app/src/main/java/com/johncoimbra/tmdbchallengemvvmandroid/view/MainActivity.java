package com.johncoimbra.tmdbchallengemvvmandroid.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.johncoimbra.tmdbchallengemvvmandroid.R;
import com.johncoimbra.tmdbchallengemvvmandroid.fragments.MovieFavoriteFragment;
import com.johncoimbra.tmdbchallengemvvmandroid.fragments.MovieTopFragment;
import com.johncoimbra.tmdbchallengemvvmandroid.fragments.MoviePopularFragment;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private Fragment selectedFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottom_menu);
        bottomNavigationView.setOnItemSelectedListener(navListener);
        //Iniciando o app com o fragment "MovieTopFragment"
        selectedFragment = new MovieTopFragment();
        addFragment(selectedFragment);
    }

    private NavigationBarView.OnItemSelectedListener navListener = new NavigationBarView.OnItemSelectedListener() {
        @SuppressLint("NonConstantResourceId")
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_home:
                    selectedFragment = new MovieTopFragment();
                    break;

                case R.id.nav_popular:
                    selectedFragment = new MoviePopularFragment();
                    break;

                case R.id.nav_favorite:
                    selectedFragment = new MovieFavoriteFragment();
                    break;
            }
            addFragment(selectedFragment);
            return true;
        }
    };

    public void addFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();
    }
}