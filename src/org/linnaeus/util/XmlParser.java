package org.linnaeus.util;

import org.linnaeus.R;
import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Romchee
 * Date: 28/07/11
 * Time: 10:46
 * To change this template use File | Settings | File Templates.
 */
public class XmlParser {

    private static final String CATEGORY_TAG = "category";

    public static ArrayList<String> parseCategories(){
        ArrayList<String> categories = new ArrayList<String>();
        try {
			XmlPullParser xpp = MainActivityContext.getInstance()
                    .getContext().getResources().getXml(R.xml.categories);
			while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
				if (xpp.getEventType() == XmlPullParser.START_TAG) {
					if (xpp.getName().equals(CATEGORY_TAG)) {
                        xpp.next();
						categories.add(xpp.getText());
					}
				}
				xpp.next();
			}
		}
		catch (Exception e) {
		    e.printStackTrace();
            throw new RuntimeException(e);
		}
        return categories;
    }
}
