package com.example.movieapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;

import com.example.movieapp.fragments.HomeScreenFragment;
import com.example.movieapp.fragments.LoginFragment;
import com.example.movieapp.fragments.RegisterFragment;

public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Fragment newFragment = new HomeScreenFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_container, newFragment);
        transaction.addToBackStack(null);

        transaction.commit();

    }

    public void toLoginScreen(View view) {
        loadFragment(new LoginFragment());
    }

    public void toRegisterScreen(View view) {
        loadFragment(new RegisterFragment());
    }

    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }
}
