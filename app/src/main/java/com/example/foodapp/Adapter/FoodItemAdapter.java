package com.example.foodapp.Adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapp.Model.FoodItem;
import com.example.foodapp.R;

import java.util.ArrayList;
import java.util.List;

public class FoodItemAdapter extends RecyclerView.Adapter<FoodItemAdapter.ViewHolder> {
    private List<FoodItem> foodItemList;

    public FoodItemAdapter(List<FoodItem> foodItemList) {
        this.foodItemList = foodItemList;
    }

    @NonNull
    @Override
    public FoodItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item_recycler, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodItemAdapter.ViewHolder holder, int position) {
        FoodItem foodItem = foodItemList.get(position);
        holder.foodNameTextView.setText(foodItem.getName());
        holder.foodPriceTextView.setText("Rs." + foodItem.getPrice());
        holder.foodDescription.setText(foodItem.getDescription());

//        holder.itemView.setOnClickListener(v -> {
//            foodItem.setSelected(!foodItem.isSelected());
//            notifyDataSetChanged();
//        });

        holder.AddToCart.setOnClickListener(view -> {
            foodItem.setSelected(!foodItem.isSelected());
            notifyDataSetChanged();
        });

      //  holder.itemView.setBackgroundColor(foodItem.isSelected() ? Color.GRAY : Color.WHITE);
        holder.AddToCart.setText(foodItem.isSelected() ? "Added" : "Add To Cart");
//        holder.AddToCart.setBackgroundColor(foodItem.isSelected() ? Color.GREEN : Color.WHITE);
    }

    @Override
    public int getItemCount() {
        return foodItemList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView foodNameTextView;
        TextView foodPriceTextView;
        TextView foodDescription,AddToCart;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            foodNameTextView = itemView.findViewById(R.id.food_name);
            foodPriceTextView = itemView.findViewById(R.id.food_price);
            foodDescription = itemView.findViewById(R.id.food_desc);
            AddToCart = itemView.findViewById(R.id.tv_addToCart);
        }
    }

    public List<FoodItem> getSelectedItems() {
        List<FoodItem> selectedItems = new ArrayList<>();
        for (FoodItem item : foodItemList) {
            if (item.isSelected()) {
                selectedItems.add(item);
            }
        }
        return selectedItems;
    }
}

