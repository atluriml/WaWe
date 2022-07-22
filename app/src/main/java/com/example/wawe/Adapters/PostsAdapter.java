package com.example.wawe.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.wawe.Activities.MainActivity;
import com.example.wawe.ParseModels.Post;
import com.example.wawe.R;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import org.json.JSONException;

import java.util.Date;
import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    Context context;
    static List<Post> posts;

    public PostsAdapter(Context context, List<Post> posts){
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.post_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostsAdapter.ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView tvUsername;
        public TextView tvPostTitle;
        public ImageView ivPostImage;
        public TextView tvPostDescription;
        public ImageView ivProfileImage;
        public TextView tvCreationTime;
        public ImageButton imBtnIsLiked;
        public TextView tvPostLikesCount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvPostTitle = itemView.findViewById(R.id.tvPostTitle);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            ivPostImage = itemView.findViewById(R.id.ivPostImage);
            tvCreationTime = itemView.findViewById(R.id.tvCreationTime);
            tvPostDescription = itemView.findViewById(R.id.tvPostDescription);
            imBtnIsLiked = itemView.findViewById(R.id.isLiked);
            tvPostLikesCount = itemView.findViewById(R.id.tvPostLikesCount);

            itemView.setOnClickListener(this);

            imBtnIsLiked.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Post post = posts.get(position);
                        // user is liking tweet
                        try {
                            if (!post.getIsLiked(post.getLikedUsers(), ParseUser.getCurrentUser().getObjectId())) {
                                imBtnIsLiked.setImageResource(R.drawable.ic_vector_heart);
                                imBtnIsLiked.setColorFilter(Color.parseColor("#ffe0245e"));
                                post.likePost(ParseUser.getCurrentUser());
                                post.saveInBackground();
                            }
                            // user is unliking tweet
                            else {
                                imBtnIsLiked.setImageResource(R.drawable.ic_vector_heart_stroke);
                                imBtnIsLiked.setColorFilter(Color.parseColor("#000000"));
                                try {
                                    post.unLikePost(ParseUser.getCurrentUser(), post.getLikedUsers());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                post.saveInBackground();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        tvPostLikesCount.setText(post.getLikesCount(post.getLikedUsers()) + " Likes");
                    }
                }
            });

        }

        @Override
        public void onClick(View view) {}

        public void bind(Post post) {
            tvPostTitle.setText(post.getKeyTitle());
            tvPostDescription.setText(post.getKeyDescription());
            if (MainActivity.isOnline(context)) {
                try {
                    tvUsername.setText(post.getKeyUser().fetchIfNeeded().getString("username"));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                ParseFile image = post.getKeyImage();
                if (image != null) {
                    Glide.with(context).load(image.getUrl()).into(ivPostImage);
                } else {
                    ivPostImage.setVisibility(View.GONE);
                }
                Date createdAt = post.getCreatedAt();
                String timeAgo = Post.calculateTimeAgo(createdAt);
                tvCreationTime.setText(timeAgo);
                ParseFile profileImage = post.getKeyUser().getParseFile("profileImage");
                if (profileImage == null) {
                    Glide.with(context).load(R.drawable.instagram_user_outline_24).into(ivProfileImage);
                } else {
                    Glide.with(context).load(profileImage.getUrl()).into(ivProfileImage);
                }
                try {
                    if (post.getIsLiked(post.getLikedUsers(), ParseUser.getCurrentUser().getObjectId())) {
                        imBtnIsLiked.setImageResource(R.drawable.ic_vector_heart);
                        imBtnIsLiked.setColorFilter(Color.parseColor("#ffe0245e"));
                    } else {
                        imBtnIsLiked.setImageResource(R.drawable.ic_vector_heart_stroke);
                        imBtnIsLiked.setColorFilter(Color.parseColor("#000000"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                tvPostLikesCount.setText(post.getLikesCount(post.getLikedUsers()) + " Likes");
            }
            else {
                tvUsername.setText(post.getOfflineUsername());
                if (post.getOfflineProfileImage() != null){
                    Glide.with(context).load(post.getOfflineProfileImage()).into(ivProfileImage);
                }
                else {
                    Glide.with(context).load(R.drawable.instagram_user_outline_24).into(ivProfileImage);
                }
                if (post.getOfflinePostImageUrl() != null){
                    Glide.with(context).load(post.getOfflinePostImageUrl()).into(ivPostImage);
                }
                else {
                    ivPostImage.setVisibility(View.GONE);
                }
                imBtnIsLiked.setVisibility(View.GONE);
                tvPostLikesCount.setVisibility(View.GONE);
            }
        }
    }

    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }

}
