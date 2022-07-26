package com.example.wawe.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wawe.Adapters.PostsAdapter;
import com.example.wawe.ParseAndDatabaseApplication;
import com.example.wawe.ParseModels.Groups;
import com.example.wawe.ParseModels.Post;
import com.example.wawe.Fragments.PostDialogFragment;
import com.example.wawe.R;
import com.example.wawe.RoomClasses.PostRoom;
import com.example.wawe.RoomClasses.RestaurantListsDao;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.parceler.Parcels;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GroupActivity extends AppCompatActivity implements PostDialogFragment.DialogListener{

    TextView tvGroupName;
    TextView tvGroupLocation;
    TextView tvGroupDescription;
    ImageButton btnAddPost;
    Groups group;
    private SwipeRefreshLayout swipeContainer;
    private RecyclerView rvPosts;
    private PostsAdapter adapter;
    private List<Post> groupPosts;
    RestaurantListsDao restaurantListsDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        restaurantListsDao = ( (ParseAndDatabaseApplication) getApplicationContext()).getDatabase().restaurantListsDao();

        tvGroupName = findViewById(R.id.tvGroupTitle);
        tvGroupLocation = findViewById(R.id.tvGroupLocation);
        tvGroupDescription = findViewById(R.id.tvGroupDescription);
        btnAddPost = findViewById(R.id.btnAddPost);

        group = Parcels.unwrap(getIntent().getParcelableExtra("group"));

        tvGroupName.setText(group.getKeyName());
        tvGroupLocation.setText(group.getKeyLocation());
        tvGroupDescription.setText(group.getKeyDescription());

        rvPosts = findViewById(R.id.rvGroupPosts);
        groupPosts = new ArrayList<>();
        adapter = new PostsAdapter(this, groupPosts);
        rvPosts.setAdapter(adapter);
        rvPosts.setLayoutManager(new LinearLayoutManager(this));

        if (!MainActivity.isOnline(this)) {
            callAsyncPosts();
        }
        else {
            populatePosts();
        }

        btnAddPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!MainActivity.isOnline(GroupActivity.this)){
                    Toast.makeText(GroupActivity.this, "Must be connected to the internet to create post", Toast.LENGTH_SHORT).show();
                    return;
                }
                openDialog();
            }
        });

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.clear();
                if (!MainActivity.isOnline(GroupActivity.this)) {
                    callAsyncPosts();
                }
                else {
                    populatePosts();
                }
                swipeContainer.setRefreshing(false);
            }
        });
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    private void callAsyncPosts() {
        adapter.clear();
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                List<PostRoom> postRooms = restaurantListsDao.groupPosts(group.getObjectId());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < postRooms.size(); i++){
                            Post postParse = new Post(postRooms.get(i));
                            groupPosts.add(postParse);
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
            }
        });
    }

    public void openDialog() {
        PostDialogFragment postDialogFragment = new PostDialogFragment();
        postDialogFragment.show(getSupportFragmentManager(), "post dialog");
    }

    @Override
    public void applyTexts(String title, String description, File photoFile) {
        Post post = new Post();
        ParseUser user = ParseUser.getCurrentUser();
        post.setKeyDescription(description);
        post.setKeyTitle(title);
        post.setKeyUser(user);
        post.setKeyGroupId(group.getObjectId());
        if (photoFile != null) {
            post.setKeyImage(new ParseFile(photoFile));
        }
        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null){
                    return;
                }
            }
        });
    }

    private void populatePosts() {
        ParseQuery<Post> queryPosts = ParseQuery.getQuery(Post.class).whereEqualTo(Post.KEY_GROUP, group.getObjectId());
        queryPosts.include("Post.User");
        queryPosts.addDescendingOrder("createdAt");
        queryPosts.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                for (Post object : objects) {
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            PostRoom postRoom = new PostRoom(object);
                            restaurantListsDao.insertModel(postRoom);
                            groupPosts.add(object);
                        }
                    });
                }
                adapter.notifyDataSetChanged();
            }
        });

    }

}