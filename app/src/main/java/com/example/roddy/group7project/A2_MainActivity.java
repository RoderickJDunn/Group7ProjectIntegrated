package com.example.roddy.group7project;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;


/**
 * the class name is A2_MainActivity
 *
 * it is used to create the main activity for activity two
 *
 * @author Fang He on 2015-12-03
 *
 */
public class A2_MainActivity extends ActionBarActivity {


    /**
     * on create method
     * @param savedInstanceState the bundle object
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a2_activity_main);

        A2_NewTaskFragment nt = new A2_NewTaskFragment();
        getFragmentManager().beginTransaction().add(R.id.fragment_container_a2_task, nt).commit();
    }


    /**
     * create option menu method
     * @param menu the menu object
     * @return boolean : true : if adding
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_a2, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * menu item select listener
     * @param item the menuitem object
     * @return boolean for selection
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.a2_bar_item_instructor){
            StringBuilder sb = new StringBuilder();
            sb.append("APP Name: Time and Activity Tracker\n\n")
                    .append("Author: Fang He\n")
                    .append("Date: 2015-12-03\n\n")
                    .append("INSTRUCTOR\n\n")
                    .append("* Enter new task with title, category, duration and short note \n\n")
                    .append("* Created Date will be recorded by the system\n\n")
                    .append("* Summary information about category is the total duration in the same category\n\n")
                    .append("* Detail page will show all information\n\n")

                ;


            A2_TopBarFragment topbar = new A2_TopBarFragment();
            Bundle args = new Bundle();
            args.putString("Description", sb.toString());
            topbar.setArguments(args);

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container_a2_task, topbar);
            ft.addToBackStack(null);
            ft.commit();

            return true;

        }else if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * back button listener
     */
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
