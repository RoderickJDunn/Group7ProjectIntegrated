package com.example.roddy.group7project;

import android.app.Activity;
import android.app.Fragment;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.Editable;
import android.text.TextWatcher;
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

/**
 * Created by Roderick on 2015-11-27.
 */
public class CarbCalcAddTripFragment extends Fragment {
    OnTapDoneListener mCallback;
    CarbCalcDbAdapter dbHelper;
    SimpleCursorAdapter dataAdapter;
    private Cursor cursor;

    private final static double CAR_CARBON_FACTOR = 0.00025;
    private final static double PLANE_CARBON_FACTOR = 0.00007436;
    private final static double MOTORBIKE_CARBON_FACTOR = 0.000195;
    private final static double BUS_CARBON_FACTOR = 0.000175;
    private final static double TRAIN_CARBON_FACTOR = 0.00002;

    public void setCursor(Cursor cursor) {
        this.cursor = cursor;
    }

    public interface OnTapDoneListener {
        /** Called by HeadlinesFragment when a list item is selected */
        public void onTapDone();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbHelper = new CarbCalcDbAdapter(getActivity());
        dbHelper.open();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inputTripInfoView = inflater.inflate(R.layout.carb_calc_add_trip, container, false);
        // add listeners to various views
        registerListeners(inputTripInfoView);

        // get reference to spinner (to select vehicle) and add items from string resource
        Spinner vehicleSpinner = (Spinner)inputTripInfoView.findViewById(R.id.vehicle_input);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(
                this.getActivity(), R.array.vehicles_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vehicleSpinner.setAdapter(spinnerAdapter);

        // if cursor object is not null, the user has pressed edit on a trip, so this will fill the
        // fields with the current trip information
        if (cursor != null) {
            preFillInformationFromEdit(inputTripInfoView);
        }
        // Inflate the layout for this fragment
        return inputTripInfoView;
    }

    private void registerListeners(final View inputTripInfoView) {
        final EditText categoryInput = (EditText)inputTripInfoView.findViewById(R.id.category_input);
        final Button doneButton = (Button)inputTripInfoView.findViewById(R.id.done_button_add_trip);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (categoryInput.getText().toString().trim().length()==0) {
                    Toast errorNoCategory = Toast.makeText(getActivity(), "Enter a category", Toast.LENGTH_SHORT);
                    // TextView toastView = (TextView)
                    errorNoCategory.getView().setBackgroundColor(Color.argb(255, 250, 0, 0));
                    errorNoCategory.show();

                }
                else {
                    storeInDatabase();
                }
            }
        });

        final EditText distanceInput = (EditText)inputTripInfoView.findViewById(R.id.distance_input);

        distanceInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence distanceFieldText, int start, int before, int count) {
                updateCarbonField(inputTripInfoView, distanceFieldText);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        final Spinner vehicleInput = (Spinner)inputTripInfoView.findViewById(R.id.vehicle_input);

        // The code for this listener method for handling Spinner events is based upon a
        // Stackoverflow.com answer by user "ccheneson"
        // link: http://stackoverflow.com/questions/2262412/update-content-after-selecting-item-in-spinner

        vehicleInput.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CharSequence distanceFieldText = distanceInput.getText();
                updateCarbonField(inputTripInfoView, distanceFieldText);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    public void updateCarbonField(View inputTripInfoView, CharSequence distanceFieldText){
        final TextView carbonTotalView = (TextView)inputTripInfoView.findViewById(R.id.carbon_field);

        String vehicleType = ((Spinner)inputTripInfoView.findViewById(R.id.vehicle_input))
                .getSelectedItem().toString();
        String distanceString = distanceFieldText.toString();
        if (distanceString != null && !distanceString.trim().equals("")) {
            double distance;
            try {
                distance = Double.parseDouble(distanceFieldText.toString());
                switch (vehicleType) {
                    case "Car":
                        carbonTotalView.setText(String.valueOf(distance * CAR_CARBON_FACTOR));
                        break;
                    case "Plane":
                        carbonTotalView.setText(String.valueOf(distance * PLANE_CARBON_FACTOR));
                        break;
                    case "Motorbike":
                        carbonTotalView.setText(String.valueOf(distance * MOTORBIKE_CARBON_FACTOR));
                        break;
                    case "Bus":
                        carbonTotalView.setText(String.valueOf(distance * BUS_CARBON_FACTOR));
                        break;
                    case "Train":
                        carbonTotalView.setText(String.valueOf(distance * TRAIN_CARBON_FACTOR));
                        break;
                }
            }catch(NumberFormatException e) {
                e.printStackTrace();
            }

        }
        else {
            carbonTotalView.setText("");
        }
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception.
        try {
            mCallback = (OnTapDoneListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnTapDoneListener");
        }
    }

    public void storeInDatabase() {
        // get text from each of the input fields in the AddTrip fragment view

        final String category = ((EditText)getActivity().findViewById(R.id.category_input)).getText().toString();
        final String vehicleType = ((Spinner)getActivity().findViewById(R.id.vehicle_input)).getSelectedItem().toString();
        final String distanceString = ((EditText)getActivity().findViewById(R.id.distance_input)).getText().toString();
        final String cO2String = ((TextView)getActivity().findViewById(R.id.carbon_field)).getText().toString();
        final double distance;
        final double co2Amount;
        if (distanceString != null && !distanceString.trim().equals("")) {
            distance = Double.parseDouble(distanceString);
            co2Amount = Double.parseDouble(cO2String);
        } else {
            distance = 0;
            co2Amount = 0;
        }

        final String date = ((EditText)getActivity().findViewById(R.id.date_input)).getText().toString();
        final String note = ((EditText)getActivity().findViewById(R.id.note_input)).getText().toString();


        new AsyncTask<Void,Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                if (cursor == null) {
                    dbHelper.insertTrip(category, vehicleType, distance, co2Amount, date, note);
                }
                else {
                    dbHelper.updateTrip(cursor.getLong(0), category, vehicleType, distance, co2Amount, date, note);
                }
                return null;
            }
        }.execute();

        mCallback.onTapDone();
    }

    public void preFillInformationFromEdit(View inputTripInfoView){
        TextView tripTitle = (TextView) inputTripInfoView.findViewById(R.id.add_edit_trip_header);
        tripTitle.setText("Edit Trip " + cursor.getLong(0));
        EditText categoryInput = (EditText)inputTripInfoView.findViewById(R.id.category_input);
        categoryInput.setText(cursor.getString(1));
        Spinner vehicleInput = (Spinner)inputTripInfoView.findViewById(R.id.vehicle_input);
        switch (cursor.getString(2)) {
            case "Car":
                vehicleInput.setSelection(0);
                break;
            case "Plane":
                vehicleInput.setSelection(1);
                break;
            case "Motorbike":
                vehicleInput.setSelection(2);
                break;
            case "Bus":
                vehicleInput.setSelection(3);
                break;
            case "Train":
                vehicleInput.setSelection(4);
                break;
        }
        EditText distanceInput = (EditText)inputTripInfoView.findViewById(R.id.distance_input);
        distanceInput.setText(cursor.getString(3));

        EditText dateInput = (EditText)inputTripInfoView.findViewById(R.id.date_input);
        dateInput.setText(cursor.getString(5));

        EditText noteInput = (EditText)inputTripInfoView.findViewById(R.id.note_input);
        noteInput.setText(cursor.getString(6));

    }

    // need method to update database row (rather than add) due to edit functionality
    // put if statement at start of store in database. If this primary key already exists
    //  then call this method, instead of doing the rest of "storeInDatabase"
}
