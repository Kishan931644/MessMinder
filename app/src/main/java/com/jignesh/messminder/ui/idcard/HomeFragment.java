package com.jignesh.messminder.ui.idcard;

import static android.Manifest.permission.MANAGE_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import static androidx.core.content.ContextCompat.getSystemService;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.view.LayoutInflater;

import org.xmlpull.v1.XmlPullParser;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private TextView nameTextView, enrollmentTextView, numberTextView, emailTextView, blockTextView, paymentTextView, paymentDateTextView;
    private Button btnDownloadPDF;
    DBHelper dbHelper;

    int pageHeight = 1120;
    int pagewidth = 792;

    // creating a bitmap variable
    // for storing our images
    Bitmap bmp, scaledbmp;
    String userEmail;

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


        nameTextView = binding.name;
        enrollmentTextView = binding.enrollment;
        blockTextView = binding.block;
        paymentTextView = binding.payment;
        emailTextView = binding.email;
        numberTextView = binding.number;
        paymentDateTextView = binding.paymentdate;

        if (!checkPermission()) {
            requestPermission();
        }

        try {
            userEmail = Utilities.getEmail(requireContext());

            dbHelper = new DBHelper(getActivity());
            String[] userDetailsArray = dbHelper.getUserByEmail(userEmail);

            if (userDetailsArray != null) {
                String name = userDetailsArray[1];
                String email = userDetailsArray[2];
                String enrollment = userDetailsArray[3];
                String block = userDetailsArray[4];
                String num = userDetailsArray[5];
                String paymentDate = userDetailsArray[6];
                String paymentStatus = "Pending";
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

                try {
                    Date paymentDateObj = sdf.parse(paymentDate);
                    Date currentDate = new Date();
                    long differenceInMillis = currentDate.getTime() - paymentDateObj.getTime();
                    long differenceInDays = differenceInMillis / (1000 * 60 * 60 * 24);

                    if (differenceInDays <= 30) {
                        paymentStatus = "Complete";
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                paymentTextView.setText(paymentStatus);
                nameTextView.setText(name);
                enrollmentTextView.setText(enrollment);
                emailTextView.setText(email);
                blockTextView.setText(block);
                paymentTextView.setText(paymentStatus);
                numberTextView.setText(num);
                paymentDateTextView.setText(paymentDate);
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), "e.toString()", Toast.LENGTH_SHORT);
        }


        btnDownloadPDF.setOnClickListener(view -> generatePDF());

        return root;
    }
    private Bitmap getBitmapFromView(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    private void generatePDF() {
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
            Toast.makeText(getContext(), "PDF created successfully", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Log.e("PDF Creation", "Error writing PDF: " + e.getMessage());
            Toast.makeText(getContext(), "Error creating PDF", Toast.LENGTH_SHORT).show();
        }

        // Close the document
        document.close();    }


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
