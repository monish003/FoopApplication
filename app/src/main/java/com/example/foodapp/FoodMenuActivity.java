package com.example.foodapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.foodapp.Adapter.FoodItemAdapter;
import com.example.foodapp.Fragments.CheckOutFragment;
import com.example.foodapp.Model.FoodItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FoodMenuActivity extends AppCompatActivity {


    private RecyclerView recyclerFoodMenu;
    private FoodItemAdapter foodItemAdapter;
    private List<FoodItem> foodItemList;
    private Button btnCheckout;

    private static final String URL = "http://10.0.2.2:5002/restaurants";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_menu);

        initView();
        setupRecyclerView();
       // loadFoodData();
        fetchFoodData();
        checkOutButtonClickListener();
    }

    private void initView() {
        recyclerFoodMenu = findViewById(R.id.recyclerFoodMenu);
        btnCheckout = findViewById(R.id.btnCheckout);
    }

    private void setupRecyclerView() {
        recyclerFoodMenu.setLayoutManager(new GridLayoutManager(this,1));
        foodItemList = new ArrayList<>();
        foodItemAdapter = new FoodItemAdapter(foodItemList);
        recyclerFoodMenu.setAdapter(foodItemAdapter);
    }

//    private void loadFoodData() {
//        foodItemList.add(new FoodItem("Pizza", 150,"basmati rice, sorted vegetables, afghani specs, slow cooked"));
//        foodItemList.add(new FoodItem("Burger", 100,"basmati rice, sorted vegetables, afghani specs, slow cooked"));
//        foodItemList.add(new FoodItem("Pasta", 120,"basmati rice, sorted vegetables, afghani specs, slow cooked"));
//        foodItemList.add(new FoodItem("Salad", 80,"basmati rice, sorted vegetables, afghani specs, slow cooked"));
//        foodItemList.add(new FoodItem("Sushi", 200,"basmati rice, sorted vegetables, afghani specs, slow cooked"));
//        foodItemAdapter.notifyDataSetChanged();
//    }

    private void fetchFoodData() {
        // Get the restaurant_name from the Intent
        String restaurantName = getIntent().getStringExtra("restaurant_name");

        if (restaurantName == null || restaurantName.isEmpty()) {
            Toast.makeText(this, "Invalid restaurant name", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        // Make a request to the /restaurants endpoint to fetch all restaurant data
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        foodItemList.clear();  // Clear previous data
                        boolean found = false;

                        try {
                            // Loop through the list of restaurants
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject restaurantObject = response.getJSONObject(i);

                                // Get the name of the current restaurant
                                String currentRestaurantName = restaurantObject.getString("name");

                                // Check if this restaurant matches the one selected by the user
                                if (currentRestaurantName.equalsIgnoreCase(restaurantName)) {
                                    found = true;

                                    // Extract food items for this restaurant
                                    JSONArray foodItemsArray = restaurantObject.getJSONArray("foodItems");

                                    // Loop through the food items of this restaurant
                                    for (int j = 0; j < foodItemsArray.length(); j++) {
                                        JSONObject foodItemObject = foodItemsArray.getJSONObject(j);

                                        // Accessing food details
                                        String foodName = foodItemObject.getString("foodName");
                                        String foodDescription = foodItemObject.getString("foodDescription");

                                        // Convert price from String to double
                                        String priceString = foodItemObject.getString("price");
                                        double price = 0;
                                        try {
                                            price = Double.parseDouble(priceString); // Parse the price as a double
                                        } catch (NumberFormatException e) {
                                            e.printStackTrace();
                                            Toast.makeText(FoodMenuActivity.this, "Invalid price format", Toast.LENGTH_SHORT).show();
                                        }

                                        // Add the food item to the list
                                        foodItemList.add(new FoodItem(foodName, price, foodDescription));
                                    }
                                    break;  // Exit loop once the restaurant is found
                                }
                            }

                            if (!found) {
                                Toast.makeText(FoodMenuActivity.this, "Restaurant not found", Toast.LENGTH_SHORT).show();
                            }

                            // Notify adapter to update RecyclerView
                            foodItemAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(FoodMenuActivity.this, "Error parsing food data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(FoodMenuActivity.this, "Error fetching food data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        requestQueue.add(jsonArrayRequest);
    }


    private void goToCheckout() {
        List<FoodItem> selectedItems = foodItemAdapter.getSelectedItems();
        if (selectedItems.isEmpty()) {
            Toast.makeText(this, "No items selected", Toast.LENGTH_SHORT).show();
            return;
        }

        double totalPrice = 0;
        for (FoodItem item : selectedItems) {
            totalPrice += item.getPrice();
        }

        Intent intent = new Intent(FoodMenuActivity.this, CheckoutActivity.class);
        intent.putExtra("selectedItems", new ArrayList<>(selectedItems));
        intent.putExtra("totalPrice", totalPrice);
        startActivity(intent);
    }

    private void checkOutButtonClickListener(){
        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToCheckout();
            }
        });
    }
}