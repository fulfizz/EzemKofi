package com.example.ezemkakofie.activities;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.ezemkakofie.R;
import com.example.ezemkakofie._Helper;
import com.example.ezemkakofie.databinding.ActivityCoffeDetailBinding;
import com.example.ezemkakofie.models.ResponseModel;

import org.json.JSONException;
import org.json.JSONObject;

public class CoffeDetailActivity extends AppCompatActivity {

    public static JSONObject selectedCoffe;
    private double originalPrice;
    private ActivityCoffeDetailBinding binding;
    private Button selectSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCoffeDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnM.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
        binding.btnM.setTextColor(ContextCompat.getColor(this, R.color.black));
        selectSize = binding.btnM;
        animationImage(1.0f);

        getData();
    }

    private void getData() {
        try {
            ResponseModel responseModel = _Helper.httpHelper("coffee/" + selectedCoffe.getInt("id"));

            if (responseModel.code == 200) {
                JSONObject jsonObject = new JSONObject(responseModel.data);
                originalPrice = jsonObject.getDouble("price");
                binding.tvTitle.setText(jsonObject.getString("name"));
                binding.tvRate.setText(jsonObject.getString("rating"));
                binding.tvDescription.setText(jsonObject.getString("description"));
                binding.tvPrice.setText(String.format("%.2f", originalPrice));
                binding.tvDescription.setText(jsonObject.getString("description"));
                _Helper.httpGetImage(this, jsonObject.getString("imagePath"), binding.imageView);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void clickMin(View view) {
        int qty = Integer.parseInt(binding.tvQuantity.getText().toString());

        if (qty == 1) return;

        binding.tvQuantity.setText(String.valueOf(qty - 1));
    }

    public void clickPlus(View view) {
        binding.tvQuantity.setText(String.valueOf(Integer.parseInt(binding.tvQuantity.getText().toString()) + 1));
    }

    public void clickAddToCart(View view) {
        try {
            int coffeeId = selectedCoffe.getInt("id");
            String size =  selectSize.getText().toString();
            int qty = Integer.parseInt(binding.tvQuantity.getText().toString());

            boolean itemExist = false;
            for (_Helper.CartModel item : _Helper.cartModels) {
                if (item.coffeeId == coffeeId && item.size.equals(size)) {
                    item.updateQuantity(qty);
                    itemExist = true;
                    break;
                }
            }

            if (!itemExist) {
                _Helper.CartModel models = new _Helper.CartModel();

                models.coffeeId = coffeeId;
                models.size = size;
                models.qty = qty;
                models.price = Double.parseDouble(binding.tvPrice.getText().toString());
                _Helper.cartModels.add(models);
            }

            Toast.makeText(this, "Successfully add to cart!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, CartActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void clickS(View view) {
        ColorButton(binding.btnS);
        binding.tvPrice.setText(String.format("%.2f", originalPrice * 0.85));
        animationImage(0.85f);
    }

    public void clickM(View view) {
        ColorButton(binding.btnM);
        binding.tvPrice.setText(String.valueOf(originalPrice));
        animationImage(1.0f);
    }

    public void clickL(View view) {
        ColorButton(binding.btnL);
        binding.tvPrice.setText(String.format("%.2f", originalPrice * 1.15));
        animationImage(1.15f);
    }

    private void ColorButton(Button button) {
        if (selectSize != null) {
            selectSize.setBackgroundColor(ContextCompat.getColor(this, R.color.green));
            selectSize.setTextColor(ContextCompat.getColor(this, R.color.white));
        }
        selectSize = button;

        button.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
        button.setTextColor(ContextCompat.getColor(this, R.color.black));
    }

    private void animationImage(float scale) {
        ImageView imageView = binding.imageView;

        ObjectAnimator scaleAnimatorX = ObjectAnimator.ofFloat(imageView, "scaleX", scale);
        ObjectAnimator scaleAnimatorY = ObjectAnimator.ofFloat(imageView, "scaleY", scale);
        scaleAnimatorX.setDuration(400);
        scaleAnimatorY.setDuration(400);
        scaleAnimatorX.setInterpolator(new AccelerateDecelerateInterpolator());
        scaleAnimatorY.setInterpolator(new AccelerateDecelerateInterpolator());

        ObjectAnimator rotateAnimator = ObjectAnimator.ofFloat(imageView, "rotation", 0f, 360f);
        rotateAnimator.setDuration(400);
        rotateAnimator.setInterpolator(new AccelerateDecelerateInterpolator());

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(scaleAnimatorX, scaleAnimatorY, rotateAnimator);
        animatorSet.start();
    }

    public void clickBack(View view) {
        finish();
    }

    @Override
    public void onBackPressed(){
        if (_Helper.backPressedTime + 2000 > System.currentTimeMillis()) {
            finishAffinity();

            super.onBackPressed();
        } else{
            Toast.makeText(this, "Press again to exit.", Toast.LENGTH_SHORT).show();
        }

        _Helper.backPressedTime = System.currentTimeMillis();
    }
}