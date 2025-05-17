package com.example.proje;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.proje.Model.User;
import com.example.proje.SessionManager.SessionManager;
import com.example.proje.ViewModel.UserViewModel;

public class LoginActivity extends AppCompatActivity {
    private SessionManager sessionManager;
    private EditText edtUsername, edtPassword;
    private UserViewModel userViewModel;
    private Button btnLogin, btnRegister, btnGuestLogin;
    private TextView tvLoginTitle;
    private boolean isAdminMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        sessionManager = new SessionManager(this);

        if (userViewModel.isLoggedIn() || sessionManager.isGuest()) {
            navigateToMain();
            return;
        }

        initializeViews();
        setupClickListeners();
    }

    private void initializeViews() {
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        btnGuestLogin = findViewById(R.id.btnGuestLogin);
        tvLoginTitle = findViewById(R.id.tv_login_title);
    }

    private void setupClickListeners() {
        btnLogin.setOnClickListener(v -> handleLogin());
        btnRegister.setOnClickListener(v -> handleRegister());
        btnGuestLogin.setOnClickListener(v -> {
            sessionManager.setGuest(true);
            Toast.makeText(this, "Misafir olarak devam ediliyor", Toast.LENGTH_SHORT).show();
            navigateToMain();
        });
    }

    private void handleLogin() {
        String username = edtUsername.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (username.equals("admin") && password.equals("admin")) {
            switchToAdminMode();
            return;
        }

        User user = userViewModel.login(username, password);
        if (user != null) {
            if (isAdminMode && !user.isAdmin()) {
                Toast.makeText(this, "Bu bir admin hesabı değil", Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(this, "Giriş başarılı", Toast.LENGTH_SHORT).show();
            navigateToMain();
        } else {
            Toast.makeText(this, "Kullanıcı adı veya şifre hatalı", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleRegister() {
        Intent intent = new Intent(this, RegisterActivity.class);
        if (isAdminMode) {
            intent.putExtra("isAdminRegistration", true);
        }
        startActivity(intent);
    }

    private void switchToAdminMode() {
        isAdminMode = true;
        tvLoginTitle.setText("Admin Giriş");
        btnRegister.setText("Admin Kayıt");
        btnGuestLogin.setVisibility(View.GONE);
        edtUsername.setText("");
        edtPassword.setText("");
        Toast.makeText(this, "Admin giriş moduna geçildi", Toast.LENGTH_SHORT).show();
    }

    private void navigateToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
