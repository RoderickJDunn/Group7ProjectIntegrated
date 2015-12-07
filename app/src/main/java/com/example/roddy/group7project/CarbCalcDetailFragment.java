package com.example.roddy.group7project;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;


/**
 * Created by Roderick on 2015-11-29.
 */
public class CarbCalcDetailFragment extends Fragment {
    CarbCalcDbAdapter dbHelper;
    SimpleCursorAdapter dataAdapter;
    OnButtonListeners mCallback;
    Cursor cursor;
    String category;
    long id=0;

    public interface OnButtonListeners {
        public void onTapDeleteButton();
        public void onTapEditButton(Cursor cursor);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new CarbCalcDbAdapter(getActivity());
        dbHelper.open();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View detailView = inflater.inflate(R.layout.carb_calc_detail, container, false);
        cursor = dbHelper.fetchTripById(id);
        // check if cursor is empty
        if (cursor != null && cursor.getCount() > 0) {
            TextView tripTitle = (TextView) detailView.findViewById(R.id.trip_title_info);
            tripTitle.setText("Trip " + cursor.getLong(0));
            TextView categoryView = (TextView) detailView.findViewById(R.id.category_info);
            this.category = cursor.getString(1);
            categoryView.setText("Category: " + category);
            TextView vehicle = (TextView) detailView.findViewById(R.id.vehicletype_info);
            vehicle.setText("Vehicle Type: " + cursor.getString(2));
            TextView distance = (TextView) detailView.findViewById(R.id.distance_info);
            distance.setText(String.valueOf(cursor.getDouble(3)) + " miles");
            TextView co2 = (TextView) detailView.findViewById(R.id.carbon_info);
            co2.setText(String.valueOf(cursor.getDouble(4)) + " metric tons of CO2");
            TextView date = (TextView) detailView.findViewById(R.id.date_info);
            date.setText(cursor.getString(5));
            TextView note = (TextView) detailView.findViewById(R.id.note_info);
            note.setText(cursor.getString(6));
        }

        Button deleteButton = (Button)detailView.findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CarbCalcDetailFragment.this.getActivity());
                builder.setTitle("Delete this trip?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dbHelper.removeTrip(id);
                                mCallback.onTapDeleteButton();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
            }
        });

        Button editButton = (Button)detailView.findViewById(R.id.editbutton);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onTapEditButton(cursor);
            }
        });

        Button summaryButton = (Button)detailView.findViewById(R.id.summarybutton);
        summaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayTotalCarbonForCategory();

            }
        });
        return detailView;
    }

    private void displayTotalCarbonForCategory() {

        new AsyncTask<Void, Void, Cursor>() {
            @Override
            protected Cursor doInBackground(Void... params) {
                Cursor cursorForCategory = dbHelper.fetchTripsByCategory(category);
                return cursorForCategory;
            }

            @Override
            protected void onPostExecute(Cursor cursorForCategory) {
                super.onPostExecute(cursorForCategory);
                double totalCarbon=0;
                while (cursorForCategory.moveToNext()) {
                    totalCarbon+=cursorForCategory.getDouble(0);

                }

                DecimalFormat df = new DecimalFormat("#0.00");
                String toastDisplay = String.format
                        ("%s metric tons of carbon produced from all %s trips",
                                df.format(totalCarbon), category);
                Toast.makeText(getActivity(), toastDisplay, Toast.LENGTH_LONG).show();
            }
        }.execute();
        ;


    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        this.id = args.getLong("Trip ID");
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception.
        try {
            mCallback = (OnButtonListeners) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnTapDoneListener");
        }
    }
}
