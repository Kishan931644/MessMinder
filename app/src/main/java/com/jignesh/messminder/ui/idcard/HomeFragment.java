package com.jignesh.messminder.ui.idcard;

import static android.Manifest.permission.MANAGE_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.jignesh.messminder.DBHelper;
import com.jignesh.messminder.R;
import com.jignesh.messminder.Utilities;
import com.jignesh.messminder.databinding.FragmentHomeBinding;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private TextView nameTextView;
    private TextView enrollmentTextView;
    private TextView numberTextView;
    private TextView emailTextView;
    private TextView blockTextView;
    private TextView paymentTextView;
    private Button btnDownloadPDF;
    DBHelper dbHelper;

    int pageHeight = 1120;
    int pagewidth = 792;

    // creating a bitmap variable
    // for storing our images
    Bitmap bmp, scaledbmp;

    private static final int PERMISSION_REQUEST_CODE = 200;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        btnDownloadPDF = root.findViewById(R.id.btn_download_pdf);

        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.avataar);
        scaledbmp = Bitmap.createScaledBitmap(bmp, 140, 140, false);

        try {
            String email = Utilities.getEmail(requireContext());
            Toast.makeText(requireContext(), email, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(requireContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }

        if (checkPermission()) {
            Toast.makeText(requireContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
        } else {
            requestPermission();
        }

        btnDownloadPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generatePDF();
            }
        });

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

    private void generatePDF() {
        PdfDocument pdfDocument = new PdfDocument();

        Paint paint = new Paint();
        Paint title = new Paint();

        PdfDocument.PageInfo mypageInfo = new PdfDocument.PageInfo.Builder(pagewidth, pageHeight, 1).create();

        PdfDocument.Page myPage = pdfDocument.startPage(mypageInfo);

        Canvas canvas = myPage.getCanvas();

        canvas.drawBitmap(scaledbmp, 56, 40, paint);

        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));

        title.setTextSize(15);

        title.setColor(ContextCompat.getColor(requireContext(), R.color.purple_200));

        canvas.drawText("A portal for IT professionals.", 209, 100, title);
        canvas.drawText("Geeks for Geeks", 209, 80, title);

        title.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        title.setColor(ContextCompat.getColor(requireContext(), R.color.purple_200));
        title.setTextSize(15);

        title.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("This is sample document which we have created.", 396, 560, title);
        pdfDocument.finishPage(myPage);

        try {
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + "", "iCard.pdf");

            pdfDocument.writeTo(new FileOutputStream(file));

            Toast.makeText(requireContext(), "PDF file generated successfully.", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(requireContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
        pdfDocument.close();
    }

    private boolean checkPermission() {
        int permission1 = ContextCompat.checkSelfPermission(requireContext(), WRITE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(requireContext(), READ_EXTERNAL_STORAGE);
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(requireActivity(), new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE, MANAGE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {

                boolean writeStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean readStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if (writeStorage && readStorage) {
                    Toast.makeText(requireContext(), "Permission Granted..", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(requireContext(), "Permission Denied.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
