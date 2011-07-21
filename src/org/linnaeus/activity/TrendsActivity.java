package org.linnaeus.activity;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import org.linnaeus.R;
import org.linnaeus.bean.SearchCircle;
import org.linnaeus.bean.Trend;
import org.linnaeus.manager.RequestManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Romchee
 * Date: 20/07/11
 * Time: 14:38
 * To change this template use File | Settings | File Templates.
 */
public class TrendsActivity extends ListActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* Setup ListAdapter */
        Bundle extras = getIntent().getExtras();
        ArrayList<Trend> trends = extras.getParcelableArrayList(RequestManager.TRENDS_REQUEST_PARCELABLE_NAME);
        ListAdapter adapter = new ArrayAdapter<Trend>(this, android.R.layout.simple_list_item_1, trends);
        this.setListAdapter(adapter);
        /* Configure the ListView */
        ListView lv = getListView();
        lv.setTextFilterEnabled(true); // Make list searchable
        lv.setOnItemClickListener(new ItemClick()); // Attach item listener
        SearchCircle searchCircle = (SearchCircle) extras.getSerializable(SearchCircle.SERIALIZABLE_NAME);
        setTitle(buildTrendsTitle(searchCircle));
    }

    private String buildTrendsTitle(SearchCircle searchCircle){
        StringBuilder title = new StringBuilder();
        title.append(getString(R.string.trends_lat)).append(": ").append(searchCircle.getLat()).append(" ");
        title.append(getString(R.string.trends_lng)).append(": ").append(searchCircle.getLng()).append(" ");
        title.append(getString(R.string.trends_radius)).append(": ").append(searchCircle.getDistance());
        title.append(getString(R.string.trends_units));
        return title.toString();
    }

    private class ItemClick implements AdapterView.OnItemClickListener {

        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            //To change body of implemented methods use File | Settings | File Templates.
        }
    }

    /*private class TrendAdapter extends ArrayAdapter<String> {

         public TrendAdapter(Context context, int textViewResourceId, List<String> objects) {
            super(context, textViewResourceId, objects);
        }

        @Override // Called when updating the ListView
        public View getView(int position, View convertView, ViewGroup parent) {
            View row;
            if (convertView == null) { // Create new row view
                LayoutInflater inflater = getLayoutInflater();
                row = inflater.inflate(R.layout.trend_list_item, parent, false);
            }
            else // reuse old row view to save time/battery
            row = convertView;
            *//**//* Add new data to row object *//**//*
            TextView country = (TextView)row.findViewById(R.id.country);
            country.setText(COUNTRIES[position]);
            TextView pos = ... retrieve and update position field
            TextView pop = ... retrieve and generate random population
        }
    }*/
}