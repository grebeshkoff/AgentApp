package ua.dn.effect.AgentApp;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.*;
import ua.dn.effect.AgentApp.data.exchenge.FtpUploader;
import ua.dn.effect.AgentApp.data.reader.EntitiesReader;
import ua.dn.effect.AgentApp.data.store.InternalStorage;
import ua.dn.effect.AgentApp.data.store.Storage;
import ua.dn.effect.AgentApp.util.AgentAppLogger;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends Activity {
    //private static final String TAG = "AGNT"; test
    Activity self;
    TextView tvTradeAgentName;
    Spinner spinnerPriceListSelect;
    Button bnOrdersList;
    Button bnUploadOrders;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.main);
        self = this;

        try {
            Init();
        }catch (Exception e){
            AgentAppLogger.Error(e);
        }
    }

    private void Init() {

        tvTradeAgentName = (TextView) findViewById(R.id.textViewTradeAgentName);
        tvTradeAgentName.setText(AgentApplication.tradeAgent != null ? AgentApplication.tradeAgent.getTradeAgentName() : "");

        bnOrdersList = (Button) findViewById(R.id.buttonOrdersList);
        bnUploadOrders = (Button) findViewById(R.id.buttonUploadOrders);
        bnOrdersList.setEnabled(AgentApplication.isDictionariesPresent);
        bnUploadOrders .setEnabled(AgentApplication.isDictionariesPresent);

        spinnerPriceListSelect = (Spinner)findViewById(R.id.spinnerSelectPrice);
        List items = new ArrayList<String>();

        File dicts = new File ("/data/data/ua.dn.effect.AgentApp/files/dictionaries/txt/");

        int id = 0;
        int selected = 0;
        if(dicts.exists() && dicts.isDirectory()){
            for (File f : dicts.listFiles()){
                String fileName = f.getName().replace(".txt", "");
                if (fileName.startsWith("PR_")){
                    String [] parts = fileName.split("_");
                    String acronim = parts[1];

                    Date date = null;
                    try {
                        date = new SimpleDateFormat("MMddHHmm").parse(parts[2]);
                        Date now = new Date();
                        date.setYear(now.getYear());
                        if(date.after(now)){
                            date.setYear(now.getYear() - 1);
                        }

                        SimpleDateFormat sdf = new SimpleDateFormat("dd:MM:yyyy hh:mm");
                        items.add(acronim + " (" +  sdf.format(date) + ")");

                        if(acronim.equals(AgentApplication.tradeAgent.getDefaultPriceAcronim())){
                              selected = id;
                        }
                        id++;
                        AgentAppLogger.Text(acronim + " (" +  sdf.format(date) + ")");
                    } catch (ParseException e) {
                        AgentAppLogger.Error(e);
                    }
                }
            }

            ArrayAdapter dataAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, items);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerPriceListSelect.setAdapter(dataAdapter);
            spinnerPriceListSelect.setSelection(selected);

            spinnerPriceListSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Object item = parent.getItemAtPosition(position);
                    if (item!=null) {
                        String acronim = item.toString().split(" ")[0];
                        AgentApplication.tradeAgent.setDefaultPriceAcronim(acronim);

                        try {
                            InternalStorage storage = new InternalStorage(self);
                            EntitiesReader reader = null;
                            reader = new EntitiesReader(storage);
                            AgentApplication.priceList = reader.getList();
                        }catch (Exception e){
                            AgentAppLogger.Error(e);
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void onClickSettings(View v) {
        Intent intent = new Intent(this, FtpSettingsActivity.class);
        startActivity(intent);
    }

    public void onClickSelectPriceList(View v) {
        //Intent intent = new Intent(this, FtpSettingsActivity.class);
        //startActivity(intent);
    }

    public void onClickDataLoad(View v) {
        Intent intent = new Intent(this, ManualUpdateActivity.class);
        startActivity(intent);
    }

    public void onClickUploadOrders(View v){
        Intent intent = new Intent(this, UploadOrdersActivity.class);
        startActivity(intent);
    }

    public void onClickOrdersList(View v){
        Intent intent = new Intent(this, OrdersListActivity.class);
        startActivity(intent);
    }

    public void onClickUpdateSoftware(View v){
        Intent intent = new Intent(this, UpdateSoftwareActivity.class);
        startActivity(intent);
    }

    public void onClickReports(View view) {
        Intent intent = new Intent(this, ReportsListActivity.class);
        startActivity(intent);
    }

    public void onClickChecksList(View view) {
        Intent intent = new Intent(this, ChecksListActivity.class);
        startActivity(intent);
    }
}
