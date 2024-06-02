package com.jignesh.messminder.ui.idcard;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.jignesh.messminder.DBHelper;
import com.jignesh.messminder.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private TextView nameTextView;
    private TextView enrollmentTextView;
    private TextView numberTextView;
    private TextView emailTextView;
    private TextView blockTextView;
    private TextView paymentTextView;
    DBHelper dbHelper;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        String userEmail = getArguments().getString("email");
        Log.e("Emks", userEmail);
//        try {
//            // Fetch data from the database (replace with your actual database access code)
//            String userEmail = getArguments().getString("email");
//            dbHelper = new DBHelper(getActivity());
//            String[] userDetailsArray = dbHelper.getUserByEmail(userEmail);
//
//            // Check if user details array is not null
//            if (userDetailsArray != null) {
//                // Access individual elements of the array
//
//                String name = userDetailsArray[1];
//                String email = userDetailsArray[2];
//                String enrollment = userDetailsArray[3];
//                String block = userDetailsArray[4];
//                String paymentStatus = "Pending";
//
//                // Set values to TextViews
//                nameTextView.setText(name);
//                enrollmentTextView.setText(enrollment);
//
//                emailTextView.setText(email);
//                blockTextView.setText(block);
//                paymentTextView.setText(paymentStatus);
//            }
//        } catch (Exception e) {
//            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT);
//        }

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
