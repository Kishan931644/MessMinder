package com.jignesh.messminder.ui.idcard;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
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

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.jignesh.messminder.DBHelper;
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
    DBHelper dbHelper;

    private Button generator;

    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    generatePdf();
                } else {
                    Toast.makeText(requireContext(), "Storage permission is required to create PDF", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        generator = binding.generatePDF;

        // Initialize your TextViews
        nameTextView = binding.name;
        enrollmentTextView = binding.enrollment;
        numberTextView = binding.number;
        emailTextView = binding.email;
        blockTextView = binding.block;
        paymentTextView = binding.payment;

        // Set OnClickListener for generating PDF
        generator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                } else {
                    generatePdf();
                }
            }
        });

        // Set sample data to TextViews
        nameTextView.setText("John Doe");
        enrollmentTextView.setText("123456789");
        numberTextView.setText("9876543210");
        emailTextView.setText("john.doe@example.com");
        blockTextView.setText("A");
        paymentTextView.setText("Completed");

        return root;
    }

    private void generatePdf() {
        View content = binding.cardview;

        // Create a bitmap of the view
        Bitmap bitmap = getBitmapFromView(content);

        // Create a PdfDocument
        PdfDocument document = new PdfDocument();

        // Define the page info and the content rect
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(bitmap.getWidth(), bitmap.getHeight(), 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        // Draw the bitmap onto the page
        Canvas canvas = page.getCanvas();
        canvas.drawBitmap(bitmap, 0, 0, null);
        document.finishPage(page);

        // Save the PDF to a file
        String pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        File file = new File(pdfPath, "generated_pdf.pdf");
        try {
            document.writeTo(new FileOutputStream(file));
            Toast.makeText(requireContext(), "PDF created successfully", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Log.e("PDF Creation", "Error writing PDF: " + e.getMessage());
            Toast.makeText(requireContext(), "Error creating PDF", Toast.LENGTH_SHORT).show();
        }

        // Close the document
        document.close();
    }

    private Bitmap getBitmapFromView(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
