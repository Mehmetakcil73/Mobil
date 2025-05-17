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
import com.example.proje.ViewModel.UserViewModel;
import com.example.proje.databinding.FragmentCarListBinding;

public class CarListFragment extends Fragment {
    private FragmentCarListBinding binding;
    private CarViewModel carViewModel;
    private UserViewModel userViewModel;
    private CarAdapter carAdapter;
    private SessionManager sessionManager;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        binding = FragmentCarListBinding.inflate(inflater, container, false);
        binding.getRoot().setFitsSystemWindows(true);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        sessionManager = new SessionManager(requireContext());
        carViewModel = new ViewModelProvider(requireActivity()).get(CarViewModel.class);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        
        setupRecyclerView();
        observeData();
    }

    private void setupRecyclerView() {
        carAdapter = new CarAdapter(requireContext());
        carAdapter.setCurrentUserId(sessionManager.getUsername());
        carAdapter.setAdmin(userViewModel.isCurrentUserAdmin());
        carAdapter.setOnDeleteClickListener(this::showDeleteConfirmationDialog);
        carAdapter.setOnEditClickListener(this::navigateToEditCar);
        
        binding.recyclerViewCars.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewCars.setAdapter(carAdapter);
    }

    private void observeData() {
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
        String message = userViewModel.isCurrentUserAdmin() ?
            "Bu ilanı silmek istediğinize emin misiniz? (Admin olarak siliyorsunuz)" :
            "Bu ilanı silmek istediğinize emin misiniz?";

        new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle("İlanı Sil")
                .setMessage(message)
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
        args.putString("car_userId", car.getUserId());
        args.putFloat("car_price", (float) car.getPrice());
        
        Navigation.findNavController(requireView())
                .navigate(R.id.action_carListFragment_to_editCarFragment, args);
    }
}
