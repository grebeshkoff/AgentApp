package ua.dn.effect.AgentApp;

import android.app.Application;
import android.content.Context;
import android.telephony.TelephonyManager;
import ua.dn.effect.AgentApp.data.config.AgentAppConfig;
import ua.dn.effect.AgentApp.data.exchenge.*;
import ua.dn.effect.AgentApp.data.model.*;
import ua.dn.effect.AgentApp.data.store.InternalStorage;
import ua.dn.effect.AgentApp.data.store.Storage;
import ua.dn.effect.AgentApp.util.ApplicationState;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * User: igrebeshkov
 * Date: 25.10.13
 * Time: 11:26
 */
public class AgentApplication extends Application {

    public static FtpConnection ftpConnection = new FtpConnection();

    public static String TEST_TEXT = "";

    public static boolean isDictionariesPresent = false;

    public static Order currentOrder = new Order();

    public static Report currentReport;

    public static List<Client> ClientsList;

    public static SalesHistory salesHistory;

    public static PriceList priceList;

    public static Date controlDate = new Date();

    public static TradeAgent tradeAgent;

    public static ArrayList<String> ordersFiles;

    public static AgentAppConfig config;

    public static List<Report> reportsList;

    public static List<Check> checkList;

    public static Check currentCheck;

    public static ApplicationState state = ApplicationState.NAN;
}
