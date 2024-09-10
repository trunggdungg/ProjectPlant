package com.example.projectplant.ui.home;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectplant.adapter.ProductAdapter;
import com.example.projectplant.databinding.FragmentHomeBinding;
import com.example.projectplant.model.Product;
import com.example.projectplant.retrofit.ApiAppPlant;
import com.example.projectplant.retrofit.RetrofitClient;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class HomeFragment extends Fragment {
    Toolbar toolbarmanhinh;
    ViewFlipper viewflipper;
    RecyclerView rcv_sp;
    NavigationView nagvigationview_home;
    ListView listviewhome;
    ApiAppPlant apiAppPlan;
    List<Product> arrsp;
    ProductAdapter sanPhamAdapter;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        // Sử dụng view binding
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        AnhXa();

        // Kiểm tra kết nối mạng trước khi gọi API
        if (isConnected(getContext())) {
//            Toast.makeText(getContext(), "Kết nối thành công", Toast.LENGTH_SHORT).show();
            getsp(); // Gọi API để lấy sản phẩm
        } else {
            Toast.makeText(getContext(), "Không có kết nối internet", Toast.LENGTH_LONG).show();
        }

        return root;
    }

    private void getsp() {
        apiAppPlan = RetrofitClient.getInstance().create(ApiAppPlant.class);

        // Gọi API lấy danh sách sản phẩm và quản lý với RxJava
        compositeDisposable.add(apiAppPlan.getsp()
                .subscribeOn(Schedulers.io())               // Chạy trong thread nền
                .observeOn(AndroidSchedulers.mainThread())   // Hiển thị trên main thread
                .subscribe(
                        sanphamModel -> {
                            if (sanphamModel.isSuccess()) {
                                arrsp = sanphamModel.getResult();
                                sanPhamAdapter = new ProductAdapter(arrsp, getContext());
                                rcv_sp.setAdapter(sanPhamAdapter);
                            }
                        },
                        throwable -> {
                            Toast.makeText(getContext(), "Lỗi tải sản phẩm: " + throwable.getMessage(), Toast.LENGTH_LONG).show();
                        }
                ));
    }

    private void AnhXa() {

        toolbarmanhinh = binding.toolbarmanhinh;
        viewflipper = binding.viewflipper;
        rcv_sp = binding.rcvSp;
        nagvigationview_home = binding.nagvigationviewHome;
        listviewhome = binding.listviewhome;

        // Cài đặt layout cho RecyclerView
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        rcv_sp.setLayoutManager(layoutManager);
        rcv_sp.setHasFixedSize(true);

        // Khởi tạo danh sách sản phẩm
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        compositeDisposable.clear(); // Giải phóng tài nguyên khi view bị hủy
    }
}