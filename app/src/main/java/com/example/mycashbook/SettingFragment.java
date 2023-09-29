package com.example.mycashbook;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mycashbook.database.DatabaseHelper;
import com.example.mycashbook.databinding.FragmentSettingBinding;

public class SettingFragment extends Fragment {

    FragmentSettingBinding binding;
    DatabaseHelper DB;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSettingBinding.inflate(inflater, container, false);
        DB = new DatabaseHelper(getActivity());

        binding.buttonKembali.setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(R.id.action_settingFragment_to_homepageFragment);
        });

        binding.buttonSimpan.setOnClickListener(view -> {
            String oldPass = binding.editTextOldPassword.getText().toString();
            String newPass = binding.editTextNewPassword.getText().toString();

            Boolean changePass = DB.changePassword(oldPass,newPass);

            if (changePass){
                Navigation.findNavController(view).navigate(R.id.action_settingFragment_self);
                Toast.makeText(this.getActivity(), "New Password Saved", Toast.LENGTH_SHORT).show();
            }
            else{
                Navigation.findNavController(view).navigate(R.id.action_settingFragment_self);
                Toast.makeText(this.getActivity(), "Invalid Value", Toast.LENGTH_SHORT).show();
            }
        });


        View view = binding.getRoot();
        return view;
    }
}