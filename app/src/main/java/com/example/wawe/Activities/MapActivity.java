package com.example.wawe.Activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.wawe.MapSubclasses.FetchURL;
import com.example.wawe.MapSubclasses.TaskLoadedCallback;
import com.example.wawe.R;
import com.example.wawe.YelpClasses.YelpRestaurant;
import com.example.wawe.Fragments.RouletteFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.parceler.Parcels;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, TaskLoadedCallback {

    public static final String BASE_URL = "https://maps.googleapis.com/maps/api/directions/";
    private GoogleMap map;
    private MarkerOptions origin, restaurantLocation;
    private Polyline currentPolyline;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        origin = new MarkerOptions().position(new LatLng(RouletteFragment.latitude, RouletteFragment.longitude)).title("Your Location");
        YelpRestaurant restaurant = Parcels.unwrap(getIntent().getParcelableExtra("restaurant"));
        restaurantLocation = new MarkerOptions().position(new LatLng(restaurant.getCoordinates().getLatitude(), restaurant.getCoordinates().getLongitude())).title(restaurant.getName());
        restaurantLocation.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.addMarker(origin);
        map.addMarker(restaurantLocation); //1
        final LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(origin.getPosition()); // this is a LatLng value
        builder.include(restaurantLocation.getPosition());
        map.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 100));
        new FetchURL(MapActivity.this).execute(getUrl(origin.getPosition(), restaurantLocation.getPosition(), "driving"), "driving");
    }

    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        String mode = "mode=" + directionMode;
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        String output = "json";
        String url = BASE_URL + output + "?" + parameters + "&key=" + getString(R.string.google_maps_api_key);
        return url;
    }

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = map.addPolyline((PolylineOptions) values[0]);
    }
}
