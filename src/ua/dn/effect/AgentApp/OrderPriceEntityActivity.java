package ua.dn.effect.AgentApp;

import android.app.Activity;
import android.app.ActivityGroup;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import ua.dn.effect.AgentApp.data.config.AgentAppConfig;
import ua.dn.effect.AgentApp.data.model.Entity;
import ua.dn.effect.AgentApp.data.model.PaymentType;
import ua.dn.effect.AgentApp.data.model.Position;
import ua.dn.effect.AgentApp.util.AgentAppLogger;

import java.util.Currency;

/**
 * User: igrebeshkov
 * Date: 08.11.13
 * Time: 12:57
 */
public class OrderPriceEntityActivity extends Activity {

    Activity activity = this;

    TextView tvEntityName;
    TextView tvEntityInWarehouse;
    TextView tvEntityMinSale;

    TextView tvEntityHint;

    Spinner spinnerEntityPriceType1;
    TextView tvEntityPrice1;
    TextView tvEntityCount1;
    EditText etEntityCount1;
    TextView tvEntitySumm1;
    TextView tvEntitySummLable1;

    Spinner spinnerEntityPriceType2;
    TextView tvEntityPrice2;
    TextView tvEntityCount2;
    EditText etEntityCount2;
    TextView tvEntitySumm2;
    TextView tvEntitySummLable2;

    TextView tvEntitySummTotal;

    ImageButton bnAddBox1;
    ImageButton bnAddBox2;
    Button bnAddPosition;
    Button bnCancelPosition;

    ScrollView svOrderPriceEntity;

    View viewToLoad;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewToLoad = LayoutInflater.from(this.getParent().getParent()).inflate(R.layout.order_price_entity, null);
        setContentView(viewToLoad);
        spinnerEntityPriceType1 = (Spinner) viewToLoad.findViewById(R.id.spinnerEntityPriceType1);
        spinnerEntityPriceType2 = (Spinner) viewToLoad.findViewById(R.id.spinnerEntityPriceType2);

        bnAddBox1 = (ImageButton) findViewById(R.id.imageButtonAddBox1);
        bnAddBox1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = etEntityCount1.getText().toString();
                Integer count = 0;
                if(!str.equals("")){
                    count = Integer.parseInt(str);
                }
                count = count + AgentApplication.currentOrder.currentEntity.countInBox;
                etEntityCount1.setText(count.toString());
            }
        });

        bnAddBox2 = (ImageButton) findViewById(R.id.imageButtonAddBox2);
        bnAddBox2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = etEntityCount2.getText().toString();
                Integer count = 0;
                if(!str.equals("")){
                    count = Integer.parseInt(str);
                }

                count = count + AgentApplication.currentOrder.currentEntity.countInBox;
                etEntityCount2.setText(count.toString());
            }
        });

        bnCancelPosition = (Button) findViewById(R.id.buttonCancelPosition);
        bnCancelPosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, OrderPriceGroupEntryActivity.class);

                //Intent intent = new Intent(activity, OrderPriceGroupEntryActivity.class);
                //OrderPriceActivityGroup.replaceContentView("tag2", intent);

                //startActivity(intent);
                View vw = OrderPriceActivityGroup.group.getLocalActivityManager()
                        .startActivity("show_city", intent
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                        .getDecorView();
                OrderPriceActivityGroup.group.replaceView(vw);
            }
        });

        bnAddPosition = (Button) findViewById(R.id.buttonAddPosition);
        bnAddPosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Position p = new Position();
                p.entity = AgentApplication.currentOrder.currentEntity;
                p.priceTypeId1 = (int)spinnerEntityPriceType1.getSelectedItemId();

                if(etEntityCount1.getText().toString().equals("")){
                    p.count1 = 0;
                }else{
                    p.count1 = Integer.parseInt(etEntityCount1.getText().toString());
                }

                p.paymentType1 =  AgentApplication.currentOrder.paymentType1;
                p.priceTypeId2 = (int)spinnerEntityPriceType2.getSelectedItemId();

                if(etEntityCount2.getText().toString().equals("")){
                    p.count2 = 0;
                }else{
                    p.count2 = Integer.parseInt(etEntityCount2.getText().toString());
                }

                p.paymentType2 =  AgentApplication.currentOrder.paymentType2;

                AgentApplication.currentOrder.positions.add(p);

                Intent intent = new Intent(activity, OrderPriceGroupEntryActivity.class);

                //Intent intent = new Intent(activity, OrderPriceGroupEntryActivity.class);
                //OrderPriceActivityGroup.replaceContentView("tag2", intent);

                //startActivity(intent);
                View vw = OrderPriceActivityGroup.group.getLocalActivityManager()
                        .startActivity("show_city", intent
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                        .getDecorView();
                OrderPriceActivityGroup.group.replaceView(vw);
                //replaceContentView("tag2", intent);
                //startActivity(intent);
                //getParent().getParent().setTitle(AgentApplication.currentOrder.getTotalSumm());
            }
        });

        Init();
    }

    private void Init() {
        svOrderPriceEntity = (ScrollView)findViewById(R.id.scrollViewOrderPriceEntity);

        svOrderPriceEntity.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager)getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(svOrderPriceEntity.getWindowToken(), 0);
                return false;
            }
        });

        tvEntityName = (TextView) findViewById(R.id.textViewEntityName);
        tvEntityInWarehouse = (TextView) findViewById(R.id.textViewEntityInWarehouse);
        tvEntityMinSale =  (TextView) findViewById(R.id.textViewEntityMinSale);

        tvEntityHint = (TextView) findViewById(R.id.textViewEntityHint);

        tvEntityPrice1 = (TextView) findViewById(R.id.textViewEntityPrice1);
        tvEntityCount1 = (TextView) findViewById(R.id.textViewEntityCount1);
        etEntityCount1 = (EditText)viewToLoad.findViewById(R.id.editTextEntityCount1);
        tvEntitySumm1 = (TextView)findViewById(R.id.textViewEntitySumm1);
        tvEntitySummLable1 = (TextView)findViewById(R.id.textViewEntitySummLable1);

        tvEntityPrice2 = (TextView) findViewById(R.id.textViewEntityPrice2);
        tvEntityCount2 = (TextView) findViewById(R.id.textViewEntityCount2);
        etEntityCount2 = (EditText)viewToLoad.findViewById(R.id.editTextEntityCount2);
        tvEntitySumm2 = (TextView)findViewById(R.id.textViewEntitySumm2);
        tvEntitySummLable2 = (TextView)findViewById(R.id.textViewEntitySummLable2);

        tvEntitySummTotal = (TextView)findViewById(R.id.textViewEntitySummTotal);

        tvEntityName.setText(AgentApplication.currentOrder.currentEntity.name);
        tvEntityInWarehouse.setText("Остаток: " + AgentApplication.currentOrder.currentEntity.inWarehouse.toString());
        tvEntityMinSale.setText("Мин.  отпуск: " + AgentApplication.currentOrder.currentEntity.minSale);
        tvEntityHint.setText(AgentApplication.currentOrder.currentEntity.action);



        FillPriceTypeSpinner1();
        FillPriceTypeSpinner2();

        //TextWatcher tw =

        etEntityCount1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                UpdateMoneyValues();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
        etEntityCount2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                UpdateMoneyValues();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

//        tvEntityCount1.setText("Кол-во ("+AgentApplication.currentOrder.paymentType1.value+")");
//        tvEntityCount2.setText("Кол-во ("+AgentApplication.currentOrder.paymentType2.value+")");
//        tvEntitySummLable1.setText("Сумма ("+AgentApplication.currentOrder.paymentType1.value+")");
//        tvEntitySummLable2.setText("Сумма ("+AgentApplication.currentOrder.paymentType2.value+")");
//
//        if(AgentApplication.currentOrder.paymentType1 == PaymentType.NONE){
//            etEntityCount1.setEnabled(false);
//            bnAddBox1.setEnabled(false);
//        }else{
//            etEntityCount1.setEnabled(true);
//            bnAddBox1.setEnabled(true);
//        }
//
//        if(AgentApplication.currentOrder.paymentType2 == PaymentType.NONE){
//            etEntityCount2.setEnabled(false);
//            bnAddBox2.setEnabled(false);
//        }else{
//            etEntityCount2.setEnabled(true);
//            bnAddBox2.setEnabled(true);
//        }



    }

    private void UpdateMoneyValues() {
        int spinner1 = spinnerEntityPriceType1.getSelectedItemPosition();
        int spinner2 = spinnerEntityPriceType2.getSelectedItemPosition();
        tvEntityPrice1.setText(AgentApplication.currentOrder.currentEntity.getPriceText(spinner1));
        tvEntityPrice2.setText(AgentApplication.currentOrder.currentEntity.getPriceText(spinner2));

        Double price1 = AgentApplication.currentOrder.currentEntity.getPriceValue(spinner1);
        Double price2 = AgentApplication.currentOrder.currentEntity.getPriceValue(spinner2);

        int count1 = 0;
        int count2 = 0;
        try {
            count1 = Integer.parseInt(etEntityCount1.getText().toString());
            count2 = Integer.parseInt(etEntityCount2.getText().toString());
        }catch (Exception e){}


        Double summ1 =  price1 * count1;
        Double summ2  = price2 * count2;
        Double total = summ1 + summ2;

        if ((count1 + count2) == 0){
            bnAddPosition.setEnabled(false);
        }else{
            bnAddPosition.setEnabled(true);
        }

        tvEntitySumm1.setText(String.format("%.2f", summ1) + " " + AgentAppConfig.CURRENCY_ABR);
        tvEntitySumm2.setText(String.format("%.2f", summ2) + " " + AgentAppConfig.CURRENCY_ABR);
        tvEntitySummTotal.setText(String.format("%.2f", total) + " " + AgentAppConfig.CURRENCY_ABR);
    }

    private void FillPriceTypeSpinner1(){

        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(
                        viewToLoad.getContext(),
                        android.R.layout.simple_spinner_item,
                        AgentApplication.priceList.priceTypes);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerEntityPriceType1.setPrompt("Вид цены");
        spinnerEntityPriceType1.setAdapter(adapter);
        spinnerEntityPriceType1.setSelection(AgentApplication.priceList.currentPriseTypeId);

        spinnerEntityPriceType1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                //Position position1 = AgentApplication.currentOrder.currentPosition;
                //position1.priceTypeId1 = position;
                UpdateMoneyValues();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    private void FillPriceTypeSpinner2(){
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(
                        viewToLoad.getContext(),
                        android.R.layout.simple_spinner_item,
                        AgentApplication.priceList.priceTypes);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEntityPriceType2.setPrompt("Вид цены");
        spinnerEntityPriceType2.setAdapter(adapter);
        spinnerEntityPriceType2.setSelection(AgentApplication.priceList.currentPriseTypeId);

        spinnerEntityPriceType2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                UpdateMoneyValues();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(AgentApplication.currentOrder.paymentType1 == PaymentType.NONE){
            etEntityCount1.setEnabled(false);
            etEntityCount1.setText("");
            bnAddBox1.setEnabled(false);
        }else{
            etEntityCount1.setEnabled(true);
            bnAddBox1.setEnabled(true);
        }

        if(AgentApplication.currentOrder.paymentType2 == PaymentType.NONE){
            etEntityCount2.setEnabled(false);
            etEntityCount2.setText("");
            bnAddBox2.setEnabled(false);
        }else{
            etEntityCount2.setEnabled(true);
            bnAddBox2.setEnabled(true);
        }
        tvEntityCount1.setText("Кол-во ("+AgentApplication.currentOrder.paymentType1.value+")");
        tvEntityCount2.setText("Кол-во ("+AgentApplication.currentOrder.paymentType2.value+")");
        tvEntitySummLable1.setText("Сумма ("+AgentApplication.currentOrder.paymentType1.value+")");
        tvEntitySummLable2.setText("Сумма ("+AgentApplication.currentOrder.paymentType2.value+")");

        UpdateMoneyValues();
    }


}