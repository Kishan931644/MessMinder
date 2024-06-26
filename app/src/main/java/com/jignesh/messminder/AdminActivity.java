package com.jignesh.messminder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class AdminActivity extends AppCompatActivity {
    private DBHelper dbHelper;
    private LinearLayout usersContainer;
    private Button schedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        dbHelper = new DBHelper(this);
        usersContainer = findViewById(R.id.usersContainer);

        List<String[]> usersList = dbHelper.getAllUsers();
        for (String[] user : usersList) {
            addUserCard(user);
            Log.e("User Name:12334 ", user[1]);
        }

        schedule = findViewById(R.id.schedulebtn);

        schedule.setOnClickListener(e->{
            startActivity(new Intent(AdminActivity.this, settings.class));
        });
    }

    private void addUserCard(String[] userDetails) {
        // Create CardView
        CardView cardView = new CardView(this);
        LinearLayout.LayoutParams cardLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        cardLayoutParams.setMargins(0, 0, 0, 16); // Margin bottom 16dp
        cardView.setLayoutParams(cardLayoutParams);
        cardView.setClickable(true);

        // Set selectable item background
        TypedValue outValue = new TypedValue();
        getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
        cardView.setForeground(getResources().getDrawable(outValue.resourceId, null));

        cardView.setRadius(4);
        cardView.setCardElevation(1);

        // Create inner LinearLayout
        LinearLayout innerLayout = new LinearLayout(this);
        innerLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        innerLayout.setOrientation(LinearLayout.HORIZONTAL);
        int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());
        innerLayout.setPadding(padding, padding, padding, padding);

        // Add ImageView
        ImageView imageView = new ImageView(this);
        int imageSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 120, getResources().getDisplayMetrics());
        imageView.setLayoutParams(new LinearLayout.LayoutParams(imageSize, imageSize)); // Width and height 120dp
        imageView.setImageResource(R.drawable.avataar); // Replace with your image resource
        innerLayout.addView(imageView);

        // Add inner vertical LinearLayout
        LinearLayout verticalLayout = new LinearLayout(this);
        verticalLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        verticalLayout.setOrientation(LinearLayout.VERTICAL);
        int vpadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
        verticalLayout.setPadding(vpadding, vpadding, 0, 0);

        TextView nameTextView = new TextView(this);
        nameTextView.setText(userDetails[1]);
        nameTextView.setTextSize(16);
        nameTextView.setTypeface(null, Typeface.BOLD);
        verticalLayout.addView(nameTextView);

        TextView enrollmentTextView = new TextView(this);
        enrollmentTextView.setText(userDetails[3]);
        verticalLayout.addView(enrollmentTextView);

        TextView semTextView = new TextView(this);
        semTextView.setText("Sem: 5"); // Change as per your data
        verticalLayout.addView(semTextView);

        TextView statusTextView = new TextView(this);
        statusTextView.setText(userDetails[6].equals("0") ? "Not Paid" : "Paid");
        statusTextView.setTextColor(userDetails[6].equals("1") ? Color.GREEN : Color.RED);
        verticalLayout.addView(statusTextView);

        // Add vertical layout to inner layout
        innerLayout.addView(verticalLayout);

        // Add inner layout to CardView
        cardView.addView(innerLayout);

        // Add CardView to container
        usersContainer.addView(cardView);
    }

}