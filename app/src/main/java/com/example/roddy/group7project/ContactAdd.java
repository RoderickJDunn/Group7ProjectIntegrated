package com.example.roddy.group7project;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Senedu on 2015-12-02.
 */
public class ContactAdd extends Fragment {
    OnTapDoneListener contactCallback;
    ContactDbAdapter contactdbHelper;
    SimpleCursorAdapter contactAdapter;


    public interface OnTapDoneListener {
        /** Called by HeadlinesFragment when a list item is selected */
        public void onTapDone();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        contactdbHelper = new ContactDbAdapter(getActivity());
        contactdbHelper.open();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inputContactInfoView = inflater.inflate(R.layout.contact_add_activity, container, false);

        registerListeners(inputContactInfoView);
        return inputContactInfoView;
    }

    private void registerListeners(final View inputContactInfoView) {

        final Button doneContactButton = (Button) inputContactInfoView.findViewById(R.id.done_button_contact);
        doneContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storeInDatabase();
            }
        });
    }



    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception.
        try {
            contactCallback= (OnTapDoneListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnTapDoneListener");
        }
    }

    public void storeInDatabase() {
        // get text from each of the input fields in the AddContact fragment view

        String firstName = ((EditText)getActivity().findViewById(R.id.first_name_input)).getText().toString();
        String lastName= ((EditText)getActivity().findViewById(R.id.last_name_input)).getText().toString();
        String phoneNumberS = ((EditText)getActivity().findViewById(R.id.phone_number_input)).getText().toString();
        String email= ((EditText)getActivity().findViewById(R.id.email_input)).getText().toString();
        Integer phoneNumber=0;
        if (phoneNumberS  != null && !phoneNumberS .trim().equals("")) {
            phoneNumber = Integer.parseInt(phoneNumberS);
        }

        String cDate = ((EditText)getActivity().findViewById(R.id.date_input)).getText().toString();
        String cNote = ((EditText)getActivity().findViewById(R.id.note_input)).getText().toString();

        contactdbHelper.insertContact(firstName, lastName, phoneNumber , email, cDate, cNote, "");

        contactCallback.onTapDone();


    }
}
