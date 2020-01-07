package com.example.movieapp.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.movieapp.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * This fragment displays the profile page
 */
public class ProfileFragment extends Fragment {


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_profile, container, false);

        // display username and profile picture if exists
        SharedPreferences sharedPreferences = rootview.getContext().getSharedPreferences("LOGGED_USER", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");
        String path = sharedPreferences.getString("profile_path", "");

        ((TextView) rootview.findViewById(R.id.tv_name)).setText(username);

        if(path.equals("")) {
            ((CircleImageView) rootview.findViewById(R.id.profile_image)).setImageResource(R.drawable.blank_profile_picture_973460_1280);
        }
        else{
            ((CircleImageView) rootview.findViewById(R.id.profile_image)).setImageURI(Uri.parse(path));
        }

        return rootview;
    }

}
