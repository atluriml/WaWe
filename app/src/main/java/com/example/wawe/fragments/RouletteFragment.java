package com.example.wawe.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wawe.Activities.MainActivity;
import com.example.wawe.Activities.RestaurantActivity;
import com.example.wawe.BuildConfig;
import com.example.wawe.ParseModels.Restaurant;
import com.example.wawe.ParseModels.UserFavorites;
import com.example.wawe.ParseModels.UserVisited;
import com.example.wawe.R;
import com.example.wawe.YelpClasses.YelpRestaurant;
import com.example.wawe.RestaurantClient;
import com.example.wawe.YelpClasses.RestaurantCategories;
import com.example.wawe.YelpClasses.RestaurantSearch;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONException;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class RouletteFragment extends Fragment implements LocationListener {

    public static final String TAG = "RouletteFragment";
    public static final int MAX_RADIUS = 40000; // default 4000 meters or ~25 miles

    public static final String BASE_URL = "https://api.yelp.com/v3/";
    public static final String REST_APPLICATION_ID = BuildConfig.YELP_APPLICATION_ID;
    public static final String REST_CLIENT_ID = BuildConfig.YELP_CLIENT_ID;
    Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
    RestaurantClient restaurantClient = retrofit.create(RestaurantClient.class);

    LocationManager locationManager;
    public static double longitude;
    public static double latitude;
    ArrayList<YelpRestaurant> restaurants = new ArrayList<>();
    List<Restaurant> userFavorites = new ArrayList<>();
    List<Restaurant> userVisited = new ArrayList<>();
    List<YelpRestaurant> userVisitedYelpRestaurantObjects = new ArrayList<>();
    LinkedHashMap<String, Integer> sortedFavoritesPriceMap = new LinkedHashMap<>();
    LinkedHashMap<String, Integer> sortedFavoritesCategoriesMap = new LinkedHashMap<>();
    Set<String> topFavoritedCategories = new HashSet<>();
    Set<YelpRestaurant> topScoredRestaurants = new HashSet<>();
    LinkedHashMap<String, YelpRestaurant> userVisitedSet = new LinkedHashMap<>();
    private Spinner spPrice;
    private Spinner spRadius;
    private Button btnGenerateRestaurant;
    private EditText etCuisine;
    private View aRouletteSplash;
    private TextView tvCuisineSelection;
    private TextView tvRadiusSelection;
    private TextView tvPriceSelection;
    private CheckBox btnVisitedRestaurants;

    public RouletteFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_roulette, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        aRouletteSplash = view.findViewById(R.id.rouletteAnimation);
        tvCuisineSelection = view.findViewById(R.id.tvCuisine);
        tvPriceSelection = view.findViewById(R.id.tvPrice);
        tvRadiusSelection = view.findViewById(R.id.tvRadius);
        btnVisitedRestaurants = view.findViewById(R.id.btnVisitedRestaurants);

        SensorManager sensorManager = (SensorManager) getContext().getSystemService(getContext().SENSOR_SERVICE);
        Sensor sensorShake = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        SensorEventListener sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                if (sensorEvent!=null){
                    float x_accl = sensorEvent.values[0];
                    float y_accl = sensorEvent.values[1];
                    float z_accl = sensorEvent.values[2];

                    float floatSum = Math.abs(x_accl) + Math.abs(y_accl) + Math.abs(z_accl);

                    if (floatSum > 60){
                        aRouletteSplash.setVisibility(View.VISIBLE);
                        spRadius.setVisibility(View.GONE);
                        spPrice.setVisibility(View.GONE);
                        etCuisine.setVisibility(View.GONE);
                        btnGenerateRestaurant.setVisibility(View.GONE);
                        tvCuisineSelection.setVisibility(View.GONE);
                        tvPriceSelection.setVisibility(View.GONE);
                        tvRadiusSelection.setVisibility(View.GONE);
                        btnVisitedRestaurants.setVisibility(View.GONE);
                        generateRestaurant();
                        sensorManager.unregisterListener(this);
                    }

                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }
        };
        sensorManager.registerListener(sensorEventListener, sensorShake, SensorManager.SENSOR_DELAY_NORMAL);

        // gets user's current longitude and latitude
        locationManager = (LocationManager) getContext().getSystemService(getContext().LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(), new String[] {android.Manifest.permission.ACCESS_FINE_LOCATION}, 100);
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        // cuisine filter
        etCuisine = view.findViewById(R.id.etCuisine);

        // radius filter
        spRadius = view.findViewById(R.id.spRadius);
        String[] possibleRadius = new String[]{"", "5", "10", "15", "25"};
        ArrayAdapter<String> adapterRadius = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, possibleRadius);
        spRadius.setAdapter(adapterRadius);

        // price filter
        spPrice = view.findViewById(R.id.spPrice);
        String[] priceOptions = new String[]{"","$", "$$", "$$$", "$$$$"};
        ArrayAdapter<String> adapterPrice = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, priceOptions);
        spPrice.setAdapter(adapterPrice);

        btnGenerateRestaurant = view.findViewById(R.id.btnGenerateRestaurant);

        if (MainActivity.isOnline(getContext())){{
            callFavorites();
            callAlreadyVisitedRestaurants();
        }}

        if (doesUserHaveFavorites()){
            buckSortFavoritesPrice();
            sortFavoritesTopCategories();
        }

        // when user is ready to generate recommended restaurant
        btnGenerateRestaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aRouletteSplash.setVisibility(View.VISIBLE);
                spRadius.setVisibility(View.GONE);
                spPrice.setVisibility(View.GONE);
                etCuisine.setVisibility(View.GONE);
                btnGenerateRestaurant.setVisibility(View.GONE);
                tvCuisineSelection.setVisibility(View.GONE);
                tvPriceSelection.setVisibility(View.GONE);
                tvRadiusSelection.setVisibility(View.GONE);
                btnVisitedRestaurants.setVisibility(View.GONE);
                generateRestaurant();
            }
        });
    }

    private void defaultVisibilities() {
        aRouletteSplash.setVisibility(View.GONE);
        spRadius.setVisibility(View.VISIBLE);
        spPrice.setVisibility(View.VISIBLE);
        etCuisine.setVisibility(View.VISIBLE);
        btnGenerateRestaurant.setVisibility(View.VISIBLE);
        tvCuisineSelection.setVisibility(View.VISIBLE);
        tvPriceSelection.setVisibility(View.VISIBLE);
        tvRadiusSelection.setVisibility(View.VISIBLE);
        btnVisitedRestaurants.setVisibility(View.VISIBLE);
    }

    private void generateRestaurant () {
        String cuisine = etCuisine.getText().toString();
        String rad = spRadius.getSelectedItem().toString();
        int maxDistance = MAX_RADIUS;
        if (!rad.equals("")){
            maxDistance = Integer.parseInt(rad) * 1609;
            if (maxDistance > MAX_RADIUS){
                maxDistance = MAX_RADIUS;
            }
        }
        String price = spPrice.getSelectedItem().toString();
        String dietaryRestriction = ParseUser.getCurrentUser().getString("dietaryRestriction");
        if (!dietaryRestriction.equals("")) {
            dietaryRestriction = dietaryRestriction.trim();
        }
        Call<RestaurantSearch> call = restaurantClient.searchRestaurants("Bearer " + REST_APPLICATION_ID , cuisine, dietaryRestriction, latitude, longitude, maxDistance, String.valueOf(price.length()), 50);
        obtainRestaurant(call);
    }

    private void obtainRestaurant(Call<RestaurantSearch> call) {
        call.enqueue(new Callback<RestaurantSearch>() {
            @Override
            public void onResponse(Call<RestaurantSearch> call, Response<RestaurantSearch> response) {
                RestaurantSearch body = response.body();
                if (body == null){
                    return;
                }
                restaurants.addAll(body.getRestaurants());
                if (btnVisitedRestaurants.isChecked()){
                    removeAlreadyVisitedRestaurants();
                }
                sortRestaurants();
                obtainRandomRestaurant();
            }
            @Override
            public void onFailure(Call<RestaurantSearch> call, Throwable t) {
                Log.e(TAG, "error " + t );
            }
        });
    }

    private void obtainRandomRestaurant () {
        Random random = new Random();
        List<YelpRestaurant> topRestaurants = new ArrayList<>();
        if(!topScoredRestaurants.isEmpty()) {
            topRestaurants.addAll(topScoredRestaurants);
            YelpRestaurant randomRestaurant = topRestaurants.get(random.nextInt(topRestaurants.size()));
            Intent intent = new Intent(getContext(), RestaurantActivity.class);
            intent.putExtra("restaurant", Parcels.wrap(randomRestaurant));
            restaurants.clear();
            topScoredRestaurants.clear();
            getContext().startActivity(intent);
            defaultVisibilities();
        }
        else {
            Toast.makeText(getContext(), "Could not find restaurants. Please select choose different criteria", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        longitude = location.getLongitude();
        latitude = location.getLatitude();
        locationManager.removeUpdates(this);
    }

    private void callFavorites () {
        ParseQuery<UserFavorites> queryRestaurants = ParseQuery.getQuery(UserFavorites.class).whereEqualTo(UserFavorites.KEY_USER, ParseUser.getCurrentUser());
        try {
            List <UserFavorites> userFavoritesList = queryRestaurants.find();
            for (UserFavorites entry: userFavoritesList) {
                ParseQuery<Restaurant> queryRestaurant = ParseQuery.getQuery(Restaurant.class).whereEqualTo(Restaurant.KEY_OBJECT_ID, entry.getRestaurantFavorite().getObjectId());
                userFavorites.add(queryRestaurant.getFirst());
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void callAlreadyVisitedRestaurants() {
        ParseQuery<UserVisited> queryRestaurants = ParseQuery.getQuery(UserVisited.class).whereEqualTo(UserFavorites.KEY_USER, ParseUser.getCurrentUser());
        try {
            List <UserVisited> userVisitedList = queryRestaurants.find();
            for (UserVisited entry: userVisitedList) {
                ParseQuery<Restaurant> queryRestaurant = ParseQuery.getQuery(Restaurant.class).whereEqualTo(Restaurant.KEY_OBJECT_ID, entry.getRestaurantVisited().getObjectId());
                userVisited.add(queryRestaurant.getFirst());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < userVisited.size(); i++){
            YelpRestaurant yelpRestaurant = new YelpRestaurant(userVisited.get(i));
            userVisitedYelpRestaurantObjects.add(yelpRestaurant);
        }
    }

    private boolean doesUserHaveFavorites() {
        return !userFavorites.isEmpty();
    }

    private void removeAlreadyVisitedRestaurants() {
        YelpRestaurant value = null;
        for (int i = 0; i < restaurants.size(); i++) {
            userVisitedSet.put(restaurants.get(i).getId(), restaurants.get(i));
        }
        for (int i = 0; i < userVisitedYelpRestaurantObjects.size(); i++){
            value = userVisitedSet.get(userVisitedYelpRestaurantObjects.get(i).getId());
            if (value != null) {
                userVisitedSet.remove(userVisitedYelpRestaurantObjects.get(i).getId());
            }
        }
        restaurants.clear();
        for (Map.Entry<String,YelpRestaurant> entry : userVisitedSet.entrySet()) {
            restaurants.add(entry.getValue());
        }
    }

    /*
     * This method gets the price point of the restaurants that a user has favorited sorts them based on the number
     * of times the price point is present in the user's favorited
     */
    private void buckSortFavoritesPrice () {
        LinkedHashMap<String, Integer> mostCommonPrice = new LinkedHashMap<>();
        for (int i = 0; i < userFavorites.size(); i++) {
            Integer integer = null;
            String restaurantPrice = userFavorites.get(i).getKeyPrice();
            integer = mostCommonPrice.get(restaurantPrice);
            if (integer == null)
                mostCommonPrice.put(restaurantPrice, 1);
            else {
                mostCommonPrice.put(restaurantPrice, integer + 1);
            }
        }
        mostCommonPrice.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEachOrdered(x -> sortedFavoritesPriceMap.put(x.getKey(), x.getValue()));
    }

    /*
     * This method gets the categories of the restaurants that a user has favorited sorts them based on the number
     * of times the category is present in the user's favorited then all of the most common categories with the highest score
     * are stored in the topFavoritedCategories set
     */
    private void sortFavoritesTopCategories () {
        HashMap<String, Integer> categoriesPresentInFavorites = new HashMap<>();
        for (int i = 0; i < userFavorites.size(); i++){
            Integer integer = null;
            String restaurantCategory = null;
            for (int j = 0; j < userFavorites.get(i).getKeyCategories().length(); j++){
                try {
                    restaurantCategory = userFavorites.get(i).getKeyCategories().get(j).toString();
                    integer = categoriesPresentInFavorites.get(restaurantCategory);
                    if (integer == null)
                        categoriesPresentInFavorites.put(restaurantCategory, 1);
                    else {
                        categoriesPresentInFavorites.put(restaurantCategory, integer + 1);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        categoriesPresentInFavorites.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEachOrdered(x -> sortedFavoritesCategoriesMap.put(x.getKey(), x.getValue()));
        Map.Entry<String, Integer> firstCategoryInMap = sortedFavoritesCategoriesMap.entrySet().iterator().next();
        int value = firstCategoryInMap.getValue(); // contains the value of the first category in the map
        for (Map.Entry<String,Integer> entry: sortedFavoritesCategoriesMap.entrySet())
        {
            if (entry.getValue() == value) {
                topFavoritedCategories.add(entry.getKey());
            }
        }
    }

    /*
     * This method sorts the restaurants based on their scores, then all of the restaurant(s) with the highest score
     * are stored in the topScoredRestaurants set
     */
    private void sortRestaurants() {
        LinkedHashMap<YelpRestaurant, Integer> scores = computeRestaurantScores();
        LinkedHashMap<YelpRestaurant, Integer> sortedRestaurants = new LinkedHashMap<>();

        scores.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEachOrdered(x -> sortedRestaurants.put(x.getKey(), x.getValue()));

        Map.Entry<YelpRestaurant, Integer> firstRestaurantInMap = sortedRestaurants.entrySet().iterator().next();
        int value = firstRestaurantInMap.getValue(); // contains the value of the first category in the map
        for (Map.Entry<YelpRestaurant, Integer> entry: sortedRestaurants.entrySet())
        {
            if (entry.getValue() == value) {
                topScoredRestaurants.add(entry.getKey());
            }
        }
    }

    /*
     * This method will compute each restaurants score from the Yelp query
     */
    private LinkedHashMap<YelpRestaurant, Integer> computeRestaurantScores() {
        LinkedHashMap<YelpRestaurant, Integer> scores = new LinkedHashMap<>();
        for (YelpRestaurant restaurant: restaurants){
            LinkedHashMap<String, Boolean> attributes = getUsersPreferredAttributes();
            /*
             * The attribute at the top will add 3^3 to the restaurant's current score, the second attribute
             * will add 3^3 and the last attribute will add 3^1
             */
            int score = 0, currentAttributeScore = (int) Math.pow(3, 4);
            for (Map.Entry<String,Boolean> entry : attributes.entrySet()) {
                currentAttributeScore /= 3;
                if (entry.getKey().equals("category")){
                    if(sortQueriedRestaurantsCategories(entry.getValue(), restaurant)){
                        score += currentAttributeScore;
                    }
                }
                else if (entry.getKey().equals("price")){
                    if (sortQueriedRestaurantsPrice(entry.getValue(), restaurant)){
                        score += currentAttributeScore;
                    }
                }
                else if (entry.getKey().equals("radius")){
                    if (sortQueriedRestaurantsRadius(restaurant) == 5){
                        score += currentAttributeScore;
                    }
                    else if (sortQueriedRestaurantsRadius(restaurant) == 10){
                        int radiusScore = (int) currentAttributeScore / 3;
                        score += radiusScore;
                    }
                    else if (sortQueriedRestaurantsRadius(restaurant) == 15){
                        int radiusScore = (int) currentAttributeScore / 9;
                        score += radiusScore;
                    }
                }
                scores.put(restaurant, score);
            }
        }
        return scores;
    }

    /*
     * This method will indicate whether the restaurant's categories matches the category the user indicated on the roulette screen
     * or the most common categories from the user's favorites depending on the Boolean value
     */
    private boolean sortQueriedRestaurantsCategories(Boolean value, YelpRestaurant restaurant) {
        if (restaurant.getCategory() == null || restaurant.getCategory().isEmpty()) return false;
        // utilize the most common categories from the user's favorites
        if (value){
            if (!doesUserHaveFavorites()){
                return false;
            }
            List<RestaurantCategories> categoriesList = restaurant.getCategory();
            for (int j = 0; j < categoriesList.size(); j++){
                if (topFavoritedCategories.contains(categoriesList.get(j).getTitle())){
                    return true;
                }
            }
        }
        // utilize the most the category the user's indicated on the roulette screen
        else {
            String cuisine = etCuisine.getText().toString();
            cuisine = cuisine.trim().toLowerCase();
            List<RestaurantCategories> categoriesList = restaurant.getCategory();
            for (int j = 0; j < categoriesList.size(); j++){
                if (categoriesList.get(j).getTitle().toLowerCase().equals(cuisine) || categoriesList.get(j).getTitle().toLowerCase().contains(cuisine)){
                    return true;
                }
            }
        }
        return false;
    }

    /*
     * This method will indicate whether the restaurant's price matches the price point the user indicated on the roulette screen
     * or the most common price point from the user's favorites depending on the Boolean value
     */
    private boolean sortQueriedRestaurantsPrice(Boolean value, YelpRestaurant restaurant) {
        if (restaurant.getPrice() == null || restaurant.getPrice().isEmpty()) return false;
        // utilize the most common price point from the user's favorites
        if (value){
            if (!doesUserHaveFavorites()){
                return false;
            }
            Map.Entry<String, Integer> mostFavoritedPrice = sortedFavoritesPriceMap.entrySet().iterator().next();
            String price = mostFavoritedPrice.getKey();
            return restaurant.getPrice().equals(price);
        }
        // utilize the most the price point the user's indicated on the roulette screen
        else {
            String price = spPrice.getSelectedItem().toString();
            return restaurant.getPrice().equals(price);
        }
    }

    /*
     * This method will return a value indicating how close it is to the user's location. The closer the
     * restaurant, the more the restaurant's score will increase.
     */
    private int sortQueriedRestaurantsRadius(YelpRestaurant restaurant) {
        if (restaurant.getDistanceMeters() == 0) {
            return 25;
        }
        if(restaurant.getDistanceInMiles() <= 5){
            return 5;
        }
        else if(restaurant.getDistanceInMiles() <= 10){
            return 10;
        }
        else if(restaurant.getDistanceInMiles() <= 15){
            return 15;
        }
        return 25;
    }

    /*
     * This method will create a LinkedHashMap that holds the order of which attributes will be used for comparison. The key
     * in the LinkedHashMap indicates the attributes name and the Boolean indicates whether or not the most common
     * attributes from user's favorites will be utilized. If the user does not specify a certain filter on the roulette screen
     * the Boolean will be true, otherwise it will be false.
     */
    private LinkedHashMap<String, Boolean> getUsersPreferredAttributes () {
        LinkedHashMap<String, Boolean> attributes = new LinkedHashMap<>();
        if (etCuisine.getText().toString().isEmpty() && spPrice.getSelectedItem().toString().isEmpty() && spRadius.getSelectedItem().toString().isEmpty()){ // none
            attributes.put("category", true);
            attributes.put("price", true);
            attributes.put("radius", false);
        }
        else if (!etCuisine.getText().toString().isEmpty() && spPrice.getSelectedItem().toString().isEmpty() && spRadius.getSelectedItem().toString().isEmpty()){ // cuisine
            attributes.put("category", false);
            attributes.put("price", true);
            attributes.put("radius", false);
        }
        else if (!etCuisine.getText().toString().isEmpty() && !spPrice.getSelectedItem().toString().isEmpty() && spRadius.getSelectedItem().toString().isEmpty()){ // cuisine and price
            attributes.put("category", false);
            attributes.put("price", false);
            attributes.put("radius", false);
        }
        else if (!etCuisine.getText().toString().isEmpty() && spPrice.getSelectedItem().toString().isEmpty() && !spRadius.getSelectedItem().toString().isEmpty()){ // cuisine and radius
            attributes.put("category", false);
            attributes.put("radius", false);
            attributes.put("price", true);
        }
        else if (etCuisine.getText().toString().isEmpty() && !spPrice.getSelectedItem().toString().isEmpty() && spRadius.getSelectedItem().toString().isEmpty()){ // price
            attributes.put("price", false);
            attributes.put("category", true);
            attributes.put("radius", false);
        }
        else if (etCuisine.getText().toString().isEmpty() && !spPrice.getSelectedItem().toString().isEmpty() && !spRadius.getSelectedItem().toString().isEmpty()){ // price and radius
            attributes.put("price", false);
            attributes.put("radius", false);
            attributes.put("category", true);
        }
        else if (etCuisine.getText().toString().isEmpty() && spPrice.getSelectedItem().toString().isEmpty() && !spRadius.getSelectedItem().toString().isEmpty()){ // radius
            attributes.put("radius", false);
            attributes.put("category", true);
            attributes.put("price", true);
        }
        else if (!etCuisine.getText().toString().isEmpty() && !spPrice.getSelectedItem().toString().isEmpty() && !spRadius.getSelectedItem().toString().isEmpty()){ // price, radius, cuisine
            attributes.put("category", false);
            attributes.put("price", false);
            attributes.put("radius", false);
        }
        return attributes;
    }

}