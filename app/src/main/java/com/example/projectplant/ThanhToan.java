package com.example.projectplant;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import com.example.projectplant.utils.Utils;
import com.google.gson.Gson;

import java.text.DecimalFormat;

public class ThanhToan extends AppCompatActivity {
    TextView  TongTien,Email,Diachi;
    Toolbar toolbarThanhToan;

    AppCompatButton btnDatHang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanhtoan);

        initView();
        innitControl();
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

        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        Float tongtien = getIntent().getFloatExtra("totalPrice",0);

        TongTien.setText("Tong tien: " + decimalFormat.format(tongtien) + " VNĐ");
        Email.setText("Email: "+ Utils.user_current.getEmail());

        //
        btnDatHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Xử lý đặt hàng
                String str_diachi = Diachi.getText().toString().trim();
                if (str_diachi.isEmpty()){
                    Toast.makeText(ThanhToan.this, "Vui lòng nhập địa chỉ giao hàng", Toast.LENGTH_SHORT).show();
                }else {

                    Toast.makeText(ThanhToan.this, "Đặt hàng thành công", Toast.LENGTH_SHORT).show();

                    Log.d("Test",new Gson().toJson(Utils.cartList));
                }
            }
        });

    }

    private void initView() {
        toolbarThanhToan = findViewById(R.id.toolbarThanhToan);
        TongTien = findViewById(R.id.txtTongtien);
        Email = findViewById(R.id.txtEmail);
        Diachi = findViewById(R.id.txtDiaChiGiaoHang);
        btnDatHang = findViewById(R.id.btnDatHang);
    }


}
