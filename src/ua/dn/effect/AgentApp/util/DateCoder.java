package ua.dn.effect.AgentApp.util;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by igrebeshkov on 11.04.14.
 */
public class DateCoder {
    public static String encryptDate(String date, boolean isEncrypt){
        String result = "";
        //try {
        //Date d = new SimpleDateFormat("MMddHHmm").parse(date);
        String strL, strR = "";
        if(isEncrypt){
            if(date.length() != 8)
                return "00000000";

            int D = Integer.parseInt(date.substring(1, 4));
            int T = Integer.parseInt(date.substring(5, 8));
            int plus = D + T;
            int minus = D - T;
            int summ = plus + minus;
            strL = Integer.toString(plus);
            strR = Integer.toString(summ);
        }else{
            if(date.length() != 8)
                return "01010101";

            int plus = Integer.parseInt(date.substring(0, 4));
            int summ = Integer.parseInt(date.substring(5, 8));
            int minus = summ - plus;

            int T = (plus - minus) / 2;
            int D = plus - T;
            strL = Integer.toString(D);
            strR = Integer.toString(T);
        }

        strL = ("0000" + strL);
        strL = strL.substring(strL.length() - 4, strL.length());
        strR = ("0000" + strR);
        strR = strR.substring(strR.length() - 4, strR.length());


        result = strL + strR;
        return result;
//        } catch (ParseException e) {
//            return result;
//        }


    }

    public static Date getDateFromFileName(File file, String extension){
        Date result;
        try {
            result =  new SimpleDateFormat("MMddHHmm").parse(file.getName().split("_", 3)[2].replace("." + extension, ""));

        } catch (ParseException e) {
            AgentAppLogger.Error(e);
            return null;
        }
        Date lm = new Date(file.lastModified());
        //int year = lm.getYear();
        result.setYear(lm.getYear());
        return result;
    }
}

