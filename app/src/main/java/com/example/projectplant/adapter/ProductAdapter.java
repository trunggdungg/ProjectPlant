package com.example.projectplant.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectplant.R;
import com.example.projectplant.model.Product;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {
    Context context;
    List<Product> array;

    public ProductAdapter(List<Product> array, Context context) {
        this.array = array;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tv_tensp,tv_gia;
        ImageView img_sanpham;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_tensp = itemView.findViewById(R.id.tv_tensp);
            tv_gia = itemView.findViewById(R.id.tv_gia);
            img_sanpham = itemView.findViewById(R.id.img_sanpham);
        }
    }
    @NonNull
    @Override
    public ProductAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Bước 1: Tạo view từ layout XML (item_sanpham) sử dụng LayoutInflater
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sanpham, parent, false);
        // Bước 2: Trả về ViewHolder để quản lý view vừa tạo
        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.MyViewHolder holder, int position) {
        Product SanPham = array.get(position);
        holder.tv_tensp.setText(SanPham.getName_tree());
        holder.tv_gia.setText(String.valueOf(SanPham.getPrice_tree()));
        byte[] imageBytes = SanPham.getImage_tree();
        if (imageBytes != null && imageBytes.length > 0) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            holder.img_sanpham.setImageBitmap(bitmap);
        } else {
            holder.img_sanpham.setImageResource(R.drawable.product);
        }
    }

    @Override
    public int getItemCount() {
        return array.size();
    }
}
