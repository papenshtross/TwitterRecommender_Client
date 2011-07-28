package org.linnaeus.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.linnaeus.bean.Advice;
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
    private static final String ADVICE_NAME = "name";
    private static final String ADVICE_DESCRIPTION = "description";
    private static final String ADVICE_RATING = "rating";
    private static final String TREND_OBJECT = "trend";
    private static final String ADVICE_OBJECT = "advice";

    /*
    ***Json response format***
    *
    {"advice":
        [
        {"description":"test","name":"Shops","rating":"4.5"},
        {"description":"test2","name":"Shops","rating":"3.5"}
        ]
    }
    */

    public static ArrayList<Advice> parseAdvicesFromJson(String jsonString) throws JSONException {
        ArrayList<Advice> advices = new ArrayList<Advice>();
        JSONObject json = new JSONObject(jsonString);
        try {
            JSONArray values = json.getJSONArray(ADVICE_OBJECT);
            for (int i = 0; i < values.length(); i++){
                JSONObject jsonAdvice = values.getJSONObject(i);
                advices.add(newAdviceFromJson(jsonAdvice));
            }
        } catch (JSONException e){
            JSONObject value = json.getJSONObject(ADVICE_OBJECT);
            advices.add(newAdviceFromJson(value));
        }
        return advices;
    }

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
        try {
            JSONArray values = json.getJSONArray(TREND_OBJECT);
            for (int i = 0; i < values.length(); i++){
                JSONObject jsonTrend = values.getJSONObject(i);
                trends.add(newTrendFromJson(jsonTrend));
            }
        } catch (JSONException e){
            JSONObject value = json.getJSONObject(TREND_OBJECT);
            trends.add(newTrendFromJson(value));
        }
        return trends;
    }

    private static Trend newTrendFromJson(JSONObject jsonTrend) throws JSONException{
        Trend trend = new Trend();
        trend.setMentions(jsonTrend.getInt(MENTIONS));
        trend.setTrend(jsonTrend.getString(VALUE));
        return trend;
    }

     private static Advice newAdviceFromJson(JSONObject jsonAdvice) throws JSONException{
        Advice advice = new Advice();
        advice.setName(jsonAdvice.getString(ADVICE_NAME));
        advice.setDescription(jsonAdvice.getString(ADVICE_DESCRIPTION));
        advice.setRating(jsonAdvice.getDouble(ADVICE_RATING));
        return advice;
    }
}
