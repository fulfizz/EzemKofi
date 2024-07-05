package com.example.ezemkakofie.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ezemkakofie._Helper;
import com.example.ezemkakofie.adapters.CartAdapter;
import com.example.ezemkakofie.databinding.ActivityCartBinding;
import com.example.ezemkakofie.models.ResponseModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CartActivity extends AppCompatActivity {

    private ActivityCartBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getData();
    }

    private void getData() {
        binding.recyclerView.setAdapter(new CartAdapter(_Helper.cartModels));
    }

    public void clickBack(View view) {
        startActivity(new Intent(this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
    }

    @Override
    public void onBackPressed(){
        if (_Helper.backPressedTime + 2000 > System.currentTimeMillis()) {
            finishAffinity();

            super.onBackPressed();
        } else {
            Toast.makeText(this, "Press again to exit.", Toast.LENGTH_SHORT).show();
        }

        _Helper.backPressedTime = System.currentTimeMillis();
    }

    public void clickCheckout(View view) {
        JSONArray jsonArray = new JSONArray();
        for (_Helper.CartModel i : _Helper.cartModels) {
            try {
                JSONObject jsonObject = new JSONObject()
                        .put("coffeeid", i.coffeeId)
                        .put("size", i.size)
                        .put("qty", i.qty);

                jsonArray.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        ResponseModel responseModel = _Helper.httpHelper("checkout", jsonArray.toString());

        if (responseModel.code == 200) {
            _Helper.cartModels.clear();
            CartAdapter cartAdapter = new CartAdapter(_Helper.cartModels);
            binding.recyclerView.setAdapter(cartAdapter);
            startActivity(new Intent(this, ReviewActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));

            Toast.makeText(this, "Successfully checkout the cart.", Toast.LENGTH_SHORT).show();
        } else if (responseModel.code == 400) {
            Toast.makeText(this, "Cart is empty.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, responseModel.data, Toast.LENGTH_SHORT).show();
        }
    }
}