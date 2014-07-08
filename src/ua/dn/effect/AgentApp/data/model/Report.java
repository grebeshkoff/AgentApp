package ua.dn.effect.AgentApp.data.model;

import java.io.File;
import java.util.Date;

/**
 * Created by igrebeshkov on 16.04.14.
 */
public class Report {
    public static String FAILED_ORDERS = "Непрошедшие заказы";
    public static String MPD_ORDERS = "Менеджер Покупатель Документ";
    public static String PLAN = "План-факт за период";
    public static String PD = "Дебиторская задолженность";
    public static String PP = "План платежей";



    public String name;
    public Date date;
    public File file;
}
