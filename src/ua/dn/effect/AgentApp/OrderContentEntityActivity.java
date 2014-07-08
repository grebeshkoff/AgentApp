package ua.dn.effect.AgentApp;

import android.app.Activity;
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
import ua.dn.effect.AgentApp.data.model.PaymentType;
import ua.dn.effect.AgentApp.data.model.Position;

/**
 * User: igrebeshkov
 * Date: 08.11.13
 * Time: 12:57
 */
public class OrderContentEntityActivity extends Activity {

    private Activity activity = this;

    private TextView tvEntityName;
    TextView tvEntityInWarehouse;
    TextView tvEntityMinSale;

    TextView tvEntityHint;

    Spinner spinnerEntityPriceType1;
    TextView tvEntityPrice1;
    TextView tvEntityCount1;
    EditText etEntityCount1;
    private TextView tvEntitySumm1;
    private TextView tvEntitySummLable1;

    private Spinner spinnerEntityPriceType2;
    private TextView tvEntityPrice2;
    private TextView tvEntityCount2;
    private EditText etEntityCount2;
    private TextView tvEntitySumm2;
    private TextView tvEntitySummLable2;

    private TextView tvEntitySummTotal;

    private ImageButton bnAddBox1;
    private ImageButton bnAddBox2;
    private Button bnAddPosition;
    private Button bnCancelPosition;

    private ScrollView svOrderPriceEntity;

    private View viewToLoad;


    private Position position;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        position = AgentApplication.currentOrder.currentPosition;

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
                count = count + position.entity.countInBox;
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

                count = count + position.entity.countInBox;
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

                //Position p = AgentApplication.currentOrder.currentPosition;//AgentApplication.currentOrder;

                //p.entity = position.entity;
                position.priceTypeId1 = (int)spinnerEntityPriceType1.getSelectedItemId();

                if(etEntityCount1.getText().toString().equals("")){
                    position.count1 = 0;
                }else{
                    position.count1 = Integer.parseInt(etEntityCount1.getText().toString());
                }

                position.paymentType1 =  AgentApplication.currentOrder.paymentType1;
                position.priceTypeId2 = (int)spinnerEntityPriceType2.getSelectedItemId();

                if(etEntityCount2.getText().toString().equals("")){
                    position.count2 = 0;
                }else{
                    position.count2 = Integer.parseInt(etEntityCount2.getText().toString());
                }

                position.paymentType2 =  AgentApplication.currentOrder.paymentType2;

                //AgentApplication.currentOrder.positions.add(p);

                Intent intent = new Intent(activity, OrderContentActivity.class);

                View vw = OrderContentActivityGroup.group.getLocalActivityManager()
                        .startActivity("show_city", intent
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                        .getDecorView();
                OrderContentActivityGroup.group.replaceView(vw);
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

        tvEntityName.setText(position.entity.name);
        tvEntityInWarehouse.setText("Остаток: " + position.entity.inWarehouse.toString());
        tvEntityMinSale.setText("Мин.  отпуск: " + position.entity.minSale);
        tvEntityHint.setText(position.entity.action);



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
        tvEntityPrice1.setText(position.entity.getPriceText(spinner1));
        tvEntityPrice2.setText(position.entity.getPriceText(spinner2));

        Double price1 = position.entity.getPriceValue(spinner1);
        Double price2 = position.entity.getPriceValue(spinner2);

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