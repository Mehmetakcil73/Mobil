package com.example.proje.UI;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.proje.R;
import com.example.proje.databinding.FragmentProfileBinding;

public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;
    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "user_pref";
    private static final String KEY_NAME = "name";
    private static final String KEY_SURNAME = "surname";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        sharedPreferences = requireActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        String name = sharedPreferences.getString(PREF_NAME, "");
        String surname = sharedPreferences.getString(PREF_NAME, "");
        if (!name.isEmpty() || !surname.isEmpty()){
            binding.txtGreeting.setText(getString(R.string.hosgeldiniz) + name + surname);
        }

        binding.btnSave.setOnClickListener(v -> {
            String enteredName = binding.edtName.getText().toString().trim();
            String enteredSurame = binding.edtSurname.getText().toString().trim();

            if (enteredName.isEmpty() || enteredSurame.isEmpty()){
                Toast.makeText(getContext(), "Lütfen ad ve soyad giriniz!!", Toast.LENGTH_SHORT).show();
            }
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(KEY_NAME, enteredName);
            editor.putString(KEY_SURNAME, enteredSurame);
            editor.apply();

            binding.txtGreeting.setText(getString(R.string.hosgeldiniz) + enteredName + " " + enteredSurame);
            Toast.makeText(getContext(), "Bilgiler kaydedildi!!", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}