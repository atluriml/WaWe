package com.example.wawe.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.wawe.Activities.GroupActivity;
import com.example.wawe.ParseModels.Groups;
import com.example.wawe.R;
import org.parceler.Parcels;
import java.util.List;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolder> {

    Context context;
    List<Groups> groupsList;

    public GroupAdapter(Context context, List<Groups> groups){
        this.context = context;
        this.groupsList = groups;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_group, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Groups group = groupsList.get(position);
        holder.bind(group);
    }

    @Override
    public int getItemCount() {
        return groupsList.size();
    }

    public void setFilteredList(List<Groups> filteredGroups) {
        this.groupsList = filteredGroups;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvGroupName;
        TextView tvGroupLocation;
        TextView tvDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvGroupName = itemView.findViewById(R.id.tvGroupName);
            tvGroupLocation = itemView.findViewById(R.id.tvGroupLocation);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Groups group = groupsList.get(position);
                Intent intent = new Intent(context, GroupActivity.class);
                intent.putExtra("group", Parcels.wrap(group));
                context.startActivity(intent);
            }
        }

        public void bind(Groups group) {
            tvGroupName.setText(group.getKeyName());
            tvGroupLocation.setText(group.getKeyLocation());
            tvDescription.setText(group.getKeyDescription());
        }
    }

    public void clear() {
        groupsList.clear();
        notifyDataSetChanged();
    }

}
