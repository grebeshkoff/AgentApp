package ua.dn.effect.AgentApp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TabHost;
import android.widget.Toast;

import java.io.File;

/**
 * User: igrebeshkov
 * Date: 23.09.13
 * Time: 9:32
 */
public class OrderActivity extends TabActivity {

    Activity activity;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.order);

        TabHost tabHost = getTabHost();
        TabHost.TabSpec tabSpec;

        tabSpec = tabHost.newTabSpec("tag1");
        tabSpec.setIndicator("Реквизиты");
        tabSpec.setContent(new Intent(this, OrderHeaderActivity.class));
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag2");
        tabSpec.setIndicator("Прайс");
        tabSpec.setContent(new Intent(this, OrderPriceActivityGroup.class));
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag3");
        tabSpec.setIndicator("Заказ");
        tabSpec.setContent(new Intent(this, OrderContentActivityGroup.class));
        tabHost.addTab(tabSpec);

        activity = this;
    }

    @Override
    protected void onPause() {
        super.onPause();
        setTitle(R.string.app_name);
    }
}