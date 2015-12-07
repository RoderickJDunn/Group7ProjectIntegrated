package com.example.roddy.group7project;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * the class name is A2_NewTaskFragment
 *
 * it is used to handle new task window of the Activity two
 *
 * @author Fang He on 2015-12-03
 *
 */
public class A2_NewTaskFragment extends Fragment implements View.OnClickListener{


    /**
     * define all fields
     */
    private String title;
    private String category;
    private double duration;
    private String poriority;
    private String note;
    private String date;


    /**
     * define window comp's object
     */
    private EditText et;
    private Spinner sp;


    /**
     * default constructor
     */
    public A2_NewTaskFragment(){ }

    /**
     * create view and set the listoner for button
     * @param inflater layoutinflater object
     * @param container viewgroup object
     * @param savedInstanceState bundle object
     * @return rootview the fragment view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.a2_fragment_newtask, container, false);
        Button saveBtn = (Button)rootView.findViewById(R.id.a2_new_Btn_save);
        saveBtn.setOnClickListener(this);
        Button listBtn = (Button)rootView.findViewById(R.id.a2_new_btn_list);
        listBtn.setOnClickListener(this);
        return rootView;
    }

    /**
     * on start method
     */
    @Override
    public void onStart() {
        super.onStart();
    }

    /**
     * activity create method
     * @param savedInstanceState the bundle object
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * click listener for save, list button
     * @param view the view object
     */
    @Override
    public void onClick(View view){

        String TAG = "A2_DbAdapter";

        switch(view.getId()){
            case R.id.a2_new_Btn_save:

                et = (EditText)getActivity().findViewById(R.id.a2_new_et_title);
                String temp_et=et.getText().toString();
                if(temp_et != null && temp_et.length()!=0) {
                    title = et.getText().toString();
                }else{
                    title="NEW TASK";
                }

                et = (EditText)getActivity().findViewById(R.id.a2_new_et_category);
                temp_et=et.getText().toString();
                if(temp_et != null && temp_et.length()!=0) {
                    category = et.getText().toString();
                }else{
                    category="NEW TASK";
                }

                et = (EditText)getActivity().findViewById(R.id.a2_new_et_duration);
                String duration_temp = et.getText().toString();
                if(duration_temp == null || duration_temp.length() == 0){
                    duration = 0.0;
                }else{
                    duration = Double.parseDouble(et.getText().toString());
                }

                sp = (Spinner)getActivity().findViewById(R.id.a2_new_sp_poriority);
                if(sp.getSelectedItem()!= null) {
                    poriority = sp.getSelectedItem().toString();
                }

                et = (EditText)getActivity().findViewById(R.id.a2_new_et_note);
                if(et.getText() != null) {
                    note = et.getText().toString();
                }

                date=new SimpleDateFormat("MM/dd/yyyy").format(new Date());



                    A2_DbAdapter dbEntry = new A2_DbAdapter(getActivity());
                    try {
                        dbEntry.open();
                        long temp = dbEntry.createExpense(title, category, Double.toString(duration),poriority,note, date);
                        if(temp != -1){

                            Toast.makeText(getActivity().getApplicationContext(), "GREAT!-----NEW TASK IS ADDED",
                                    Toast.LENGTH_SHORT).show();

                        }else{
                            Toast.makeText(getActivity().getApplicationContext(), "ERROR!---CAN NOT BE ADDED",
                                    Toast.LENGTH_SHORT).show();
                        }
                        dbEntry.close();
                    }catch(Exception e){
                        e.printStackTrace();
                    }


                A2_NewTaskFragment temp1 = new A2_NewTaskFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container_a2_task, temp1);
                ft.addToBackStack(null);
                ft.commit();
                break;

            case R.id.a2_new_btn_list:

                A2_ListFragment temp2 = new A2_ListFragment();
                ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container_a2_task, temp2);
                ft.addToBackStack(null);
                ft.commit();

        }
    }


}
