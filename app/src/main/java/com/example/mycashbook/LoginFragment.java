package com.example.mycashbook;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mycashbook.database.DatabaseHelper;
import com.example.mycashbook.databinding.FragmentLoginBinding;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    FragmentLoginBinding binding;
    DatabaseHelper DB;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        DB = new DatabaseHelper(getActivity());

        binding.buttonLogin.setOnClickListener(view -> {
            String user = binding.usernameLogin.getText().toString();
            String password = binding.passwordLogin.getText().toString();

            Boolean checkuserpass = DB.checkusernamepassword(user, password);
            if (checkuserpass){
                Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_homepageFragment);
            }
            else{
                Toast.makeText(this.getActivity(), "Invalid Credentials", Toast.LENGTH_SHORT).show();
            }
        });
        View view = binding.getRoot();
        return view;
    }

//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        binding.
//    }
}