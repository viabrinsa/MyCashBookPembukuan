package com.example.mycashbook;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mycashbook.database.DatabaseHelper;
import com.example.mycashbook.databinding.FragmentHomepageBinding;
import com.example.mycashbook.databinding.FragmentLoginBinding;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;


public class HomepageFragment extends Fragment {
    FragmentHomepageBinding binding;
    DatabaseHelper DB;

    public HomepageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomepageBinding.inflate(inflater, container, false);
        DB = new DatabaseHelper(getActivity());


        binding.addIncomeButton.setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(R.id.action_homepageFragment_to_addIncomeFragment);
        });

        binding.addExpenseButton.setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(R.id.action_homepageFragment_to_addExpenseFragment);
        });

        binding.detailsCashflow.setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(R.id.action_homepageFragment_to_cashFlowDetailFragment);
        });

        binding.settings.setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(R.id.action_homepageFragment_to_settingFragment);
        });

        Cursor data_pemasukan = DB.getNewestPemasukanByUser();

        if (data_pemasukan.moveToFirst()){
            double nominal = data_pemasukan.getDouble(0);

            Locale myIndonesianLocale = new Locale("in", "ID");
            DecimalFormat format = (DecimalFormat) NumberFormat.getCurrencyInstance(myIndonesianLocale);
            format.setMaximumFractionDigits(2);
            format.setPositivePrefix("Pemasukan Rp. ");
            format.setNegativePrefix("Pemasukan Rp. -");
            binding.textPemasukan.setText(format.format(nominal));

//            binding.textPemasukan.setText("Pemasukan: " + String.valueOf(nominal));
        }else{
            Log.d("coba", "Kosong");
        }

        Cursor data_pengeluaran = DB.getNewestPengeluaranByUser();
        if (data_pengeluaran.moveToFirst()){
            double nominalPengeluaran = data_pengeluaran.getDouble(0);

            Locale myIndonesianLocale = new Locale("in", "ID");
            DecimalFormat format = (DecimalFormat) NumberFormat.getCurrencyInstance(myIndonesianLocale);
            format.setMaximumFractionDigits(2);
            format.setPositivePrefix("Pengeluaran Rp. ");
            format.setNegativePrefix("Pengeluaran Rp. -");
            binding.textPengeluaran.setText(format.format(nominalPengeluaran));

//            binding.textPemasukan.setText("Pemasukan: " + String.valueOf(nominal));
        }else{
            Log.d("coba", "Kosong");
        }

        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        configureChart();

        List<DetailsModels> data = DB.getDetails();
        setLineChart(data);
    }

    public void configureChart(){
        Description desc = new Description();
        desc.setText("Book History");
        desc.setTextSize(16);
        desc.setTextColor(Color.BLACK);
        binding.chart.setDescription(desc);


    }

    public void setLineChart(List<DetailsModels> detailsModels)  {
        ArrayList<Entry> listPemasukan = new ArrayList<>();
        ArrayList<Entry> listPengeluaran = new ArrayList<>();
        LineDataSet pemasukan = new LineDataSet(listPemasukan, "Pemasukan");
        LineDataSet pengeluaran = new LineDataSet(listPengeluaran, "Pengeluaran");
        binding.chart.getAxisLeft().setTextColor(Color.BLACK);
        binding.chart.getXAxis().setTextColor(Color.BLACK);
        binding.chart.getXAxis().setTextSize(10);
        binding.chart.getAxisLeft().setTextSize(10);
        binding.chart.getXAxis().setLabelCount(3);
        binding.chart.getAxisRight().setEnabled(false);
        binding.chart.getXAxis().setDrawGridLines(false);
        binding.chart.setPinchZoom(true);
        binding.chart.setDragEnabled(true);
        binding.chart.setScaleEnabled(true);

        List<DetailsModels> detailsModelsList = detailsModels;

        Collections.sort(detailsModelsList, new Comparator<DetailsModels>() {
            @Override
            public int compare(DetailsModels detailsModels, DetailsModels t1) {
                return detailsModels.getDate().compareTo(t1.getDate());
            }
        });

        for (DetailsModels p: detailsModelsList){
            long epoch = p.getDate().getTime();

            if (p.getType().equalsIgnoreCase("pengeluaran")) {
                listPengeluaran.add(new Entry(epoch, (float) p.getNominal()));
            }
            else{
                listPemasukan.add(new Entry(epoch, (float) p.getNominal()));
            }
        }


        XAxis xAxis = binding.chart.getXAxis();
        xAxis.setValueFormatter(new ValueFormatter() {

            final SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy ");
            @Override
            public String getFormattedValue(float value) {
                format.setTimeZone(TimeZone.getTimeZone("Asia/Jakarta"));
                String formatted =format.format(value);
                return formatted;
            }
        });

        YAxis yAxis = binding.chart.getAxisLeft();
        yAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                Locale myIndonesianLocale = new Locale("in", "ID");
                DecimalFormat format = (DecimalFormat) NumberFormat.getCurrencyInstance(myIndonesianLocale);
                format.setPositivePrefix("Rp. ");
                format.setNegativePrefix("Rp. -");
                return (format.format(value));
            }
        });


        pemasukan.setDrawCircleHole(true);
        pemasukan.setCircleRadius(4);
        pemasukan.setDrawValues(false);
        pemasukan.setLineWidth(2);
        pemasukan.setColor(Color.GREEN);
        pemasukan.setCircleColor(Color.GREEN);
//        pemasukan.setDrawFilled(true);
//        pemasukan.setFillColor(Color.GREEN);

        pengeluaran.setDrawCircleHole(true);
        pengeluaran.setCircleRadius(4);
        pengeluaran.setDrawValues(false);
        pengeluaran.setLineWidth(2);
        pengeluaran.setColor(Color.RED);
        pengeluaran.setCircleColor(Color.RED);
//        pengeluaran.setDrawFilled(true);
//        pengeluaran.setFillColor(Color.RED);

        Legend legend = binding.chart.getLegend();
        legend.setEnabled(true);
        legend.setTextColor(Color.BLACK);
//        binding.chart.setAutoScaleMinMaxEnabled(true);

        pemasukan.setValues(listPemasukan);
        pengeluaran.setValues(listPengeluaran);

        LineData lineData = new LineData(pemasukan, pengeluaran);
        binding.chart.setData(lineData);
        binding.chart.invalidate();

    }



}