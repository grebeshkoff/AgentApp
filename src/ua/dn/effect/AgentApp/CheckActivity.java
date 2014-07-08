package ua.dn.effect.AgentApp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import ua.dn.effect.AgentApp.data.model.Check;

/**
 * Created by igrebeshkov on 24.06.14.
 */
public class CheckActivity extends Activity {

    TextView tvCheckClient;
    DatePicker dpCheckInDate;
    EditText etCheckNumber;
    EditText etCheckMoney;
    EditText etCheckCoins;
    Button bnCheckSave;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check);
        Init();
    }

    @Override
    protected void onResume() {
        tvCheckClient.setText(AgentApplication.currentCheck.client.getName());
        super.onResume();
    }

    private void Init() {
        tvCheckClient = (TextView) findViewById(R.id.textViewCheckClient);
        dpCheckInDate = (DatePicker) findViewById(R.id.datePickerCheckInDate);
        etCheckNumber = (EditText) findViewById(R.id.editTextCheckNumber);
        etCheckMoney = (EditText) findViewById(R.id.editTextCheckMoney);
        etCheckCoins = (EditText) findViewById(R.id.editTextCheckCoins);
        bnCheckSave =  (Button) findViewById(R.id.buttonCheckSave);

        etCheckNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkSaveButtonEnable();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etCheckMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkSaveButtonEnable();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    public void onClickCheckClient(View view) {
        Intent intent = new Intent(this, ClientsActivity.class);
        startActivity(intent);
    }

    public void onClickCancelCheck(View v) {
        Intent intent = new Intent(this, ChecksListActivity.class);
        startActivity(intent);
    }

    public void onClickSaveCheck(View v) {
        AgentApplication.currentCheck.agent = AgentApplication.tradeAgent;
        Intent intent = new Intent(this, ChecksListActivity.class);
        startActivity(intent);
    }

    private void checkSaveButtonEnable(){
        if((!etCheckNumber.getText().equals(""))&&(!etCheckMoney.getText().equals(""))){
            bnCheckSave.setEnabled(true);
        }else{
            bnCheckSave.setEnabled(false);
        }
    }
}
