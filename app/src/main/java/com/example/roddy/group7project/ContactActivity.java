package com.example.roddy.group7project;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class ContactActivity extends ActionBarActivity
        implements ContactList.OnContactSelectedListener, ContactAdd.OnTapDoneListener,
        ContactDetail.OnTapDeleteListener
    {

    public static final String Contact_TITLE = "Contact List";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_activity_four);
        setTitle(Contact_TITLE );
        // Check whether the activity is using the layout version with
        // the fragment_container for small screens. If so, we must add the first fragment



        if (findViewById(R.id.contact_activity_four) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Create an instance of ExampleFragment
            ContactList contactListFragment = new ContactList();

          //  contactListFragment.setArguments(getIntent().getExtras());
            FragmentTransaction transaction = getFragmentManager().beginTransaction();

            transaction.add(R.id.contact_list_view, contactListFragment).commit();
            // In case this activity was started with special instructions from an Intent,
            // pass the Intent's extras to the fragment as arguments

            Log.d("Check", "past transaction");
        }
        else {

            ContactDetail detailContactFragment = new ContactDetail();

            //  contactListFragment.setArguments(getIntent().getExtras());
            FragmentTransaction transaction = getFragmentManager().beginTransaction();

            transaction.add(R.id.contact_desc_add_pane, detailContactFragment).commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.add(1, 5, 5, "Add Contact");
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        //noinspection SimplifiableIfStatement
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
                openAddContact();
        }
        return super.onOptionsItemSelected(item);
    }
    private void go(Class c){
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }

    private void openAddContact() {
        Fragment detailContactFrag = getFragmentManager().findFragmentById(R.id.contact_desc_add_pane);
        if (detailContactFrag != null) {
            Fragment addContactFrag= new ContactAdd();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();

            transaction.replace(R.id.contact_desc_add_pane, addContactFrag);
            transaction.addToBackStack(null);


            transaction.commit();
        }
        else {
            Fragment addContactFrag = new ContactAdd();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();

            transaction.replace(R.id.contact_list_view, addContactFrag);
            transaction.addToBackStack(null);


            transaction.commit();
        }

    }

    @Override
    public void onContactSelected(long id) {
        Bundle args = new Bundle();
        args.putLong("Contact ID", id);
        ContactDetail detailContactFragment = new ContactDetail();
        detailContactFragment.setArguments(args);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        // get a reference to the details pane
        Fragment currentContactFrag = getFragmentManager().findFragmentById(R.id.contact_desc_add_pane);
        // if the details pane is null, we're in a single-pane layout
        if (currentContactFrag == null) {
            transaction.replace(R.id.contact_list_view, detailContactFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
        else {
            // in dual-pane layout
            transaction.replace(R.id.contact_desc_add_pane, detailContactFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    @Override
    public void onTapDone() {
        // Create an instance of ExampleFragment
        Fragment contactListFragment = getFragmentManager().findFragmentById(R.id.list_pane);

        if (contactListFragment != null) {
            //in two-pane view
            ContactDetail detailContactFragment = new ContactDetail();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();

            transaction.replace(R.id.contact_desc_add_pane, detailContactFragment).commit();

            Intent intent = getIntent();
            startActivity(intent);
        }
        else {
            // in single-pane view
            ContactList newContactListFragment = new ContactList();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();

            transaction.replace(R.id.contact_list_view, newContactListFragment).commit();
        }
    }


    public void onTapDeleteButton(long id) {
        Fragment contactListFragment = getFragmentManager().findFragmentById(R.id.list_pane);
        if (contactListFragment != null) {
            //in two-pane layout
           /*  ContactDetail detailContactFragment = new  ContactDetail();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.contact_desc_add_pane, detailContactFragment).commit();*/
            Intent intent = getIntent();
            startActivity(intent);
        }
        else {
            //in single pane layout
            ContactList newContactListFragment = new ContactList();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.contact_list_view, newContactListFragment).commit();

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
