package com.example.projectplant;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import com.example.projectplant.model.Cart;
import com.example.projectplant.model.Order;
import com.example.projectplant.model.User;
import com.example.projectplant.utils.Utils;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class ThanhToan extends AppCompatActivity {
    TextView TongTien, Email, Diachi;
    Toolbar toolbarThanhToan;
    AppCompatButton btnDatHang;
    private int id_user;
    private String status = "Chưa xử lý";
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanhtoan);
        Paper.init(this);
        initView();
        initControl();

        // Lấy thông tin người dùng từ PaperDB
        User user = Utils.user_current;
        if (user != null) {
            id_user = Utils.user_current.getId();
            Log.d("Login", "User ID: " + id_user);
        } else {
            Log.d("Login", "User not found in PaperDB");
        }
    }

    private void initControl() {
        setSupportActionBar(toolbarThanhToan);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarThanhToan.setNavigationOnClickListener(v -> finish());

        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        Float tongtien = getIntent().getFloatExtra("totalPrice", 0);

        TongTien.setText("Tong tien: " + decimalFormat.format(tongtien) + " VNĐ");
        Email.setText("Email: " + Utils.user_current.getEmail());

        // Xử lý khi người dùng nhấn nút đặt hàng
        btnDatHang.setOnClickListener(v -> placeOrder());
    }

    private void placeOrder() {
        String address_shipping = Diachi.getText().toString().trim();
        Float price = getIntent().getFloatExtra("totalPrice", 0);

        if (address_shipping.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập địa chỉ giao hàng!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Lấy danh sách giỏ hàng từ nơi lưu trữ (ví dụ: PaperDB hoặc SharedPreferences)
        List<Cart> cartList =Utils.cartList;

        // Tạo đối tượng đơn hàng
        long timestamp = System.currentTimeMillis();
        Order order = new Order(id_user, cartList, status, price, address_shipping, timestamp);

        // Lưu đơn hàng vào Firebase Realtime Database
        firebaseDatabase = FirebaseDatabase.getInstance("https://projectplant-f8356-default-rtdb.firebaseio.com/");
        databaseReference = firebaseDatabase.getReference("orders");
        String orderId = databaseReference.push().getKey();

        if (orderId != null) {
            databaseReference.child(orderId).setValue(order)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Xóa giỏ hàng sau khi đặt hàng thành công
                            Utils.cartList = new ArrayList<>();

                            Toast.makeText(ThanhToan.this, "Đơn hàng của bạn đã được đặt, vui lòng chờ phản hồi!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ThanhToan.this, MainActivity.class);
                            startActivity(intent);
                            finish(); // Đóng activity hiện tại
                        } else {
                            Toast.makeText(ThanhToan.this, "Đặt hàng thất bại!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(ThanhToan.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(this, "Không tạo được đơn hàng", Toast.LENGTH_SHORT).show();
        }
    }

    private void initView() {
        toolbarThanhToan = findViewById(R.id.toolbarThanhToan);
        TongTien = findViewById(R.id.txtTongtien);
        Email = findViewById(R.id.txtEmail);
        Diachi = findViewById(R.id.txtDiaChiGiaoHang);
        btnDatHang = findViewById(R.id.btnDatHang);
    }
}
