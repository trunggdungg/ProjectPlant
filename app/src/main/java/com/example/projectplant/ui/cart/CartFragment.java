package com.example.projectplant.ui.cart;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectplant.R;
import com.example.projectplant.ThanhToan;
import com.example.projectplant.adapter.CartAdapter;
import com.example.projectplant.databinding.FragmentCartBinding;
import com.example.projectplant.model.Cart;
import com.example.projectplant.utils.Utils;

import java.text.DecimalFormat;
import java.util.List;

public class CartFragment extends Fragment {
    RecyclerView recyclerView;
    Button buyButton;
    TextView totalPriceLabelTextView, totalPriceTextView;
    private FragmentCartBinding binding;
    List<Cart> cartList;
    CartAdapter cartAdapter;
    float total ;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CartViewModel cartViewModel =
                new ViewModelProvider(this).get(CartViewModel.class);

        binding = FragmentCartBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        AnhXa();

        return root;
    }

    private void AnhXa() {
        recyclerView = binding.cartRecyclerView;
        buyButton = binding.buyButton;
        totalPriceLabelTextView = binding.totalPriceLabelTextView;
        totalPriceTextView = binding.totalPriceTextView;

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(layoutManager);

        // Lấy danh sách sản phẩm trong giỏ hàng
        cartList = Utils.cartList;
        // Hiển thị danh sách sản phẩm trong giỏ hàng
        if (cartList.isEmpty()) {
            showEmptyCart();
        } else {
            // Hiển thị danh sách sản phẩm trong giỏ hàng
            showCartContent();
        }

        buyButton.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), ThanhToan.class);
            intent.putExtra("totalPrice", total);
            startActivity(intent);
        });
    }
    // Hiển thị thông báo khi giỏ hàng trống
    private void showEmptyCart() {
        recyclerView.setVisibility(View.GONE);
    }
    // Hiển thị danh sách sản phẩm trong giỏ hàng
    private void showCartContent() {
        recyclerView.setVisibility(View.VISIBLE);
        cartAdapter = new CartAdapter(getContext(), cartList,this);
        recyclerView.setAdapter(cartAdapter);
        calculateTotalPrice();
    }
    // Tính tổng tiền
    public void calculateTotalPrice() {
        total = 0;
        for (Cart item : cartList) {
            total += item.getPrice_tree() * item.getQuantity();
        }
        // Định dạng số tiền
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");

        // Hiển thị tổng tiền
        totalPriceTextView.setText(decimalFormat.format(total) + " VND");
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}