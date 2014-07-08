package ua.dn.effect.AgentApp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import ua.dn.effect.AgentApp.data.model.Check;
import ua.dn.effect.AgentApp.data.model.Order;
import ua.dn.effect.AgentApp.util.ApplicationState;

/**
 * Created by igrebeshkov on 23.06.14.
 */
public class ChecksListActivity extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checks_list);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.checks_list_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        AgentApplication.state = ApplicationState.NAN;
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_new:
                AgentApplication.state = ApplicationState.CHECK_ADDING;
                AgentApplication.currentCheck = new Check();
                Intent intent = new Intent(this, CheckActivity.class);
                startActivity(intent);
                return true;
            case R.id.orders_list_processed:
//                copyItems(savedOrders);
//                adapter.notifyDataSetChanged();
                return true;
            case R.id.orders_list_not_processed:
//                copyItems(newOrders);
//                adapter.notifyDataSetChanged();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
   }
}