package com.example.projectplant.ui.cart;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
import com.example.projectplant.model.User;
import com.example.projectplant.retrofit.ApiAppPlant;
import com.example.projectplant.retrofit.RetrofitClient;
import com.example.projectplant.utils.Utils;

import java.text.DecimalFormat;
import java.util.List;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Retrofit;

public class CartFragment extends Fragment {
    RecyclerView recyclerView;
    Button buyButton;
    TextView totalPriceLabelTextView, totalPriceTextView;
    private FragmentCartBinding binding;
    List<Cart> cartList;
    CartAdapter cartAdapter;
    float total ;
    ApiAppPlant apiAppPlant;
    private int id_user;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CartViewModel cartViewModel =
                new ViewModelProvider(this).get(CartViewModel.class);

        binding = FragmentCartBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        User user = Paper.book().read("user");
        if (user != null) {
            id_user = user.getId();
            Log.d("Login", "User ID: " + id_user);
        } else {
            Log.d("Login", "User not found in PaperDB");
        }

        AnhXa();
        return root;
    }

    private void AnhXa() {
        apiAppPlant = RetrofitClient.getInstance().create(ApiAppPlant.class);
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
            if (cartList.isEmpty()) {
                Toast.makeText(getContext(), "Giỏ hàng trống! Thêm sản phẩm vào giỏ hàng trước khi thanh toán.", Toast.LENGTH_SHORT).show();
            }
            else
                {
                    for (Cart cartItem : cartList) {
                        addCartToDatabase(id_user, cartItem.getId_tree(), cartItem.getQuantity(), cartItem.getPrice_tree());
                        Intent intent = new Intent(getContext(), ThanhToan.class);
                        intent.putExtra("totalPrice", total);
                        startActivity(intent);
                    }
            }
        });
    }
    private void addCartToDatabase(int id_user, int id_tree,int quantity, float price) {
        if (apiAppPlant != null) {
            compositeDisposable.add(apiAppPlant.addCart(id_user, id_tree,quantity, price)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            response -> {
                                Log.d(TAG, "Add to cart response received: " + response.toString());
                                if (response.isSuccess()) {
                                    Toast.makeText(getContext(), "Đã thêm vào giỏ hàng thành công", Toast.LENGTH_SHORT).show();
                                } else {
                                    Log.w(TAG, "Add to cart failed: " + response.getMessage());
                                    Toast.makeText(getContext(), "Không thể thêm vào giỏ hàng!" + response.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            },
                            throwable -> {
                                Log.e(TAG, "Add to cart error", throwable);
                                Toast.makeText(getContext(), "Loi them san pham!", Toast.LENGTH_SHORT).show();
                            }
                    ));
        } else {
            Toast.makeText(getContext(), "Loi!", Toast.LENGTH_SHORT).show();
        }
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