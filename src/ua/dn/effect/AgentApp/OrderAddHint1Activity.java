package ua.dn.effect.AgentApp;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * User: igrebeshkov
 * Date: 25.11.13
 * Time: 11:14
 */
public class OrderAddHint1Activity extends Activity {

    Activity activity = this;
    Button bnHint1Save;
    Button bnHint1Cancel;
    EditText etHint;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_add_hint1);
        Init();
    }

    private void Init() {
        etHint  = (EditText) findViewById(R.id.editTextHint1);
        bnHint1Save = (Button) findViewById(R.id.buttonHint1Save);
        etHint.setText(AgentApplication.currentOrder.remark1);
        bnHint1Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AgentApplication.currentOrder.remark1 =  etHint.getText().toString();
                activity.finish();
            }
        });

        bnHint1Cancel = (Button) findViewById(R.id.buttonHint1Cancel);
        bnHint1Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("Качественные");
        menu.add("Даты");
        menu.add("Распечатать ТТН");
        menu.add("Счет фактура");
        menu.add("Переоценка");
        menu.add("Оплата по факту");
        menu.add("Агенту в машину");
        menu.add("План");
        menu.add("Отчистить");
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getTitle().equals("Отчистить")){
            etHint.setText("");
        } else {
            String text = etHint.getText().toString();
            text = text + "<" + item.getTitle() + ">";
            etHint.setText(text);
        }
        return super.onOptionsItemSelected(item);
    }
}