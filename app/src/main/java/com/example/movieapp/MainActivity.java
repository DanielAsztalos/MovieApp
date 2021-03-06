package com.example.movieapp;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.movieapp.fragments.HomeScreenFragment;
import com.example.movieapp.fragments.LoginFragment;
import com.example.movieapp.fragments.RegisterFragment;
import com.example.movieapp.helpers.Encrypt;
import com.example.movieapp.models.User;
import com.example.movieapp.tasks.LoginAsyncTask;
import com.example.movieapp.tasks.RegisterAsyncTask;

public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // if user is logged in redirect to the MainSectionActivity
        SharedPreferences sharedPreferences = getSharedPreferences("LOGGED_USER", Context.MODE_PRIVATE);
        if(!sharedPreferences.getString("id", "").equals("")){
            Intent intent = new Intent(getApplicationContext(), MainSectionActivity.class);
            startActivity(intent);
        }

        // load HomeScreenFragment
        Fragment newFragment = new HomeScreenFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_container, newFragment);
        transaction.addToBackStack(null);

        transaction.commit();

    }

    // load login screen
    public void toLoginScreen(View view) {
        loadFragment(new LoginFragment());
    }

    // load register screen
    public void toRegisterScreen(View view) {
        loadFragment(new RegisterFragment());
    }

    // this helper function gets a fragment and loads it
    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }

    // if login action is initiated
    public void loginClicked(View view) {
        String username = ((EditText) findViewById(R.id.et_username_login)).getText().toString();
        if(username.length() == 0) {
            Toast.makeText(this, R.string.usernameEmpty, Toast.LENGTH_LONG).show();
            return;
        }

        String password = ((EditText) findViewById(R.id.et_pass_login)).getText().toString();
        if(password.length() == 0) {
            Toast.makeText(this, R.string.passwordEmpty, Toast.LENGTH_LONG).show();
            return;
        }

        LoginAsyncTask task = new LoginAsyncTask(this, username, Encrypt.md5(password));
        task.execute();
    }

    // register initiated
    public void registerClicked(View view) {
        String username = ((EditText) findViewById(R.id.et_username_register)).getText().toString();
        if(username.length() == 0) {
            Toast.makeText(this, R.string.usernameEmpty, Toast.LENGTH_LONG).show();
            return;
        }

        String email = ((EditText) findViewById(R.id.et_email)).getText().toString();
        if(email.length() == 0) {
            Toast.makeText(this, R.string.email, Toast.LENGTH_LONG).show();
            return;
        }

        String password = ((EditText) findViewById(R.id.et_pass_register)).getText().toString();
        if(password.length() < 6) {
            Toast.makeText(this, R.string.passShort, Toast.LENGTH_LONG).show();
            return;
        }

        String repeat = ((EditText) findViewById(R.id.et_repeat_pass)).getText().toString();
        if(!repeat.equals(password)) {
            Toast.makeText(this, R.string.passNoMatch, Toast.LENGTH_LONG).show();
            return;
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(Encrypt.md5(password));
        user.setPathToProfilePic("");

        RegisterAsyncTask task = new RegisterAsyncTask(this);
        task.execute(user);
    }
}
