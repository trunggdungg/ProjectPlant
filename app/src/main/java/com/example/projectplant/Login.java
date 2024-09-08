package com.example.projectplant;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.projectplant.retrofit.ApiAppPlant;
import com.example.projectplant.retrofit.RetrofitClient;
import com.example.projectplant.utils.Utils;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class Login extends AppCompatActivity {

    private TextView textViewSignUp;
    private EditText editEmail, editPassword;
    AppCompatButton btnLogin;

    ApiAppPlant apiAppPlant;
    CompositeDisposable compositeDisposable  = new CompositeDisposable();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        // Set padding for system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        initView();
        // Thiết lập listener cho TextView
        setupSignUpNavigation();
    }

    public void initView() {
        Paper.init(this);

        apiAppPlant = RetrofitClient.getInstance().create(ApiAppPlant.class);
        editEmail = findViewById(R.id.etEmail);
        editPassword= findViewById(R.id.etPassword);
        btnLogin= findViewById(R.id.btnLoginOfLog);
        textViewSignUp = findViewById(R.id.txtdangky);

        // Lấy thông tin người dùng từ Paper và hiển thị nếu có
        String email = Paper.book().read("user_email", null);
        String password = Paper.book().read("user_password", null);

        if (email != null && password != null) {
            editEmail.setText(email);
            editPassword.setText(password);
        }
    }

    private void setupSignUpNavigation() {
        textViewSignUp.setOnClickListener(view -> {
            Intent intent = new Intent(Login.this, SignUp.class);
            startActivity(intent);
        });


        btnLogin.setOnClickListener(view -> {
            String str_email = editEmail.getText().toString().trim();
            String str_password = editPassword.getText().toString().trim();

            if (TextUtils.isEmpty(str_email)) {
                Toast.makeText(getApplicationContext(), "Bạn chưa nhập Email!", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(str_password)) {
                Toast.makeText(getApplicationContext(), "Bạn chưa nhập Mật khẩu!", Toast.LENGTH_SHORT).show();
            } else {
                Log.d(TAG, "Attempting login with email: " + str_email);
                compositeDisposable.add(apiAppPlant.dangnhap(str_email, str_password)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                userModel -> {
                                    Log.d(TAG, "Login response received: " + userModel.toString());
                                    if (userModel.isSuccess()) {
                                        if (userModel.getResult() != null && !userModel.getResult().isEmpty()) {
                                            Utils.user_current = userModel.getResult().get(0);
                                            Log.d(TAG, "Login successful. User: " + Utils.user_current.toString());

                                            // Lưu thông tin người dùng vào Paper
                                            Paper.book().write("user_email", str_email);
                                            Paper.book().write("user_password", str_password);

                                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                            startActivity(intent);
                                            Toast.makeText(getApplicationContext(), "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Log.w(TAG, "User list is null or empty");
                                            Toast.makeText(getApplicationContext(), "Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Log.w(TAG, "Login failed: " + userModel.getMessage());
                                        Toast.makeText(getApplicationContext(), userModel.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                },
                                throwable -> {
                                    Log.e(TAG, "Login error", throwable);
                                    Toast.makeText(getApplicationContext(), "Lỗi đăng nhập: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                        ));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Utils.user_current != null && Utils.user_current.getEmail() != null && Utils.user_current.getPassword() != null) {
            editEmail.setText(Utils.user_current.getEmail());
            editPassword.setText(Utils.user_current.getPassword());
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

}
