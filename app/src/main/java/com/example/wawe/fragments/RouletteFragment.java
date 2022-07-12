package com.example.wawe.fragments;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wawe.Activities.RestaurantActivity;
import com.example.wawe.BuildConfig;
import com.example.wawe.R;
import com.example.wawe.YelpClasses.YelpRestaurant;
import com.example.wawe.RestaurantClient;
import com.example.wawe.YelpClasses.RestaurantCategories;
import com.example.wawe.YelpClasses.RestaurantSearch;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
    Set<YelpRestaurant> set = new HashSet<>();
    private Spinner spPrice;
    private Spinner spRadius;
    private Button btnGenerateRestaurant;
    private EditText etCuisine;
    private View aRouletteSplash;
    private TextView tvCuisineSelection;
    private TextView tvRadiusSelection;
    private TextView tvPriceSelection;

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
    }

    public void generateRestaurant () {
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

    public void obtainRestaurant(Call<RestaurantSearch> call) {
        call.enqueue(new Callback<RestaurantSearch>() {
            @Override
            public void onResponse(Call<RestaurantSearch> call, Response<RestaurantSearch> response) {
                RestaurantSearch body = response.body();
                if (body == null){
                    return;
                }
                Log.i(TAG, "" + response);
                restaurants.addAll(body.getRestaurants());
                if (!etCuisine.getText().toString().equals("")){
                    validateCorrectCategories();
                }
                //  checkIfVisited();
                obtainRandomRestaurant();
            }
            @Override
            public void onFailure(Call<RestaurantSearch> call, Throwable t) {
                Log.e(TAG, "error " + t );
            }
        });
    }

    private void validateCorrectCategories() {
        String cuisine = etCuisine.getText().toString();
        cuisine = cuisine.trim().toLowerCase();
        for (int i = 0; i < restaurants.size(); i++){
            boolean correctCategories = false;
            List<RestaurantCategories> categoriesList = restaurants.get(i).getCategory();
            for (int j = 0; j < categoriesList.size(); j++){
                if (categoriesList.get(j).getTitle().toLowerCase().equals(cuisine) || categoriesList.get(j).getTitle().toLowerCase().contains(cuisine)){
                    correctCategories = true;
                    break;
                }
            }
            if (!correctCategories){
                restaurants.remove(i);
                i--;
            }
        }
    }

    public void obtainRandomRestaurant () {
        Random random = new Random();
        if(!restaurants.isEmpty()) {
            YelpRestaurant randomRestaurant = restaurants.get(random.nextInt(restaurants.size()));
            Intent intent = new Intent(getContext(), RestaurantActivity.class);
            intent.putExtra("restaurant", Parcels.wrap(randomRestaurant));
            restaurants.clear();
            getContext().startActivity(intent);
            defaultVisibilities();
        }
        else {
            Toast.makeText(getContext(), "Could not find restaurants. Please select a different filter", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        longitude = location.getLongitude();
        latitude = location.getLatitude();
        locationManager.removeUpdates(this);
    }


    //    public void checkIfVisited () {
//        User currentUser = new User(ParseUser.getCurrentUser());
//        JSONArray visitedIds = new JSONArray();
//        for (int i = 0; i < currentUser.getVisited().length(); i++){
//            String str = null;
//            try {
//                str = currentUser.getVisited().getString(i);
//                String id = str.substring(str.indexOf("*") + 1, str.indexOf("*"));
//                visitedIds.put(id);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//        set.addAll(restaurants);
//        for (int i = 0; i < visitedIds.length(); i++){
//            try {
//                if (set.contains(visitedIds.get(i))){
//                    set.remove(visitedIds.get(i));
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//        //restaurants.clear();
//        new Task().execute();
//    }

    private class Task extends AsyncTask<URL, Integer, Long>{
        @Override
        protected Long doInBackground(URL... urls) {
            for (YelpRestaurant id: set) {
//                Call<Restaurant> call = restaurantClient.searchRestaurants("Bearer " + REST_APPLICATION_ID, id);
//                try {
//                    Response<Restaurant> response = call.execute();
//                    Log.i(TAG, "" + response);
//                    Restaurant body = response.body();
//                    restaurants.add(body);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Long aLong) {
            obtainRandomRestaurant();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }
    }











}