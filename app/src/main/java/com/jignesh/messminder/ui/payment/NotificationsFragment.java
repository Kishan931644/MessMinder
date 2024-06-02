package com.jignesh.messminder.ui.payment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.jignesh.messminder.R;
import com.jignesh.messminder.databinding.FragmentNotificationsBinding;

import java.util.Random;

import dev.shreyaspatil.easyupipayment.EasyUpiPayment;
import dev.shreyaspatil.easyupipayment.exception.AppNotFoundException;

public class NotificationsFragment extends Fragment {
    private Button paybtn;

    private FragmentNotificationsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        paybtn = binding.paybtn;

        paybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EasyUpiPayment.Builder builder = new EasyUpiPayment.Builder(requireActivity())
                        .setPayeeVpa("kishanrathod9679561@ybl")
                        .setPayeeName("Kishan rathod")
                        .setPayeeMerchantCode("5812")
                        .setTransactionId("T"+System.currentTimeMillis()+ "_" + new Random().nextInt(1000))
                        .setTransactionRefId("T037033025024")
                        .setDescription("Mess fees Payment")
                        .setAmount("10.00");
                try {
                    EasyUpiPayment easyUpiPayment = builder.build();
                    easyUpiPayment.startPayment();
                } catch (AppNotFoundException e) {
                    throw new RuntimeException(e);

                }
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}