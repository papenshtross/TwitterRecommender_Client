package org.linnaeus.util;

import android.app.Activity;
import android.content.Context;

/**
 * Created by IntelliJ IDEA.
 * User: Romchee
 * Date: 19/07/11
 * Time: 18:08
 * To change this template use File | Settings | File Templates.
 */
public class MainActivityContext {

    private static MainActivityContext instance = new MainActivityContext();
    private Context context;

    public void initContext(Context context){
        this.context = context;
    }

    public static MainActivityContext getInstance(){
        return instance;
    }

    public Context getContext(){
        return context;
    }
}
