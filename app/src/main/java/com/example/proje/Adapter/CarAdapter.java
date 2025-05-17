package com.example.proje.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proje.Model.Car;
import com.example.proje.databinding.ItemCarBinding;
import com.example.proje.DB.UserDatabaseHelper;
import com.example.proje.Model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.CarViewHolder> {
    private final List<Car> carList = new ArrayList<>();
    private OnDeleteClickListener deleteClickListener;
    private OnEditClickListener editClickListener;
    private String currentUserId;
    private boolean isAdmin = false;
    private final Context context;

    public CarAdapter(Context context) {
        this.context = context;
    }

    public void setCarList(List<Car> newList) {
        carList.clear();
        if (newList != null) {
            carList.addAll(newList);
        }
        notifyDataSetChanged();
    }

    public void setCurrentUserId(String userId) {
        this.currentUserId = userId;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
        notifyDataSetChanged();
    }

    public static class CarViewHolder extends RecyclerView.ViewHolder {
        private final ItemCarBinding binding;

        public CarViewHolder(ItemCarBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Car car, OnDeleteClickListener deleteClickListener, 
                        OnEditClickListener editClickListener, 
                        String currentUserId, boolean isAdmin) {
            if (car != null) {
                binding.txtBrand.setText(car.getBrand());
                binding.txtModel.setText(car.getModel());
                binding.txtYear.setText(String.valueOf(car.getYear()));
                binding.txtPrice.setText(String.format("%.2f TL", car.getPrice()));

                // İlan sahibini göster
                String ownerUsername = car.getUserId() != null ? car.getUserId() : "Bilinmiyor";
                binding.txtOwner.setText("İlan Sahibi: " + ownerUsername);

                // Admin veya kullanıcının kendi ilanları için düzenleme ve silme butonlarını göster
                boolean canEdit = isAdmin || (currentUserId != null && currentUserId.equals(car.getUserId()));
                binding.btnEdit.setVisibility(canEdit ? View.VISIBLE : View.GONE);
                binding.btnDelete.setVisibility(canEdit ? View.VISIBLE : View.GONE);

                if (canEdit) {
                    binding.btnEdit.setOnClickListener(v -> {
                        if (editClickListener != null) {
                            editClickListener.onEditClick(car);
                        }
                    });

                    binding.btnDelete.setOnClickListener(v -> {
                        if (deleteClickListener != null) {
                            deleteClickListener.onDeleteClick(car);
                        }
                    });
                }
            }
        }
    }

    @NonNull
    @Override
    public CarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCarBinding binding = ItemCarBinding.inflate(
            LayoutInflater.from(parent.getContext()), parent, false);
        return new CarViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CarViewHolder holder, int position) {
        if (position >= 0 && position < carList.size()) {
            holder.bind(carList.get(position), deleteClickListener, editClickListener, 
                       currentUserId, isAdmin);
        }
    }

    @Override
    public int getItemCount() {
        return carList.size();
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(Car car);
    }

    public interface OnEditClickListener {
        void onEditClick(Car car);
    }

    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.deleteClickListener = listener;
    }

    public void setOnEditClickListener(OnEditClickListener listener) {
        this.editClickListener = listener;
    }
}
