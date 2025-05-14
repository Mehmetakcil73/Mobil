package com.example.proje;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.proje.ViewModel.UserViewModel;

public class RegisterActivity extends AppCompatActivity {

    private EditText edtUsername, edtPassword, edtEmail;
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        edtEmail = findViewById(R.id.edtEmail);
        Button btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(v -> {
            String username = edtUsername.getText().toString();
            String password = edtPassword.getText().toString();
            String email = edtEmail.getText().toString();

            boolean success = userViewModel.register(username, password, email);
            if (success) {
                Toast.makeText(this, "Kayıt başarılı. Giriş yapabilirsiniz.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Bu kullanıcı adı zaten alınmış.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
