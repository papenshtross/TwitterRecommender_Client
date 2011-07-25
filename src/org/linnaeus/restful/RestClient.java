package org.linnaeus.restful;

import java.io.*;
import java.util.ArrayList;

import android.os.Looper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import org.linnaeus.bean.Advice;
import org.linnaeus.bean.SearchCircle;
import org.linnaeus.bean.Trend;
import org.linnaeus.util.Properties;

public class RestClient {

	private static String convertStreamToString(InputStream is) {
		/*
		 * To convert the InputStream to String we use the BufferedReader.readLine()
		 * method. We iterate until the BufferedReader return null which means
		 * there's no more data to read. Each line will appended to a StringBuilder
		 * and returned as String.
		 */
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return sb.toString();
	}

    public static String sendCircleToService(SearchCircle searchCircle, String serviceUrl) throws Exception{

        Looper.prepare();
        HttpClient client = new DefaultHttpClient();
        JSONObject json = new JSONObject();
        HttpResponse response;
        String result = null;

        HttpPost post = new HttpPost(serviceUrl);
        post.setHeader("Content-type", "application/json");
        json.put("lat", searchCircle.getLat());
        json.put("lng", searchCircle.getLng());
        json.put("distance", searchCircle.getDistance());

        StringEntity se = new StringEntity(json.toString(), "UTF-8");

        post.setEntity(se);

        response = client.execute(post);

        // Examine the response status
        Log.i("Praeda",response.getStatusLine().toString());

        // Get hold of the response entity
        HttpEntity entity = response.getEntity();
        // If the response does not enclose an entity, there is no need
        // to worry about connection release

        if (entity != null) {

            // A Simple JSON Response Read
            InputStream instream = entity.getContent();
            result = convertStreamToString(instream);
            Log.i("Praeda",result);


            /*// A Simple JSONObject Creation
            json = new JSONObject(result);
            Log.i("Praeda","<jsonobject>\n"+json.toString()+"\n</jsonobject>");

            // A Simple JSONObject Parsing
            JSONArray nameArray=json.names();
            JSONArray valArray=json.toJSONArray(nameArray);
            for(int i=0;i<valArray.length();i++)
            {
                Log.i("Praeda","<jsonname"+i+">\n"+nameArray.getString(i)+"\n</jsonname"+i+">\n"
                        +"<jsonvalue"+i+">\n"+valArray.getString(i)+"\n</jsonvalue"+i+">");
            }

            // A Simple JSONObject Value Pushing
            json.put("sample key", "sample value");
            Log.i("Praeda","<jsonobject>\n"+json.toString()+"\n</jsonobject>");*/

            // Closing the input stream will trigger connection release
            instream.close();
        }
        return result;
    }
}
