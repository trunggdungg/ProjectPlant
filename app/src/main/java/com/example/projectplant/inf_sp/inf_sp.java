package com.example.projectplant.inf_sp;

import android.os.Bundle;
import android.util.Log;
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
import com.example.projectplant.model.Cart;
import com.example.projectplant.model.Product;
import com.example.projectplant.utils.Utils;
import com.nex3z.notificationbadge.NotificationBadge;

import java.util.Objects;

import io.paperdb.Paper;
import okhttp3.internal.Util;

public class inf_sp extends AppCompatActivity {
    TextView tv_tensp, tv_gia, tvif_sp, quantityText;
    Button btn_cart;
    ImageView img_sp;
    ImageButton increaseButton, decreaseButton;
    Toolbar toolbar;
    private int quantity = 1;

    Product product;
    NotificationBadge badge;

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
        Paper.init(this);
        AnhXa();
        ActionToolBar();
        HienThongTin();
        setupQuantityButtons();
        initControll();
    }

    private void initControll() {
        btn_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCart();
            }
        });
    }

    public void addCart(){
        if (product == null) {
            Log.e("addCart", "Product is null");
            return;
        }

        if (Utils.cartList.size() > 0){
            // Thêm sản phẩm vào giỏ hàng
            boolean flag = false;
            int quantity = Integer.parseInt(quantityText.getText().toString());
            for (int i = 0; i < Utils.cartList.size(); i++){
                if (Utils.cartList.get(i).getId_tree() == product.getId_tree()){
                    Utils.cartList.get(i).setQuantity(Utils.cartList.get(i).getQuantity() + quantity);
                    float price = product.getPrice_tree() * Utils.cartList.get(i).getQuantity();
                    Utils.cartList.get(i).setPrice_tree(price);
                    flag = true;

                }
            }
            if (!flag){
                float price = product.getPrice_tree() * quantity;
                Cart cart = new Cart();
                cart.setPrice_tree(price);
                cart.setQuantity(quantity);
                cart.setId_tree(product.getId_tree());
                cart.setName_tree(product.getName_tree());
                cart.setImage_tree(product.getImage_tree());
                Utils.cartList.add(cart);
            }
        } else {
            // Thêm sản phẩm vào giỏ hàng khi giỏ hàng rỗng
            int quantity = Integer.parseInt(quantityText.getText().toString());
            float price = product.getPrice_tree() * quantity;
            Cart cart  = new Cart();
            cart.setPrice_tree(price);
            cart.setQuantity(quantity);
            cart.setId_tree(product.getId_tree());
            cart.setName_tree(product.getName_tree());
            cart.setImage_tree(product.getImage_tree());
            Utils.cartList.add(cart);
        }
        Paper.book().write("cartList", Utils.cartList);
        badge.setText(String.valueOf(Utils.cartList.size()));
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
        badge = findViewById(R.id.badge);
        if (Utils.cartList != null){
            badge.setText(String.valueOf(Utils.cartList.size()));
        }
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
        product = (Product) getIntent().getSerializableExtra("info_tree");

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