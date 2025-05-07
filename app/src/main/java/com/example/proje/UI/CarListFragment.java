package com.example.proje.UI;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.proje.Adapter.CarAdapter;
import com.example.proje.Model.Car;
import com.example.proje.ViewModel.CarViewModel;
import com.example.proje.databinding.FragmentCarListBinding;

public class CarListFragment extends Fragment {
    private FragmentCarListBinding binding;
    private CarViewModel carViewModel;
    private CarAdapter carAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        binding = FragmentCarListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        carViewModel = new ViewModelProvider(requireActivity()).get(CarViewModel.class);
        carAdapter = new CarAdapter();
        carAdapter.setOnDeleteClickListener(this::showDeleteConfirmationDialog);
        binding.recyclerViewCars.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewCars.setAdapter(carAdapter);

        carViewModel.getCars().observe(getViewLifecycleOwner(), cars -> {
            carAdapter.setCarList(cars);
        });
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        binding = null;
    }

    private void showDeleteConfirmationDialog(Car car) {
        new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle("İlanı Sil")
                .setMessage("Bu ilanı silmek istediğinize emin misiniz?")
                .setPositiveButton("Evet", (dialog, which) -> {
                    carViewModel.deleteCar(car);
                })
                .setNegativeButton("Hayır", null)
                .show();
    }
}
