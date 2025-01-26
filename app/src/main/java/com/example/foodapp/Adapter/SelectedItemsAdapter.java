package com.example.foodapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.foodapp.Model.FoodItem;
import com.example.foodapp.R;

import java.util.ArrayList;

public class SelectedItemsAdapter extends RecyclerView.Adapter<SelectedItemsAdapter.ViewHolder> {

    private ArrayList<FoodItem> selectedItems;

    public SelectedItemsAdapter(ArrayList<FoodItem> selectedItems) {
        this.selectedItems = selectedItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_selected, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FoodItem item = selectedItems.get(position);
        holder.txtItemName.setText(item.getName());
        holder.txtItemPrice.setText(String.format("Rs.%.2f", item.getPrice()));
    }

    @Override
    public int getItemCount() {
        return selectedItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtItemName;
        public TextView txtItemPrice;

        public ViewHolder(View itemView) {
            super(itemView);
            txtItemName = itemView.findViewById(R.id.txtItemName);
            txtItemPrice = itemView.findViewById(R.id.txtItemPrice);
        }
    }
}
