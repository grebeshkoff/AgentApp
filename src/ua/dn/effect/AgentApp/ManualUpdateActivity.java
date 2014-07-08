package ua.dn.effect.AgentApp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import ua.dn.effect.AgentApp.data.exchenge.FtpConnection;
import ua.dn.effect.AgentApp.data.exchenge.FtpDownloader;
import ua.dn.effect.AgentApp.data.model.TradeAgent;
import ua.dn.effect.AgentApp.data.reader.EntitiesReader;
import ua.dn.effect.AgentApp.data.reader.ReportReader;
import ua.dn.effect.AgentApp.data.reader.SalesReader;
import ua.dn.effect.AgentApp.data.store.InternalStorage;
import ua.dn.effect.AgentApp.data.store.StorageException;
import ua.dn.effect.AgentApp.util.AgentAppLogger;
import ua.dn.effect.AgentApp.util.ZipExtractor;

/**
 * User: igrebeshkov
 * Date: 12.11.13
 * Time: 16:49
 */
public class ManualUpdateActivity extends Activity {

    TextView tvManualUpdateStatus;
    FtpDownloaderTask task;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.setFinishOnTouchOutside(false);
        setContentView(R.layout.manual_update);
        Init();
        task = (FtpDownloaderTask) getLastNonConfigurationInstance();
        if (task == null) {
            task = new FtpDownloaderTask();
            task.execute();
        }
        task.link(this);
    }

    public Object onRetainNonConfigurationInstance() {
        task.unLink();
        return task;
    }

    private void Init() {
        tvManualUpdateStatus = (TextView) findViewById(R.id.textViewManualUpdateStatus);
    }

    @Override
    public void onBackPressed() {
    }

    public static class FtpDownloaderTask extends AsyncTask<Void, String, Void> {
        String status;

        ManualUpdateActivity activity;

        void link(ManualUpdateActivity act) {
            activity = act;
        }

        void unLink() {
            activity = null;
        }

        @Override
        protected Void doInBackground (Void... params) {
            try {
                publishProgress("Инициализация...");
                InternalStorage storage = new InternalStorage(activity);
                FtpConnection c = new FtpConnection();
                c.Init(activity);
                FtpDownloader ftpDownloader = new FtpDownloader(c, storage);
                if(!ftpDownloader.isReady){
                    return null;
                }

                publishProgress("Загрузка данных...");
                ftpDownloader.downloadFile();
                publishProgress("Распаковка...");
                ZipExtractor extractor = new ZipExtractor(storage);
                extractor.extractAll();
                publishProgress("Обновление справочников...");
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

                return null;
            }catch (Exception e){
                AgentAppLogger.Error(e);
                return null;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            activity.tvManualUpdateStatus.setText(values[0]);
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            Intent intent = new Intent(activity, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            activity.startActivity(intent);
        }
    }
}

