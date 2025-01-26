package com.example.foodapp;

import static android.app.PendingIntent.getActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.foodapp.Adapter.RestaurantAdapter;
import com.example.foodapp.Model.FoodItem;
import com.example.foodapp.Model.Restaurant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class OrderFoodActivity extends AppCompatActivity {

    RecyclerView recyclerOrderFood;
    private RestaurantAdapter restaurantAdapter;
    private List<Restaurant> restaurantList;

    private static final String URL = "http://10.0.2.2:5002/restaurants";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_food);

        initView();
        setupRecyclerView();
       // loadRestaurantData();
        fetchRestaurantData();
    }

    private void initView(){
        recyclerOrderFood=findViewById(R.id.recyclerOrderFood);
    }

    private void setupRecyclerView() {
        recyclerOrderFood.setLayoutManager(new LinearLayoutManager(this));
        restaurantList = new ArrayList<>();
        restaurantAdapter = new RestaurantAdapter(restaurantList);
        restaurantAdapter.setOnRestaurantClickListener(restaurant -> {
            Intent intent = new Intent(OrderFoodActivity.this, FoodMenuActivity.class);
            intent.putExtra("restaurant_name", restaurant.getName());
            intent.putExtra("restaurant_address", restaurant.getAddress());
            intent.putExtra("restaurant_rating",restaurant.getRating());
            startActivity(intent);
        });
        recyclerOrderFood.setAdapter(restaurantAdapter);
    }

//    private void loadRestaurantData() {
//        restaurantList.add(new Restaurant("A2B", "Bangalore", "4.5"));
//        restaurantList.add(new Restaurant("Burger Joint", "Hyderabad", "4.0"));
//        restaurantList.add(new Restaurant("Sushi Spot", "Hyderabad", "4.8"));
//        restaurantList.add(new Restaurant("Santhosh Dhaba", "Hyderabad", "4.2"));
//        restaurantAdapter.notifyDataSetChanged();
//    }

    private void fetchRestaurantData() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        restaurantList.clear();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject restaurantObject = response.getJSONObject(i);

                                String name = restaurantObject.getString("name");
                                String location = restaurantObject.getString("location");
                                int rating = restaurantObject.getInt("rating");

                                // Add restaurant with its food items
                                Restaurant restaurant = new Restaurant(name, location, String.valueOf(rating));
                                restaurantList.add(restaurant);
                            }
                            restaurantAdapter.notifyDataSetChanged();
                            Toast.makeText(OrderFoodActivity.this, "Data fetched successfully!", Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(OrderFoodActivity.this, "Error parsing data", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(OrderFoodActivity.this, "Error fetching data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        requestQueue.add(jsonArrayRequest);
    }

}