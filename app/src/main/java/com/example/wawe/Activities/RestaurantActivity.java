package com.example.wawe.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.wawe.R;
import com.example.wawe.fragments.MapFragment;
import com.example.wawe.fragments.ProfileFragment;
import com.example.wawe.fragments.RestaurantFragment;
import com.example.wawe.fragments.RouletteFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class RestaurantActivity extends AppCompatActivity {


    public static final String TAG = "RestaurantActivity";

    final FragmentManager fragmentManager = getSupportFragmentManager();
    private BottomNavigationView bottomNavigationViewRestaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        bottomNavigationViewRestaurant = findViewById(R.id.bottom_navigation_restaurant);
        bottomNavigationViewRestaurant.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                switch (item.getItemId()) {
                    case R.id.action_restaurant_details:c:
                    fragment = new RestaurantFragment();
                        Toast.makeText(RestaurantActivity.this, "clicked on restaurant details", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_map:
                    default:
                        fragment = new MapFragment();
                        Toast.makeText(RestaurantActivity.this, "clicked on map", Toast.LENGTH_SHORT).show();
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.flContainerRest, fragment).commit();
                return true;
            }
        });

        // default
        bottomNavigationViewRestaurant.setSelectedItemId(R.id.action_restaurant_details);
    }
}