package com.example.roddy.group7project;

/**
 * Created by Jiebin Gao on 2015-11-30.
 */
import android.os.Bundle;

/**
 * This class define a interface to support the fragment communication via activity.
 */
public interface TipFragmentCallBack {
    public void addExpenseItem();
    public void showDetailedInfo(Bundle arg);
    public void tipListView();
}
