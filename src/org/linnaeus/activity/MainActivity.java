package org.linnaeus.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import com.google.android.maps.*;
import org.linnaeus.R;
import org.linnaeus.bean.SearchCircle;
import org.linnaeus.manager.RequestManager;
import org.linnaeus.overlay.CurrentLocationOverlay;
import org.linnaeus.overlay.SearchCircleOverlay;
import org.linnaeus.restful.RestClient;
import org.linnaeus.util.LocationUtils;
import org.linnaeus.util.MainActivityContext;
import org.linnaeus.util.MyLocation;
import org.linnaeus.util.Properties;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

public class MainActivity extends MapActivity {

    private static final int CURRENT_LOCATION_ZOOM_LEVEL = 10;

    public static final int SEARCH_DIALOG = 1;
    public static final int PROGRESS_DIALOG = 2;

    public final int REQUEST_TRENDS = 1;
    public final int REQUEST_ADVICE = 2;

    private MapController mapController;
    private MyLocation myLocation = new MyLocation();
    private MyLocation.LocationResult locationResult;
    private MapView mapView;
    private SearchCircleOverlay searchCircleOverlay;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        //Init context to allow static access throughout the application
        MainActivityContext.getInstance().initContext(this);

        mapView = (MapView) findViewById(R.id.mapmain);
        mapView.setBuiltInZoomControls(true); // Attach zoom control
        /* Give start condition (location/zoom) */
        mapController = mapView.getController();

        locationResult = new MyLocation.LocationResult() {
            public void gotLocation(final Location location) {
                if (location != null) {
                    double lat = location.getLatitude() * 1000000;
                    double lng = location.getLongitude() * 1000000;
                    GeoPoint geoPoint = new GeoPoint((int) lat, (int) lng);
                    MyLocation.setLastLocation(geoPoint);
                    mapController.animateTo(geoPoint);
                    mapController.setZoom(CURRENT_LOCATION_ZOOM_LEVEL);
                }
            };
        };

        CurrentLocationOverlay myLocationOverlay = new CurrentLocationOverlay(getApplicationContext());
        List<Overlay> list = mapView.getOverlays();
        list.add(myLocationOverlay);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.my_location:
                moveToMyLocation();
                return true;
            case R.id.help:
                showHelp();
                return true;
            case R.id.circle:
                drawCircle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void drawCircle() {
        mapView.getOverlays().remove(searchCircleOverlay);
        searchCircleOverlay = new SearchCircleOverlay(this);
        mapView.getOverlays().add(searchCircleOverlay);
    }

    private void moveToMyLocation() {
        try {
            if (!myLocation.getLocation(this, locationResult)) {
                showErrorLocationToast(getString(R.string.main_error_services));
            }
        } catch (Exception e) {
            showErrorLocationToast(getString(R.string.main_error_mylocation));
        }
    }

    private void showErrorLocationToast(String text) {
        Toast.makeText(getApplicationContext(),
                text, Toast.LENGTH_SHORT).show();
    }

    private void showHelp() {
        //To change body of created methods use File | Settings | File Templates.
    }

    @Override // Required by MapActivity
    protected boolean isRouteDisplayed() {
        return false;
    }

    public MapView getMapView() {
        return mapView;
    }

    @Override
    public Dialog onCreateDialog(int id) {
        switch(id) {
            case (SEARCH_DIALOG) :
                AlertDialog.Builder ad = new AlertDialog.Builder(this);
                ad.setTitle(getString(R.string.search_circle_dialog_title));
                ad.setMessage(getString(R.string.search_circle_dialog_message));
                ad.setPositiveButton(getString(R.string.search_circle_dialog_button_trends),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int arg1) {
                                requestTrends();
                            }
                        });
                ad.setNegativeButton(getString(R.string.search_circle_dialog_button_cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int arg1) {
                                cancelSearch();
                            }
                        });
                ad.setNeutralButton(getString(R.string.search_circle_dialog_button_recommend),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int arg1) {
                                requestRecommendation();
                            }
                        });
                return ad.create();
            case (PROGRESS_DIALOG) :
                ProgressDialog dialog = new ProgressDialog(this);
                dialog.setTitle(getString(R.string.main_progress_dialog_title));
                dialog.setMessage(getString(R.string.main_progress_dialog_text));
                dialog.show();
                return dialog;
        }
        return null;
    }

    private void cancelSearch() {
        removeSearchOverlay();
    }

    private void requestRecommendation() {
        new SearchRequest().execute(REQUEST_ADVICE);
        removeSearchOverlay();
    }

    private void requestTrends() {
        new SearchRequest().execute(REQUEST_TRENDS);
        removeSearchOverlay();
    }

    private void removeSearchOverlay() {
        dismissDialog(SEARCH_DIALOG);
        getMapView().getOverlays().remove(searchCircleOverlay);
    }

    class SearchRequest extends AsyncTask<Integer, String, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showDialog(PROGRESS_DIALOG);
		}

		@Override
		protected Void doInBackground(Integer... request) {
            SearchCircle searchCircle = searchCircleOverlay.getSearchCircle(getMapView());
            switch (request[0]){
                case REQUEST_TRENDS:
                    RequestManager.getInstance().requestTrends(searchCircle);
                    break;
                case REQUEST_ADVICE:
                    RequestManager.getInstance().requestRecommendation(searchCircle);
                    break;
            }
			return null;
		}

		@Override
		protected void onPostExecute(Void unused) {
			dismissDialog(PROGRESS_DIALOG);
		}
	}
}
