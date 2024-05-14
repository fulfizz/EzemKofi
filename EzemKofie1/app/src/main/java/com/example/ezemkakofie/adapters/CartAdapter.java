package com.example.ezemkakofie.adapters;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ezemkakofie._Helper;
import com.example.ezemkakofie.databinding.ListCartBinding;
import com.example.ezemkakofie.models.ResponseModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.VH> {
    private List<_Helper.CartModel> models;

    public CartAdapter(List<_Helper.CartModel> cartItems) {
        this.models = cartItems;
    }

    @NonNull
    @Override
    public CartAdapter.VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(ListCartBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.VH holder, int position) {
        _Helper.CartModel cartItem = models.get(position);
        holder.binding.tvSize.setText(String.format("Size: %s", cartItem.size));
        holder.binding.tvPrice.setText(String.format("%.2f", cartItem.getPrice()));
        holder.binding.tvQuantity.setText(String.valueOf(cartItem.qty));

        ResponseModel responseModel = _Helper.httpHelper("coffee/" + cartItem.coffeeId);
        if (responseModel.code == 200) {
            try {
                JSONObject jsonObject = new JSONObject(responseModel.data);
                holder.binding.tvName.setText(jsonObject.getString("name"));
                holder.binding.tvCategory.setText(jsonObject.getString("category"));
                _Helper.httpGetImage(holder.itemView.getContext(), jsonObject.getString("imagePath"), holder.binding.imageView);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        holder.binding.tvMin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartItem.qty--;

                if (cartItem.qty < 1) {
                    AlertDialog.Builder ad = new AlertDialog.Builder(holder.itemView.getContext());
                    ad.setTitle("Are you sure want to remove the item?");

                    ad.setItems(new CharSequence[]{"Yes", "No"}, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == 0) {
                                models.remove(cartItem);
                                Toast.makeText(holder.itemView.getContext(), "Item deleted", Toast.LENGTH_SHORT).show();
                            } else {
                                cartItem.qty = 1;
                            }
                        }
                    });

                    notifyDataSetChanged();
                    ad.show();
                }
            }
        });

        holder.binding.tvPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartItem.qty++;
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class VH extends RecyclerView.ViewHolder {
        private ListCartBinding binding;

        public VH(ListCartBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
