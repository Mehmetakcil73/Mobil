package com.example.proje;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.proje.Model.User;
import com.example.proje.ViewModel.UserViewModel;

public class RegisterActivity extends AppCompatActivity {

    private EditText edtUsername, edtPassword, edtEmail;
    private TextView tvRegisterTitle;
    private UserViewModel userViewModel;
    private boolean isAdminRegistration = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        isAdminRegistration = getIntent().getBooleanExtra("isAdminRegistration", false);

        initializeViews();
        setupUI();
        setupClickListeners();
    }

    private void initializeViews() {
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        edtEmail = findViewById(R.id.edtEmail);
        tvRegisterTitle = findViewById(R.id.tv_register_title);
        Button btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(v -> handleRegistration());
    }

    private void setupUI() {
        if (isAdminRegistration) {
            tvRegisterTitle.setText("Admin Kayıt");
        }
    }

    private void setupClickListeners() {
        findViewById(R.id.btnRegister).setOnClickListener(v -> handleRegistration());
    }

    private void handleRegistration() {
        String username = edtUsername.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Lütfen tüm alanları doldurun", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean success;
        if (isAdminRegistration) {
            success = userViewModel.registerAdmin(username, password, email);
        } else {
            success = userViewModel.register(username, password, email);
        }

        if (success) {
            String message = isAdminRegistration ? 
                "Admin kaydı başarılı. Giriş yapabilirsiniz." : 
                "Kayıt başarılı. Giriş yapabilirsiniz.";
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        } else {
            Toast.makeText(this, "Bu kullanıcı adı zaten alınmış.", Toast.LENGTH_SHORT).show();
        }
    }
}
