package com.example.roddy.group7project;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

/**
 * This class displays the detailed information of a specified expense
 * it enables the user to check the summary information about the specified restaurant
 * it also allows user to delete the displayed expense from the database
 * Created by Jiebin Gao on 2015-11-27.
 */
public class TipDetailedInfoFragment extends Fragment implements View.OnClickListener{

    /**
     * The Id of specified expense entry
     */
    private long expenseID;
    /**
     * the number of visits to the specified restaurant
     */
    private String count;
    /**
     * The total expense spent in the specified restaurant
     */
    private String totalExpense;
    /**
     * The specified restaurant name
     */
    private String restName;
    /**
     * Define the format of summary expense
     */
    DecimalFormat formatter = new DecimalFormat("#0.00");

    /**
     * Define an instance of FragmentCallBack to switch between fragments
     */
    TipFragmentCallBack mCallBack = null;

    /**
     * Constructor
     */
    public TipDetailedInfoFragment(){ }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tip_expense_details, container, false);

        // Add two buttons to click listener: delete and display summary info
        Button delButton = (Button)rootView.findViewById(R.id.delete_exp);
        delButton.setOnClickListener(this);

        Button summaryButton = (Button)rootView.findViewById(R.id.show_summary);
        summaryButton.setOnClickListener(this);


        //Get detailed expense information sent from the previous fragment
        Bundle args = getArguments();
        expenseID = args.getInt("Expense ID");
        restName = args.getString("RestName");
        String amount = args.getString("Normal Amount");
        String tipRate = args.getString("Tip Rate");
        String tipAmount = args.getString("Tip Amount");
        String totalAmount = args.getString("Total Amount");
        String note = args.getString("Note");
        String date = args.getString("Date");
        count = args.getString("Number Of Visits");
        totalExpense = args.getString("Total Expense");

        //Display the detailed expense information
        TextView tv = null;
        tv = (TextView)rootView.findViewById(R.id.name_detail);
        tv.setText(restName);
        tv = (TextView)rootView.findViewById(R.id.normal_expense_detail);
        tv.setText(amount);
        tv = (TextView)rootView.findViewById(R.id.tip_rate_detail);
        tv.setText(tipRate);
        tv = (TextView)rootView.findViewById(R.id.tip_amount_detail);
        tv.setText(tipAmount);
        tv = (TextView)rootView.findViewById(R.id.total_amount_detail);
        tv.setText(totalAmount);
        tv = (TextView)rootView.findViewById(R.id.note_detail);
        tv.setText(note);
        tv = (TextView)rootView.findViewById(R.id.date_detail);
        tv.setText(date);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        mCallBack = (TipMainActivity)activity;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
    }

    //Implements the onClick method of the OnClickListener interface to handle the response to the click
    @Override
    public void onClick(View v){
        switch(v.getId()) {
            // delete expense info
            case R.id.delete_exp:
//                TipListViewFragment tipList = new TipListViewFragment();
                Bundle args = new Bundle();
//                args.putString("Delete Expense", "DeleteExp");
                args.putLong("Expense ID", expenseID);
//                tipList.setArguments(args);

                TipDbAdapter dbEntry = new TipDbAdapter(getActivity());
                try {
                    dbEntry.open();
                   // Log.i(TAG, "Database opened.");
                    long temp = dbEntry.deleteExpense(expenseID);
                    if(temp != -1){
//                            Log.i(TAG, "Data has been written into database successfully!");
                        Toast.makeText(getActivity().getApplicationContext(), "Record has been delete from database successfully!",
                                Toast.LENGTH_SHORT).show();

                    }else{
//                            Log.i(TAG, "Failed to write data into database!");
                        Toast.makeText(getActivity().getApplicationContext(), "Failed to delete data from database!",
                                Toast.LENGTH_SHORT).show();
                    }
                    dbEntry.close();
                }catch(Exception e){
                    e.printStackTrace();
                }

                mCallBack.tipListView();
                break;

            // displays the summary information for the specified restaurant
            case R.id.show_summary:
                TextView tv = (TextView) getActivity().findViewById(R.id.summary_info);
                String summaryInfo = "Restaurant Name: " + restName + " \n" +
                                     "Now of Visits to it: " + count + "\n" +
                                     "Total Expense in it($): " + formatter.format(Double.parseDouble(totalExpense));
                tv.setText(summaryInfo);
        }

    }
}
