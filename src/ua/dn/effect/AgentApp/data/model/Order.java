package ua.dn.effect.AgentApp.data.model;

import android.app.Activity;
import android.content.Context;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import ua.dn.effect.AgentApp.AgentApplication;
import ua.dn.effect.AgentApp.data.config.AgentAppConfig;
import ua.dn.effect.AgentApp.data.store.InternalStorage;
import ua.dn.effect.AgentApp.util.AgentAppLogger;
import ua.dn.effect.AgentApp.util.DateCoder;
import ua.dn.effect.AgentApp.util.Transliterator;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

/**
 * User: igrebeshkov
 * Date: 25.10.13
 * Time: 15:50
 */
public class Order {
    public Client client = new Client();
    public Client tempClient  = new Client();

    public int version = 0;

    public Date date = new Date();

    public Date deliverDate;

    public File orderFile;

    public ArrayList<Position> positions = new ArrayList<Position>();

    public PaymentType paymentType1 = PaymentType.NONE;
    public PaymentType paymentType2 = PaymentType.NONE;

    public EntityGroup currentGroup;
    public Entity currentEntity;
    public Position currentPosition;

    public int currentPriceTypeId;
    public String remark1 = "";
    public String remark2 = "";

    public void CommitTempClient(){
        client = tempClient;
        tempClient = new Client();

    }

    public void Save(Activity ctx) {
        version = version + 1;

        String orderPath = "/data/data/ua.dn.effect.AgentApp/files/orders";
        File ordersDir = new File(orderPath);
        if(!ordersDir.exists()){
            ordersDir.mkdir();
        }

        Transliterator tr = new Transliterator();

        SimpleDateFormat outFormat = new SimpleDateFormat("MMddHHmm");

        String dateString = outFormat.format(date);

        String fileName = AgentApplication.tradeAgent.getTradeAgentId() + "_" + dateString + "_" + tr.transliterate(client.getName()) + ".txt";
        AgentAppLogger.Text(fileName);

        if(orderFile == null){
            orderFile = new File(orderPath + "/" + fileName);
        }
        try{
            orderFile.createNewFile();

            PrintWriter writer = new PrintWriter(orderFile, "cp1251");
            writer.println("[NEW_ZAKAZ]");
            writer.println("Версия=2");
            writer.println("Макс=" + positions.size());
            writer.println("Заказ=" + orderFile.getName());

            //TODO Fill file Size
            writer.println("Размер=");

            writer.println("Время=" + DateCoder.encryptDate(new SimpleDateFormat("MMddHHmm").format(date), true));
            writer.println("Время2=" + DateCoder.encryptDate(new SimpleDateFormat("MMddHHmm").format(AgentApplication.controlDate), true));

            if(deliverDate != null){
                writer.println("Время3=" + new SimpleDateFormat("MMddHHmm").format(deliverDate));
            }else{
                writer.println("Время3=00000000");
            }

            writer.println("Размещение=");
            writer.println("Стандарт=19.12.2012");

            UUID uuid = UUID.randomUUID();

            //String androidId = Settings.Secure.getString(ctx.getContentResolver(),Settings.Secure.ANDROID_ID);
            //String randomUUIDString = new UUID(Settings.Secure.ANDROID_ID);

            writer.println("GUID=" + deviceUDID(ctx));
            writer.println("ID=");
            writer.println();
            writer.println("Прайс=" + AgentApplication.priceList.FileName);
            writer.println("Дата=" + new SimpleDateFormat("dd.MM.yyyy").format(new Date()));
            writer.println();
            writer.println(AgentApplication.tradeAgent.getTradeAgentName());
            writer.println("Агент="  + AgentApplication.tradeAgent.getTradeAgentId());
            writer.println();
            writer.println(client.getName());
            writer.println("Клиент=" + client.getId());
            writer.println();
            writer.println("Опл.1=" + paymentType1.getAcronim());
            writer.println("Опл.2=" + paymentType2.getAcronim());
            writer.println();


            String ac1 = paymentType1.getAcronim();
            String ac2 = paymentType2.getAcronim();
            //TODO

            String realNameAppendix = "";
            if (!client.getRealName().equals("")) {
                realNameAppendix = "Название: " + client.getRealName() + "; ";
            }

            if(!ac1.equals("")){
                writer.println("Прим.1=" + realNameAppendix + remark1 + AgentApplication.TEST_TEXT);
            }else{
                writer.println("Прим.1=");
            }
            if(!ac2.equals("")){
                writer.println("Прим.2=" +  realNameAppendix + remark1 + AgentApplication.TEST_TEXT);
            }else{
                writer.println("Прим.2=");
            }

            //writer.println("FileUser=");
            //writer.println("Identity=2432156");
            writer.println();

            int counter = 1;
            for (Position p : positions){
                writer.println("[П" + counter + "]");
                writer.println("N=" + p.entity.id);
                if (p.count1 != 0){
                    writer.println("S1=" + p.getPriceTypeName(p.priceTypeId1));
                    writer.println("Q1=" + p.count1);
                }
                if (p.count2 != 0){
                    writer.println("S2=" + p.getPriceTypeName(p.priceTypeId2));
                    writer.println("Q2=" + p.count2);
                }
                writer.println();
                counter = counter + 1;
            }
            writer.println();
            writer.close();

            orderFile.setReadable(true, false);

            long size =  orderFile.length();

            Reader reader = new InputStreamReader(new FileInputStream(orderFile), "cp1251");
            BufferedReader file = new BufferedReader(reader);
            String line;String input = "";
            while ((line = file.readLine()) != null) input += line + '\n';
            input = input.replace("Размер=", "Размер=" + size);
            writer = new PrintWriter(orderFile, "cp1251");
            writer.write(input);
            writer.flush();
            writer.close();
        }catch (IOException e){
            AgentAppLogger.Error(e);
        }

    }

    public static String deviceUDID(Context ctx) {
        final TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);

        final String tmDevice, tmSerial, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" +android.provider.Settings.Secure.getString(ctx.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

        UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
        String deviceId = deviceUuid.toString();
        //Log.d("Device Id", deviceId);
        return deviceId;
    }

    public String getTotalSumm() {
        String result = "";
        Double summ = 0d;
        for (Position p : positions){

            summ =summ + p.count1 * p.entity.prices.get(p.priceTypeId1) + p.count2 * p.entity.prices.get(p.priceTypeId2);
        }
        return "Сумма заказа: " + String.format("%.2f", summ) + " " + AgentAppConfig.CURRENCY_ABR;
    }
}
