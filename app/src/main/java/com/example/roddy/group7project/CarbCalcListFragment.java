package com.example.roddy.group7project;

import android.app.Activity;
import android.app.ListFragment;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.ListView;

/**
 * Created by Roderick on 2015-11-27.
 */
public class CarbCalcListFragment extends ListFragment {

    CarbCalcDbAdapter dbHelper;
    SimpleCursorAdapter dataAdapter;
    private OnTripSelectedListener mCallback;

    public interface OnTripSelectedListener {
        /** Called by HeadlinesFragment when a list item is selected */
        public void onTripSelected(long id);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       int layout = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
                android.R.layout.simple_list_item_activated_1 : android.R.layout.simple_list_item_1;

        dbHelper = new CarbCalcDbAdapter(CarbCalcListFragment.this.getActivity());
        dbHelper.open();

        // how do you add a header to list view? I tried to do it programmatically here, but its not workings
       /* TextView listHeader = new TextView(getActivity());
        listHeader.setText("TRIPS MAN");
        listHeader.setTextSize(20);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(AbsListView.LayoutParams.WRAP_CONTENT, AbsListView.LayoutParams.WRAP_CONTENT);
        listHeader.setLayoutParams(params);
        LinearLayout listLayout = (LinearLayout) getActivity().findViewById(R.id.carb_calc_activity);
        listLayout.addView(listHeader);*/

        // add a lot of rows to database for testing
        /* for (int i = 0; i<1000; i++) {
            dbHelper.insertTrip("category" + i, "vehicle"+ i, i, i/10, "", "", "");
        }*/
         displayListView();

    }

    private void displayListView() {

        new AsyncTask<Void, Void, Cursor>() {

            @Override
            protected Cursor doInBackground(Void... params) {

                Cursor cursor = dbHelper.fetchAllTrips();
                return cursor;
            }

            @Override
            protected void onPostExecute(Cursor cursor) {
                super.onPostExecute(cursor);
                String[] columns = new String[]{
                        CarbCalcDbAdapter.KEY_TRIP_ID,
                        CarbCalcDbAdapter.KEY_CATEGORY,
                        CarbCalcDbAdapter.KEY_CO2
                };

                // the XML defined views which the data will be bound to
                int[] to = new int[]{
                        R.id.trip_id,
                        R.id.category,
                        R.id.carbonListView
                };

                // create the adapter using the cursor pointing to the desired data
                //as well as the layout information
                dataAdapter = new SimpleCursorAdapter(
                        getActivity(), R.layout.simple_text_view1,
                        cursor,
                        columns,
                        to,
                        0);
                setListAdapter(dataAdapter);
            }
        }.execute();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception.
        try {
            mCallback = (OnTripSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnTripSelectedListener");
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        mCallback.onTripSelected(id);
       // getListView().setItemChecked(position, true);
    }
}
