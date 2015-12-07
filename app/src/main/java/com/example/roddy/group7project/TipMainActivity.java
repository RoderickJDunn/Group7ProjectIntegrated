/* Copyright © 2011-2013 mysamplecode.com, All rights reserved.
  This source code is provided to students of CST2335 for educational purposes only.
  Upadted By: Jiebin Gao
  Date: 2015-11-20
 */
package com.example.roddy.group7project;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

/**
 * This class handles the activities in the main activity, it displays the adding expense fragment by default
 */
public class TipMainActivity extends ActionBarActivity implements TipFragmentCallBack {

    private int backCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tip_activity_main);

        TipListViewFragment tipListFragment = new TipListViewFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
//        transaction.addToBackStack(null);
        transaction.add(R.id.fragment_container_tip, tipListFragment).commit();
    }

    @Override
    public void addExpenseItem(){
        TipAddFragment addTipFragment = new TipAddFragment();

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container_tip, addTipFragment);
        ft.addToBackStack(null);
        ft.commit();

    }

    @Override
    public void showDetailedInfo(Bundle arg){
        TipDetailedInfoFragment detailedFragment = new TipDetailedInfoFragment();
        detailedFragment.setArguments(arg);

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container_tip, detailedFragment);
        ft.addToBackStack(null);
        ft.commit();

    }

    @Override
    public void tipListView(){
        TipListViewFragment tipList = new TipListViewFragment();
//        tipList.setArguments(arg);

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container_tip, tipList);
        ft.addToBackStack(null);
        ft.commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_tip, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        int backCount = getFragmentManager().getBackStackEntryCount();
        if(backCount == 0){
            super.onBackPressed();
        }else {
            getFragmentManager().popBackStack();
        }
    }
}
