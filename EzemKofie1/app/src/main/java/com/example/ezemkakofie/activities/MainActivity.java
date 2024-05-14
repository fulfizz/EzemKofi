package com.example.ezemkakofie.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.ezemkakofie._Helper;
import com.example.ezemkakofie.adapters.CategoryAdapter;
import com.example.ezemkakofie.adapters.TopPicksAdapter;
import com.example.ezemkakofie.databinding.ActivityMainBinding;
import com.example.ezemkakofie.models.ResponseModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.edtSearch.setFocusable(false);
        binding.edtSearch.setFocusableInTouchMode(false);

        LinearLayoutManager layoutCategory = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        binding.recyclerCategory.setLayoutManager(layoutCategory);
        LinearLayoutManager layoutCoffe = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        binding.recyclerCoffe.setLayoutManager(layoutCoffe);

        getMe();
        getData();

        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        if (hour >= 0 && hour < 12){
            binding.tvGreeting.setText("Good Morning");
        } else if (hour >= 12 && hour < 18) {
            binding.tvGreeting.setText("Good Afternoon");
        } else if (hour >= 18 && hour < 21) {
            binding.tvGreeting.setText("Good Evening");
        } else {
            binding.tvGreeting.setText("Good Night");
        }
    }

    private void getData() {
        ResponseModel getCategory = _Helper.httpHelper("coffee-category");
        if (getCategory.code == 200){
            try {
                CategoryAdapter categoryAdapter = new CategoryAdapter(new JSONArray(getCategory.data), binding.recyclerCoffe);
                binding.recyclerCategory.setAdapter(categoryAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        ResponseModel getTopPicks = _Helper.httpHelper("coffee/top-picks");

        if (getTopPicks.code == 200){
            try {
                TopPicksAdapter topPicksAdapter = new TopPicksAdapter(new JSONArray(getTopPicks.data));
                binding.recyclerTopPicks.setAdapter(topPicksAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void getMe() {
        ResponseModel responseModel = _Helper.httpHelper("me");

        if (responseModel.code == 200) {
            try {
                JSONObject meObject = new JSONObject(responseModel.data);
                binding.tvName.setText(meObject.getString("fullName"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (_Helper.backPressedTime + 2000 > System.currentTimeMillis()) {
            finishAffinity();

            super.onBackPressed();
        } else {
            Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();
        }

        _Helper.backPressedTime = System.currentTimeMillis();
    }

    public void clickSearch(View view) {
        startActivity(new Intent(this, SearchActivity.class));
    }

    public void clickCart(View view) {
        startActivity(new Intent(this, CartActivity.class));
    }
}