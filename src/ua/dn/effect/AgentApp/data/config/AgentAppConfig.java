package ua.dn.effect.AgentApp.data.config;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import ua.dn.effect.AgentApp.util.AgentAppLogger;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

/**
 * Created by igrebeshkov on 26.02.14.
 */
public class AgentAppConfig {

    //public String ftpLogin = "agentpda040";
    //public String ftpPassword = "Za040aGt";
    //public String ftpServer = "";
    //public int ftpPort = 21;
    public static final String CURRENCY_ABR = "грн.";

    Context context;
    SharedPreferences preferences;



    public AgentAppConfig(Context ctx){
        context = ctx;
        preferences = PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public String getServer() {
        return preferences.getString("Server","81.21.3.12");
    }
    public void setServer(String server) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Server",server);
        editor.commit();
    }

    public int getPort() {
        return preferences.getInt("Port", 21);
    }
    public void setPort(int port) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("Port", port);
        editor.commit();
    }

    public String getLogin() {
        return preferences.getString("Login","agentpda040");
    }
    public void setLogin(String login) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Login",login);
        editor.commit();
    }

    public String getPassword() {
        return preferences.getString("Password","Za040aGt");
    }
    public void setPassword(String pass) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Password",pass);
        editor.commit();
    }

}
