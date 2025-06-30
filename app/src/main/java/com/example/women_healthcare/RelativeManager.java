package com.example.women_healthcare;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RelativeManager extends RecyclerView.Adapter<RelativeManager.ViewHolder> {

    private Context context;
    private List<Relative> relativeList;
    private OnRelativeClickListener listener;

    public interface OnRelativeClickListener {
        void onRelativeClick(Relative relative);
    }

    public RelativeManager(Context context, List<Relative> relativeList, OnRelativeClickListener listener) {
        this.context = context;
        this.relativeList = relativeList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.relative_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Relative relative = relativeList.get(position);
        holder.name.setText(relative.getName());
        holder.phone.setText(relative.getPhone());

        holder.itemView.setOnClickListener(v -> listener.onRelativeClick(relative));
    }

    @Override
    public int getItemCount() {
        return relativeList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, phone;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.relative_name);
            phone = itemView.findViewById(R.id.relative_phone);
        }
    }
}
