package com.example.ezemkakofie.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import android.database.sqlite.SQLiteDatabase;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import androidx.core.view.WindowInsetsCompat;

import com.example.ezemkakofie.DatabaseHelper;
import com.example.ezemkakofie.R;
import com.example.ezemkakofie.databinding.ActivityReviewBinding;

import org.json.JSONObject;

public class ReviewActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_CAMERA_PERMISSION = 100;
    private Uri photoUri;

    private ActivityReviewBinding binding;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dbHelper = new DatabaseHelper(this);
    }

    public void clickBack(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }

    public void clickAddPhoto(View view) {
        checkCameraPermissionAndTakePhoto();
    }

    private void checkCameraPermissionAndTakePhoto() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        } else {
            takePhoto();
        }
    }

    private void takePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takePhoto();
            } else {
                Toast.makeText(this, "Camera permission is required to take photo", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            photoUri = getImageUri(imageBitmap);
            binding.imagePhoto.setImageBitmap(imageBitmap);

            binding.imagePhoto.setVisibility(View.VISIBLE);
            binding.tvAddPhoto.setVisibility(View.GONE);
        }
    }

    private Uri getImageUri(Bitmap bitmap) {
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Review Photo", null);
        return Uri.parse(path);
    }

    public void clickSubmit(View view) {
        String reviewText = binding.edtRating.getText().toString().trim();
        int rating = (int) binding.ratingBar.getRating();
        String photoUriString = photoUri != null ? photoUri.toString() : "";

        if (reviewText.isEmpty() || rating == 0) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        long id = dbHelper.addReview(reviewText, rating, photoUriString);
        if (id != -1) {
            Toast.makeText(this, "Thank you for your review, your input is very valuable to us.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, MainActivity.class));
        } else {
            Toast.makeText(this, "Failed to add review", Toast.LENGTH_SHORT).show();
        }
    }
}