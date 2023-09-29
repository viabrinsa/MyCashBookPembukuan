package com.example.mycashbook;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycashbook.database.DatabaseHelper;
import com.example.mycashbook.databinding.DetailsItemBinding;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.ViewHolder> {

    private static final String TAG = "RecyclerAdapter";
    private List<DetailsModels> list = new ArrayList<>();

    public void updateList(List<DetailsModels> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        DetailsItemBinding detailsItemBinding = DetailsItemBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(detailsItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DetailsModels detailsModels = list.get(position);
        try {
            holder.bindView(detailsModels);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: " + list.size());
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        DetailsItemBinding detailsItemBinding;

        public ViewHolder(@NonNull DetailsItemBinding detailsItemBinding) {
            super(detailsItemBinding.getRoot());
            this.detailsItemBinding = detailsItemBinding;
        }

        public void bindView(DetailsModels detailsModels) throws ParseException {
            double nominal = detailsModels.getNominal();
            Locale myIndonesianLocale = new Locale("in", "ID");
            DecimalFormat format = (DecimalFormat) NumberFormat.getCurrencyInstance(myIndonesianLocale);
            format.setMaximumFractionDigits(2);
            format.setPositivePrefix("Rp. ");
            format.setNegativePrefix("Rp. -");

            String final_date = new SimpleDateFormat("dd-MM-yyyy").format(detailsModels.getDate());

            detailsItemBinding.textDate.setText(final_date);
            detailsItemBinding.textDescription.setText(String.valueOf(detailsModels.getDesc()));
            if (String.valueOf(detailsModels.getType()).equalsIgnoreCase("pemasukan")){
                detailsItemBinding.textNominalDetails.setText("[ + ] " + String.valueOf(format.format(nominal)));
                detailsItemBinding.arrowLogo.setImageResource(R.drawable.ic_baseline_arrow_income);
                detailsItemBinding.constraintBorder.setBackgroundResource(R.drawable.border_income);
            }
            else{
                detailsItemBinding.textNominalDetails.setText("[ - ] " + String.valueOf(format.format(nominal)));
                detailsItemBinding.arrowLogo.setImageResource(R.drawable.ic_baseline_arrow_expense);
                detailsItemBinding.constraintBorder.setBackgroundResource(R.drawable.border_expense);
            }
        }
    }
}
