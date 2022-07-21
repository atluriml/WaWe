package com.example.wawe.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.wawe.Activities.MainActivity;
import com.example.wawe.Activities.RestaurantActivity;
import com.example.wawe.Activities.RestaurantListsActivity;
import com.example.wawe.BuildConfig;
import com.example.wawe.R;
import com.example.wawe.ParseModels.Restaurant;
import com.example.wawe.RestaurantClient;
import com.example.wawe.fragments.RouletteFragment;
import com.example.wawe.YelpClasses.YelpRestaurant;
import org.parceler.Parcels;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantListAdapter.ViewHolder> {

    private static final String REST_APPLICATION_ID = BuildConfig.YELP_APPLICATION_ID;
    public static final String BASE_URL = "https://api.yelp.com/v3/";

    Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
    RestaurantClient restaurantClient = retrofit.create(RestaurantClient.class);

    Context context;
    List<Restaurant> restaurantList;
    YelpRestaurant yelpRestaurant;
    String id;

    public RestaurantListAdapter(Context context, List<Restaurant> restaurantList){
        this.context = context;
        this.restaurantList = restaurantList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_favorite_or_visited, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Restaurant restaurant = restaurantList.get(position);
        holder.bind(restaurant);
    }

    @Override
    public int getItemCount() {
        return restaurantList.size();
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
                Restaurant restaurant = restaurantList.get(position);
                if (MainActivity.isOnline(context)){
                    id = restaurant.getKeyId();
                    new Task().execute();
                }
                else {
                    Intent intent = new Intent(context, RestaurantActivity.class);
                    YelpRestaurant newYelpRest = new YelpRestaurant(restaurant);
                    intent.putExtra("restaurant", Parcels.wrap(newYelpRest));
                    context.startActivity(intent);
                }
            }
        }

        public void bind(Restaurant restaurant) {
            tvItemName.setText(restaurant.getKeyName());
            tvPriceItem.setText(restaurant.getKeyPrice());
            tvAddressItem.setText(restaurant.getKeyAddress());
            rbVoteAverageItem.setRating((float) restaurant.getKeyRating());
            Glide.with(context)
                    .load(restaurant.getKeyImage()).into(ivRestItemImage);

        }
    }

    public void clear() {
        restaurantList.clear();
        notifyDataSetChanged();
    }

    private class Task extends AsyncTask<URL, Integer, Long> {
        @Override
        protected Long doInBackground(URL... urls) {
            Call<YelpRestaurant> call = restaurantClient.searchRestaurants("Bearer " + REST_APPLICATION_ID, id);
            try {
                Response<YelpRestaurant> response = call.execute();
                yelpRestaurant = response.body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Long aLong) {
            Intent intent = new Intent(context, RestaurantActivity.class);
            intent.putExtra("restaurant", Parcels.wrap(yelpRestaurant));
            context.startActivity(intent);
        }
    }

}
