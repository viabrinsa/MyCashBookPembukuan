package com.example.mycashbook;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mycashbook.database.DatabaseHelper;
import com.example.mycashbook.databinding.FragmentAddIncomeBinding;
import com.example.mycashbook.databinding.FragmentCashFlowDetailBinding;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class CashFlowDetailFragment extends Fragment {
    FragmentCashFlowDetailBinding binding;
    DatabaseHelper DB;
    DetailsAdapter detailsAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCashFlowDetailBinding.inflate(inflater, container, false);
        DB = new DatabaseHelper(getActivity());

        binding.buttonKembali.setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(R.id.action_cashFlowDetailFragment_to_homepageFragment);
        });

        List<DetailsModels> data = DB.getDetails();

        Log.d("TAG", data.toString());

        detailsAdapter = new DetailsAdapter();
        binding.DetailsRv.setAdapter(detailsAdapter);



        Collections.sort(data, new Comparator<DetailsModels>() {
            @Override
            public int compare(DetailsModels detailsModels, DetailsModels t1) {
                return detailsModels.getDate().compareTo(t1.getDate());
            }
        });

        detailsAdapter.updateList(data);

        View view = binding.getRoot();
        return view;
    }
}