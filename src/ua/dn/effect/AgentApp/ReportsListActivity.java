package ua.dn.effect.AgentApp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import ua.dn.effect.AgentApp.data.model.Report;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by igrebeshkov on 16.04.14.
 */
public class ReportsListActivity extends Activity {

    private ListView lvReports;
    private SimpleAdapter adapter;
    private Context ctx;
    private ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reports_list);

        Init();
    }

    private void Init() {
        ctx = this;
        lvReports = (ListView) findViewById(R.id.listViewReports);

        for (Report report : AgentApplication.reportsList){


            if(report.name != null){
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("Date", new SimpleDateFormat("dd.MM.yyyy  hh:mm").format(report.date));
                map.put("Name", report.name);
                list.add(map);
            }
        }

        adapter = new SimpleAdapter(this, list, R.layout.reports_list_item,
                new String[] {"Date", "Name"},
                new int[] {
                        R.id.textViewReportDate,
                        R.id.textViewReportName});
        lvReports.setAdapter(adapter);

        lvReports.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AgentApplication.currentReport = AgentApplication.reportsList.get(position);
                Intent intent = new Intent(ctx, ReportActivity.class);
                startActivity(intent);
            }
        });
    }
}