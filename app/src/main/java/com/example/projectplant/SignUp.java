package com.example.projectplant;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.projectplant.retrofit.ApiAppPlant;
import com.example.projectplant.retrofit.RetrofitClient;
import com.example.projectplant.utils.Utils;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class SignUp extends AppCompatActivity {
    private EditText etFullName, etEmail, etPassword, confirmPassword;
    private Button btnSignUp;
    private TextView textViewSignIn;

    private ApiAppPlant apiAppPlant;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initializeViews();
        setupSignUpButton();
    }

    private void initializeViews() {
        apiAppPlant = RetrofitClient.getInstance().create(ApiAppPlant.class);

        etFullName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        confirmPassword = findViewById(R.id.etConfirmPassword);
        btnSignUp = findViewById(R.id.btnSignUp);
    }

    private void setupSignUpButton() {
        if (btnSignUp == null) {
            Toast.makeText(this, "Sign up button not found", Toast.LENGTH_LONG).show();
            return;
        }

        btnSignUp.setOnClickListener(v -> {
            String fullName = etFullName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String confirmPasswordText = confirmPassword.getText().toString().trim();

            if (fullName.isEmpty()) {
                Toast.makeText(this, "Please enter your full name", Toast.LENGTH_SHORT).show();
            } else if (email.isEmpty()) {
                Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show();
            } else if (password.isEmpty()) {
                Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show();
            } else if (confirmPasswordText.isEmpty()) {
                Toast.makeText(this, "Please enter your confirm password", Toast.LENGTH_SHORT).show();
            } else if (!password.equals(confirmPasswordText)) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            } else {
                registerUser(fullName, email, password);
            }
        });
    }

    private void registerUser(String fullName, String email, String password) {
        compositeDisposable.add(apiAppPlant.dangky(fullName, email, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        userModel -> {
                            if (userModel.isSuccess()) {
                                Utils.user_current.setEmail(email);
                                Utils.user_current.setPassword(password);
                                Intent intent  = new Intent(getApplicationContext(),Login.class);
                                startActivity(intent);
                                finish();
                                Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(this, userModel.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        },
                        throwable -> {
                            Log.e(TAG, "Error: " + throwable.getMessage());
                            Toast.makeText(this, "Error: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}