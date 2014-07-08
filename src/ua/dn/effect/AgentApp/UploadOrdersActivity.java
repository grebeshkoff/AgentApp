package ua.dn.effect.AgentApp;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;
import ua.dn.effect.AgentApp.data.exchenge.FtpConnection;
import ua.dn.effect.AgentApp.data.exchenge.FtpDownloader;
import ua.dn.effect.AgentApp.data.exchenge.FtpUploader;
import ua.dn.effect.AgentApp.data.model.TradeAgent;
import ua.dn.effect.AgentApp.data.reader.EntitiesReader;
import ua.dn.effect.AgentApp.data.store.InternalStorage;
import ua.dn.effect.AgentApp.util.AgentAppLogger;
import ua.dn.effect.AgentApp.util.ZipExtractor;

/**
 * User: igrebeshkov
 * Date: 12.11.13
 * Time: 16:49
 */
public class UploadOrdersActivity extends Activity {

    //private UploadOrdersActivity self;
    UploadOrdersTask task;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.setFinishOnTouchOutside(false);

        setContentView(R.layout.upload_orders);

        Init();

        task = (UploadOrdersTask) getLastNonConfigurationInstance();
        if (task == null){
            task = new UploadOrdersTask();
            task.execute();
        }
        task.link(this);
    }

    public Object onRetainNonConfigurationInstance() {
        task.unLink();
        return task;
    }

    private void Init() {
    }

    @Override
    public void onBackPressed() {
    }

    public static class UploadOrdersTask extends AsyncTask<Void, String, Void> {
        UploadOrdersActivity  activity;

        void link(UploadOrdersActivity act) {
            activity = act;
        }

        void unLink() {
            activity = null;
        }

        @Override
        protected Void doInBackground (Void... params) {
            try {
                FtpUploader worker = new FtpUploader();
                worker.run();
                return null;
            }catch (Exception e){
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
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            activity.finish();
            Intent intent = new Intent(activity, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_CLEAR_TASK |
                    Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent);
        }
    }
}