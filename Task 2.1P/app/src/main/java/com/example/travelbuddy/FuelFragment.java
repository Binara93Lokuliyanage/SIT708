package com.example.travelbuddy;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class FuelFragment extends Fragment {

    Spinner spinnerFrom, spinnerTo;
    EditText editTextValue;
    Button buttonConvert;
    TextView textResult;

    public FuelFragment() {
        // Required empty public constructor
    }

    String[] fuelTypes = {"mpg", "km/L", "Gallon", "Liters", "Nautical Mile", "km"};
    String[] efficiencyUnits = {"mpg", "km/L"};
    String[] volumeUnits = {"Gallon", "Liters"};
    String[] distanceUnits = {"Nautical Mile", "km"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_fuel, container, false);

        spinnerFrom = view.findViewById(R.id.spinnerFrom);
        spinnerTo = view.findViewById(R.id.spinnerTo);
        editTextValue = view.findViewById(R.id.editTextValue);
        buttonConvert = view.findViewById(R.id.buttonConvert);
        textResult = view.findViewById(R.id.textResult);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                fuelTypes
        );

        spinnerFrom.setAdapter(adapter);

        spinnerFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String selected = spinnerFrom.getSelectedItem().toString();

                if (selected.equals("mpg") || selected.equals("km/L")) {
                    updateToSpinner(efficiencyUnits, selected);

                } else if (selected.equals("Gallon") || selected.equals("Liters")) {
                    updateToSpinner(volumeUnits, selected);

                } else if (selected.equals("Nautical Mile") || selected.equals("km")) {
                    updateToSpinner(distanceUnits, selected);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });


        buttonConvert.setOnClickListener(v -> convertFuel());

        return view;
    }

    private void updateToSpinner(String[] units, String fromUnit) {

        ArrayList<String> filtered = new ArrayList<>();

        for (String unit : units) {
            if (!unit.equals(fromUnit)) {
                filtered.add(unit);
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                filtered
        );

        spinnerTo.setAdapter(adapter);
    }


    private void convertFuel() {

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

        if (value < 0) {
            Toast.makeText(requireContext(), "Value cannot be negative", Toast.LENGTH_SHORT).show();
            return;
        }

        String from = spinnerFrom.getSelectedItem().toString();
        String to = spinnerTo.getSelectedItem().toString();

        double result = convert(from, to, value);

        textResult.setText(String.format("Result: %.2f %s", result, to));
    }
    // Subtask 2: Implement the Conversion Logic
    private double convert(String from, String to, double value) {

        // Efficiency
        if (from.equals("mpg") && to.equals("km/L")) return value * 0.425;
        if (from.equals("km/L") && to.equals("mpg")) return value / 0.425;

        // Volume
        if (from.equals("Gallon") && to.equals("Liters")) return value * 3.785;
        if (from.equals("Liters") && to.equals("Gallon")) return value / 3.785;

        // Distance
        if (from.equals("Nautical Mile") && to.equals("km")) return value * 1.852;
        if (from.equals("km") && to.equals("Nautical Mile")) return value / 1.852;

        return 0;
    }
}