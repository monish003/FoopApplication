package com.example.foodapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
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
import com.example.foodapp.Adapter.TiffenServiceAdapter;
import com.example.foodapp.Model.Restaurant;
import com.example.foodapp.Model.kitchen;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TiffenServiceActivity extends AppCompatActivity {

    private RecyclerView RecyclerTiffenService;
    ArrayList<kitchen> kitchenList;
    private TextView tv_title;
    private TiffenServiceAdapter kitchenAdapter;


    private static final String TiffenDataURL = "http://10.0.2.2:5002/tiffin-centers";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tiffen_service);
        initViews();
        setRecyclerTiffenService();
        fetchTiffins();
    }

    private void initViews(){
        RecyclerTiffenService = findViewById(R.id.RecyclerTiffenService);
        tv_title=findViewById(R.id.tv_title);
        tv_title.setText(getText(R.string.tiffenService));
    }

    private void setRecyclerTiffenService(){
        kitchenList = new ArrayList<>();
//        kitchenList.add(new kitchen("Student Tiffen", "Note : Non Refundable: minimum 31 tiffen units.. validity 45 days ", 1,75,1000.00,500,"Date \nNov 02 - Dec 03,2024"));
//        kitchenList.add(new kitchen("Classic Tiffen", "Note : Non Refundable: minimum 31 tiffen units.. validity 45 days ", 2,100,1350.00,1700,"Date \nNov 03 - Dec 03,2024"));
//        kitchenList.add(new kitchen("Excutive Tiffen", "Note : Non Refundable: minimum 31 tiffen units.. validity 45 days ", 3,145,2400.00,2300,"Date \nNov 03 - Dec 03,2024"));

        RecyclerTiffenService.setLayoutManager(new LinearLayoutManager(this));
        kitchenAdapter = new TiffenServiceAdapter(kitchenList, new TiffenServiceAdapter.OnKitchenClickListener() {
            @Override
            public void onKitchenClick(kitchen kitchen) {
                Intent intent = new Intent(TiffenServiceActivity.this, FoodItemsActivity.class);
                intent.putExtra("kitchenId", kitchen.getId());
                startActivity(intent);
            }
        });
        RecyclerTiffenService.setAdapter(kitchenAdapter);
    }

    private void fetchTiffins() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                TiffenDataURL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        kitchenList.clear();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject tiffinDataObject = response.getJSONObject(i);

                                String name = tiffinDataObject.getString("name");
                                String description = tiffinDataObject.getString("description");
                                int lunchPrice = tiffinDataObject.getInt("lunch_price");
                                int dinnerPrice = tiffinDataObject.getInt("dinner_price");
                                String dateRange = tiffinDataObject.getString("date_range");

                                kitchenList.add(new kitchen(name,description,0,0,lunchPrice,dinnerPrice,dateRange));
                            }
                            kitchenAdapter.notifyDataSetChanged();
                            Toast.makeText(TiffenServiceActivity.this, "Data fetched successfully!", Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(TiffenServiceActivity.this, "Error parsing data", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(TiffenServiceActivity.this, "Error fetching data", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        requestQueue.add(jsonArrayRequest);
    }


}