package com.example.roddy.group7project;

import android.app.Activity;
import android.app.Fragment;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

/**
 * This class fetches the the saved items from database and displays them in a list view
 * also it implements the View OnclickListener interface to handle the click action
 */
public class TipListViewFragment extends Fragment implements View.OnClickListener{

    /**
     * Object to connect to database
     */
    private TipDbAdapter dbHelper;
    /**
     * Adapter to handle the data from database and display them in the list view
     */
    private SimpleCursorAdapter dataAdapter;
    /**
     * This is used to identify the item will be deleted
     */
    private long expenseID;
    /**
     * designate if a delete button is pressed
     */
    private Boolean deleteItem = false;

    /**
     * Define an interface instance of FragmentCallBack to show detailed expense info
     */
    TipFragmentCallBack mCallBack = null;

    /**
     * constructor
     */
    public TipListViewFragment(){ }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.action_bar, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_bar){
            //Do whatever you want to do
            StringBuilder sb = new StringBuilder();
            sb.append("1. This app will Store tip records for an expense in a restaurant: \n\n")
                    .append("2. Normal amount means the expense without tips\n\n")
                    .append("3. You can write some information as a note for the expense\n\n")
                    .append("4. There is no specific requirements on the date format\n\n")
                    .append("5. Please press \"OK\" button once you finishing input, " +
                            "the tip amount for each tip rate will be calculated\n\n")
                    .append("Author: Jiebin Gao\n")
                    .append("Date: 2015-11-28");
            Toast.makeText(getActivity().getApplicationContext(), sb,
                    Toast.LENGTH_SHORT).show();

//            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.tip_all_expenses, container, false);

        // Add the button to listener to respond the click action
        Button addButton = (Button)rootView.findViewById(R.id.add_expense);
        addButton.setOnClickListener(this);



       /* // get the argument passed form the previous fragment
        Bundle args = getArguments();
        if(args != null){
            expenseID = args.getLong("Expense ID");
            deleteItem = true;
        }*/

        //Generate ListView from SQLite Database
        displayListView(rootView);

        return rootView;
    }

    /**
     * Display the values get from database
     * @param v: current View
     */
    private void displayListView(View v) {

        // put the database operation in background so that it will not block the main process
        new AsyncTask<Object, Object, Cursor>() {
            @Override
            public Cursor doInBackground(Object... ignore) {
                dbHelper = new TipDbAdapter(getActivity());
                dbHelper.open();

                if(deleteItem) {
                    dbHelper.deleteExpense(expenseID);
                }

                Cursor cursor = dbHelper.fetchAllExpenses();
                return cursor;
            }

            @Override
            public void onPostExecute(Cursor cursor) {
                dataAdapter.changeCursor(cursor);

            }
        }.execute();

        // The desired columns to be bound
        String[] columns = new String[] {
                TipDbAdapter.EXPID,
                TipDbAdapter.RESTNAME,
                TipDbAdapter.TOTALAMOUNT,
                TipDbAdapter.CREATEDATE
        };

        // the XML defined views which the data will be bound to
        int[] to = new int[] {
                R.id.expid_info,
                R.id.name_info,
                R.id.total_amount_info,
                R.id.date_info
        };

        // create the adapter using the cursor pointing to the desired data
        //as well as the layout information
        dataAdapter = new SimpleCursorAdapter(
                getActivity(), R.layout.tip_expense_info,
                null,
                columns,
                to,
                0);

        ListView listView = (ListView) v.findViewById(R.id.listView1);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);

        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view,
                                    int position, long id) {
                // Get the cursor, positioned to the corresponding row in the result set
                Cursor cursor1 = (Cursor) listView.getItemAtPosition(position);
                int columnNameIndex = cursor1.getColumnIndex("name");
                int columnTotalAmountIndex = cursor1.getColumnIndex("totalamount");

                Cursor cursor = dbHelper.fetchExpenseByID(id);

                // Get the detailed information from this row in the database.
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String amount = cursor.getString(cursor.getColumnIndexOrThrow("amount"));
                String tiprate = cursor.getString(cursor.getColumnIndexOrThrow("tiprate"));
                String tipamount = cursor.getString(cursor.getColumnIndexOrThrow("tipamount"));
                String totalamount = cursor.getString(cursor.getColumnIndexOrThrow("totalamount"));
                String note = cursor.getString(cursor.getColumnIndexOrThrow("note"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("createdate"));

                // find all restaurant with the same specified name, count the number as
                // the number of visits, then calculates the total expense in this restaurant.
                int count = 0;
                double totalExpense = 0.0;
                Cursor cur = null;
                for(int i = 0; i < listView.getCount(); i++){
                    cur = (Cursor)listView.getItemAtPosition(i);
                    if(name.equals(cur.getString(columnNameIndex))) {
                        totalExpense += Double.parseDouble(cur.getString(columnTotalAmountIndex));
                        count++;
                    }
                }

                // Collect the detailed expense information and send them as argument to another fragment to display
//                TipDetailedInfoFragment detailedInfo = new TipDetailedInfoFragment();
                Bundle args = new Bundle();
                args.putInt("Expense ID", (int)id);
                args.putString("RestName", name);
                args.putString("Normal Amount", amount);
                args.putString("Tip Rate", tiprate);
                args.putString("Tip Amount", tipamount);
                args.putString("Total Amount", totalamount);
                args.putString("Note", note);
                args.putString("Date", date);
                args.putString("Number Of Visits", Integer.toString(count));
                args.putString("Total Expense", Double.toString(totalExpense));

                mCallBack.showDetailedInfo(args);
            }
        });

        //
        EditText myFilter = (EditText) v.findViewById(R.id.myFilter);
        myFilter.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                dataAdapter.getFilter().filter(s.toString());
            }
        });

        dataAdapter.setFilterQueryProvider(new FilterQueryProvider() {
            public Cursor runQuery(CharSequence constraint) {
                return dbHelper.fetchExpenseByName(constraint.toString());
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallBack = (TipMainActivity) activity;
        }catch(ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement FragmentCallBack");
        }
    }

    @Override
    public void onStop(){
        dbHelper.close();
        super.onStop();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
    }

    //Implement onClick method of OnclickListener interface to handle the actions responding to click.
    @Override
    public void onClick(View v){
        mCallBack.addExpenseItem();
    }

}