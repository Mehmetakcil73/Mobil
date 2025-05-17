package com.example.proje.UI;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.proje.Adapter.UserAdapter;
import com.example.proje.Model.User;
import com.example.proje.ViewModel.UserViewModel;
import com.example.proje.databinding.FragmentAdminUserListBinding;

import java.util.List;

public class AdminUserListFragment extends Fragment {
    private FragmentAdminUserListBinding binding;
    private UserViewModel userViewModel;
    private UserAdapter userAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAdminUserListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        // Check if current user is admin
        if (!userViewModel.isCurrentUserAdmin()) {
            Toast.makeText(requireContext(), "Bu sayfaya erişim yetkiniz yok!", Toast.LENGTH_SHORT).show();
            requireActivity().onBackPressed();
            return;
        }

        setupRecyclerView();
        observeUsers();
    }

    private void setupRecyclerView() {
        userAdapter = new UserAdapter();
        binding.recyclerViewUsers.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerViewUsers.setAdapter(userAdapter);

        userAdapter.setOnEditClickListener(this::showEditUserDialog);
        userAdapter.setOnDeleteClickListener(this::showDeleteConfirmationDialog);
        userAdapter.setOnToggleAdminClickListener(this::toggleAdminStatus);
    }

    private void observeUsers() {
        userViewModel.getAllUsers().observe(getViewLifecycleOwner(), users -> {
            if (users != null && !users.isEmpty()) {
                updateUserList(users);
            } else {
                showEmptyState();
            }
        });
    }

    private void updateUserList(List<User> users) {
        binding.recyclerViewUsers.setVisibility(View.VISIBLE);
        userAdapter.setUserList(users);
    }

    private void showEmptyState() {
        binding.recyclerViewUsers.setVisibility(View.GONE);
        Toast.makeText(requireContext(), "Henüz hiç kullanıcı bulunmuyor", Toast.LENGTH_SHORT).show();
    }

    private void showEditUserDialog(User user) {
        EditUserDialogFragment dialog = EditUserDialogFragment.newInstance(user);
        dialog.show(getChildFragmentManager(), "EditUserDialog");
    }

    private void showDeleteConfirmationDialog(User user) {
        if (user.getUsername().equals(userViewModel.getCurrentUser().getUsername())) {
            Toast.makeText(requireContext(), "Kendi hesabınızı silemezsiniz!", Toast.LENGTH_SHORT).show();
            return;
        }

        new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle("Kullanıcıyı Sil")
                .setMessage("Bu kullanıcıyı silmek istediğinize emin misiniz?")
                .setPositiveButton("Evet", (dialog, which) -> {
                    // TODO: Implement user deletion in UserViewModel and Repository
                    Toast.makeText(requireContext(), "Kullanıcı silindi", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Hayır", null)
                .show();
    }

    private void toggleAdminStatus(User user) {
        if (user.getUsername().equals(userViewModel.getCurrentUser().getUsername())) {
            Toast.makeText(requireContext(), "Kendi admin durumunuzu değiştiremezsiniz!", Toast.LENGTH_SHORT).show();
            return;
        }

        user.setAdmin(!user.isAdmin());
        userViewModel.updateUser(user);
        Toast.makeText(requireContext(), 
            user.isAdmin() ? "Kullanıcı admin yapıldı" : "Kullanıcının admin yetkisi kaldırıldı", 
            Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
} 