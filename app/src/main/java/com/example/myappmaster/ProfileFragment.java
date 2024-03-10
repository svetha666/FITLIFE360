package com.example.myappmaster;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {

    private EditText weightEditText;
    private EditText heightEditText;
    private EditText nameEditText;
    private EditText ageEditText;
    private TextView bmiTextView;
    private TextView caloriesTextView;
    private Button editButton;
    private boolean isEditMode = false;

    // Initialize database reference
    private DatabaseReference databaseReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Initialize views
        weightEditText = view.findViewById(R.id.weightEditText);
        heightEditText = view.findViewById(R.id.heightEditText);
        nameEditText = view.findViewById(R.id.nameEditText);
        ageEditText = view.findViewById(R.id.ageEditText);
        bmiTextView = view.findViewById(R.id.bmiTextView);
        caloriesTextView = view.findViewById(R.id.caloriesTextView);
        editButton = view.findViewById(R.id.editButton);

        // Initialize database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("profiles");

        // Handle edit mode
        editButton.setOnClickListener(v -> toggleEditMode());

        // Add text change listeners to trigger calculations
        weightEditText.addTextChangedListener(textWatcher);
        heightEditText.addTextChangedListener(textWatcher);
        ageEditText.addTextChangedListener(textWatcher);

        // Load user data from database
        loadData();

        return view;
    }

    private void toggleEditMode() {
        if (isEditMode) {
            // Save data to database
            saveData();

            // Switch to display mode
            isEditMode = false;
            editButton.setText("Edit");
            disableEditing();

        } else {
            // Switch to edit mode
            isEditMode = true;
            editButton.setText("Save");
            enableEditing();
        }
    }

    private void enableEditing() {
        // Enable editing of input fields
        weightEditText.setEnabled(true);
        heightEditText.setEnabled(true);
        nameEditText.setEnabled(true);
        ageEditText.setEnabled(true);
    }

    private void disableEditing() {
        // Disable editing of input fields
        weightEditText.setEnabled(false);
        heightEditText.setEnabled(false);
        nameEditText.setEnabled(false);
        ageEditText.setEnabled(false);
    }

    private void saveData() {
        // Save user data to database
        String weight = weightEditText.getText().toString();
        String height = heightEditText.getText().toString();
        String name = nameEditText.getText().toString();
        String age = ageEditText.getText().toString();

        // Update database
        UserService.updateProfile(this.getContext(), name, Integer.parseInt(age), Float.parseFloat(weight), Float.parseFloat(height));
    }

    private void loadData() {
        // Load user data from database
        UserService.getProfile(this.getContext(), new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    weightEditText.setText(snapshot.child("weight").getValue(Float.class).toString());
                    heightEditText.setText(snapshot.child("height").getValue(Float.class).toString());
                    nameEditText.setText(snapshot.child("name").getValue(String.class));
                    ageEditText.setText(snapshot.child("age").getValue(Integer.class).toString());

                    // Calculate BMI and calories with the UserProfile instance
                    calculateBMI();
                    calculateCalories();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error
            }
        });
    }

    private void calculateBMI() {
        // Calculate BMI and display
        try {
            double weight = Double.parseDouble(weightEditText.getText().toString());
            double height = Double.parseDouble(heightEditText.getText().toString()) / 100; // Convert to meters
            double bmi = weight / (height * height);
            bmiTextView.setText("BMI: " + String.format("%.2f", bmi));
        } catch (NumberFormatException e) {
            bmiTextView.setText("BMI: N/A");
        }
    }

    private void calculateCalories() {
        // Calculate calories burned and display
        try {
            double age = Double.parseDouble(ageEditText.getText().toString());
            double weight = Double.parseDouble(weightEditText.getText().toString());
            double height = Double.parseDouble(heightEditText.getText().toString()) / 100; // Convert to meters

            // Basic calculation, replace with your own logic
            double calories = 10 * weight + 6.25 * height - 5 * age + 5;
            caloriesTextView.setText("Calories Burned: " + String.format("%.2f", calories));
        } catch (NumberFormatException e) {
            caloriesTextView.setText("Calories Burned: N/A");
        }
    }

    // TextWatcher to trigger calculations when EditText fields change
    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            calculateBMI();
            calculateCalories();
        }

        @Override
        public void afterTextChanged(Editable s) {}
    };
}
