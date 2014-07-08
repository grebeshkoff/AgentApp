package ua.dn.effect.AgentApp.data.exchenge;

import android.content.Context;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.FTPClient;
import java.io.IOException;
import java.io.PrintWriter;

import ua.dn.effect.AgentApp.data.config.AgentAppConfig;
import ua.dn.effect.AgentApp.util.*;

/**
 * User: igrebeshkov
 * Date: 28.08.13
 * Time: 10:25
 */
public class FtpConnection  {
    public String Host = "195.225.158.2";
    public int Port = 21;
    public String UserName = "agentpda040";
    public String UserPassword = "Za040aGt";
    public String RemoteFolder = "/" + UserName + "/";

    private FTPClient client = new FTPClient();

    public FtpConnection(){
        client.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
    }

    public void Init(Context ctx){
        AgentAppConfig cfg = new AgentAppConfig(ctx);
        UserName = cfg.getLogin();
        Host = cfg.getServer();
        UserPassword = cfg.getPassword();
        RemoteFolder = "/" + UserName + "/";
    }

    public boolean isReady(){
        int reply = client.getReplyCode();
        if (FTPReply.isPositiveCompletion(reply)) {
            AgentAppLogger.Text("Can't connect in isReady.FtpConnection");
            return false;
        }
        return true;
    }

    public FTPClient getClient(){
        return client;
    }
}
