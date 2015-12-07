package com.example.roddy.group7project;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Roderick on 2015-12-06.
 */
public class CarbCalcAboutFragment extends Fragment
{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View instructionsView = inflater.inflate(R.layout.carb_calc_instructions, container, false);
        return instructionsView;
    }
}
