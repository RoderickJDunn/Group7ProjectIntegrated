package com.example.roddy.group7project;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class CarbCalculatorActivity extends ActionBarActivity
        implements CarbCalcListFragment.OnTripSelectedListener, CarbCalcAddTripFragment.OnTapDoneListener,
        CarbCalcDetailFragment.OnButtonListeners
    {

    public static final String TITLE = "CO2 Calculator";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.carb_calc_activity);
        setTitle(TITLE);

        // Check whether the activity is using the layout version with
        // the fragment_container for small screens. If so, we must add the first fragment
        if (findViewById(R.id.carb_calc_activity) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Create an instance of ExampleFragment
            CarbCalcListFragment listFragment = new CarbCalcListFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.add(R.id.carb_calc_list_view, listFragment).commit();
            // In case this activity was started with special instructions from an Intent,
            // pass the Intent's extras to the fragment as arguments
        }
        else {
            CarbCalcDetailFragment detailFragment = new CarbCalcDetailFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.add(R.id.details_add_pane, detailFragment).commit();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.add(1, 5, 5, "Add Trip");
        menu.add(1, 6, 5, "Instructions");
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        switch (id){
            case R.id.tip_calc:
                go(TipMainActivity.class);
                break;
            case R.id.time_track:
                go(A2_MainActivity.class);
                break;
            case R.id.carb_calc:
                go(CarbCalculatorActivity.class);
                break;
            case R.id.contacts:
               go(ContactActivity.class);
                break;
            case 5:
                openAddTrip();
                break;
            case 6:
                openInstructions();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


        private void go(Class c){
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }

    private void openAddTrip() {
        // fragment transaction (input trip info)
        // Create new fragment and transaction

        Fragment detailFrag = getFragmentManager().findFragmentById(R.id.details_add_pane);

        if (detailFrag != null) {
            //two-pane
            Fragment addTripFrag = new CarbCalcAddTripFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.details_add_pane, addTripFrag);
            transaction.addToBackStack(null);
            transaction.commit();
        }
        else {
            Fragment addTripFrag = new CarbCalcAddTripFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.carb_calc_list_view, addTripFrag);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    private void openInstructions() {
        Fragment detailFrag = getFragmentManager().findFragmentById(R.id.details_add_pane);
        if (detailFrag != null) {
            // two-pane


        }
        else {
            CarbCalcAboutFragment aboutFrag = new CarbCalcAboutFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.carb_calc_list_view, aboutFrag);
            transaction.addToBackStack(null);
            transaction.commit();
        }

    }

    @Override
    public void onTripSelected(long id) {
        Bundle args = new Bundle();
        args.putLong("Trip ID", id);
        CarbCalcDetailFragment detailFragment = new CarbCalcDetailFragment();
        detailFragment.setArguments(args);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        // get a reference to the details pane view
        View detailsFrame = findViewById(R.id.details_add_pane);

        if (detailsFrame != null && detailsFrame.getVisibility()== View.VISIBLE) {
            // in dual-pane layout
            transaction.replace(R.id.details_add_pane, detailFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
        else {
            // if the details pane is null, or not in current layout, we're in a single-pane layout
            transaction.replace(R.id.carb_calc_list_view, detailFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    @Override
    public void onTapDone() {

        Fragment listFragment = getFragmentManager().findFragmentById(R.id.list_pane);
        View listPane = findViewById(R.id.list_pane);

        if (listFragment != null && listPane.getVisibility()== View.VISIBLE) {
            //in two-pane view
            CarbCalcDetailFragment detailFragment = new CarbCalcDetailFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.details_add_pane, detailFragment).commit();
            Intent intent = getIntent();
            startActivity(intent);
        }
        else {
            // in single-pane view
            // listFragment.setArguments(getIntent().getExtras());
            CarbCalcListFragment newListFragment = new CarbCalcListFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.carb_calc_list_view, newListFragment).commit();
        }
    }


    public void onTapDeleteButton() {
        Fragment listFragment = getFragmentManager().findFragmentById(R.id.list_pane);
        View listPane = findViewById(R.id.list_pane);
        if (listFragment != null && listPane.getVisibility()== View.VISIBLE) {
            //in two-pane layout
            Intent intent = getIntent();
            startActivity(intent);
        }
        else {
            //in single pane layout
            CarbCalcListFragment newListFragment = new CarbCalcListFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.carb_calc_list_view, newListFragment).commit();
        }
    }

    @Override
    public void onTapEditButton(Cursor cursor) {
        CarbCalcAddTripFragment editFragment = new CarbCalcAddTripFragment();
        editFragment.setCursor(cursor);
        Fragment listFragment = getFragmentManager().findFragmentById(R.id.list_pane);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        View listPane = findViewById(R.id.list_pane);
        if (listFragment != null && listPane.getVisibility()== View.VISIBLE) {
            //in two-pane layout
            transaction.replace(R.id.details_add_pane, editFragment);
            transaction.addToBackStack(null).commit();
        }
        else {
            // in single-pane layout
            transaction.replace(R.id.carb_calc_list_view, editFragment);
            transaction.addToBackStack(null).commit();
        }
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0 ){
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }




    }
