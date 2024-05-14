package com.example.ezemkakofie.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ezemkakofie._Helper;
import com.example.ezemkakofie.adapters.TopPicksAdapter;
import com.example.ezemkakofie.databinding.ActivitySearchBinding;
import com.example.ezemkakofie.models.ResponseModel;

import org.json.JSONArray;
import org.json.JSONException;

public class SearchActivity extends AppCompatActivity {

    private ActivitySearchBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ResponseModel responseModel = _Helper.httpHelper("coffee?search=" + binding.edtSearch.getText().toString().trim());

                if (responseModel.code == 200) {
                    try {
                        TopPicksAdapter topPicksAdapter = new TopPicksAdapter(new JSONArray(responseModel.data));
                        binding.recyclerView.setAdapter(topPicksAdapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        getData();
    }

    private void getData() {
        ResponseModel responseModel = _Helper.httpHelper("coffee");

        if (responseModel.code == 200) {
            try {
                TopPicksAdapter topPicksAdapter = new TopPicksAdapter(new JSONArray(responseModel.data));
                binding.recyclerView.setAdapter(topPicksAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void clickBack(View view) {
        finish();
    }
}