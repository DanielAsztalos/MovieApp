package com.example.movieapp.helpers;

import android.content.Context;

import androidx.room.Room;

import com.example.movieapp.R;
import com.example.movieapp.database.AppDatabase;
import com.example.movieapp.models.User;

public class DatabaseTransactions {

//    public static boolean usernameExists(String username, Context context) {
//        AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, "movie-db").build();
//        if(db.userDao().getUsernameUsed(username) > 0) {
//            return true;
//        }
//        return false;
//    }
//
//    public static boolean emailExists(String email, Context context) {
//        AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, "movie-db").build();
//        if(db.userDao().getEmailUsed(email) > 0) {
//            return true;
//        }
//        return false;
//    }
//
//    public static void registerUser(String username, String email, String password, Context context) {
//        AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, "movie-db").build();
//
//        User userToRegister = new User();
//        userToRegister.setUsername(username);
//        userToRegister.setEmail(email);
//        userToRegister.setPassword(Encrypt.md5(password));
//        userToRegister.setPathToProfilePic("drawable://" + R.drawable.blank_profile_picture_973460_1280);
//
//        db.userDao().insertAll(userToRegister);
//    }
}
