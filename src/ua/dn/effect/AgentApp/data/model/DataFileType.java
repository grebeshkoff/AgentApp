package ua.dn.effect.AgentApp.data.model;

/**
 * User: igrebeshkov
 * Date: 31.10.13
 * Time: 12:32
 */

public enum DataFileType{
    CLIENTS_FILE("AK"),
    PRICE_FILE("PR"),
    SALES_HISTORY("SH"),
    REPORT_NZ("NZ"),
    REPORT_MPD("MPD"),
    REPORT_PLAN("PLAN"),
    REPORT_PD("PD"),
    REPORT_PP("PP"),
    UNDEFINED("UNDEFINED");

    public final String abr;
    DataFileType(String s) {
        abr = s;
    }
}