package com.example.wawe.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.wawe.R;
import com.example.wawe.restaurantClasses.Restaurant;

import org.parceler.Parcels;

public class RestaurantActivity extends AppCompatActivity {


    public static final String TAG = "RestaurantActivity";

    Restaurant restaurant;
    TextView tvName;
    TextView tvPrice;
    TextView tvMilesAway;
    RatingBar ratingBar;
    TextView tvAddress;
    TextView tvCategory; //TODO display all the categories
    ImageView ivRestaurantImage;
    Button btnGetDirections;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        tvName = findViewById(R.id.tvRestName);
        tvMilesAway = findViewById(R.id.tvMilesAway);
        tvAddress = findViewById(R.id.tvAddress);
        tvCategory = findViewById(R.id.tvCategories);
        tvPrice = findViewById(R.id.tvPrice);
        ratingBar = findViewById(R.id.rbVoteAverage);
        ivRestaurantImage = findViewById(R.id.ivRestImage);
        btnGetDirections = findViewById(R.id.btnGetDirections);


        restaurant = Parcels.unwrap(getIntent().getParcelableExtra("restaurant"));
        tvName.setText(restaurant.getName());
        tvMilesAway.setText(restaurant.displayDistance());
        tvAddress.setText(restaurant.getLocation().getAddress());
        tvCategory.setText(restaurant.getCategory().get(0).getTitle());
        tvPrice.setText(restaurant.getPrice());
        ratingBar.setRating((float) restaurant.getRating());

        Glide.with(this)
                .load(restaurant.getRestaurantImage()).into(ivRestaurantImage);


        btnGetDirections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mapIntent = new Intent(RestaurantActivity.this, MapActivity.class);
                startActivity(mapIntent);

            }
        });

    }
}