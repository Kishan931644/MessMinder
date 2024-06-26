package com.jignesh.messminder.ui.schedule;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.jignesh.messminder.DBHelper;
import com.jignesh.messminder.Utilities;
import com.jignesh.messminder.databinding.FragmentDashboardBinding;

public class DashboardFragment extends Fragment {
    private TextView monMenu, tueMenu, wedMenu, thuMenu, friMenu, satMenu, sunMenu;
    private FragmentDashboardBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        monMenu = binding.monmenu;
        tueMenu = binding.tuemenu;
        wedMenu = binding.wedmenu;
        thuMenu = binding.thumenu;
        friMenu = binding.frimenu;
        satMenu = binding.satmenu;
        sunMenu = binding.sunmenu;

        DBHelper db = new DBHelper(this.getContext());

        monMenu.setText(db.getSetting("monday"));
        tueMenu.setText(db.getSetting("tuesday"));
        wedMenu.setText(db.getSetting("wednesday"));
        thuMenu.setText(db.getSetting("thursday"));
        friMenu.setText(db.getSetting("friday"));
        satMenu.setText(db.getSetting("saturday"));
        sunMenu.setText(db.getSetting("sunday"));
        try {
            String email = Utilities.getEmail(requireContext());
        } catch (Exception e) {
            Toast.makeText(requireContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}