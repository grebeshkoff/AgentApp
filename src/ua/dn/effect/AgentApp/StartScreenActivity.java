package ua.dn.effect.AgentApp;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import ua.dn.effect.AgentApp.data.config.AgentAppConfig;
import ua.dn.effect.AgentApp.data.model.SalesHistory;
import ua.dn.effect.AgentApp.data.model.TradeAgent;
import ua.dn.effect.AgentApp.data.reader.EntitiesReader;
import ua.dn.effect.AgentApp.data.reader.ReportReader;
import ua.dn.effect.AgentApp.data.reader.SalesReader;
import ua.dn.effect.AgentApp.data.store.InternalStorage;
import ua.dn.effect.AgentApp.util.AgentAppLogger;

import java.io.File;

/**
 * User: igrebeshkov
 * Date: 07.11.13
 * Time: 22:57
 */
public class StartScreenActivity extends Activity {

    ProgressBar pbCheck;
    TextView tvCheck;
    StartScreenActivity self;
    boolean start = false;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.start_screen);

        //start = true;
        Init();
        self = this;
        if(AgentApplication.priceList == null){
            new CheckDataAsyncTask().execute();
        }else {
            StartMainActivity();
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(start){
            this.finish();
        }
        start = true;
    }

    private void Init() {
        View sample = findViewById(R.id.progressBarCheck);

        pbCheck = (ProgressBar) findViewById(R.id.progressBarCheck);
        tvCheck = (TextView) findViewById(R.id.textViewCheck);

        AgentAppConfig cfg = new AgentAppConfig(this);
    }

    private class CheckDataAsyncTask extends AsyncTask<Void, Void, Void> {
        String status;

        @Override
        protected Void doInBackground(Void... params) {
            try {
                InternalStorage storage = null;
                storage = new InternalStorage(self);
                AgentApplication.ftpConnection.Init(self);
                AgentApplication.tradeAgent = new TradeAgent();
                EntitiesReader reader = null;
                reader = new EntitiesReader(storage);
                AgentApplication.priceList = reader.getList();
                AgentApplication.salesHistory = SalesReader.getHistory(storage);
                AgentApplication.reportsList = ReportReader.getReportsList(storage);
                if(AgentApplication.priceList.list.size() == 0){
                    AgentApplication.isDictionariesPresent = false;
                } else {
                    AgentApplication.isDictionariesPresent = true;
                }

            } catch (Exception e) {
                AgentAppLogger.Error(e);
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            self.StartMainActivity();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

    }
    private void StartMainActivity(){

        Intent intent = new Intent(self, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK);

//        Toast.makeText(self, "!!!!!!",
//                Toast.LENGTH_SHORT).show();
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        startActivity(intent);
    }
}