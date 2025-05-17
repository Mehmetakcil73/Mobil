package com.example.proje.UI;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.proje.Model.User;
import com.example.proje.ViewModel.UserViewModel;
import com.example.proje.databinding.DialogEditUserBinding;

public class EditUserDialogFragment extends DialogFragment {
    private DialogEditUserBinding binding;
    private UserViewModel userViewModel;
    private User user;
    private String originalUsername;

    public static EditUserDialogFragment newInstance(User user) {
        EditUserDialogFragment fragment = new EditUserDialogFragment();
        Bundle args = new Bundle();
        args.putString("username", user.getUsername());
        args.putString("email", user.getEmail());
        args.putString("password", user.getPassword());
        args.putBoolean("isAdmin", user.isAdmin());
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
        binding = DialogEditUserBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        if (getArguments() != null) {
            String username = getArguments().getString("username");
            String email = getArguments().getString("email");
            String password = getArguments().getString("password");
            boolean isAdmin = getArguments().getBoolean("isAdmin");

            originalUsername = username;
            user = new User(username, password, email, isAdmin);

            binding.edtUsername.setText(username);
            if (username.equals(userViewModel.getCurrentUser().getUsername())) {
                binding.edtUsername.setEnabled(false);
            }
            binding.edtEmail.setText(email);
            binding.edtPassword.setText(password);
            binding.switchAdmin.setChecked(isAdmin);

            if (username.equals(userViewModel.getCurrentUser().getUsername())) {
                binding.switchAdmin.setEnabled(false);
            }
        }

        binding.btnSave.setOnClickListener(v -> {
            if (validateInputs()) {
                saveUserData();
            }
        });

        binding.btnCancel.setOnClickListener(v -> dismiss());

        binding.edtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                validateEmail(s.toString());
            }
        });

        binding.edtUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                validateUsername(s.toString());
            }
        });
    }

    private boolean validateInputs() {
        String newUsername = binding.edtUsername.getText().toString().trim();
        String email = binding.edtEmail.getText().toString().trim();
        String password = binding.edtPassword.getText().toString().trim();

        if (newUsername.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(requireContext(), "Tüm alanları doldurun", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!newUsername.equals(originalUsername) && !validateUsername(newUsername)) {
            return false;
        }

        if (!validateEmail(email)) {
            return false;
        }

        if (password.length() < 6) {
            binding.edtPassword.setError("Şifre en az 6 karakter olmalıdır");
            return false;
        }

        return true;
    }

    private boolean validateEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (!email.matches(emailPattern)) {
            binding.edtEmail.setError("Geçerli bir e-posta adresi girin");
            return false;
        }
        binding.edtEmail.setError(null);
        return true;
    }

    private boolean validateUsername(String username) {
        if (!username.matches("^[a-zA-Z0-9_]{3,20}$")) {
            binding.edtUsername.setError("Kullanıcı adı 3-20 karakter arası olmalı ve sadece harf, rakam ve alt çizgi içerebilir");
            return false;
        }

        if (!username.equals(originalUsername)) {
            User existingUser = userViewModel.getUserByUsername(username);
            if (existingUser != null) {
                binding.edtUsername.setError("Bu kullanıcı adı zaten kullanılıyor");
                return false;
            }
        }

        binding.edtUsername.setError(null);
        return true;
    }

    private void saveUserData() {
        String newUsername = binding.edtUsername.getText().toString().trim();
        String email = binding.edtEmail.getText().toString().trim();
        String password = binding.edtPassword.getText().toString().trim();
        boolean isAdmin = binding.switchAdmin.isChecked();

        User updatedUser = new User(newUsername, password, email, isAdmin);
        updatedUser.setOldUsername(originalUsername);

        userViewModel.updateUser(updatedUser);
        Toast.makeText(requireContext(), "Kullanıcı bilgileri güncellendi", Toast.LENGTH_SHORT).show();
        dismiss();
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            );
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
} 