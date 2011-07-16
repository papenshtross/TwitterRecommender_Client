package org.linnaeus.activity;

import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import com.google.android.maps.*;
import org.linnaeus.R;
import org.linnaeus.overlay.CurrentLocationOverlay;
import org.linnaeus.util.MyLocation;

import java.util.List;

public class MainActivity extends MapActivity {

    private MapController mapController;
    private MyLocation myLocation = new MyLocation();
    private MyLocation.LocationResult locationResult;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        MapView mapView = (MapView) findViewById(R.id.mapmain);
        mapView.setBuiltInZoomControls(true); // Attach zoom control
        /* Give start condition (location/zoom) */
        mapController = mapView.getController();

        locationResult = new MyLocation.LocationResult(){
        public void gotLocation(final Location location){
                if (location != null){
                    double lat = location.getLatitude() * 1000000;
                    double lng = location.getLongitude() * 1000000;
                    GeoPoint geoPoint = new GeoPoint((int) lat, (int) lng);
                    MyLocation.setLastLocation(geoPoint);
                    mapController.animateTo(geoPoint);
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
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    private void moveToMyLocation() {
        try{
            if(!myLocation.getLocation(this, locationResult)){
                showErrorLocationToast(getString(R.string.main_error_services));
            }
        } catch (Exception e){
            showErrorLocationToast(getString(R.string.main_error_mylocation));
        }
    }

    private void showErrorLocationToast(String text){
        Toast.makeText(getApplicationContext(),
                text, Toast.LENGTH_SHORT).show();
    }

    private void showHelp() {
        //To change body of created methods use File | Settings | File Templates.
    }

    @Override // Required by MapActivity
    protected boolean isRouteDisplayed() { return false; }
    }
