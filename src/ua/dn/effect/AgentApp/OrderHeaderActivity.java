package ua.dn.effect.AgentApp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.*;
import ua.dn.effect.AgentApp.data.model.Order;
import ua.dn.effect.AgentApp.data.model.PaymentType;
import ua.dn.effect.AgentApp.util.ApplicationState;

import java.util.ArrayList;
import java.util.Date;

/**
 * User: igrebeshkov
 * Date: 24.09.13
 * Time: 11:32
 */
public class OrderHeaderActivity extends Activity {

    /**
     * Called when the activity is first created.
     */
    Activity activity = this;
    TextView tvClient;
    TextView tvClientRealName;

    TextView tvHint1;
    TextView tvHint2;

    Button bnSaveOrder;
    Spinner spinnerPaymentType1;
    Spinner spinnerPaymentType2;

    DatePicker dpOrderInDate;
    CheckBox cbOrderInDate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_header);
        Init();
    }

    private void Init() {
        tvClient = (TextView) findViewById(R.id.textViewClient);
        tvClientRealName = (TextView) findViewById(R.id.textViewClientRealName);


        tvHint1 =  (TextView) findViewById(R.id.textViewHint1);
        tvHint1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, OrderAddHint1Activity.class);
                activity.startActivity(intent);
            }
        });
        tvHint2 =  (TextView) findViewById(R.id.textViewHint2);
        tvHint2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, OrderAddHint2Activity.class);
                activity.startActivity(intent);
            }
        });


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new ArrayList<String>());

        for (PaymentType pt : PaymentType.values()){
            adapter.add(pt.value);
        }

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        String spinnerTitle = "Способ оплаты";

        bnSaveOrder = (Button) findViewById(R.id.buttonSaveOrder);

        spinnerPaymentType1 = (Spinner) findViewById(R.id.spinnerFirstPayType);
        spinnerPaymentType1.setPrompt(spinnerTitle);
        spinnerPaymentType1.setAdapter(adapter);
        spinnerPaymentType1.setSelection(PaymentType.getPosition(AgentApplication.currentOrder.paymentType1));
        spinnerPaymentType1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                AgentApplication.currentOrder.paymentType1 = PaymentType.values()[position];
                setSaveButtonVisible();
                setHintsClickability();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinnerPaymentType2 = (Spinner) findViewById(R.id.spinnerSecondPayType);
        spinnerPaymentType2.setAdapter(adapter);
        spinnerPaymentType2.setPrompt(spinnerTitle);
        spinnerPaymentType2.setSelection(PaymentType.getPosition(AgentApplication.currentOrder.paymentType2));
        spinnerPaymentType2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                AgentApplication.currentOrder.paymentType2 = PaymentType.values()[position];

                setSaveButtonVisible();
                setHintsClickability();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });



        if(AgentApplication.currentOrder.deliverDate==null){
            cbOrderInDate = (CheckBox) findViewById(R.id.checkBoxOrderInDate);
            cbOrderInDate.setChecked(false);
            dpOrderInDate = (DatePicker) findViewById(R.id.datePickerOrderInDate);
            dpOrderInDate.setEnabled(false);
            Date dt = new Date();
            dpOrderInDate.init(dt.getYear() + 1900, dt.getMonth(), dt.getDate(), new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    if(cbOrderInDate.isChecked())
                        AgentApplication.currentOrder.deliverDate = new Date(year, monthOfYear, dayOfMonth);
                    else
                        AgentApplication.currentOrder.deliverDate = null;
                }
            });
        }else{
            cbOrderInDate = (CheckBox) findViewById(R.id.checkBoxOrderInDate);
            cbOrderInDate.setChecked(true);
            dpOrderInDate = (DatePicker) findViewById(R.id.datePickerOrderInDate);
            dpOrderInDate.setEnabled(true);
            dpOrderInDate.init(AgentApplication.currentOrder.deliverDate.getYear() + 1900, AgentApplication.currentOrder.deliverDate.getMonth(), AgentApplication.currentOrder.deliverDate.getDate(), new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    if(cbOrderInDate.isChecked())
                        AgentApplication.currentOrder.deliverDate = new Date(year, monthOfYear, dayOfMonth);
                    else
                        AgentApplication.currentOrder.deliverDate = null;
                }
            });
        }



        cbOrderInDate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    dpOrderInDate.setEnabled(true);
                }else{
                    dpOrderInDate.setEnabled(false);
                }
            }
        });

    }

    private void setSaveButtonVisible() {
        if(((AgentApplication.currentOrder.paymentType1 == PaymentType.NONE)&&(AgentApplication.currentOrder.paymentType2 == PaymentType.NONE)) || (tvClient.getText().equals("...")) || (AgentApplication.currentOrder.positions.size() == 0)){
            bnSaveOrder.setEnabled(false);
        } else {
            bnSaveOrder.setEnabled(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        tvClient.setText(AgentApplication.currentOrder.client.getName());

        if(AgentApplication.currentOrder.client != null){
            if (AgentApplication.currentOrder.client.getRealName().equals("")){
                tvClientRealName.setText("");
                tvClientRealName.setVisibility(View.GONE);
            }else {
                tvClientRealName.setText("(" + AgentApplication.currentOrder.client.getRealName() + ")");
                tvClientRealName.setVisibility(View.VISIBLE);
            }
        }

        if(AgentApplication.currentOrder.remark1.equals("")){
            tvHint1.setText("...");
        }else {
            tvHint1.setText(AgentApplication.currentOrder.remark1);
        }

        if(AgentApplication.currentOrder.remark2.equals("")){
            tvHint2.setText("...");
        }else {
            tvHint2.setText(AgentApplication.currentOrder.remark2);
        }
        setSaveButtonVisible();
        setHintsClickability();

        getParent().setTitle(AgentApplication.currentOrder.getTotalSumm());
    }

    private void setHintsClickability() {
        if (AgentApplication.currentOrder.paymentType1 == PaymentType.NONE){
            tvHint1.setTextColor(getResources().getColor(R.color.hint_disabled));
            tvHint1.setClickable(false);
        }else {
            tvHint1.setTextColor(getResources().getColor(R.color.hint_enabled));
            tvHint1.setClickable(true);
        }

        if (AgentApplication.currentOrder.paymentType2 == PaymentType.NONE){
            tvHint2.setTextColor(getResources().getColor(R.color.hint_disabled));
            tvHint2.setClickable(false);
        }else {
            tvHint2.setTextColor(getResources().getColor(R.color.hint_enabled));
            tvHint2.setClickable(true);
        }

    }

    public void onClickClient(View v) {
        Intent intent = new Intent(this, ClientsActivity.class);
        startActivity(intent);
    }

    public void onClickSaveOrder(View v){

        AgentApplication.currentOrder.Save(this);
        AgentApplication.currentOrder = new Order();
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        AgentApplication.state = ApplicationState.NAN;
        startActivity(intent);

    }

    public void onClickCancelOrder(View v){
        AgentApplication.currentOrder = new Order();

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        AgentApplication.state = ApplicationState.NAN;
        startActivity(intent);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            new AlertDialog.Builder(this)
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
        return super.onKeyDown(keyCode, event);
    }

    public void onClickOrderInDate(View view) {
        setDeliverDate();
    }

    public void onclickDatePickerOrderInDate(View view) {
        setDeliverDate();
    }

    private void setDeliverDate() {
        Integer year = dpOrderInDate.getYear();
        Integer month = dpOrderInDate.getMonth();
        Integer date = dpOrderInDate.getDayOfMonth();
        if(cbOrderInDate.isChecked()){
            AgentApplication.currentOrder.deliverDate = new Date(year, month, date);
        }else {
            AgentApplication.currentOrder.deliverDate = null;
        }
    }

}
