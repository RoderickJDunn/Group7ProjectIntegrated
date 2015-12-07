package com.example.roddy.group7project;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Senedu on 2015-12-01.
 */
public class ContactDetail extends Fragment {
    ContactDbAdapter contactdbHelper;
    SimpleCursorAdapter contactAdapter;
    OnTapDeleteListener contactCallback;
    long id=0;

    public interface OnTapDeleteListener {
        public void onTapDeleteButton(long id);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contactdbHelper = new ContactDbAdapter(getActivity());
        contactdbHelper.open();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View detailView = inflater.inflate(R.layout.contact_detail_activity, container, false);
        Cursor contactAdapter = contactdbHelper.fetchContactById(id);
        // check if cursor is empty
        if (contactAdapter != null && contactAdapter.getCount() > 0) {
            TextView contactTitleDetail = (TextView) detailView.findViewById(R.id.contact_title_detail);
            contactTitleDetail.setText("Contact " + contactAdapter.getLong(0));
            TextView fn = (TextView) detailView.findViewById(R.id.fn_detail);
            fn.setText("First Name: " + contactAdapter.getString(1));
            TextView ln = (TextView) detailView.findViewById(R.id.ln_detail);
            ln.setText("Last Name: " + contactAdapter.getString(2));
            TextView pn = (TextView) detailView.findViewById(R.id.pn_detail);
            pn.setText("Phone Number: " + String.valueOf(contactAdapter.getInt(3)) );
            TextView ema = (TextView) detailView.findViewById(R.id.email_detail);
            ema.setText("E-mail: " + contactAdapter.getString(4));
            TextView contDate = (TextView) detailView.findViewById(R.id.date_detail);
            contDate.setText(contactAdapter.getString(5));
            TextView contNote = (TextView) detailView.findViewById(R.id.note_detail);
            contNote.setText(contactAdapter.getString(6));

        }

        Button deleteButton = (Button)detailView.findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ContactDetail.this.getActivity());
                builder.setTitle("Delete this contact?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        contactdbHelper.deleteContact(id);
                        contactCallback.onTapDeleteButton(id);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
            }
        });
        return detailView;
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        this.id = args.getLong("Contact ID");
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            contactCallback= (OnTapDeleteListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement listener");
        }
    }
}
