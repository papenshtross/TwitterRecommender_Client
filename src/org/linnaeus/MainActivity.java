package org.linnaeus;

import android.app.Activity;
import android.os.Bundle;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

public class MainActivity extends MapActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        MapView mapView = (MapView) findViewById(R.id.mapmain);
        mapView.setBuiltInZoomControls(true); // Attach zoom control
        /* Give start condition (location/zoom) */
        MapController mapController = mapView.getController();
        GeoPoint cityCenter = new GeoPoint(56876646,14809034); // Vaxjo
        mapController.setCenter(cityCenter);
        mapController.setZoom(8);
    }

    @Override // Required by MapActivity
    protected boolean isRouteDisplayed() { return false; }
    }
