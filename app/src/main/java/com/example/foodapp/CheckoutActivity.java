package com.example.foodapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.foodapp.Adapter.SelectedItemsAdapter;
import com.example.foodapp.DatabaseHandler.DatabaseHandler;
import com.example.foodapp.Model.FoodItem;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CheckoutActivity extends AppCompatActivity {

    RecyclerView checkoutRecycler;
    public static TextView tv_totalSum,tv_GSTAmt,tv_grandAmt;
    Button BtnCheckOut,btn_Cash,btn_UPI;
    ArrayList<FoodItem> selectedItems;
    double totalPrice;
    DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        db = new DatabaseHandler(CheckoutActivity.this);
        initViews();
        recyclerViewSetup();
        btnCheckoutListener();
    }

    private void initViews() {
        checkoutRecycler = findViewById(R.id.CheckOutRecyclerView);
        tv_totalSum = findViewById(R.id.tv_totalAmt);
        BtnCheckOut = findViewById(R.id.btn_CheckOut);
        tv_grandAmt = findViewById(R.id.tv_grandAmt);
        tv_GSTAmt = findViewById(R.id.tv_GSTAmt);
        btn_Cash = findViewById(R.id.btn_Cash);
        btn_UPI = findViewById(R.id.btn_UPI);
    }

    private void recyclerViewSetup() {
        checkoutRecycler.setLayoutManager(new LinearLayoutManager(this));

         selectedItems = (ArrayList<FoodItem>) getIntent().getSerializableExtra("selectedItems");
         totalPrice = getIntent().getDoubleExtra("totalPrice", 0.0);

        tv_totalSum.setText(String.format("Total: Rs.%.2f", totalPrice));

        tv_grandAmt.setText(getString(R.string.to_pay)+ String.valueOf(totalPrice+50));

        SelectedItemsAdapter adapter = new SelectedItemsAdapter(selectedItems);
        checkoutRecycler.setAdapter(adapter);
    }

    private void btnCheckoutListener() {
        BtnCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initiatePaymentMethods();
            }
        });
    }

    private void sendCheckoutData(ArrayList<FoodItem> selectedItems, double totalPrice, String paymentMode) {
        JSONArray itemsArray = new JSONArray();
        for (FoodItem item : selectedItems) {
            JSONObject itemObject = new JSONObject();
            try {
                itemObject.put("name", item.getName());
                itemObject.put("price", item.getPrice());
                itemObject.put("username",db.getLatestUsername());
                itemObject.put("email",db.getLatestEmail());
                itemObject.put("phoneNum",db.getLatestPhone());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            itemsArray.put(itemObject);
        }

        JSONObject payload = new JSONObject();
        try {
            payload.put("selectedItems", itemsArray);
            payload.put("totalPrice", totalPrice);
            payload.put("paymentMode",paymentMode);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = "http://10.0.2.2:5002/checkout";

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                payload,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Extract the message from the JSON response
                            String message = response.getString("message");
                            Toast.makeText(CheckoutActivity.this, message, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(CheckoutActivity.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        Toast.makeText(CheckoutActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
        );
        requestQueue.add(jsonObjectRequest);
    }


    private void showPaymentDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.upi_checkout, null);

        TextView tvPay = dialogView.findViewById(R.id.tv_pay);
        ImageView imgQRCode = dialogView.findViewById(R.id.img_qr);
        AppCompatButton btnConfirmPay = dialogView.findViewById(R.id.btn_confirmPay);

        tvPay.setText("Pay: "+ tv_grandAmt.getText().toString());

        // Generate UPI QR code
        String upiDetails = "upi://pay?pa=merchant@upi&pn=Merchant%20Name&mc=1234&tid=1234567890&url=http://merchantwebsite.com";
        String currencyCode = "INR";
        String transactionNote = "Payment for FoodApp";
        String beneficiaryName = "monish";
        String pspAccount = "8095929416@ybl";
        String upiQRCodeData = "upi://pay?pa=" + pspAccount + "&pn=" + beneficiaryName
                + "&am=" + tv_grandAmt.getText().toString()
                + "&cu=" + currencyCode +
                "&tn=" + transactionNote;
        Bitmap qrCodeBitmap = generateQRCode(upiQRCodeData);

        imgQRCode.setImageBitmap(qrCodeBitmap);

        // Create the AlertDialog
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setView(dialogView);

        // Set the dialog title and configure buttons
        dialogBuilder.setTitle("Payment");
        dialogBuilder.setCancelable(true);

        // Set confirm payment button behavior
        btnConfirmPay.setOnClickListener(v -> {
            dialogBuilder.create().dismiss();
            sendCheckoutData(selectedItems,totalPrice,"UPI");
            Toast.makeText(CheckoutActivity.this, "Payment Confirmed", Toast.LENGTH_SHORT).show();
        });

        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }

    // Method to generate QR Code from a string
    private Bitmap generateQRCode(String data) {
        try {
            MultiFormatWriter writer = new MultiFormatWriter();
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            hints.put(EncodeHintType.MARGIN, 1);

            // Encoding the string to QR code
            BitMatrix bitMatrix = writer.encode(data, BarcodeFormat.QR_CODE, 512, 512, hints);

            // Convert the BitMatrix to Bitmap
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF); // Black or white
                }
            }

            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void initiatePaymentMethods(){
        btn_Cash.setVisibility(View.VISIBLE);
        btn_UPI.setVisibility(View.VISIBLE);

        btn_UPI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPaymentDialog();

            }
        });

        btn_Cash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CheckoutActivity.this, "Cash Received", Toast.LENGTH_SHORT).show();
                sendCheckoutData(selectedItems,totalPrice,"CASH");
            }
        });
    }

}
