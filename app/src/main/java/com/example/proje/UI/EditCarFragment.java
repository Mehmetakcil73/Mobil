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
import androidx.navigation.Navigation;

import com.example.proje.Model.Car;
import com.example.proje.SessionManager.SessionManager;
import com.example.proje.ViewModel.CarViewModel;
import com.example.proje.databinding.FragmentEditCarBinding;

public class EditCarFragment extends Fragment {
    private FragmentEditCarBinding binding;
    private CarViewModel carViewModel;
    private SessionManager sessionManager;
    private Car currentCar;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEditCarBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        carViewModel = new ViewModelProvider(requireActivity()).get(CarViewModel.class);
        sessionManager = new SessionManager(requireContext());

        // Bundle'dan gelen araç bilgilerini al
        if (getArguments() != null) {
            int carId = getArguments().getInt("car_id");
            String brand = getArguments().getString("car_brand");
            String model = getArguments().getString("car_model");
            int year = getArguments().getInt("car_year");
            double price = getArguments().getDouble("car_price");
            String userId = getArguments().getString("car_userId");

            currentCar = new Car(carId, brand, model, year, price, userId);

            // Mevcut kullanıcı kontrolü
            String currentUserId = sessionManager.getUsername();
            if (!currentUserId.equals(currentCar.getUserId())) {
                Toast.makeText(requireContext(), "Bu ilanı düzenleme yetkiniz yok!", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(view).navigateUp();
                return;
            }

            // Araç bilgilerini form alanlarına doldur
            binding.edtBrand.setText(brand);
            binding.edtModel.setText(model);
            binding.edtYear.setText(String.valueOf(year));
            binding.edtPrice.setText(String.valueOf(price));
        }

        binding.btnUpdate.setOnClickListener(v -> {
            String brand = binding.edtBrand.getText().toString().trim();
            String model = binding.edtModel.getText().toString().trim();
            String yearStr = binding.edtYear.getText().toString().trim();
            String priceStr = binding.edtPrice.getText().toString().trim();

            if (brand.isEmpty() || model.isEmpty() || yearStr.isEmpty() || priceStr.isEmpty()) {
                Toast.makeText(getContext(), "Lütfen tüm alanları doldurunuz!", Toast.LENGTH_SHORT).show();
                return;
            }

            int year = Integer.parseInt(yearStr);
            double price = Double.parseDouble(priceStr);

            // Güncellenmiş araç bilgileri
            Car updatedCar = new Car(currentCar.getId(), brand, model, year, price, currentCar.getUserId());
            carViewModel.updateCar(updatedCar);

            Toast.makeText(getContext(), "İlan başarıyla güncellendi!", Toast.LENGTH_SHORT).show();
            Navigation.findNavController(view).navigateUp();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
} 