package org.linnaeus.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import org.linnaeus.R;
import org.linnaeus.bean.SearchCircle;
import org.linnaeus.bean.Trend;
import org.linnaeus.manager.RequestManager;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Romchee
 * Date: 20/07/11
 * Time: 14:38
 * To change this template use File | Settings | File Templates.
 */
public class TrendsActivity extends ListActivity {

    public static final int APPROVE_DIALOG = 1;
    private String textToGoogle;

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

    private String buildTrendsTitle(SearchCircle searchCircle) {
        StringBuilder title = new StringBuilder();
        title.append(getString(R.string.trends_lat)).append(": ").append(searchCircle.getLat()).append(" ");
        title.append(getString(R.string.trends_lng)).append(": ").append(searchCircle.getLng()).append(" ");
        title.append(getString(R.string.trends_radius)).append(": ").append(searchCircle.getDistance());
        title.append(getString(R.string.trends_units));
        return title.toString();
    }

    private class ItemClick implements AdapterView.OnItemClickListener {

        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            textToGoogle = String.valueOf(((TextView) view).getText());
            showDialog(APPROVE_DIALOG);
        }
    }

    @Override
    public Dialog onCreateDialog(int id) {
        switch (id) {
            case (APPROVE_DIALOG):
                AlertDialog.Builder ad = new AlertDialog.Builder(this);
                ad.setTitle(getString(R.string.trends_google_it));
                ad.setMessage(getString(R.string.trends_google_it));
                ad.setPositiveButton("Google",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int arg1) {
                                Intent search = new Intent(Intent.ACTION_WEB_SEARCH);
                                search.putExtra(SearchManager.QUERY, textToGoogle);
                                startActivity(search);
                            }
                        });
                ad.setNegativeButton(getString(R.string.search_circle_dialog_button_cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int arg1) {
                                dismissDialog(APPROVE_DIALOG);
                            }
                        });
                return ad.create();
        }
        return null;
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