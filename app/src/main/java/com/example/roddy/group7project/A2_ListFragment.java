
package com.example.roddy.group7project;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

/**
 * the class name is A2_ListFragment
 *
 * it is used to handle the list fragment for activity two
 *
 * @author Fang He on 2015-12-03
 *
 */
public class A2_ListFragment extends Fragment implements View.OnClickListener{

    /**
     * sqlite database fields
     */
    private A2_DbAdapter dbHelper;
    private SimpleCursorAdapter dataAdapter;

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
     * boolean fields for delete or update
     */
    private Boolean check_delete = false;
    private Boolean check_update = false;

    /**
     * default constructor
     */
    public A2_ListFragment(){ }

    /**
     * on create method
     * @param savedInstanceState bundle object
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * create the view based on the information
     * @param inflater the layout inflater object
     * @param container the view group object
     * @param savedInstanceState the bundle object
     * @return rootView the view object
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.a2_fragment_list, container, false);

        Button addButton = (Button)rootView.findViewById(R.id.a2_list_btn_addnew);
        addButton.setOnClickListener(this);

        Bundle args = getArguments();
        if(args != null){
            if(args.getString("title")!= null){
                check_update=true;
                title = args.getString("title");
                category = args.getString("category");
                duration = args.getString("duration");
                poriority=args.getString("poriority");
                note = args.getString("note");
                date = args.getString("date");
            }else{
                check_delete = true;
            }
            taskid = args.getLong("taskid");
            Log.w("-update-", check_update.toString());
            Log.w("-delete-", check_delete.toString());
        }

        displayListView(rootView);

        return rootView;
    }

    /**
     * the desplay list view method
     * handle database then display it in the view v
     * @param v the view object
     */
    private void displayListView(View v) {

        new AsyncTask<Object, Object, Cursor>() {
            @Override
            public Cursor doInBackground(Object... ignore) {
                dbHelper = new A2_DbAdapter(getActivity());
                dbHelper.open();

                if(check_delete) {
                    dbHelper.deleteTask(taskid);
                }

                if(check_update) {

                    ContentValues content = new ContentValues();
                    content.put("title", title);
                    content.put("category", category);
                    content.put("duration", duration);
                    content.put("poriority", poriority);
                    content.put("note", note);
                    content.put("date", date);
                    dbHelper.updateEntry(content, taskid);
                }

                Cursor cursor = dbHelper.fetchAllExpenses();
                return cursor;
            }


            @Override
            public void onPostExecute(Cursor cursor) {
                dataAdapter.changeCursor(cursor);

            }
        }.execute();


        String[] columns = new String[] {
                A2_DbAdapter.TASKID,
                A2_DbAdapter.TITLE,
                A2_DbAdapter.DURATION,
                A2_DbAdapter.DATE
        };

        int[] to = new int[] {
                R.id.a2_inf_tv_taskid,
                R.id.a2_inf_tv_title,
                R.id.a2_inf_tv_duration,
                R.id.a2_inf_tv_date
        };

        dataAdapter = new SimpleCursorAdapter(
                getActivity(), R.layout.a2_list_inf,
                null,
                columns,
                to,
                0);

        ListView listView = (ListView) v.findViewById(R.id.a2_list_lv_list);
        listView.setAdapter(dataAdapter);

        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view,
                                    int position, long id) {

                Cursor cursor_temp = (Cursor) listView.getItemAtPosition(position);
                int cid = cursor_temp.getColumnIndex("category");
                int did = cursor_temp.getColumnIndex("duration");

                Cursor cursor = dbHelper.fetchExpenseByID(id);

                String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                String category=cursor.getString(cursor.getColumnIndexOrThrow("category"));
                String duration = cursor.getString(cursor.getColumnIndexOrThrow("duration"));
                String poriority=cursor.getString(cursor.getColumnIndexOrThrow("poriority"));
                String note = cursor.getString(cursor.getColumnIndexOrThrow("note"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));


                int count = 0;
                double totalduration = 0.0;
                Cursor cur = null;
                for(int i = 0; i < listView.getCount(); i++){
                    cur = (Cursor)listView.getItemAtPosition(i);
                    if(category.equals(cur.getString(cid))) {
                        totalduration += Double.parseDouble(cur.getString(did));
                        count++;
                    }
                }

                A2_DetailFragment df = new A2_DetailFragment();
                Bundle args = new Bundle();
                args.putInt("taskid", (int)id);
                args.putString("title", title);
                args.putString("duration", duration);
                args.putString("poriority",poriority);
                args.putString("category",category);
                args.putString("note", note);
                args.putString("date", date);
                args.putString("count",count+"");
                args.putString("total_duration", Double.toString(totalduration));
                df.setArguments(args);


                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container_a2_task, df);
                ft.addToBackStack(null);
                ft.commit();

            }
        });

    }

    /**
     * on start method
     */
    @Override
    public void onStart() {
        super.onStart();
    }

    /**
     * on strop method
     */
    @Override
    public void onStop(){
        dbHelper.close();
        super.onStop();
    }

    /**
     * activity creat method
     * @param savedInstanceState the bundle object
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * click listener for each item
     * @param v the view object
     */
    @Override
    public void onClick(View v){
        A2_NewTaskFragment ntf = new A2_NewTaskFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container_a2_task, ntf);
        ft.addToBackStack(null);
        ft.commit();
    }

}