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

import com.example.proje.DB.CarDatabaseHelper;
import com.example.proje.Model.Car;
import com.example.proje.ViewModel.CarViewModel;
import com.example.proje.databinding.FragmentAddCarBinding;

public class AddCarFragment extends Fragment {
    private FragmentAddCarBinding binding;
    private CarDatabaseHelper dbHelper;
    private CarViewModel carViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        binding = FragmentAddCarBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        carViewModel = new ViewModelProvider(requireActivity()).get(CarViewModel.class);

        binding.btnSave.setOnClickListener(v -> {
            String brand = binding.edtBrand.getText().toString().trim();
            String model = binding.edtModel.getText().toString().trim();
            String yearStr = binding.edtYear.getText().toString().trim();
            String priceStr = binding.edtPrice.getText().toString().trim();

            if (brand.isEmpty() || model.isEmpty() || yearStr.isEmpty() || priceStr.isEmpty()){
                Toast.makeText(getContext(), "Lütfen tüm alanları doldurunuz!!", Toast.LENGTH_SHORT).show();
            }

            int year = Integer.parseInt(yearStr);
            double price = Double.parseDouble(priceStr);

            Car car = new Car(brand, model, year, price);
            carViewModel.addCar(car);

            Toast.makeText(getContext(), "Araç başarıyla kaydedildi!!", Toast.LENGTH_SHORT).show();

            binding.edtBrand.setText("");
            binding.edtModel.setText("");
            binding.edtYear.setText("");
            binding.edtPrice.setText("");
        });
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        binding = null;
    }
}
