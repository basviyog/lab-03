
package com.example.listycitylab3;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AddCityFragment extends DialogFragment {

    private EditText editCityName;
    private EditText editProvinceName;
    private AddCityDialogListener listener;

    private String originalCityName; // To store name if editing the city
    private String originalProvinceName; // To store province if editing the province
    private int cityPosition = -1; // -1 for adding, >= 0 for editing

    public static final String ARG_CITY_NAME = "arg_city_name";
    public static final String ARG_PROVINCE_NAME = "arg_province_name";
    public static final String ARG_POSITION = "arg_position";

    public interface AddCityDialogListener {
        void addCity(City city);
        void editCity(City newCityData, int position); // For editing
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof AddCityDialogListener) {
            listener = (AddCityDialogListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement AddCityDialogListener");
        }
    }


    public static AddCityFragment newInstance() {
        return new AddCityFragment();
    }

    public static AddCityFragment newInstance(City cityToEdit, int position) {
        AddCityFragment fragment = new AddCityFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CITY_NAME, cityToEdit.getName());
        args.putString(ARG_PROVINCE_NAME, cityToEdit.getProvince());
        args.putInt(ARG_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_add_city, null);
        editCityName = view.findViewById(R.id.edit_text_city_text);
        editProvinceName = view.findViewById(R.id.edit_text_province_text);

        String dialogTitle = "Add City";
        String positiveButtonText = "Add";

        Bundle arguments = getArguments();
        if (arguments != null) {
            originalCityName = arguments.getString(ARG_CITY_NAME);
            originalProvinceName = arguments.getString(ARG_PROVINCE_NAME);
            cityPosition = arguments.getInt(ARG_POSITION, -1);

            if (originalCityName != null && originalProvinceName != null && cityPosition != -1) {
                editCityName.setText(originalCityName);
                editProvinceName.setText(originalProvinceName);
                dialogTitle = "Edit City";
                positiveButtonText = "Save";
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(view)
                .setTitle(dialogTitle)
                .setNegativeButton("Cancel", null)
                .setPositiveButton(positiveButtonText, (dialog, which) -> {
                    String cityName = editCityName.getText().toString().trim();
                    String provinceName = editProvinceName.getText().toString().trim();

                    if (cityName.isEmpty()) {
                        editCityName.setError("City name cannot be empty");
                        return;
                    }
                    if (provinceName.isEmpty()) {
                        editProvinceName.setError("Province name cannot be empty");
                        return;
                    }

                    City currentCityData = new City(cityName, provinceName);

                    if (cityPosition != -1) { // Editing
                        listener.editCity(currentCityData, cityPosition);
                    } else { // Adding
                        listener.addCity(currentCityData);
                    }
                });
        return builder.create();
    }
}

