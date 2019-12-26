package com.example.movieapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.movieapp.fragments.FavoritesFragment;
import com.example.movieapp.fragments.HomeFragment;
import com.example.movieapp.fragments.InCinemasFragment;
import com.example.movieapp.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainSectionActivity extends FragmentActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()){
                        case R.id.action_profile:
                            loadFragment(new ProfileFragment());
                            break;
                        case R.id.action_favs:
                            loadFragment(new FavoritesFragment());
                            break;
                        case R.id.action_theatres:
                            loadFragment(new InCinemasFragment());
                            break;
                        default:
                            loadFragment(new HomeFragment());
                            break;
                    }

                    return true;
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_section);

        loadFragment(new HomeFragment());

        BottomNavigationView navigationView = findViewById(R.id.bnv_nav);
        navigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
    }

    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_container2, fragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }
}
