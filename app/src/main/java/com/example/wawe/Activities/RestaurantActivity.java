package com.example.wawe.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.wawe.ParseAndDatabaseApplication;
import com.example.wawe.R;
import com.example.wawe.ParseModels.Restaurant;
import com.example.wawe.ParseModels.UserFavorites;
import com.example.wawe.ParseModels.UserVisited;
import com.example.wawe.YelpClasses.YelpRestaurant;
import com.example.wawe.fragments.RouletteFragment;
import com.example.wawe.roomClasses.RestaurantListsDao;
import com.example.wawe.roomClasses.RestaurantRoom;
import com.example.wawe.roomClasses.UserFavoritesRoom;
import com.example.wawe.roomClasses.UserRoom;
import com.example.wawe.roomClasses.UserVisitedRoom;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.parceler.Parcels;

public class RestaurantActivity extends AppCompatActivity implements View.OnClickListener {

    YelpRestaurant restaurant;
    TextView tvName;
    TextView tvPrice;
    TextView tvMilesAway;
    RatingBar ratingBar;
    TextView tvAddress;
    TextView tvCategory;
    ImageView ivRestaurantImage;
    Button btnGetDirections;
    ImageButton btnLiked;
    String objectId;
    Restaurant parseRestaurant;
    CheckBox btnClickIfVisited;
    RestaurantListsDao restaurantListsDao;
    private View likesAnimation;

    boolean liked;
    int numClicks = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        restaurantListsDao = ( (ParseAndDatabaseApplication) getApplicationContext()).getDatabase().restaurantListsDao();
        tvName = findViewById(R.id.tvRestName);
        tvMilesAway = findViewById(R.id.tvMilesAway);
        tvAddress = findViewById(R.id.tvAddress);
        tvCategory = findViewById(R.id.tvCategories);
        tvPrice = findViewById(R.id.tvPrice);
        ratingBar = findViewById(R.id.rbVoteAverage);
        ivRestaurantImage = findViewById(R.id.ivRestImage);
        btnGetDirections = findViewById(R.id.btnGetDirections);
        btnLiked = findViewById(R.id.btnLike);
        btnClickIfVisited = (CheckBox) findViewById(R.id.btnClickIfVisited);
        likesAnimation = findViewById(R.id.likes_animation);
        restaurant = Parcels.unwrap(getIntent().getParcelableExtra("restaurant"));

        ParseQuery<Restaurant> queryRestaurant = ParseQuery.getQuery(Restaurant.class);
        queryRestaurant.whereEqualTo(Restaurant.KEY_ID, restaurant.getId());
        queryRestaurant.getFirstInBackground(new GetCallback<Restaurant>()
        {
            @Override
            public void done(Restaurant object, ParseException e) {
                if(e == null) // restaurant exists in parseDatabase
                {
                    objectId = object.getObjectId();
                    parseRestaurant = object;
                    // checks to see if the user has liked the restaurant
                    ParseQuery<UserFavorites> queryUserFavorites = ParseQuery.getQuery(UserFavorites.class);
                    queryUserFavorites.whereEqualTo(UserFavorites.KEY_FAVORITED_RESTAURANT, parseRestaurant).whereEqualTo(UserFavorites.KEY_USER, ParseUser.getCurrentUser());
                    queryUserFavorites.getFirstInBackground(new GetCallback<UserFavorites>()
                    {
                        @Override
                        public void done(UserFavorites object, ParseException e) {
                            if(e == null) {
                                btnLiked.setImageResource(R.drawable.ic_vector_heart);
                                btnLiked.setColorFilter(Color.parseColor("#92c7d6"));
                                liked = true;

                            }
                            else {
                                btnLiked.setImageResource(R.drawable.ic_vector_heart_stroke);
                                btnLiked.setColorFilter(Color.parseColor("#000000"));
                                liked = false;
                            }
                        }
                    });
                    //check to see if the user has visited the restaurant
                    ParseQuery<UserVisited> queryUserVisited = ParseQuery.getQuery(UserVisited.class);
                    queryUserVisited.whereEqualTo(UserVisited.KEY_VISITED_RESTAURANT, parseRestaurant).whereEqualTo(UserVisited.KEY_USER_VISITED, ParseUser.getCurrentUser());
                    queryUserVisited.getFirstInBackground(new GetCallback<UserVisited>() {
                        @Override
                        public void done(UserVisited object, ParseException e) {
                            if(e == null) {
                                btnClickIfVisited.setChecked(true);
                            }
                            else {
                                btnClickIfVisited.setChecked(false);
                            }
                        }
                    });
                }
                else
                {
                    parseRestaurant = new Restaurant(restaurant);
                    btnLiked.setImageResource(R.drawable.ic_vector_heart_stroke);
                    btnLiked.setColorFilter(Color.parseColor("#000000"));
                    liked = false;
                    btnClickIfVisited.setChecked(false);
                }
            }
        });

        tvName.setText(restaurant.getName());
        tvAddress.setText(restaurant.getLocation().getAddress());
        String categories = restaurant.getCategory().get(0).getTitle();
        for (int i = 1; i < restaurant.getCategory().size(); i++){
            categories = categories + ", " +   restaurant.getCategory().get(i).getTitle();
        }
        tvCategory.setText(categories);
        tvPrice.setText(restaurant.getPrice());
        double userLatitude = RouletteFragment.latitude;
        double userLongitude = RouletteFragment.longitude;
        double restaurantLatitude = restaurant.getCoordinates().getLatitude();
        double restaurantLongitude = restaurant.getCoordinates().getLongitude();
        String distance = getDistance(userLatitude, userLongitude, restaurantLatitude, restaurantLongitude);
        tvMilesAway.setText(distance);
        ratingBar.setRating((float) restaurant.getRating());
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
                mapIntent.putExtra("restaurant", Parcels.wrap(restaurant));
                startActivity(mapIntent);
            }
        });

        //adding or removing from favorites
        btnLiked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                favoriteOrUnfavorite();
            }
        });

        btnClickIfVisited.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // user is marking restaurant as visited
                if (btnClickIfVisited.isChecked()){
                   Restaurant.markRestaurantVisited(parseRestaurant);
                   btnClickIfVisited.setChecked(true);
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            RestaurantRoom restaurantRoom = new RestaurantRoom(parseRestaurant);
                            UserRoom userRoom = new UserRoom(ParseUser.getCurrentUser());
                            restaurantListsDao.insertModel(restaurantRoom);
                            restaurantListsDao.insertModel(userRoom);
                            UserVisitedRoom userVisitedRoom = new UserVisitedRoom();
                            userVisitedRoom.userId = userRoom.getUserId();
                            userVisitedRoom.restaurantId = restaurantRoom.yelpId;
                            restaurantListsDao.insertModel(userVisitedRoom);
                        }
                    });
                }
                else{
                    Restaurant.markAsNotVisited(parseRestaurant);
                    btnClickIfVisited.setChecked(false);
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            UserVisitedRoom userVisitedRoom = restaurantListsDao.userVisitedToDelete(ParseUser.getCurrentUser().getObjectId(), restaurant.getId());
                            restaurantListsDao.deleteModel(userVisitedRoom);
                        }
                    });
                }
            }
        });

    }

    private String getDistance(double userLatitude, double userLongitude, double restaurantLatitude, double restaurantLongitude) {
        userLongitude = Math.toRadians(userLongitude);
        restaurantLongitude = Math.toRadians(restaurantLongitude);
        userLatitude = Math.toRadians(userLatitude);
        restaurantLatitude = Math.toRadians(restaurantLatitude);
        double dlon = restaurantLongitude - userLongitude;
        double dlat = restaurantLatitude - userLatitude;
        double a = Math.pow(Math.sin(dlat / 2), 2)
                + Math.cos(userLatitude) * Math.cos(restaurantLatitude)
                * Math.pow(Math.sin(dlon / 2),2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double r = 3956;
        String distanceInMiles = String.format("%.01f", c * r);
        return distanceInMiles + "mi";
    }

    public void favoriteOrUnfavorite(){
        // user is unliking restaurant
        if (liked) {
            likesAnimation.setVisibility(View.GONE);
            btnLiked.setImageResource(R.drawable.ic_vector_heart_stroke);
            btnLiked.setColorFilter(Color.parseColor("#000000"));
            Restaurant.unFavoriteRestaurant(parseRestaurant);
            liked = false;
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    UserFavoritesRoom userFavoritesRoom = restaurantListsDao.userFavoriteToDelete(ParseUser.getCurrentUser().getObjectId(), restaurant.getId());
                    restaurantListsDao.deleteModel(userFavoritesRoom);
                }
            });
        }
        // user is liking restaurant
        else {
            likesAnimation.setVisibility(View.VISIBLE);
            btnLiked.setImageResource(R.drawable.ic_vector_heart);
            btnLiked.setColorFilter(Color.parseColor("#92c7d6"));
            Restaurant.likeRestaurant(parseRestaurant);
            liked = true;
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    RestaurantRoom restaurantRoom = new RestaurantRoom(parseRestaurant);
                    UserRoom userRoom = new UserRoom(ParseUser.getCurrentUser());
                    restaurantListsDao.insertModel(restaurantRoom);
                    restaurantListsDao.insertModel(userRoom);
                    UserFavoritesRoom userFavoritesRoom = new UserFavoritesRoom();
                    userFavoritesRoom.userId = userRoom.getUserId();
                    userFavoritesRoom.restaurantId = restaurantRoom.yelpId;
                    restaurantListsDao.insertModel(userFavoritesRoom);
                }
            });
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
                }
                else if (numClicks == 2){
                    favoriteOrUnfavorite();
                }
                numClicks = 0;
            }
        }, 500);
    }
}