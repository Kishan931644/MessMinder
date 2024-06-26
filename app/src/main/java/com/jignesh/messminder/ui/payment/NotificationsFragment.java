package com.jignesh.messminder.ui.payment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.jignesh.messminder.DBHelper;
import com.jignesh.messminder.Utilities;
import com.jignesh.messminder.databinding.FragmentNotificationsBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import dev.shreyaspatil.easyupipayment.EasyUpiPayment;
import dev.shreyaspatil.easyupipayment.exception.AppNotFoundException;
import dev.shreyaspatil.easyupipayment.listener.PaymentStatusListener;
import dev.shreyaspatil.easyupipayment.model.TransactionDetails;

public class NotificationsFragment extends Fragment implements PaymentStatusListener {
    private Button paybtn;
    private FragmentNotificationsBinding binding;
    private DBHelper dbHelper;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        dbHelper = new DBHelper(requireContext());

        paybtn = binding.paybtn;
        paybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EasyUpiPayment.Builder builder = new EasyUpiPayment.Builder(requireActivity())
                        .setPayeeVpa("kishanrathod9679561@ybl")
                        .setPayeeName("Kishan Rathod")
                        .setPayeeMerchantCode("5812")
                        .setTransactionId("T" + System.currentTimeMillis() + "_" + new Random().nextInt(1000))
                        .setTransactionRefId("T037033025024")
                        .setDescription("Mess fees Payment")
                        .setAmount("10.00");
                try {
                    EasyUpiPayment easyUpiPayment = builder.build();
                    easyUpiPayment.setPaymentStatusListener(NotificationsFragment.this);
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

    @Override
    public void onTransactionCancelled() {
        Toast.makeText(requireContext(), "Transaction Cancelled", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTransactionCompleted(@NonNull TransactionDetails transactionDetails) {
        if (transactionDetails.getTransactionStatus().equals("SUCCESS")) {
            String userEmail = Utilities.getEmail(requireContext());

            String todayDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

            // Update the payment date and status in the database
            dbHelper.updatePaymentStatus(userEmail, todayDate, "1");

            Toast.makeText(requireContext(), "Transaction Successful", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(requireContext(), "Transaction Failed ", Toast.LENGTH_SHORT).show();
        }
    }
}
