package com.example.projectplant.inf_sp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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

import java.util.Objects;

public class inf_sp extends AppCompatActivity {
    TextView tv_tensp, tv_gia, tvif_sp, quantityText;
    Button btn_cart;
    ImageView img_sp;
    ImageButton increaseButton, decreaseButton;
    Toolbar toolbar;
    private int quantity = 1;

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
        setupQuantityButtons();
    }

    private void AnhXa() {
        tv_tensp = findViewById(R.id.productName);
        tv_gia = findViewById(R.id.productPrice);
        tvif_sp = findViewById(R.id.descriptionText);
        btn_cart = findViewById(R.id.addToCartButton);
        img_sp = findViewById(R.id.productImageView);
        quantityText = findViewById(R.id.quantityText);
        decreaseButton = findViewById(R.id.decreaseButton);
        increaseButton = findViewById(R.id.increaseButton);
        toolbar = findViewById(R.id.toolbarInfo);
    }

    private void setupQuantityButtons() {
        decreaseButton.setOnClickListener(v -> updateQuantity(-1));
        increaseButton.setOnClickListener(v -> updateQuantity(1));
    }

    private void updateQuantity(int change) {
        quantity += change;
        if (quantity < 1) quantity = 1;
        if (quantity > 10) quantity = 10;
        quantityText.setText(String.valueOf(quantity));
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

    private void ActionToolBar() {
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