package com.example.ezemkakofie.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ezemkakofie._Helper;
import com.example.ezemkakofie.databinding.ActivityRegisterBinding;
import com.example.ezemkakofie.models.ResponseModel;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    public void clickRegister(View view) {
        String username = binding.edtUsername.getText().toString().trim();
        String fullName = binding.edtFullName.getText().toString().trim();
        String email = binding.edtEmail.getText().toString().trim();
        String password = binding.edtPassword.getText().toString().trim();
        String confirmPass = binding.edtConfirmPass.getText().toString().trim();

        if (username.isEmpty() || fullName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPass.isEmpty()) {
            Toast.makeText(this, "All field must be filled.", Toast.LENGTH_SHORT).show();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Email Invalid.", Toast.LENGTH_SHORT).show();
        } else if (password.length() < 4) {
            Toast.makeText(this, "Password minimal four characters.", Toast.LENGTH_SHORT).show();
        } else if (!password.equals(confirmPass)) {
            Toast.makeText(this, "Password & Confirm Password must be same.", Toast.LENGTH_SHORT).show();
        } else {
            ResisterVoid(username, fullName, email, confirmPass);
        }
    }

    private void ResisterVoid(String username, String fullName, String email, String confirmPass) {
        try {
            JSONObject registerObject = new JSONObject()
                    .put("username", username)
                    .put("fullname", fullName)
                    .put("email", email)
                    .put("password", confirmPass);

            ResponseModel register = _Helper.httpHelper("register", registerObject.toString());

            if (register.code == 200) {
                JSONObject authObject = new JSONObject()
                        .put("username", username)
                        .put("password", confirmPass);

                ResponseModel login = _Helper.httpHelper("auth", authObject.toString());
                _Helper.token = register.data;
                startActivity(new Intent(this, MainActivity.class));
            } else if (register.code == 400) {
                Toast.makeText(this, "Username already exist.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, register.data, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void clickLogin(View view) {
        finish();
    }
}