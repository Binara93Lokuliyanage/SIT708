package com.example.travelbuddy;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.fragment.app.Fragment;

public class CurrencyFragment extends Fragment {

    Spinner spinnerFrom, spinnerTo;
    EditText editTextValue;
    Button buttonConvert;
    TextView textResult;

    String[] currencies = {"USD", "AUD", "EUR", "JPY", "GBP"};

    public CurrencyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_currency, container, false);

        spinnerFrom = view.findViewById(R.id.spinnerFrom);
        spinnerTo = view.findViewById(R.id.spinnerTo);
        editTextValue = view.findViewById(R.id.editTextValue);
        buttonConvert = view.findViewById(R.id.buttonConvert);
        textResult = view.findViewById(R.id.textResult);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                currencies
        );

        spinnerFrom.setAdapter(adapter);
        spinnerTo.setAdapter(adapter);

        buttonConvert.setOnClickListener(v -> convertCurrency());

        return view;
    }

    private void convertCurrency() {

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
            textResult.setText("Same currency: " + value);
            return;
        }

        double result = convert(from, to, value);

        textResult.setText("Result: " + result + " " + to);
    }

    // Subtask 2: Implement the Conversion Logic
    private double convert(String from, String to, double amount) {

        double usd = 0;

        switch (from) {
            case "USD": usd = amount; break;
            case "AUD": usd = amount / 1.55; break;
            case "EUR": usd = amount / 0.92; break;
            case "JPY": usd = amount / 148.50; break;
            case "GBP": usd = amount / 0.78; break;
        }

        switch (to) {
            case "USD": return usd;
            case "AUD": return usd * 1.55;
            case "EUR": return usd * 0.92;
            case "JPY": return usd * 148.50;
            case "GBP": return usd * 0.78;
        }

        return 0;
    }
}