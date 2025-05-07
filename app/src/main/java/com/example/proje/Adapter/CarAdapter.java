package com.example.proje.Adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proje.Model.Car;
import com.example.proje.databinding.ItemCarBinding;

import java.util.ArrayList;
import java.util.List;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.CarViewHolder> {
    private final List<Car> carList = new ArrayList<>();
    private OnDeleteClickListener deleteClickListener;


    public void setCarList(List<Car> newList){
        carList.clear();
        carList.addAll(newList);
        notifyDataSetChanged();
    }

    public static class CarViewHolder extends RecyclerView.ViewHolder {
        private final ItemCarBinding binding;

        public CarViewHolder(ItemCarBinding binding){
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Car car, OnDeleteClickListener deleteClickListener) {
            binding.txtBrand.setText(car.getBrand());
            binding.txtModel.setText(car.getModel());
            binding.txtYear.setText(String.valueOf(car.getYear()));
            binding.txtPrice.setText(String.format("%.2f TL", car.getPrice()));

            binding.btnDelete.setOnClickListener(v -> {
                if (deleteClickListener != null) {
                    deleteClickListener.onDeleteClick(car);
                }
            });
        }
    }

    @NonNull
    @Override
    public CarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCarBinding binding = ItemCarBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CarViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CarViewHolder holder, int position) {
        holder.bind(carList.get(position), deleteClickListener);
    }

    @Override
    public int getItemCount() {
        return carList.size();
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(Car car);
    }

    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.deleteClickListener = listener;
    }
}
