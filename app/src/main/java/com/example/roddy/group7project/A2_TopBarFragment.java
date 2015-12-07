package com.example.roddy.group7project;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


/**
 * the class name is A2_topbarFragment
 *
 * it is used to handle top bar of the Activity two
 *
 * @author Fang He on 2015-12-03
 *
 */
public class A2_TopBarFragment extends Fragment implements View.OnClickListener{

    /**
     * default Constructor
     */
    public A2_TopBarFragment(){ }

    /**
     * create a fragment
     * @param inflater:inflater object
     * @param container: viewGroup object
     * @param savedInstanceState: bundle object
     * @return rootView: View for the fragment's UI
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.a2_fragment_topbar, container, false);

        Bundle args = getArguments();
        String temp = args.getString("Description");
        TextView tv = (TextView)rootView.findViewById(R.id.a2_bar_tv_desc);
        tv.setText(temp);

        Button okBtn = (Button)rootView.findViewById(R.id.a2_bar_btn_ok);
        okBtn.setOnClickListener(this);


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
     * onActivity created method
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * click listener
     * @param v view object
     */
    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.a2_bar_btn_ok:
                getActivity().getFragmentManager().popBackStack();
                break;
        }
    }
}
