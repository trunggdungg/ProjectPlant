package com.example.projectplant;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import com.example.projectplant.model.User;
import com.example.projectplant.retrofit.ApiAppPlant;
import com.example.projectplant.retrofit.RetrofitClient;
import com.example.projectplant.utils.Utils;

import java.text.DecimalFormat;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ThanhToan extends AppCompatActivity {
    TextView TongTien, Email, Diachi;
    Toolbar toolbarThanhToan;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiAppPlant apiAppPlant;
    AppCompatButton btnDatHang;
    private int id_user;
    private String status = "Chua xu ly";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanhtoan);
        Paper.init(this);
        initView();
        innitControl();
        // Read the user from PaperDB
        User user = Paper.book().read("user");
        if (user != null) {
            id_user = user.getId();
            Log.d("Login", "User ID: " + id_user);
        } else {
            Log.d("Login", "User not found in PaperDB");
        }
    }

    private void innitControl() {
        setSupportActionBar(toolbarThanhToan);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarThanhToan.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        apiAppPlant = RetrofitClient.getInstance().create(ApiAppPlant.class);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        Float tongtien = getIntent().getFloatExtra("totalPrice", 0);

        TongTien.setText("Tong tien: " + decimalFormat.format(tongtien) + " VNĐ");
        Email.setText("Email: " + Utils.user_current.getEmail());

        // Handle the order button click
        btnDatHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the order placement
                getdonhang();
            }
        });
    }

    private void getdonhang() {
        String address_shipping = Diachi.getText().toString().trim();
        Float price = getIntent().getFloatExtra("totalPrice", 0);

        if (address_shipping.isEmpty()) {
            Toast.makeText(this, "Please provide a shipping address", Toast.LENGTH_SHORT).show();
            return;
        }

        if (apiAppPlant != null) {
            compositeDisposable.add(apiAppPlant.addbill(id_user, status, price,address_shipping)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            response -> {
                                Log.d(TAG, "Add bill response received: " + response.toString());
                                if (response.isSuccess()) {
                                    Toast.makeText(ThanhToan.this, "Order placed successfully", Toast.LENGTH_SHORT).show();
                                } else {
                                    Log.w(TAG, "Order failed: " + response.getMessage());
                                    Toast.makeText(ThanhToan.this, "Failed to place order: " + response.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            },
                            throwable -> {
                                Log.e(TAG, "Order error", throwable);
                                Toast.makeText(ThanhToan.this, "Error: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                    ));
        } else {
            Toast.makeText(this, "API is not initialized", Toast.LENGTH_SHORT).show();
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
