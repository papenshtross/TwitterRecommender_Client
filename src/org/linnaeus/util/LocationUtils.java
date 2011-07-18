package org.linnaeus.util;

import android.location.Location;
import com.google.android.maps.GeoPoint;

/**
 * Created by IntelliJ IDEA.
 * User: Romchee
 * Date: 18/07/11
 * Time: 13:57
 * To change this template use File | Settings | File Templates.
 */
public class LocationUtils {

    public static int distanceBetweenGeoPoints(GeoPoint pointA, GeoPoint pointB) {
        Location locationA = new Location("point A");

        locationA.setLatitude(pointA.getLatitudeE6() / 1E6);
        locationA.setLongitude(pointA.getLongitudeE6() / 1E6);

        Location locationB = new Location("point B");

        locationB.setLatitude(pointB.getLatitudeE6() / 1E6);
        locationB.setLongitude(pointB.getLongitudeE6() / 1E6);

        return (int)locationA.distanceTo(locationB);
    }
}
