package ua.dn.effect.AgentApp;

import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import ua.dn.effect.AgentApp.data.model.Order;

import java.util.ArrayList;

/**
 * User: igrebeshkov
 * Date: 26.11.13
 * Time: 10:35
 */
public class OrderPriceActivityGroup extends ActivityGroup {

    // Keep this in a static variable to make it accessible for all the nesten activities, lets   manipulate the view
    public static OrderPriceActivityGroup group;

    // Need to keep track of the history if you want the back-button to work properly, don't use this if your activities requires a lot of memory.
    private ArrayList history;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.history = new ArrayList();
        group = this;

        // Start the root activity withing the group and get its view
        View view = getLocalActivityManager().startActivity("tag2", new
                Intent(this, OrderPriceActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                .getDecorView();

        // Replace the view of this ActivityGroup
        replaceView(view);

    }

    public void replaceView(View v) {
        history.add(v);
        setContentView(v);
    }

    public void back() {
        if(history.size() > 1) {
            history.remove(history.size()-1);
            setContentView((View)history.get(history.size()-1));
        }else {
            new AlertDialog.Builder(this)
                    .setTitle("Внимание")
                    .setMessage("Сохранить текущий заказ заказа?")
                    .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            AgentApplication.currentOrder.Save(group);
                            AgentApplication.currentOrder = new Order();
                            group.finish();
                        }
                    })
                    .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            group.finish();
                        }
                    }).show();
        }
    }

    @Override
    public void onBackPressed() {
        OrderPriceActivityGroup.group.back();
        return;
    }

}
