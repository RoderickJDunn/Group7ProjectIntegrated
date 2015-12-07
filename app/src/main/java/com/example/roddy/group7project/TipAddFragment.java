package com.example.roddy.group7project;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.text.DecimalFormat;

/**
 * Created by Jiebin Gao on 2015-11-20.
 */
public class TipAddFragment extends Fragment implements View.OnClickListener{

    private double normalExpense;
    private double tipAmount;
    private double totalAmount;
    private String note;
    private String restName;
    private String date;
    private String tipRateChosen;
    private EditText et;
    private RadioButton rb;
    DecimalFormat formatter = new DecimalFormat("#0.00");
    TipFragmentCallBack mCallBack = null;

    public TipAddFragment(){ }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.tip_add_expense_fragment, container, false);

        Button conformButton = (Button)rootView.findViewById(R.id.conform_expense_and_note);
        conformButton.setOnClickListener(this);
        Button saveButton = (Button)rootView.findViewById(R.id.save_items);
        saveButton.setOnClickListener(this);
        Button listButton = (Button)rootView.findViewById(R.id.list_all_expenses);
        listButton.setOnClickListener(this);


        rb = (RadioButton)rootView.findViewById(R.id.ten_percent);
        rb.setOnClickListener(this);
        rb = (RadioButton)rootView.findViewById(R.id.fifteen_percent);
        rb.setOnClickListener(this);
        rb = (RadioButton)rootView.findViewById(R.id.twenty_percent);
        rb.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
    }
    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        mCallBack = (TipMainActivity)activity;
    }

    @Override
    public void onClick(View view){

        String TAG = "ExpenseDbAdapter";

        switch(view.getId()){
            case R.id.conform_expense_and_note:
                et = (EditText)getActivity().findViewById(R.id.rest_name);
                if(et.getText() != null) {
                    restName = et.getText().toString();
                }

                et = (EditText)getActivity().findViewById(R.id.editText_expense);
                String normalExp = et.getText().toString();
                if(normalExp == null || normalExp.length() == 0){
                    normalExpense = 0.0;
                }else{
                    normalExpense = Double.parseDouble(et.getText().toString());
                }

                et = (EditText)getActivity().findViewById(R.id.editText_Note);
                if(et.getText() != null) {
                    note = et.getText().toString();
                }

                et = (EditText)getActivity().findViewById(R.id.date_of_expense);
                if(et.getText() != null) {
                    date = et.getText().toString();
                }

                et = (EditText)getActivity().findViewById(R.id.tip_10_percent);
                displayTipAmount(0.1, et);

                et = (EditText)getActivity().findViewById(R.id.amount_10_percent);
                displayTotalAmount(et);

                et = (EditText)getActivity().findViewById(R.id.tip_15_percent);
                displayTipAmount(0.15, et);

                et = (EditText)getActivity().findViewById(R.id.amount_15_percent);
                displayTotalAmount(et);

                et = (EditText)getActivity().findViewById(R.id.tip_20_percent);
                displayTipAmount(0.20, et);

                et = (EditText)getActivity().findViewById(R.id.amount_20_percent);
                displayTotalAmount(et);

                break;

            case R.id.save_items:
                if(tipRateChosen != null) {
                    if (tipRateChosen.equals("10")) {
                        tipAmount = getAmount(R.id.tip_10_percent);
                        totalAmount = getAmount(R.id.amount_10_percent);
                    } else if (tipRateChosen.equals("15")) {
                        tipAmount = getAmount(R.id.tip_15_percent);
                        totalAmount = getAmount(R.id.amount_15_percent);
                    } else if (tipRateChosen.equals("20")) {
                        tipAmount = getAmount(R.id.tip_20_percent);
                        totalAmount = getAmount(R.id.amount_20_percent);
                    }

                    TipDbAdapter dbEntry = new TipDbAdapter(getActivity());
                    try {
                        dbEntry.open();
                        Log.i(TAG, "Database opened.");
                        long temp = dbEntry.createExpense(restName, Double.toString(normalExpense), tipRateChosen+"%", Double.toString(tipAmount),
                                Double.toString(totalAmount), note, date);
                        if(temp != -1){
//                            Log.i(TAG, "Data has been written into database successfully!");
                            Toast.makeText(getActivity().getApplicationContext(), "Data has been written into database successfully!",
                                    Toast.LENGTH_SHORT).show();

                        }else{
//                            Log.i(TAG, "Failed to write data into database!");
                            Toast.makeText(getActivity().getApplicationContext(), "Failed to write data into database!",
                                    Toast.LENGTH_SHORT).show();
                        }
                        dbEntry.close();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }else{
					Toast.makeText(getActivity().getApplicationContext(), "Please select a tip rate to pay!",
                            Toast.LENGTH_SHORT).show();
				}

                break;

            case R.id.ten_percent:
                disableRadioButton(R.id.fifteen_percent);
                disableRadioButton(R.id.twenty_percent);
                tipRateChosen = "10";
                break;

            case R.id.fifteen_percent:
                disableRadioButton(R.id.ten_percent);
                disableRadioButton(R.id.twenty_percent);
                tipRateChosen = "15";
                break;

            case R.id.twenty_percent:
                disableRadioButton(R.id.ten_percent);
                disableRadioButton(R.id.fifteen_percent);
                tipRateChosen = "20";
                break;

            case R.id.list_all_expenses:
                mCallBack.tipListView();
//                TipListViewFragment alv = new TipListViewFragment();
//                FragmentTransaction ft = getFragmentManager().beginTransaction();
//                ft.replace(R.id.fragment_container_tip, alv);
//                ft.addToBackStack(null);
//                ft.commit();
        }
    }

    public void displayTipAmount(double tipRate, EditText et){
        tipAmount = normalExpense * tipRate;
        et.setText(formatter.format(tipAmount).toString());
    }

    public void displayTotalAmount(EditText et){
        totalAmount = normalExpense + tipAmount;
        et.setText(formatter.format(totalAmount).toString());
    }

    public void disableRadioButton(int id){
        rb = (RadioButton)getActivity().findViewById(id);
        if(rb.isChecked()){
            rb.setChecked(false);
        }
    }

    public double getAmount(int id){
        et = (EditText)getActivity().findViewById(id);
        return Double.parseDouble(et.getText().toString());
    }
}
