package org.linnaeus.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.linnaeus.bean.Trend;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Romchee
 * Date: 20/07/11
 * Time: 13:45
 * To change this template use File | Settings | File Templates.
 */
public class JsonParser {

    private static final String MENTIONS = "mentions";
    private static final String VALUE = "value";

    /*
    ***Json response format***
    *
    {"trend":
        [
        {"mentions":"124","value":"Test Trend"},
        {"mentions":"124","value":"Test Trend"},
        {"mentions":"124","value":"Test Trend"},
        {"mentions":"124","value":"Test Trend"},
        {"mentions":"124","value":"Test Trend"}
        ]
    }*/
    public static ArrayList<Trend> parseTrendsFromJson(String jsonString) throws JSONException {
        ArrayList<Trend> trends = new ArrayList<Trend>();
        JSONObject json = new JSONObject(jsonString);
        JSONArray values = json.getJSONArray("trend");
        for (int i = 0; i < values.length(); i++){
            Trend trend = new Trend();
            JSONObject jsonTrend = values.getJSONObject(i);
            trend.setMentions(jsonTrend.getInt(MENTIONS));
            trend.setTrend(jsonTrend.getString(VALUE));
            trends.add(trend);
        }
        return trends;
    }
}
