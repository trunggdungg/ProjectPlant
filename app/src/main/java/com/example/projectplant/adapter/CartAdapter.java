package com.example.projectplant.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.example.projectplant.R;
import com.example.projectplant.model.Cart;
import com.example.projectplant.ui.cart.CartFragment;

import java.text.DecimalFormat;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {
    Context context;
    List<Cart> cartList;

    private CartFragment cartFragment;

    public CartAdapter(Context context, List<Cart> cartList, CartFragment cartFragment) {
        this.context = context;
        this.cartList = cartList;
        this.cartFragment = cartFragment;
    }

    @NonNull
    @Override
    //onCreateViewHolder() được gọi khi RecyclerView cần một ViewHolder mới của kiểu đã chỉ định
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Cart cart = cartList.get(position);
        // Hiển thị thông tin sản phẩm
        holder.productNameTextView.setText(cart.getName_tree());
        holder.productQuantityTextView.setText(cart.getQuantity() + " ");
        Glide.with(context)
                .load(cart.getImage_tree())
                .error(R.drawable.product)
                .into(holder.productImageView);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.productPriceTextView.setText(decimalFormat.format(cart.getPrice_tree()) + " VND");
        // Tính tổng tiền
        float totalPrice = cart.getQuantity() * cart.getPrice_tree();
        holder.totalPriceText2.setText("Tổng: " + decimalFormat.format(totalPrice) + " VND");

        // Sự kiện thêm và bớt sản phẩm
        holder.addButton.setOnClickListener(v -> {
            cart.setQuantity(cart.getQuantity() + 1);
            holder.quantityText.setText(String.valueOf(cart.getQuantity()));
            cartFragment.calculateTotalPrice();  // Cập nhật tổng tiền
            // Tính toán lại tổng giá cho sản phẩm và cập nhật UI
            float newTotalPrice = cart.getQuantity() * cart.getPrice_tree();
            holder.totalPriceText2.setText("Tổng: " + decimalFormat.format(newTotalPrice) + " VND");
        });

        holder.removeButton.setOnClickListener(v -> {
            if (cart.getQuantity() > 1) {
                cart.setQuantity(cart.getQuantity() - 1);
                holder.quantityText.setText(String.valueOf(cart.getQuantity()));
                cartFragment.calculateTotalPrice();  // Cập nhật tổng tiền
                // Tính toán lại tổng giá cho sản phẩm và cập nhật UI
                float newTotalPrice = cart.getQuantity() * cart.getPrice_tree();
                holder.totalPriceText2.setText("Tổng: " + decimalFormat.format(newTotalPrice) + " VND");
            }
        });

        //
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageButton addButton, removeButton;
        ImageView productImageView;
        TextView productNameTextView, productPriceTextView, productQuantityTextView, totalPriceText2, quantityText;
        // Ánh xạ
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            productImageView = itemView.findViewById(R.id.productImageView);
            productNameTextView = itemView.findViewById(R.id.productName);
            productPriceTextView = itemView.findViewById(R.id.productPrice);
            productQuantityTextView = itemView.findViewById(R.id.quantityText);
            totalPriceText2 = itemView.findViewById(R.id.totalPriceText2);
            addButton = itemView.findViewById(R.id.addButton);
            quantityText = itemView.findViewById(R.id.quantityText);
            removeButton = itemView.findViewById(R.id.removeButton);
        }
    }
}
