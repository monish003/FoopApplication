package com.example.foodapp.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.foodapp.OrderFoodActivity;
import com.example.foodapp.ProfileActivity;
import com.example.foodapp.R;
import com.example.foodapp.TiffenServiceActivity;

public class HomeFragment extends Fragment {

    CardView CardViewOrderFood, CardViewTiffenCenter;
    View rootview;
    ImageView image_prof;


    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootview =  inflater.inflate(R.layout.fragment_home, container, false);

        initViews();
        setUpOrderFoodCardListener();
        setUpTiffenCenterCardListener();
        navigateToProfile();
        return rootview;
    }

    private void initViews(){
        CardViewOrderFood = rootview.findViewById(R.id.CardView_OrderFood);
        CardViewTiffenCenter = rootview.findViewById(R.id.CardView_TiffenCenter);
        image_prof = rootview.findViewById(R.id.image_prof);
    }

    private void setUpOrderFoodCardListener(){
        CardViewOrderFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), OrderFoodActivity.class));
            }
        });
    }

    private void setUpTiffenCenterCardListener(){
        CardViewTiffenCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), TiffenServiceActivity.class));

            }
        });
    }

    private void navigateToProfile(){
        image_prof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ProfileActivity.class));
            }
        });
    }

}