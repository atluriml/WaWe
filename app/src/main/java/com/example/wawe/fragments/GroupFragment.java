package com.example.wawe.fragments;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import androidx.appcompat.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wawe.Activities.MainActivity;
import com.example.wawe.Adapters.GroupAdapter;
import com.example.wawe.ParseAndDatabaseApplication;
import com.example.wawe.ParseModels.Groups;
import com.example.wawe.R;
import com.example.wawe.roomClasses.GroupsRoom;
import com.example.wawe.roomClasses.RestaurantListsDao;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;


public class GroupFragment extends Fragment {

    private SwipeRefreshLayout swipeContainer;
    private RecyclerView rvGroups;
    private GroupAdapter adapter;
    private List<Groups> groupsList;
    private TextView tvListTitle;
    RestaurantListsDao restaurantListsDao;
    SearchView searchView;

    public GroupFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_group, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        restaurantListsDao = ( (ParseAndDatabaseApplication) getContext().getApplicationContext()).getDatabase().restaurantListsDao();

        rvGroups = view.findViewById(R.id.rvGroups);
        tvListTitle = view.findViewById(R.id.tvListTitle);
        tvListTitle.setText("WaWe Groups");
        searchView = view.findViewById(R.id.svSearchGroups);
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterList(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return false;
            }
        });

        groupsList = new ArrayList<>();
        adapter = new GroupAdapter(getContext(), groupsList);
        rvGroups.setAdapter(adapter);
        rvGroups.setLayoutManager(new LinearLayoutManager(getContext()));

        if (!MainActivity.isOnline(requireContext())) {
            callAsyncGroups();
        }
        else {
            populateGroups();
        }

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                if (!MainActivity.isOnline(requireContext())) {
                    callAsyncGroups();
                }
                else {
                    populateGroups();
                }
                return false;
            }
        });

        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        swipeContainer.setEnabled(true);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.clear();
                if (!searchView.isIconified()){
                    filterList(searchView.getQuery().toString());
                }
                else if (!MainActivity.isOnline(requireContext())) {
                    callAsyncGroups();
                }
                else {
                    populateGroups();
                }
                swipeContainer.setRefreshing(false);
            }
        });
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    private void filterList(String newText) {
        List<Groups> filteredGroups = new ArrayList<>();
        for (Groups group: groupsList){
            String filterPattern = newText.toLowerCase().trim();
            if (group.getKeyName().toLowerCase().contains(filterPattern)){
                filteredGroups.add(group);
            }
        }
        if (!filteredGroups.isEmpty()){
            adapter.setFilteredList(filteredGroups);
        }
        else {
            adapter.setFilteredList(filteredGroups);
            Toast.makeText(getContext(), "No Groups Found", Toast.LENGTH_SHORT).show();
        }
    }

    private void callAsyncGroups() {
        adapter.clear();
        groupsList.clear();
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                List<GroupsRoom> groupsRooms = restaurantListsDao.groupsOffline();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < groupsRooms.size(); i++){
                            Groups groupParse = new Groups(groupsRooms.get(i));
                            groupsList.add(groupParse);
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
            }
        });
    }

    private void populateGroups() {
        groupsList.clear();
        ParseQuery<Groups> queryGroups = ParseQuery.getQuery(Groups.class);
        queryGroups.findInBackground(new FindCallback<Groups>() {
            @Override
            public void done(List<Groups> objects, ParseException e) {
                if(e == null) {
                    for (Groups object : objects) {
                        AsyncTask.execute(new Runnable() {
                            @Override
                            public void run() {
                                GroupsRoom groupsRoom = new GroupsRoom(object);
                                restaurantListsDao.insertModel(groupsRoom);
                                groupsList.add(object);
                            }
                        });
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });

    }

}
