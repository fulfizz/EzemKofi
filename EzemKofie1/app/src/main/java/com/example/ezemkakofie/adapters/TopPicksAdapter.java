package com.example.ezemkakofie.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ezemkakofie._Helper;
import com.example.ezemkakofie.activities.CoffeDetailActivity;
import com.example.ezemkakofie.databinding.ListTopPiksBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TopPicksAdapter extends RecyclerView.Adapter<TopPicksAdapter.VH> {
    private JSONArray jsonArray;

    public TopPicksAdapter(JSONArray jsonArray){
        this.jsonArray = jsonArray;
    }

    @NonNull
    @Override
    public TopPicksAdapter.VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(ListTopPiksBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TopPicksAdapter.VH holder, int position) {
        try {
            JSONObject jsonObject = jsonArray.getJSONObject(position);

            holder.binding.tvName.setText(jsonObject.getString("name"));
            holder.binding.tvCategory.setText(jsonObject.getString("category"));
            holder.binding.tvRate.setText(jsonObject.getString("rating"));
            holder.binding.tvPrice.setText(String.format("$ %s", jsonObject.getString("price")));
            _Helper.httpGetImage(holder.itemView.getContext(), jsonObject.getString("imagePath"), holder.binding.imageView);

            holder.itemView.setOnClickListener(v -> {
                CoffeDetailActivity.selectedCoffe = jsonObject;
                holder.itemView.getContext().startActivity(new Intent(holder.itemView.getContext(), CoffeDetailActivity.class));
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return jsonArray.length();
    }

    public class VH extends RecyclerView.ViewHolder {
        private ListTopPiksBinding binding;
        public VH(ListTopPiksBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
