package ua.dn.effect.AgentApp.data.model;

/**
 * User: igrebeshkov
 * Date: 14.11.13
 * Time: 11:03
 */
public enum PaymentType {
    CASH("Наличные", "Н"),
    TRANSFER("Безналичные", "БН"),
    DEBT_ORDER("Ордер", "ПО"),
    NONE("Не задан", "");

    public final String value;
    public final String acronim;

    PaymentType (String val, String ac){
        value = val;
        acronim = ac;
    }

    @Override
    public String toString() {
        return value;
    }
    public static PaymentType getByAcronim(String acr){
        if(acr.equals("Н"))
            return CASH;
        if(acr.equals("БН"))
            return TRANSFER;
        if(acr.equals("ПО"))
            return DEBT_ORDER;
        return null;
    }

    public static int getPosition(PaymentType pt){
        if (pt == CASH) return 0;
        if (pt == TRANSFER) return 1;
        if (pt == DEBT_ORDER) return 2;
        else return 3;
    }


    public String getAcronim() {
        return acronim;
    }
}
