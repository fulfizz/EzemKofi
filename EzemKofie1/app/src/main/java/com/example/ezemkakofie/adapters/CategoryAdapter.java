package com.example.ezemkakofie.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ezemkakofie.R;
import com.example.ezemkakofie._Helper;
import com.example.ezemkakofie.databinding.ListCategoryBinding;
import com.example.ezemkakofie.models.ResponseModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.VH> {
    private RecyclerView recyclerCoffe;
    private JSONArray jsonArray;
    private Button lastClickedButton;
    public CategoryAdapter(JSONArray jsonArray, RecyclerView recyclerCoffe){
        this.jsonArray = jsonArray;
        this.recyclerCoffe = recyclerCoffe;
    }
    @NonNull
    @Override
    public CategoryAdapter.VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(ListCategoryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        try {
            JSONObject jsonObject = jsonArray.getJSONObject(position);
            holder.binding.btnCategory.setText(jsonObject.getString("name"));

            if (jsonObject.getString("name").equals("Americano")){
                try {
                    getCoffe(jsonObject.getInt("id"));

                    lastClickedButton = holder.binding.btnCategory;
                    holder.binding.btnCategory.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.green));
                    holder.binding.btnCategory.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.white));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            holder.binding.btnCategory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (lastClickedButton != null) {
                        lastClickedButton.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.lightGray));
                        lastClickedButton.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.btn));
                    }

                    lastClickedButton = holder.binding.btnCategory;
                    holder.binding.btnCategory.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.green));
                    holder.binding.btnCategory.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.white));

                    try {
                        getCoffe(jsonObject.getInt("id"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getCoffe(int coffeCategoryId) {
        ResponseModel responseModel = _Helper.httpHelper("coffee?coffeeCategoryID=" + coffeCategoryId);
        if (responseModel.code == 200) {
            try {
                CoffeAdapter coffeAdapter = new CoffeAdapter(new JSONArray(responseModel.data));
                recyclerCoffe.setAdapter(coffeAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getItemCount() {
        return jsonArray.length();
    }

    public class VH extends RecyclerView.ViewHolder {
        private ListCategoryBinding binding;
        public VH(ListCategoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
