package ua.dn.effect.AgentApp;

import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import ua.dn.effect.AgentApp.data.model.Order;

import java.util.ArrayList;

/**
 * User: igrebeshkov
 * Date: 26.11.13
 * Time: 10:35
 */
public class OrderContentActivityGroup extends ActivityGroup {

    public static OrderContentActivityGroup group;
    private ArrayList history;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.history = new ArrayList();
        group = this;

        // Start the root activity withing the group and get its view
        View view = getLocalActivityManager().startActivity("tag3", new
                Intent(this, OrderContentActivity.class)
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
        OrderContentActivityGroup.group.back();
        return;
    }

}
