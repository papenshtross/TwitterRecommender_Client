package org.linnaeus.bean;

/**
 * Created by IntelliJ IDEA.
 * User: Romchee
 * Date: 18/07/11
 * Time: 10:47
 * To change this template use File | Settings | File Templates.
 */
public class SearchCircle {

    private int lat;
    private int lng;
    private int distance;

    public float getLat() {
        return lat;
    }

    public void setLat(int lat) {
        this.lat = lat;
    }

    public float getLng() {
        return lng;
    }

    public void setLng(int lng) {
        this.lng = lng;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }
}
