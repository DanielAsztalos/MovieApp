package com.example.movieapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import com.esafirm.imagepicker.model.Image;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ReturnMode;
import com.example.movieapp.dialogs.ChangePassDialogFragment;
import com.example.movieapp.fragments.FavoritesFragment;
import com.example.movieapp.fragments.HomeFragment;
import com.example.movieapp.fragments.InCinemasFragment;
import com.example.movieapp.fragments.ProfileFragment;
import com.example.movieapp.services.NowPlayingIntentService;
import com.example.movieapp.tasks.ChangeProfileImageAsyncTask;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Calendar;
import java.util.List;

public class MainSectionActivity extends FragmentActivity {

    // define fragments to load when a navigation item is selected
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

        SharedPreferences sharedPreferences = getSharedPreferences("LOGGED_USER", Context.MODE_PRIVATE);

        // check if a NowPlayingService is running
        boolean alarmUp = (PendingIntent.getService(this, 0,
                new Intent(this, NowPlayingIntentService.class),
                PendingIntent.FLAG_NO_CREATE) != null);

        // if not then start one
        if(!alarmUp) {
            Intent intent = new Intent(this, NowPlayingIntentService.class);
            PendingIntent pintent = PendingIntent.getService(this, 0, intent, 0);
            AlarmManager alarm = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
            alarm.setRepeating(AlarmManager.RTC, Calendar.getInstance().getTimeInMillis(), 12 * 60 * 60 * 1000,
                    pintent);
        }



    }

    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_container2, fragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }

    // open image picker to select new profile image
    public void onChangeProfileClicked(View view){
        ImagePicker.create(this)
                .returnMode(ReturnMode.ALL)
                .single()
                .start();
    }

    // when image was selected
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            Image image = ImagePicker.getFirstImageOrNull(data);
            if(image != null) {
                // execute ChangeProfileImage async task
                ChangeProfileImageAsyncTask task = new ChangeProfileImageAsyncTask(this);
                task.execute(image.getPath());
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    // open pass change dialog
    public void onChangePassClicked(View view) {
        DialogFragment fragment = new ChangePassDialogFragment();
        fragment.show(getSupportFragmentManager(), "pass");
    }

    // logout
    public void onLogoutClicked(View view) {
        SharedPreferences sharedPreferences = getSharedPreferences("LOGGED_USER", Context.MODE_PRIVATE);
        sharedPreferences.edit().remove("id").remove("username").remove("email").remove("profile_path").commit();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}
