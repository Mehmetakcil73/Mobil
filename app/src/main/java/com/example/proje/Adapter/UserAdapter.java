package com.example.proje.Adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proje.Model.User;
import com.example.proje.databinding.ItemUserBinding;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private final List<User> userList = new ArrayList<>();
    private OnEditClickListener editClickListener;
    private OnDeleteClickListener deleteClickListener;
    private OnToggleAdminClickListener toggleAdminClickListener;

    public void setUserList(List<User> users) {
        userList.clear();
        if (users != null) {
            userList.addAll(users);
        }
        notifyDataSetChanged();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        private final ItemUserBinding binding;

        public UserViewHolder(ItemUserBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(User user, OnEditClickListener editClickListener,
                        OnDeleteClickListener deleteClickListener,
                        OnToggleAdminClickListener toggleAdminClickListener) {
            if (user != null) {
                // Kullanıcı adı
                String username = user.getUsername() != null ? user.getUsername() : "";
                binding.txtUsername.setText(username);

                // E-posta
                String email = user.getEmail() != null ? user.getEmail() : "";
                binding.txtEmail.setText(email);

                // Admin durumu
                binding.switchAdmin.setChecked(user.isAdmin());

                // Buton tıklama olayları
                binding.btnEdit.setOnClickListener(v -> {
                    if (editClickListener != null) {
                        editClickListener.onEditClick(user);
                    }
                });

                binding.btnDelete.setOnClickListener(v -> {
                    if (deleteClickListener != null) {
                        deleteClickListener.onDeleteClick(user);
                    }
                });

                binding.switchAdmin.setOnClickListener(v -> {
                    if (toggleAdminClickListener != null) {
                        toggleAdminClickListener.onToggleAdmin(user);
                    }
                });
            }
        }
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemUserBinding binding = ItemUserBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new UserViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.bind(user, editClickListener, deleteClickListener, toggleAdminClickListener);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public interface OnEditClickListener {
        void onEditClick(User user);
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(User user);
    }

    public interface OnToggleAdminClickListener {
        void onToggleAdmin(User user);
    }

    public void setOnEditClickListener(OnEditClickListener listener) {
        this.editClickListener = listener;
    }

    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.deleteClickListener = listener;
    }

    public void setOnToggleAdminClickListener(OnToggleAdminClickListener listener) {
        this.toggleAdminClickListener = listener;
    }
} 