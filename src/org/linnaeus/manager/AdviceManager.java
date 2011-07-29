package org.linnaeus.manager;

import com.facebook.android.Facebook;
import com.facebook.android.Util;
import org.json.JSONObject;
import org.linnaeus.facebook.FacebookConstants;
import org.linnaeus.facebook.FacebookFactory;
import org.linnaeus.util.JsonParser;
import org.linnaeus.util.XmlParser;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Romchee
 * Date: 28/07/11
 * Time: 10:32
 * To change this template use File | Settings | File Templates.
 */
public class AdviceManager {

    private static AdviceManager instance = new AdviceManager();

    private static String[] categoriesToImport = {"Interest"};

    private AdviceManager(){

    }

    public static AdviceManager getInstance(){
        return instance;
    }

    public ArrayList<String> getCategories(boolean isImportFromFB){
        ArrayList<String> categories = new ArrayList<String>();
        if (isImportFromFB){
            Facebook facebook = FacebookFactory.getInstance().getFacebook();
            try {
                String response = facebook.request(FacebookConstants.FB_ME_LIKES);
                JSONObject jsonObject = Util.parseJson(response);
                ArrayList<String> interests = JsonParser.parseFBInterestsFromJson(jsonObject, categoriesToImport);
                if (interests.size() > 0){
                    categories.addAll(interests);
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
        categories.addAll(XmlParser.parseCategories());
        return categories;
    }
}
