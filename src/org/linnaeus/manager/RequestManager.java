package org.linnaeus.manager;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;
import org.linnaeus.R;
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

    public static RequestManager getInstance(Context context) {
        if (instance == null) {
            instance = new RequestManager(context);
        }
        return instance;
    }

    public RequestManager(Context context) {
        this.context = context;
    }

    public void requestTrends(SearchCircle searchCircle, ProgressDialog progressDialog) {
        ArrayList<Trend> trends;
        try {
            String jsonTrends = RestClient.sendCircleToService(searchCircle, Properties.getProperty(MainActivityContext
                    .getInstance().getContext(), Properties.ANALYSER_SERVICE_URL_TRENDS));
            if (jsonTrends != null){
                trends = JsonParser.parseTrendsFromJson(jsonTrends);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(MainActivityContext.getInstance().getContext(),
                    MainActivityContext.getInstance().getContext().getString(R.string.request_trends_error)
                    , Toast.LENGTH_SHORT).show();
        }
        progressDialog.dismiss();
    }

    public void requestRecommendation(SearchCircle searchCircle, ProgressDialog progressDialog) {

    }
}
