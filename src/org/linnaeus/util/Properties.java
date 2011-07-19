package org.linnaeus.util;

import android.content.Context;
import android.content.res.Resources;
import org.linnaeus.R;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by IntelliJ IDEA.
 * User: Romchee
 * Date: 19/07/11
 * Time: 12:12
 * To change this template use File | Settings | File Templates.
 */
public class Properties {

    public static final String ANALYSER_SERVICE_URL_TRENDS = "analyser.server.url.trends";
    public static final String ANALYSER_SERVICE_URL_ADVICE = "analyser.server.url.advice";

    private static java.util.Properties properties;

    public static String getProperty(Context context, String property){
        try {
            if (properties == null) {
                Resources resources = context.getResources();
                InputStream rawResource = resources.openRawResource(R.raw.config);
                properties = new java.util.Properties();
                properties.load(rawResource);
            }
        } catch (Resources.NotFoundException e) {
            System.err.println("Did not find raw resource: "+e);
        } catch (IOException e) {
            System.err.println("Failed to open microlog property file");
        }
        return properties.getProperty(property);
    }
}
