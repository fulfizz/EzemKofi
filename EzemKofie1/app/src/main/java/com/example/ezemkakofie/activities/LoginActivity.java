package com.example.ezemkakofie.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ezemkakofie._Helper;
import com.example.ezemkakofie.databinding.ActivityLoginBinding;
import com.example.ezemkakofie.models.ResponseModel;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
    }

    public void clickLogin(View view) {
        String username = binding.edtUsername.getText().toString().trim();
        String password = binding.edtPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Field can't be empty.", Toast.LENGTH_SHORT).show();
        } else {
            LoginVoid(username, password);
        }
    }

    private void LoginVoid(String username, String password) {
        try {
            JSONObject jsonObject = new JSONObject()
                    .put("username", username)
                    .put("password", password);

            ResponseModel responseModel = _Helper.httpHelper("auth", jsonObject.toString());
            
            if (responseModel.code == 200){
                _Helper.token = responseModel.data;
                startActivity(new Intent(this, MainActivity.class));
            } else if (responseModel.code == 404) {
                Toast.makeText(this, "Your data invalid, please try again.", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, responseModel.data, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void clickRegister(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
    }
}