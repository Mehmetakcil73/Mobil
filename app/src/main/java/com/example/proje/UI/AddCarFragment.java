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
import com.example.proje.R;
import com.example.proje.SessionManager.SessionManager;
import com.example.proje.ViewModel.CarViewModel;
import com.example.proje.databinding.FragmentAddCarBinding;

public class AddCarFragment extends Fragment {
    private FragmentAddCarBinding binding;
    private CarViewModel carViewModel;
    private SessionManager sessionManager;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        binding = FragmentAddCarBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        carViewModel = new ViewModelProvider(requireActivity()).get(CarViewModel.class);
        sessionManager = new SessionManager(requireContext());

        // Geri butonu click listener
        binding.btnBack.setOnClickListener(v -> {
            Navigation.findNavController(v).navigateUp();
        });

        binding.btnSave.setOnClickListener(v -> {
            String brand = binding.edtBrand.getText().toString().trim();
            String model = binding.edtModel.getText().toString().trim();
            String yearStr = binding.edtYear.getText().toString().trim();
            String priceStr = binding.edtPrice.getText().toString().trim();

            if (brand.isEmpty() || model.isEmpty() || yearStr.isEmpty() || priceStr.isEmpty()){
                Toast.makeText(getContext(), "Lütfen tüm alanları doldurunuz!", Toast.LENGTH_SHORT).show();
                return;
            }

            String currentUserId = sessionManager.getUsername();
            if (currentUserId == null) {
                Toast.makeText(getContext(), "Oturum hatası! Lütfen tekrar giriş yapın.", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                int year = Integer.parseInt(yearStr);
                double price = Double.parseDouble(priceStr);

                Car car = new Car(brand, model, year, price, currentUserId);
                carViewModel.addCar(car);

                Toast.makeText(getContext(), "Araç başarıyla kaydedildi!", Toast.LENGTH_SHORT).show();
                
                // Navigate back to MyCarListFragment
                Navigation.findNavController(v).navigate(R.id.action_addCarFragment_to_myCarListFragment);
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Yıl ve fiyat alanları geçerli sayılar olmalıdır!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        binding = null;
    }
}
