package com.example.proje.UI;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.proje.Adapter.CarAdapter;
import com.example.proje.Model.Car;
import com.example.proje.R;
import com.example.proje.SessionManager.SessionManager;
import com.example.proje.ViewModel.CarViewModel;
import com.example.proje.databinding.FragmentMyCarListBinding;

public class MyCarListFragment extends Fragment {
    private FragmentMyCarListBinding binding;
    private CarViewModel carViewModel;
    private CarAdapter carAdapter;
    private SessionManager sessionManager;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMyCarListBinding.inflate(inflater, container, false);
        binding.getRoot().setFitsSystemWindows(true);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sessionManager = new SessionManager(requireContext());
        carViewModel = new ViewModelProvider(requireActivity()).get(CarViewModel.class);
        carAdapter = new CarAdapter(requireContext());
        carAdapter.setCurrentUserId(sessionManager.getUsername());
        carAdapter.setOnDeleteClickListener(this::showDeleteConfirmationDialog);
        carAdapter.setOnEditClickListener(this::navigateToEditCar);
        
        binding.recyclerViewMyCars.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewMyCars.setAdapter(carAdapter);

        String currentUserId = sessionManager.getUsername();
        if (currentUserId != null) {
            carViewModel.getUserCars(currentUserId).observe(getViewLifecycleOwner(), cars -> {
                carAdapter.setCarList(cars);
            });
        }

        // FAB click listener
        binding.fabAddCar.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_myCarListFragment_to_addCarFragment);
        });
    }

    @Override
    public void onDestroyView() {
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

    private void navigateToEditCar(Car car) {
        Bundle args = new Bundle();
        args.putInt("car_id", car.getId());
        args.putString("car_brand", car.getBrand());
        args.putString("car_model", car.getModel());
        args.putInt("car_year", car.getYear());
        args.putDouble("car_price", car.getPrice());
        args.putString("car_userId", car.getUserId());
        
        Navigation.findNavController(requireView())
                .navigate(R.id.action_myCarListFragment_to_editCarFragment, args);
    }
} 