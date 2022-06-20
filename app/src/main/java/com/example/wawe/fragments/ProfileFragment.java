package com.example.wawe.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.wawe.FavoritesActivity;
import com.example.wawe.LoginActivity;
import com.example.wawe.R;
import com.example.wawe.User;
import com.example.wawe.VisitedRestaurantsActivity;
import com.parse.ParseFile;
import com.parse.ParseUser;

import org.parceler.Parcels;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    public static final String TAG = "ProfileFragment";

    User user;
    private TextView tvProfileUsername;
    private ImageView ivProfileImage;
    private TextView tvProfileLocation;
    private Button btnFavorites;
    private Button btnVisited;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        tvProfileLocation = view.findViewById(R.id.tvLocation);
        tvProfileUsername = view.findViewById(R.id.tvUsername);
        ivProfileImage = view.findViewById(R.id.ivProfileImage);
        btnFavorites = view.findViewById(R.id.btnFavorites);
        btnVisited = view.findViewById(R.id.btnVisitedRestaurants);

        ParseFile profileImage = ParseUser.getCurrentUser().getParseFile("profileImage");

        if (profileImage == null){
            Glide.with(this).load(R.drawable.instagram_user_outline_24).into(ivProfileImage);
        }
        else  {
            Glide.with(this).load(profileImage.getUrl()).into(ivProfileImage);
        }

        tvProfileUsername.setText(ParseUser.getCurrentUser().getUsername());
        tvProfileLocation.setText(ParseUser.getCurrentUser().getString("location"));

        btnFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), FavoritesActivity.class);
                getContext().startActivity(intent);
            }
        });

        btnVisited.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), VisitedRestaurantsActivity.class);
                getContext().startActivity(intent);
            }
        });

    }
}