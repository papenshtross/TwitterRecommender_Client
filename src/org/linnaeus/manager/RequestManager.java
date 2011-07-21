package org.linnaeus.manager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import org.linnaeus.R;
import org.linnaeus.activity.TrendsActivity;
import org.linnaeus.bean.SearchCircle;
import org.linnaeus.bean.Trend;
import org.linnaeus.restful.RestClient;
import org.linnaeus.util.JsonParser;
import org.linnaeus.util.MainActivityContext;
import org.linnaeus.util.Properties;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Romchee
 * Date: 18/07/11
 * Time: 14:18
 * To change this template use File | Settings | File Templates.
 */
public class RequestManager {

    private static RequestManager instance;
    private Context context;
    public static String TRENDS_REQUEST_PARCELABLE_NAME = "trends";

    public static RequestManager getInstance() {
        if (instance == null) {
            instance = new RequestManager();
        }
        return instance;
    }

    public RequestManager() {
        this.context = MainActivityContext.getInstance().getContext();
    }

    public void requestTrends(SearchCircle searchCircle) {
        ArrayList<Trend> trends = null;
        try {
            String jsonTrends = RestClient.sendCircleToService(searchCircle, Properties.getProperty(context,
                    Properties.ANALYSER_SERVICE_URL_TRENDS));
            if (jsonTrends != null){
                trends = JsonParser.parseTrendsFromJson(jsonTrends);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, context.getString(R.string.request_trends_error)
                    , Toast.LENGTH_SHORT).show();
        }
        if (trends != null){
            Intent intent = new Intent(context, TrendsActivity.class);
            intent.putParcelableArrayListExtra(TRENDS_REQUEST_PARCELABLE_NAME, trends);
            intent.putExtra(SearchCircle.SERIALIZABLE_NAME, searchCircle);
            context.startActivity(intent);
        }
    }

    public void requestRecommendation(SearchCircle searchCircle) {

    }
}
