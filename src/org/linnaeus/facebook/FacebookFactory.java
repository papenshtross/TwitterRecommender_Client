package org.linnaeus.facebook;

import com.facebook.android.Facebook;

/**
 * Created by IntelliJ IDEA.
 * User: Romchee
 * Date: 29/07/11
 * Time: 13:56
 * To change this template use File | Settings | File Templates.
 */
public class FacebookFactory {

    private static FacebookFactory instance = new FacebookFactory();

    private Facebook facebook;

    public static FacebookFactory getInstance(){
        return instance;
    }

    public void initFacebook(Facebook facebookInstance){
        facebook = facebookInstance;
    }

    public Facebook getFacebook(){
        return facebook;
    }
}
