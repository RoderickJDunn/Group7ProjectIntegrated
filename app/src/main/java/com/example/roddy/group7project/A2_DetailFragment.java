package com.example.roddy.group7project;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * the class name is A2_DetailFragment
 *
 * it is used to handle the detail fragment for activity two
 *
 * @author Fang He on 2015-12-03
 *
 */
public class A2_DetailFragment extends Fragment implements View.OnClickListener{

    /**
     * entry fields
     */
    private long taskid;
    private String title;
    private String category;
    private String duration;
    private String poriority;
    private String note;
    private String date;

    /**
     * The total duration for each category
     */
    private String total_duration;

    /**
     * view field
     */
    private View rootView;

    /**
     * default Constructor
     */
    public A2_DetailFragment(){ }

    /**
     * create the view based on the information
     * @param inflater the layout inflater object
     * @param container the view group object
     * @param savedInstanceState the bundle object
     * @return rootView the view object
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         rootView = inflater.inflate(R.layout.a2_fragment_detail, container, false);

        Button delBtn = (Button)rootView.findViewById(R.id.a2_details_btn_delete);
        delBtn.setOnClickListener(this);

        Button summaryBtn = (Button)rootView.findViewById(R.id.a2_details_btn_summary);
        summaryBtn.setOnClickListener(this);

        Button saveBtn = (Button)rootView.findViewById(R.id.a2_details_btn_save);
        saveBtn.setOnClickListener(this);


        Button cancelBtn = (Button)rootView.findViewById(R.id.a2_details_btn_cancel);
        cancelBtn.setOnClickListener(this);

        Bundle args = getArguments();
        taskid = args.getInt("taskid");
        title = args.getString("title");
        category = args.getString("category");
        duration = args.getString("duration");
        poriority=args.getString("poriority");
        note = args.getString("note");
        date = args.getString("date");
        total_duration=args.getString("total_duration");


        EditText tv = null;
        tv = (EditText)rootView.findViewById(R.id.a2_details_tv_title);
        tv.setText(title);
        tv = (EditText)rootView.findViewById(R.id.a2_details_tv_category);
        tv.setText(category);
        tv = (EditText)rootView.findViewById(R.id.a2_details_tv_duration);
        tv.setText(duration);
        tv = (EditText)rootView.findViewById(R.id.a2_details_tv_poriority);
        tv.setText(poriority);
        tv = (EditText)rootView.findViewById(R.id.a2_details_tv_note);
        tv.setText(note);
        tv = (EditText)rootView.findViewById(R.id.a2_details_tv_date);
        tv.setText(date);

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
     * click listener for delete, cancel and save button
     * @param v view object
     */
    @Override
    public void onClick(View v){
        switch(v.getId()) {
            // delete expense info
            case R.id.a2_details_btn_delete:
                A2_ListFragment alvc = new A2_ListFragment();
                Bundle args = new Bundle();

                args.putLong("taskid", taskid);
                alvc.setArguments(args);

                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container_a2_task, alvc);
                ft.addToBackStack(null);
                ft.commit();

                break;

            case R.id.a2_details_btn_cancel:
                alvc = new A2_ListFragment();
                args = new Bundle();

                ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container_a2_task, alvc);
                ft.addToBackStack(null);
                ft.commit();

                break;

            case R.id.a2_details_btn_save:


                Log.w("save btn", "update  work");
                alvc = new A2_ListFragment();
                args = new Bundle();

                args.putLong("taskid", taskid);
                EditText tv = null;
                tv = (EditText)rootView.findViewById(R.id.a2_details_tv_title);
                args.putString("title", tv.getText().toString());
                tv = (EditText)rootView.findViewById(R.id.a2_details_tv_category);
                args.putString("category", tv.getText().toString());
                        tv = (EditText) rootView.findViewById(R.id.a2_details_tv_duration);
                args.putString("duration", tv.getText().toString());
                        tv = (EditText) rootView.findViewById(R.id.a2_details_tv_poriority);
                args.putString("poriority", tv.getText().toString());
                        tv = (EditText) rootView.findViewById(R.id.a2_details_tv_note);
                args.putString("note", tv.getText().toString());
                        tv = (EditText) rootView.findViewById(R.id.a2_details_tv_date);
                args.putString("date", tv.getText().toString());


                alvc.setArguments(args);

                ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container_a2_task, alvc);
                ft.addToBackStack(null);
                ft.commit();
                break;


            case R.id.a2_details_btn_summary:
                TextView tv_temp = (TextView) getActivity().findViewById(R.id.a2_details_tv_summary);
                Button btn = (Button) getActivity().findViewById(R.id.a2_details_btn_summary);
                String summaryInf = "total_duration ( for category \"" + category + "\" ) are " + total_duration;
                String btnInf="HIDE SUMMARY";
                if (tv_temp.getText()!=""){
                    summaryInf="";
                    btnInf="CATEGORY SUMMARY";

                }
                tv_temp.setText(summaryInf);
                btn.setText(btnInf);

        }

    }
}
