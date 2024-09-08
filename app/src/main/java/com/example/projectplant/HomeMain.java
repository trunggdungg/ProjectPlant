package com.example.projectplant;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectplant.adapter.ProductAdapter;
import com.example.projectplant.model.Product;
import com.example.projectplant.retrofit.ApiAppPlant;
import com.example.projectplant.retrofit.RetrofitClient;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class HomeMain extends AppCompatActivity {
    Toolbar toolbarmanhinh;
    ViewFlipper viewflipper;
    RecyclerView rcv_sp;
    NavigationView nagvigationview_home;
    ListView listviewhome;
    ApiAppPlant apiAppPlant;
    List<Product> arrsp;
    ProductAdapter sanPhamAdapter;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        AnhXa();
        if (isConnected(this))
        {
            Toast.makeText(getApplicationContext(),"ok",Toast.LENGTH_LONG).show();
//         getsp();
        }
        else
        {
            Toast.makeText(getApplicationContext(),"khong co ket noi internet",Toast.LENGTH_LONG).show();
        }
    }

    private void getsp()
    {
        apiAppPlant = RetrofitClient.getInstance().create(ApiAppPlant.class);

        // Gọi API lấy danh sách sản phẩm và quản lý với RxJava
        compositeDisposable.add(apiAppPlant.getsp()
                .subscribeOn(Schedulers.io())               // Chạy trong thread nền
                .observeOn(AndroidSchedulers.mainThread())   // Hiển thị trên main thread
                .subscribe(
                        sanphamModel -> {
                            if (sanphamModel.isSuccess()) {
                                arrsp = sanphamModel.getResult();
                                sanPhamAdapter = new ProductAdapter(arrsp, getApplicationContext());
                                rcv_sp.setAdapter(sanPhamAdapter);
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), "Lỗi tải sản phẩm: " + throwable.getMessage(), Toast.LENGTH_LONG).show();
                        }
                ));
    }
    private void AnhXa()
    {
        toolbarmanhinh = findViewById(R.id.toolbarmanhinh);
        viewflipper = findViewById(R.id.viewflipper);
        rcv_sp = findViewById(R.id.rcv_sp);
        nagvigationview_home = findViewById(R.id.nagvigationview_home);
        listviewhome = findViewById(R.id.listviewhome);
        RecyclerView.LayoutManager  layoutManager = new GridLayoutManager(this,2);
        rcv_sp.setLayoutManager(layoutManager);
        rcv_sp.setHasFixedSize(true);
        //list san pham
        arrsp = new ArrayList<>();

    }
    private Boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            Network network = connectivityManager.getActiveNetwork();
            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);
            return capabilities != null && (
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR));
        } else {
            NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            return (wifi != null && wifi.isConnected()) || (mobile != null && mobile.isConnected());
        }
    }
}
