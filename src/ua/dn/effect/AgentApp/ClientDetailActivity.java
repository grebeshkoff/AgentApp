package ua.dn.effect.AgentApp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import ua.dn.effect.AgentApp.data.model.Client;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import ua.dn.effect.AgentApp.util.*;

/**
 * User: igrebeshkov
 * Date: 28.10.13
 * Time: 14:05
 */
public class ClientDetailActivity extends Activity {

    Button commit;
    Client currentClient;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_detail);
        commit = (Button)findViewById(R.id.buttonCommitClient);
        LinearLayout clientDetailsLayout = (LinearLayout) findViewById(R.id.ClientDetailsLayout);
        LayoutInflater inflater = getLayoutInflater();


        if((AgentApplication.state == ApplicationState.ORDER_ADDING)||(AgentApplication.state == ApplicationState.ORDER_EDITING)){
            currentClient = AgentApplication.currentOrder.tempClient;
        }

        if((AgentApplication.state == ApplicationState.CHECK_ADDING)||(AgentApplication.state == ApplicationState.CHECK_EDITING)){
            currentClient = AgentApplication.currentCheck.tempClient;
        }



        if(!currentClient.getName().equals(Client.NEW_CLIENT)){

            for(Map.Entry<String, String> i : currentClient.getParams().entrySet() ){
                AgentAppLogger.Text(i.getKey() + " # " + i.getValue());

                View item = inflater.inflate(R.layout.client_detail_parameter, clientDetailsLayout, false);

                TextView tvParamName = (TextView) item.findViewById(R.id.tvClientParamName);
                tvParamName.setText(i.getKey());
                TextView tvParamValue = (TextView) item.findViewById(R.id.tvClientParamValue);
                tvParamValue.setText(i.getValue());

                try {
                    DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
                    Date result =  df.parse(i.getValue());
                    Date now = new Date();
                    if (result.before(now)){
                        tvParamValue.setTextColor(Color.parseColor("#ffad07"));
                    }
                } catch (ParseException pe) {

                }
                item.getLayoutParams().width = LinearLayout.LayoutParams.MATCH_PARENT;


                clientDetailsLayout.addView(item);
            }
        }else{
            View item = inflater.inflate(R.layout.client_detail_add_new, clientDetailsLayout, false);

            EditText realName = (EditText)item.findViewById(R.id.editTextRealNameNewClient);
            realName.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(s.length() != 0){
                        commit.setEnabled(true);
                    }else {
                        commit.setEnabled(false);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                    currentClient.setRealName(s.toString());
                    AgentAppLogger.Text(currentClient.getRealName());
                }
            });

            commit.setEnabled(false);
            clientDetailsLayout.addView(item);
        }
    }

    public void onClickRollbackClient(View v) {
        this.finish();
    }

    public void onClickCommitClient(View v) {

        if((AgentApplication.state == ApplicationState.ORDER_ADDING)||(AgentApplication.state == ApplicationState.ORDER_EDITING)){
            AgentApplication.currentOrder.CommitTempClient();
            Intent intent = new Intent(this, OrderActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

        if((AgentApplication.state == ApplicationState.CHECK_ADDING)||(AgentApplication.state == ApplicationState.CHECK_EDITING)){
            AgentApplication.currentCheck.CommitTempClient();
            Intent intent = new Intent(this, CheckActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }


    }
}