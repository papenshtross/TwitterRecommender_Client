package org.linnaeus.activity;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Created by IntelliJ IDEA.
 * User: Romchee
 * Date: 20/07/11
 * Time: 14:38
 * To change this template use File | Settings | File Templates.
 */
public class TrendsActivity extends ListActivity {

    static final String[] COUNTRIES = new String[] { // My data set
    "Afghanistan", "Albania", "Algeria", "American Samoa"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* Setup ListAdapter *//*
        int row_layout_ID = android.R.layout.simple_list_item_1; // Android's //int row_layout_ID = R.layout.list_item; // My item layout
        ListAdapter adapt = new ArrayAdapter<String>(this,row_layout_ID, COUNTRsetListAdapter(adapt);
        *//* Configure the ListView *//*
        ListView lv = getListView();
        lv.setTextFilterEnabled(true); // Make list searchable
        lv.setOnItemClickListener(new ItemClick()); // Attach item listener*/
    }
}