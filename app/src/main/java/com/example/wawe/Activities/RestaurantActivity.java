package com.example.wawe.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.wawe.R;
import com.example.wawe.User;
import com.example.wawe.restaurantClasses.Restaurant;
import com.parse.ParseUser;

import org.json.JSONException;
import org.parceler.Parcels;

public class RestaurantActivity extends AppCompatActivity implements View.OnClickListener {


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
    ImageButton btnLike;
    CheckBox btnClickIfVisited;
    int numClicks = 0;

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
        btnLike = findViewById(R.id.btnLike);
        btnClickIfVisited = (CheckBox) findViewById(R.id.btnClickIfVisited);

        restaurant = Parcels.unwrap(getIntent().getParcelableExtra("restaurant"));
        tvName.setText(restaurant.getName());
        tvMilesAway.setText(restaurant.displayDistance());
        tvAddress.setText(restaurant.getLocation().getAddress());
        tvCategory.setText(restaurant.getCategory().get(0).getTitle());
        tvPrice.setText(restaurant.getPrice());
        ratingBar.setRating((float) restaurant.getRating());
        try {
            User currentUser = new User(ParseUser.getCurrentUser());
            if (currentUser.getIsFavorited(currentUser.getFavorites(), restaurant)){
                btnLike.setImageResource(R.drawable.ic_vector_heart);
                btnLike.setColorFilter(Color.parseColor("#ffe0245e"));
            }
            else{
                btnLike.setImageResource(R.drawable.ic_vector_heart_stroke);
                btnLike.setColorFilter(Color.parseColor("#000000"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            User currentUser = new User(ParseUser.getCurrentUser());
            if (currentUser.getHaveVisited(currentUser.getVisited(), restaurant)){
                btnClickIfVisited.setChecked(true);
            }
            else{
                btnClickIfVisited.setChecked(false);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Glide.with(this)
                .load(restaurant.getRestaurantImage()).into(ivRestaurantImage);

        // checks to see if the user is double clicking to like
        ivRestaurantImage.setOnClickListener(this);
        tvAddress.setOnClickListener(this);
        tvPrice.setOnClickListener(this);
        tvCategory.setOnClickListener(this);
        ratingBar.setOnClickListener(this);
        tvName.setOnClickListener(this);
        tvMilesAway.setOnClickListener(this);

        btnGetDirections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mapIntent = new Intent(RestaurantActivity.this, MapActivity.class);
                startActivity(mapIntent);

            }
        });

        // adding to favorites
        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                favoriteOrUnfavorite();
            }
        });

        btnClickIfVisited.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User currentUser = new User(ParseUser.getCurrentUser());
                // user is marking restaurant as visited
                if (btnClickIfVisited.isChecked()){
                    currentUser.visitedRestaurant(restaurant);
                    currentUser.getUser().saveInBackground();
                }
                else{
                    try {
                        currentUser.unVisitRestaurant(restaurant, currentUser.getVisited());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    currentUser.getUser().saveInBackground();
                }
            }
        });

    }

    public void favoriteOrUnfavorite(){
        User currentUser = new User(ParseUser.getCurrentUser());
        // user is liking restaurant
        try {
            if (!currentUser.getIsFavorited(currentUser.getFavorites(), restaurant)) {
                btnLike.setImageResource(R.drawable.ic_vector_heart);
                btnLike.setColorFilter(Color.parseColor("#ffe0245e"));
                currentUser.likeRestaurant(restaurant);
                currentUser.getUser().saveInBackground();
            }
            // user is unliking restaurant
            else {
                btnLike.setImageResource(R.drawable.ic_vector_heart_stroke);
                btnLike.setColorFilter(Color.parseColor("#000000"));
                try {
                    currentUser.unLikeRestaurant(restaurant, currentUser.getFavorites());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                currentUser.getUser().saveInBackground();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        numClicks++;

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (numClicks == 1){
                    Toast.makeText(RestaurantActivity.this, "Single click!", Toast.LENGTH_SHORT).show();
                }
                else if (numClicks == 2){
                    favoriteOrUnfavorite();
                    Toast.makeText(RestaurantActivity.this, "Double click!", Toast.LENGTH_SHORT).show();
                }
                numClicks = 0;
            }
        }, 500);
    }
}