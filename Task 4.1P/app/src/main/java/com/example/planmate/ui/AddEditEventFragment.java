package com.example.planmate.ui;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.planmate.R;
import com.example.planmate.data.Event;
import com.example.planmate.viewmodel.EventViewModel;

import java.util.Calendar;


public class AddEditEventFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    int eventId = -1;

    public AddEditEventFragment() {
        // Required empty public constructor
    }

    public static AddEditEventFragment newInstance(String param1, String param2) {
        AddEditEventFragment fragment = new AddEditEventFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_edit_event, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText dateInput = view.findViewById(R.id.dateInput);

        Calendar calendar = Calendar.getInstance();

        dateInput.setOnClickListener(v -> {

            DatePickerDialog datePicker = new DatePickerDialog(
                    getContext(),
                    (view1, year, month, dayOfMonth) -> {

                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        TimePickerDialog timePicker = new TimePickerDialog(
                                getContext(),
                                (timeView, hour, minute) -> {

                                    calendar.set(Calendar.HOUR_OF_DAY, hour);
                                    calendar.set(Calendar.MINUTE, minute);

                                    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd MMM yyyy, HH:mm");
                                    dateInput.setText(sdf.format(calendar.getTime()));

                                },
                                calendar.get(Calendar.HOUR_OF_DAY),
                                calendar.get(Calendar.MINUTE),
                                true
                        );

                        timePicker.show();
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );


            datePicker.getDatePicker().setMinDate(System.currentTimeMillis());

            datePicker.show();
        });

        EventViewModel viewModel = new ViewModelProvider(this).get(EventViewModel.class);

        EditText title = view.findViewById(R.id.titleInput);
        EditText category = view.findViewById(R.id.categoryInput);
        EditText location = view.findViewById(R.id.locationInput);
        Button saveBtn = view.findViewById(R.id.saveBtn);

        saveBtn.setOnClickListener(v -> {

            String t = title.getText().toString();
            String c = category.getText().toString();
            String l = location.getText().toString();

            if (t.isEmpty() || dateInput.getText().toString().isEmpty()) {
                Toast.makeText(getContext(), "Title & Date required", Toast.LENGTH_SHORT).show();
                return;
            }

            long time = calendar.getTimeInMillis();

            if (time < System.currentTimeMillis()) {
                Toast.makeText(getContext(), "Cannot use past date", Toast.LENGTH_SHORT).show();
                return;
            }

            if (eventId != -1) {
                Event event = new Event(t, c, l, time);
                event.id = eventId;
                viewModel.update(event);
                Toast.makeText(getContext(), "Updated", Toast.LENGTH_SHORT).show();
            } else {
                Event event = new Event(t, c, l, time);
                viewModel.insert(event);
                Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();
            }

            title.setText("");
            category.setText("");
            location.setText("");
            dateInput.setText("");


            NavController navController = Navigation.findNavController(view);
            navController.popBackStack();
        });

        Bundle bundle = getArguments();

        if (bundle != null) {
            eventId = bundle.getInt("id", -1);

            if (eventId != -1) {
                title.setText(bundle.getString("title"));
                category.setText(bundle.getString("category"));
                location.setText(bundle.getString("location"));

                long time = bundle.getLong("dateTime");
                calendar.setTimeInMillis(time);

                dateInput.setText(new java.text.SimpleDateFormat("dd MMM yyyy, HH:mm")
                        .format(new java.util.Date(time)));
            }
        }
    }
}