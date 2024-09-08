package com.example.projectplant.inf_sp;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.projectplant.R;
import com.example.projectplant.model.Product;

import org.w3c.dom.Text;

import java.util.Objects;

public class inf_sp extends AppCompatActivity {
 TextView tv_tensp,tv_gia,tvif_sp;
 Button btn_cart;
 ImageView img_sp;
 Spinner spinner;
 Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_inf_sp);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        AnhXa();
        ActionToolBar();
        HienThongTin();
    }

    private void AnhXa()
    {
        tv_tensp = findViewById(R.id.tv_tensp);
        tv_gia = findViewById(R.id.tv_gia);
        tvif_sp = findViewById(R.id.tvif_sp);
        btn_cart = findViewById(R.id.btn_cart);
        img_sp = findViewById(R.id.img_sp);
        spinner = findViewById(R.id.spinner);
        toolbar = findViewById(R.id.toolbar);

        // Thiết lập mảng số từ 1 đến 10
        Integer[] numbers = new Integer[10];
        for (int i = 0; i < 10; i++) {
            numbers[i] = i + 1;
        }

        // Tạo ArrayAdapter
        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, numbers);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Thiết lập Adapter cho Spinner
        spinner.setAdapter(adapter);

    }
    private void HienThongTin() {
        // Nhận đối tượng Product từ Intent
        Product product = (Product) getIntent().getSerializableExtra("info_tree");
        if (product != null) {
            tv_tensp.setText(product.getName_tree());
            tv_gia.setText(String.format("Giá: %s$", product.getPrice_tree()));
            tvif_sp.setText(product.getInfo_tree());

            // Hiển thị hình ảnh bằng Glide
            String imageBase64 = product.getImage_tree();
            if (imageBase64 != null && !imageBase64.isEmpty()) {
                byte[] imageBytes = android.util.Base64.decode(imageBase64, android.util.Base64.DEFAULT);
                Glide.with(this).load(imageBytes).into(img_sp);
            } else {
                img_sp.setImageResource(R.drawable.product);  // Ảnh mặc định
            }
        }
    }

    private  void ActionToolBar(){
        setSupportActionBar(toolbar); // Thiết lập Toolbar làm ActionBar
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true); // Hiển thị nút back
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Khi bấm nút back, đóng Activity
            }
        });
    }

}