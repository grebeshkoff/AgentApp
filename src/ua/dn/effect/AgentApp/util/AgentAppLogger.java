package ua.dn.effect.AgentApp.util;

import android.util.Log;

/**
 * User: igrebeshkov
 * Date: 11.09.13
 * Time: 0:08
 */
public class AgentAppLogger {
    public  static  final String TAG = "AGNT" ;

    public static void Error(Exception e){
        Log.e(TAG, e.getMessage());
    }
    public static void Text(String s){
        Log.d(TAG, s);
    }

}
