package ua.dn.effect.AgentApp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import ua.dn.effect.AgentApp.data.model.*;
import ua.dn.effect.AgentApp.data.reader.EntitiesReader;
import ua.dn.effect.AgentApp.data.store.InternalStorage;
import ua.dn.effect.AgentApp.util.AgentAppLogger;
import ua.dn.effect.AgentApp.util.ApplicationState;
import ua.dn.effect.AgentApp.util.DateCoder;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * User: igrebeshkov
 * Date: 20.11.13
 * Time: 0:00
 */
public class OrdersListActivity extends Activity {

    Activity ctx;
    ListView lvOrders;
    SimpleAdapter adapter;

    private ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
    private ArrayList<HashMap<String, String>> savedOrders = new ArrayList<HashMap<String, String>>();
    private ArrayList<HashMap<String, String>> newOrders = new ArrayList<HashMap<String, String>>();

    private void copyItems(ArrayList<HashMap<String, String>> target){
        list.clear();
        for(HashMap<String, String> map : target){
            list.add(map);
        }
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.orders_list);
        ctx = this;
        //Init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.orders_list_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_new:
                AgentApplication.state = ApplicationState.ORDER_ADDING;
                AgentApplication.currentOrder = new Order();
                Intent intent = new Intent(this, OrderActivity.class);
                startActivity(intent);
                return true;
            case R.id.orders_list_processed:
                copyItems(savedOrders);
                adapter.notifyDataSetChanged();
                return true;
            case R.id.orders_list_not_processed:
                copyItems(newOrders);
                adapter.notifyDataSetChanged();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

//        if (item.getTitle().equals("Создать заказ")){
//            AgentApplication.Mode = "Add";
//            AgentApplication.currentOrder = new Order();
//            Intent intent = new Intent(this, OrderActivity.class);
//            startActivity(intent);
//        }
//        if (item.getTitle().equals("Отправленные")){
//
//        }
//
//        if (item.getTitle().equals("Не отправленные")){
//
//        }
//
//        return super.onOptionsItemSelected(item);
    }

    private void Init() {
        lvOrders = (ListView)findViewById(R.id.listViewOrders);

        String orderPath = "/data/data/ua.dn.effect.AgentApp/files/orders";
        File ordersDir = new File(orderPath);
        if(!ordersDir.exists()){
            ordersDir.mkdir();
        }
        File[] listOfFiles = ordersDir.listFiles();
        list = new ArrayList<HashMap<String, String>>();
        newOrders = new ArrayList<HashMap<String, String>>();
        savedOrders = new ArrayList<HashMap<String, String>>();
        AgentApplication.ordersFiles = new ArrayList<String>();
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {

                AgentApplication.ordersFiles.add(listOfFiles[i].getAbsolutePath());

                HashMap<String, String> map = new HashMap<String, String>();
                try {
                    Date date = new Date(listOfFiles[i].lastModified());
                    BufferedReader br = new BufferedReader(new InputStreamReader(
                            new FileInputStream(listOfFiles[i]), "cp1251"));
                    String line = br.readLine();

                    String nameLine = "";
                    String memo1Line = "";
                    String memo2Line = "";
                    while (line != null) {
                        if (line.contains("Агент="))
                        {
                            line = br.readLine();
                            nameLine = br.readLine();
                            line = br.readLine();
                            continue;
                        }
                        if (line.contains("Прим.1=")){
                            memo1Line = line;
                            memo2Line = br.readLine();
                        }
                        line = br.readLine();
                    }

                    br.close();

                    if(nameLine == null){
                        continue;
                    }

                    String realName = getRealNameString(memo1Line, memo2Line);

                    if (!realName.equals("")){
                        nameLine = nameLine + " (" + realName + ")";
                    }

                    map.put("Date", new SimpleDateFormat("dd.MM.yyyy  hh:mm").format(date));
                    map.put("Name", nameLine.trim());

                    AgentAppLogger.Text(nameLine);
                    //list.add(map);

                    if(listOfFiles[i].getName().endsWith("_")){
                        savedOrders.add(map);
                    }else{
                        newOrders.add(map);
                    }

                } catch (FileNotFoundException fnfe){
                    AgentAppLogger.Error(fnfe);
                } catch (IOException e) {
                    AgentAppLogger.Error(e);
                }
            }
        }

        copyItems(newOrders);
        adapter = new SimpleAdapter(this, list, R.layout.orders_list_item,
                new String[] {"Date", "Name"},
                new int[] {
                        R.id.textViewOrderDate,
                        R.id.textViewOrderClient });
        lvOrders.setAdapter(adapter);

        lvOrders.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                AgentApplication.state = ApplicationState.ORDER_EDITING;
                File order = new File(AgentApplication.ordersFiles.get(position));
                AgentApplication.currentOrder.orderFile = order;
                AgentApplication.currentOrder.positions.clear();

                BufferedReader br = null;
                try {
                    br = new BufferedReader(new InputStreamReader(
                            new FileInputStream(order), "cp1251"));
                    String line = br.readLine();

                    //ArrayList<Integer> positions = new ArrayList<Integer>();

                    while (line != null){


                        if (line.contains("[П")){
                            line = br.readLine();
                            Integer posId = Integer.parseInt(line.split("=")[1]);

                            Entity ent = AgentApplication.priceList.getEntityById(posId);
                            if (ent == null){
                                line = br.readLine();
                                continue;
                            }
                            Position pos = new Position();
                            pos.entity = ent;

                            line = br.readLine();

                            if(line.startsWith("S1=")){
                                pos.priceTypeId1 = pos.getPriceTypeId(line.split("=")[1]);
                                line = br.readLine();
                                pos.count1 = Integer.parseInt(line.split("=")[1]);
                                line = br.readLine();
                            }

                            if(line.startsWith("S2=")){
                                pos.priceTypeId2 = pos.getPriceTypeId(line.split("=")[1]);
                                line = br.readLine();
                                pos.count2 = Integer.parseInt(line.split("=")[1]);
                                line = br.readLine();
                            }


                            AgentApplication.currentOrder.positions.add(pos);
                            AgentAppLogger.Text(ent.name);
                        }

                        if(line.contains("Версия")){
                            AgentApplication.currentOrder.version = Integer.parseInt(line.split("=")[1]);
                            line = br.readLine();
                        }



                        if (line.contains("Агент=")){ // ! Next i9s Client !
                            br.readLine();
                            String name = br.readLine();
                            int id = Integer.parseInt(br.readLine().split("=")[1]);
                            AgentApplication.currentOrder.client = new Client(id, name);
                        }

                        if(line.contains("Прайс=")){
                            String acronim = line.split("=")[1].split("_")[1];

                            if (!AgentApplication.tradeAgent.getDefaultPriceAcronim().equals(acronim)){
                                try {
                                    InternalStorage storage = new InternalStorage(ctx);
                                    AgentApplication.tradeAgent.setDefaultPriceAcronim(acronim);
                                    EntitiesReader reader = null;
                                    reader = new EntitiesReader(storage);
                                    AgentApplication.priceList = reader.getList();
                                }catch (Exception e){
                                    AgentAppLogger.Error(e);
                                }
                            }
                            AgentAppLogger.Text(acronim);
                        }

                        if(line.startsWith("Время3="))
                        {
                              String val = line.split("=")[1];
                              if(!val.equals("00000000")){
                                  AgentApplication.currentOrder.deliverDate = new SimpleDateFormat("MMddHHmm").parse(val);
                                  AgentApplication.currentOrder.deliverDate.setYear(new Date().getYear());
                              }else{
                                  AgentApplication.currentOrder.deliverDate = null;
                              }
                            line = br.readLine();
                        }

                        if(line.contains("Опл.1=")){
                            String [] strings = line.split("=");
                            if(strings.length == 2){
                                AgentApplication.currentOrder.paymentType1 = PaymentType.getByAcronim(line.split("=")[1]);
                            }else {
                                AgentApplication.currentOrder.paymentType1 = PaymentType.NONE;
                            }
                            line = br.readLine();
                            strings = line.split("=");
                            if(strings.length == 2){
                                AgentApplication.currentOrder.paymentType2 = PaymentType.getByAcronim(line.split("=")[1]);
                            }else {
                                AgentApplication.currentOrder.paymentType2 = PaymentType.NONE;
                            }
                            line = br.readLine();
                        }

                        if(line.contains("Прим.1")){
                            if(line.split("=").length != 1){
                                AgentApplication.currentOrder.remark1 = getRemarkFromLine(line);
                            }
                            line = br.readLine();
                            if(line.split("=").length != 1){
                                AgentApplication.currentOrder.remark2 = getRemarkFromLine(line);;
                            }
                        }

                        line = br.readLine();
                    }
                    br.close();

                } catch (UnsupportedEncodingException e) {
                    AgentAppLogger.Error(e);
                } catch (FileNotFoundException e) {
                    AgentAppLogger.Error(e);
                } catch (IOException e) {
                    AgentAppLogger.Error(e);
                } catch (ParseException e) {
                    AgentAppLogger.Error(e);
                }

                Intent intent = new Intent(ctx, OrderActivity.class);
                startActivity(intent);
            }
        });

        lvOrders.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           final int pos, long id) {
                AgentAppLogger.Text("long clicked pos: " + pos);

                new AlertDialog.Builder(ctx)
                        .setTitle("Внимание")
                        .setMessage("Подтвердить удаление заказа?")
                        .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                File order = new File(AgentApplication.ordersFiles.get(pos));
                                order.setWritable(true);

                                order.delete();
                                AgentApplication.ordersFiles.remove(pos);
                                list.remove(pos);
                                adapter.notifyDataSetChanged();

                            }
                        })
                        .setNegativeButton("Отмена", null)
                        .show();
                return true;
            }
        });


    }

    private String getRemarkFromLine(String line) {
        String result = line.split("=", 2)[1];    // Прим.X=

        if (result.startsWith("Название:")){
            String [] parts = result.split(";", 2);
            AgentApplication.currentOrder.client.setRealName(parts[0].replace("Название:", "").trim());

            result = parts[1];
        }

        if(!AgentApplication.TEST_TEXT.equals(""))
            result = result.replace(AgentApplication.TEST_TEXT, "");
        result = result.trim();

        return result;
    }

    private String getRealNameString(String memo1, String memo2) {
        String result = "";
        if (memo1.contains("Прим.1=Название:")){
             return memo1.replace("Прим.1=Название:", "").split("; ")[0].trim();
        }
        if (memo2.contains("Прим.2=Название:")){
            return memo1.replace("Прим.1=Название:", "").split("; ")[0].trim();
        }
        return result;
    }


}