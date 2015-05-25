package ua.dn.effect.AgentApp;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.LocalActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import ua.dn.effect.AgentApp.data.model.Entity;
import ua.dn.effect.AgentApp.data.model.Order;
import ua.dn.effect.AgentApp.data.model.Position;
import ua.dn.effect.AgentApp.util.AgentAppLogger;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * User: igrebeshkov
 * Date: 24.09.13
 * Time: 13:45
 */
public class OrderContentActivity extends Activity {

    ListView lvPositions;
    ArrayList<HashMap<String, String>> list;
    //ArrayAdapter<Position> adapter;

    SimpleAdapter adapter;
    Context ctx;
    Activity activity;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_content);
        ctx = this;
        activity = this;
        Init();
    }

    public void onResume() {
        super.onResume();
        Init();
        getParent().getParent().setTitle(AgentApplication.currentOrder.getTotalSumm());
    }

    private void Init() {
        lvPositions = (ListView) findViewById(R.id.listViewPositions);
        FillListViewPositions();

        getParent().setTitle(AgentApplication.currentOrder.getTotalSumm());

        lvPositions.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           final int pos, long id) {
                AgentAppLogger.Text("long clicked pos: " + pos);
                new AlertDialog.Builder(OrderContentActivity.this.getParent())
                        .setTitle("Внимание")
                        .setMessage("Подтвердить удаление позиции?")
                        .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                AgentApplication.currentOrder.positions.remove(pos);
                                list.remove(pos);
                                adapter.notifyDataSetChanged();
                                activity.getParent().setTitle(AgentApplication.currentOrder.getTotalSumm());
                            }
                        })
                        .setNegativeButton("Отмена", null)
                        .show();
                return true;
            }
        });

        lvPositions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                AgentApplication.currentOrder.currentPosition = AgentApplication.currentOrder.positions.get(position);

                Intent intent = new Intent(activity, OrderContentEntityActivity.class);
                //startActivity(intent);
                //replaceContentView("tag2", intent);

                //Intent intent = new Intent(activity, OrderPriceGroupEntryActivity.class);
                //OrderPriceActivityGroup.replaceContentView("tag2", intent);

                //startActivity(intent);
                LocalActivityManager mgr = OrderContentActivityGroup.group.getLocalActivityManager();
                View vw = mgr.startActivity("tag3", intent
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                        .getDecorView();
                OrderContentActivityGroup.group.replaceView(vw);
                //activity.finish();
            }
        });
    }

    private void FillListViewPositions() {
        list = new ArrayList<HashMap<String, String>>();
        for (Position p :  AgentApplication.currentOrder.positions){
            HashMap<String, String> map = new HashMap<String, String>();

            Double d1 = new Double(p.count1);
            Double d2 = new Double(p.count2);
            Double dbox = new Double( p.entity.countInBox);

            map.put("Name", p.entity.name);
            map.put("TotalCount", String.valueOf(p.count1 + p.count2));
            map.put("PriceType1", AgentApplication.priceList.priceTypes[p.priceTypeId1]);
            map.put("PriceType2", AgentApplication.priceList.priceTypes[p.priceTypeId2]);
            map.put("Count1",String.valueOf(p.count1) + " [" + String.format("%.2f", (d1/dbox)) + "]");
            map.put("Count2",String.valueOf(p.count2) + " [" + String.format("%.2f", (d2/dbox)) + "]");
            list.add(map);
        }

        //adapter = new ArrayAdapter<Position>(this, R.layout.order_position_item, R.id.position_name, AgentApplication.currentOrder.positions);

        adapter = new SimpleAdapter(this, list, R.layout.order_content_item,
                new String[] {"Name", "TotalCount", "PriceType1", "PriceType2", "Count1", "Count2"},
                new int[] {
                        R.id.textViewPositionName,
                        R.id.textViewPsitionCount,
                        R.id.textViewPriceType1,
                        R.id.textViewPriceType2,
                        R.id.textViewCount1,
                        R.id.textViewCount2 });
        lvPositions.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(OrderContentActivity.this.getParent())
                .setTitle("Внимание")
                .setMessage("Сохранить текущий заказ заказа?")
                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        AgentApplication.currentOrder.Save(activity);
                        AgentApplication.currentOrder = new Order();
                        activity.finish();
                    }
                })
                .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        activity.finish();
                    }
                }).show();
    }
}