package com.hotelservice.hotelservice;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

/**
 * Created by DarkCraid on 18/04/2018.
 */

public class WebAppInterface {
    Context mContext;
    String h;

    /** Instantiate the interface and set the context */
    WebAppInterface(Context c,String ha) {
        mContext = c;
        h = ha;
    }

    /** Show a toast from the web page */
    @JavascriptInterface
    public void showToast(String toast) {
        Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
    }

    @JavascriptInterface
    public String getHabitacion(){
        return h;
    }
}
