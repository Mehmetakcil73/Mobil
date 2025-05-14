package com.example.proje.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.proje.LoginActivity;
import com.example.proje.Model.User;
import com.example.proje.SessionManager.SessionManager;
import com.example.proje.ViewModel.UserViewModel;
import com.example.proje.databinding.FragmentProfileBinding;

public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;
    private UserViewModel userViewModel;
    private String currentUsername;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        SessionManager sessionManager = new SessionManager(requireContext());
        currentUsername = sessionManager.getUsername();

        if (currentUsername == null) {
            Toast.makeText(requireContext(), "Kullanıcı adı bulunamadı. Lütfen tekrar giriş yapın.", Toast.LENGTH_SHORT).show();
            sessionManager.clearSession();
            startActivity(new Intent(requireContext(), LoginActivity.class));
            requireActivity().finish();
            return binding.getRoot();
        }

        userViewModel.getUser(currentUsername).observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                binding.edtUsername.setText(user.getUsername());
                binding.edtEmail.setText(user.getEmail());
                binding.edtPassword.setText(user.getPassword());
            }
        });

        binding.btnUpdate.setOnClickListener(v -> {
            String updatedUsername = binding.edtUsername.getText().toString();
            String updatedPassword = binding.edtPassword.getText().toString();
            String updatedEmail = binding.edtEmail.getText().toString();

            User updatedUser = new User(updatedUsername, updatedPassword, updatedEmail);
            userViewModel.updateUser(updatedUser);

            Toast.makeText(requireContext(), "Bilgiler güncellendi", Toast.LENGTH_SHORT).show();
        });

        binding.btnLogout.setOnClickListener(v -> {
            SessionManager sessionManager1 = new SessionManager(requireContext());
            sessionManager1.clearSession();
            Intent intent = new Intent(requireActivity(), LoginActivity.class);
            startActivity(intent);
            requireActivity().finish();
        });

        return binding.getRoot();
    }
}
