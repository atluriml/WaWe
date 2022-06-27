package com.example.wawe;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.example.wawe.Activities.RestaurantActivity;
import com.example.wawe.restaurantClasses.Restaurant;
import com.example.wawe.restaurantClasses.RestaurantSearch;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcels;

import java.text.ParseException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.ViewHolder>{


    Context context;
    List<Restaurant> favoriteRestaurants;

    public FavoritesAdapter (Context context, List<Restaurant> favoriteRestaurants){
        this.context = context;
        this.favoriteRestaurants = favoriteRestaurants;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_favorite, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Restaurant restaurant = favoriteRestaurants.get(position);
        holder.bind(restaurant);
    }

    @Override
    public int getItemCount() {
        return favoriteRestaurants.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvItemName;
        TextView tvAddressItem;
        TextView tvPriceItem;
        RatingBar rbVoteAverageItem;
        ImageView ivRestItemImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItemName = itemView.findViewById(R.id.tvItemName);
            tvAddressItem = itemView.findViewById(R.id.tvAddressItem);
            tvPriceItem = itemView.findViewById(R.id.tvPriceItem);
            rbVoteAverageItem = itemView.findViewById(R.id.rbVoteAverageItem);
            ivRestItemImage = itemView.findViewById(R.id.ivRestItemImage);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Restaurant restaurant = favoriteRestaurants.get(position);
                Intent intent = new Intent(context, RestaurantActivity.class);
                intent.putExtra("restaurant", Parcels.wrap(restaurant));
                context.startActivity(intent);
            }
        }

        public void bind(Restaurant restaurant) {
            tvItemName.setText(restaurant.getName());
            tvPriceItem.setText(restaurant.getPrice());
            tvAddressItem.setText(restaurant.getLocation().getAddress());
            rbVoteAverageItem.setRating((float) restaurant.getRating());
            Glide.with(context)
                    .load(restaurant.getRestaurantImage()).into(ivRestItemImage);
        }
    }

    public void clear() {
        favoriteRestaurants.clear();
        notifyDataSetChanged();
    }

}
