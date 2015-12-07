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
 * Created by Senedu on 2015-12-02.
 */
public class ContactList extends ListFragment {

    ContactDbAdapter contactdbHelper;
    SimpleCursorAdapter contactAdapter;
    private OnContactSelectedListener contactCallback;

    public interface OnContactSelectedListener {
        /** Called by HeadlinesFragment when a list item is selected */
        public void onContactSelected(long id);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       int layout = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
                android.R.layout.simple_list_item_activated_1 : android.R.layout.simple_list_item_1;


        contactdbHelper = new ContactDbAdapter(ContactList.this.getActivity());
        contactdbHelper.open();

        displayContactListView();

    }

    private void displayContactListView() {

        new AsyncTask<Void, Void, Cursor>() {

            @Override
            protected Cursor doInBackground(Void... params) {
                Cursor contactCursor = contactdbHelper.fetchAllContacts();
                return contactCursor;
            }

            @Override
            protected void onPostExecute(Cursor contactCursor) {
                super.onPostExecute(contactCursor);
                String[] columns = new String[]{
                        ContactDbAdapter.KEY_CONTACT_ID,
                        ContactDbAdapter.KEY_FIRST_NAME,
                        ContactDbAdapter.KEY_LAST_NAME
                };

                // the XML defined views which the data will be bound to
                int[] to = new int[]{
                        R.id.contact_id,
                        R.id.first_name,
                        R.id.last_name
                };

                // create the adapter using the contactCursor pointing to the desired data
                //as well as the layout information
                contactAdapter = new SimpleCursorAdapter(
                        getActivity(), R.layout.contact_view,
                        contactCursor,
                        columns,
                        to,
                        0);

                setListAdapter(contactAdapter);
            }
        }.execute();

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception.
        try {
            contactCallback = (OnContactSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnContactSelectedListener");
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        contactCallback.onContactSelected(id);
       // getListView().setItemChecked(position, true);
    }
}
