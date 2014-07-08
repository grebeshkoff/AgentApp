package ua.dn.effect.AgentApp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebView;
import ua.dn.effect.AgentApp.data.model.Order;
import ua.dn.effect.AgentApp.util.StringConverter;

import java.net.URLEncoder;

/**
 * Created by igrebeshkov on 17.04.14.
 */
public class ReportActivity extends Activity {

    WebView wvReport;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report);
        Init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.report_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_upload:
                wvReport.scrollTo(0,0);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void Init() {
        wvReport = (WebView) findViewById(R.id.webViewReport);
        wvReport.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onExceededDatabaseQuota(String url, String databaseIdentifier, long currentQuota, long estimatedSize,
                                                long totalUsedQuota, WebStorage.QuotaUpdater quotaUpdater) {
                quotaUpdater.updateQuota(estimatedSize * 2);
            }
        });

        WebSettings webSettings = wvReport.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabasePath("/data/data/"+this.getPackageName()+"/databases/");

        if(AgentApplication.currentReport.file == null){
            wvReport.loadUrl("file:///android_asset/www/test.html");
            return;
        }

        String data = "";
        try {
            data = "<?xml version=\"1.0\" encoding=\"utf-8\" ?>"  + StringConverter.getStringFromFile(AgentApplication.currentReport.file);
        } catch (Exception e) {
            e.printStackTrace();
        };
        wvReport.loadDataWithBaseURL("file:///", data, "text/html", "utf-8", "");
    }
}