package com.example.foodapp.Adapter;

import static android.provider.Settings.System.getString;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapp.CheckoutActivity;
import com.example.foodapp.Model.FoodItem;
import com.example.foodapp.Model.kitchen;
import com.example.foodapp.R;

import java.util.ArrayList;

public class TiffenServiceAdapter extends RecyclerView.Adapter<TiffenServiceAdapter.ViewHolder> {

    private ArrayList<kitchen> kitchenList;
    private OnKitchenClickListener kitchenClickListener;
    private ArrayList<FoodItem> selectedItems = new ArrayList<>();


    public interface OnKitchenClickListener {
        void onKitchenClick(kitchen kitchen);
    }

    public TiffenServiceAdapter(ArrayList<kitchen> kitchenList, OnKitchenClickListener kitchenClickListener) {
        this.kitchenList = kitchenList;
        this.kitchenClickListener = kitchenClickListener;
    }

    @NonNull
    @Override
    public TiffenServiceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tiffen_servce_recycler, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TiffenServiceAdapter.ViewHolder holder, int position) {
        kitchen kitchen = kitchenList.get(position);
        holder.bind(kitchen, kitchenClickListener);
    }

    @Override
    public int getItemCount() {
        return kitchenList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView kitchenName;
        private TextView kitchenAddress;
        private TextView kitchenFoodPrice;
        private ImageView img_arrow;
        private CheckBox checkBoxLunch;
        private CheckBox checkBoxDinner;
        private CardView card_Date;
        private TextView tv_total, tv_date;
        private Button button_cart;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            kitchenName = itemView.findViewById(R.id.kitchenName);
            kitchenAddress = itemView.findViewById(R.id.kitchenAddress);
            kitchenFoodPrice = itemView.findViewById(R.id.tv_price);
            img_arrow = itemView.findViewById(R.id.img_arrow);
            checkBoxLunch = itemView.findViewById(R.id.checkBoxLunch);
            checkBoxDinner = itemView.findViewById(R.id.checkBoxDinner);
            card_Date = itemView.findViewById(R.id.card_Date);
            tv_total = itemView.findViewById(R.id.tv_total);
            button_cart = itemView.findViewById(R.id.button_cart);
            tv_date = itemView.findViewById(R.id.tv_date);

            img_arrow.setOnClickListener(v -> {
                int position = getAdapterPosition();
                kitchen kitchen = kitchenList.get(position);
                kitchen.setExpanded(!kitchen.isExpanded());
                notifyItemChanged(position);
            });

//            button_cart.setOnClickListener(v -> {
//                ArrayList<FoodItem> selectedItemsList = new ArrayList<>(getSelectedItems());
//                Intent intent = new Intent(itemView.getContext(), CheckoutActivity.class);
//                intent.putExtra("selectedItems", selectedItemsList);
//                intent.putExtra("totalPrice",finalTotalPrice);
//                itemView.getContext().startActivity(intent);
//            });
        }

        public void bind(kitchen kitchen, OnKitchenClickListener kitchenClickListener) {
            kitchenName.setText(kitchen.getName());
            kitchenAddress.setText(kitchen.getAddress());
            kitchenFoodPrice.setText("Per Meal - " + kitchen.getPrice() + " Rs");
            tv_date.setText(kitchen.getDate());

            checkBoxLunch.setOnCheckedChangeListener((buttonView, isChecked) ->
                    handleCheckboxChange(isChecked, "Lunch", kitchen.getLunchPrice(),""));

            checkBoxDinner.setOnCheckedChangeListener((buttonView, isChecked) ->
                    handleCheckboxChange(isChecked, "Dinner", kitchen.getDinnerPrice(),""));

            if (kitchen.isExpanded()) {
                checkBoxLunch.setVisibility(View.VISIBLE);
                checkBoxDinner.setVisibility(View.VISIBLE);
                card_Date.setVisibility(View.VISIBLE);
                tv_total.setVisibility(View.VISIBLE);
                button_cart.setVisibility(View.VISIBLE);
            } else {
                checkBoxLunch.setVisibility(View.GONE);
                checkBoxDinner.setVisibility(View.GONE);
                card_Date.setVisibility(View.GONE);
                tv_total.setVisibility(View.GONE);
                button_cart.setVisibility(View.GONE);
            }
        }

        private void handleCheckboxChange(boolean isChecked, String mealType, double price,String mealDescription) {
            String itemName = kitchenName.getText() + " - " + mealType;
            if (isChecked) {
                selectedItems.add(new FoodItem(itemName, price,mealDescription));
            } else {
                selectedItems.removeIf(item -> item.getName().equals(itemName));
            }
            updateTotal();
        }

        private void updateTotal() {
            double total = selectedItems.stream().mapToDouble(FoodItem::getPrice).sum();
            tv_total.setText(String.format("Total : %.2f Rs", total));
            button_cart.setOnClickListener(v -> {
                ArrayList<FoodItem> selectedItemsList = new ArrayList<>(getSelectedItems());
                Intent intent = new Intent(itemView.getContext(), CheckoutActivity.class);
                intent.putExtra("selectedItems", selectedItemsList);
                intent.putExtra("totalPrice", total);
                itemView.getContext().startActivity(intent);
            });
        }

        public ArrayList<FoodItem> getSelectedItems() {
            return selectedItems;
        }

    }

}
