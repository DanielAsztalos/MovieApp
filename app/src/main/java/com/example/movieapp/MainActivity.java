package com.example.movieapp;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.movieapp.database.AppDatabase;
import com.example.movieapp.fragments.HomeScreenFragment;
import com.example.movieapp.fragments.LoginFragment;
import com.example.movieapp.fragments.RegisterFragment;
import com.example.movieapp.helpers.DatabaseTransactions;
import com.example.movieapp.helpers.Encrypt;
import com.example.movieapp.models.User;
import com.example.movieapp.tasks.LoginAsyncTask;
import com.example.movieapp.tasks.RegisterAsyncTask;

public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = getSharedPreferences("LOGGED_USER", Context.MODE_PRIVATE);
        if(!sharedPreferences.getString("id", "").equals("")){
            Intent intent = new Intent(getApplicationContext(), MainSectionActivity.class);
            startActivity(intent);
        }

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
        user.setPathToProfilePic("drawable://" + R.drawable.blank_profile_picture_973460_1280);

        RegisterAsyncTask task = new RegisterAsyncTask(this);
        task.execute(user);
    }
}
