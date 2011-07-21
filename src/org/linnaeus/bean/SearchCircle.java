package org.linnaeus.bean;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: Romchee
 * Date: 18/07/11
 * Time: 10:47
 * To change this template use File | Settings | File Templates.
 */
public class SearchCircle implements Serializable{

    public static String SERIALIZABLE_NAME = "search_circle";

    private int lat;
    private int lng;
    private int distance;

    public int getLat() {
        return lat;
    }

    public void setLat(int lat) {
        this.lat = lat;
    }

    public int getLng() {
        return lng;
    }

    public void setLng(int lng) {
        this.lng = lng;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }
}
