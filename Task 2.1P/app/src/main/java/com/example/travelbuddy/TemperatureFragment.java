package com.example.travelbuddy;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class TemperatureFragment extends Fragment {

    Spinner spinnerFrom, spinnerTo;
    EditText editTextValue;
    Button buttonConvert;
    TextView textResult;

    String[] temperatureUnits = {"Celsius", "Fahrenheit", "Kelvin"};

    public TemperatureFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_temperature, container, false);

        spinnerFrom = view.findViewById(R.id.spinnerFrom);
        spinnerTo = view.findViewById(R.id.spinnerTo);
        editTextValue = view.findViewById(R.id.editTextValue);
        buttonConvert = view.findViewById(R.id.buttonConvert);
        textResult = view.findViewById(R.id.textResult);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                temperatureUnits
        );

        spinnerFrom.setAdapter(adapter);
        spinnerTo.setAdapter(adapter);

        buttonConvert.setOnClickListener(v -> convertTemperature());

        return view;
    }

    private void convertTemperature() {

        String input = editTextValue.getText().toString();

        // Subtask 4: Add Validation and Error Handling
        if (TextUtils.isEmpty(input)) {
            Toast.makeText(requireContext(), "Enter a value", Toast.LENGTH_SHORT).show();
            return;
        }

        double value;

        try {
            value = Double.parseDouble(input);
        } catch (NumberFormatException e) {
            Toast.makeText(requireContext(), "Invalid number", Toast.LENGTH_SHORT).show();
            return;
        }

        String from = spinnerFrom.getSelectedItem().toString();
        String to = spinnerTo.getSelectedItem().toString();

        if (from.equals(to)) {
            textResult.setText("Same Temperature Unit: " + value);
            return;
        }

        double result = convert(from, to, value);

        textResult.setText("Result: " + result + " " + to);
    }

    // Subtask 2: Implement the Conversion Logic
    private double convert(String from, String to, double value) {

        double celsius = 0;

        switch (from) {
            case "Celsius": celsius = value; break;
            case "Fahrenheit": celsius = (value - 32) / 1.8; break;
            case "Kelvin": celsius = value - 273.15; break;
        }

        switch (to) {
            case "Celsius": return celsius;
            case "Fahrenheit": return (celsius * 1.8) + 32;
            case "Kelvin": return celsius + 273.15;
        }

        return 0;
    }
}