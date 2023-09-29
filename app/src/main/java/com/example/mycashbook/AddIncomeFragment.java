package com.example.mycashbook;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.mycashbook.database.DatabaseHelper;
import com.example.mycashbook.databinding.FragmentAddIncomeBinding;
import com.example.mycashbook.databinding.FragmentHomepageBinding;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddIncomeFragment extends Fragment {
    FragmentAddIncomeBinding binding;
    DatabaseHelper DB;
    DatePickerDialog pickerDialog;
    private double nominals;


    public AddIncomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddIncomeBinding.inflate(inflater, container, false);
        DB = new DatabaseHelper(getActivity());

        binding.buttonKembali.setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(R.id.action_addIncomeFragment_to_homepageFragment);
        });

        binding.buttonSimpan.setOnClickListener(view -> {
            String date = binding.editTextDate.getText().toString();
            double nominal = nominals/100;
            Log.d("coba", String.valueOf(nominal));
            String description = binding.editTextTextKeterangan.getText().toString();

            boolean insertPemasukanData = DB.insertPemasukan(date, nominal, description);
            if (insertPemasukanData){
                Navigation.findNavController(view).navigate(R.id.action_addIncomeFragment_to_homepageFragment);
            }
            else{
                Toast.makeText(this.getActivity(), "Invalid Data Insert", Toast.LENGTH_SHORT).show();
            }
        });

        View view = binding.getRoot();
        return view;
    }

    private String current = "";

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.editTextNominal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().equals(current)){
                    binding.editTextNominal.removeTextChangedListener(this);

                    String cleanString = charSequence.toString().replaceAll("[Rp.,]", "");

                    double parsed = Double.parseDouble(cleanString);

                    nominals = parsed;

                    Log.d("halah", String.valueOf(nominals));


                    Locale myIndonesianLocale = new Locale("in", "ID");
                    String formatted = NumberFormat.getCurrencyInstance(myIndonesianLocale).format((parsed/100));

                    current = formatted;

                    binding.editTextNominal.setText(formatted);
                   binding.editTextNominal.setSelection(formatted.length());

                   binding.editTextNominal.addTextChangedListener(this);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                pickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        binding.editTextDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, day);
                pickerDialog.show();
            }
        });
    }
}