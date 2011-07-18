package org.linnaeus.manager;

import android.content.Context;
import org.linnaeus.bean.SearchCircle;

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

    public void requestTrends(SearchCircle searchCircle) {
        //Request trends
    }

    public void requestRecommendation(SearchCircle searchCircle) {

    }
}
