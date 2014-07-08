package ua.dn.effect.AgentApp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;

import android.widget.*;

import ua.dn.effect.AgentApp.data.config.AgentAppConfig;
import ua.dn.effect.AgentApp.util.AgentAppLogger;


public class FtpSettingsActivity extends Activity {
    EditText editTextHost;
    EditText editTextLogin;
    EditText editTextPassword;
    //Button buttonTestConnection;
    private Button buttonSaveSettings;
    private Button buttonCancelSettings;
    AgentAppConfig config;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ftpsettings);
        config = new AgentAppConfig(this);
        Init();
    }

    private void Init() {
        editTextHost = (EditText)findViewById(R.id.editTextHost);
        editTextLogin = (EditText)findViewById(R.id.editTextLogin);
        editTextPassword = (EditText)findViewById(R.id.editTextPassword);
        buttonSaveSettings = (Button)findViewById(R.id.buttonSaveSettings);
        buttonCancelSettings = (Button)findViewById(R.id.buttonCancelSettings);

        editTextHost.setText(config.getServer());
        editTextLogin.setText(config.getLogin());
        editTextPassword.setText(config.getPassword());
        // адаптер
        String[] data = {"15 минут", "30 минут", "60 минут"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinner = (Spinner) findViewById(R.id.spinnerUpdate);
        spinner.setAdapter(adapter);
        // заголовок
        spinner.setPrompt("Частота обновления");
        // выделяем элемент
        spinner.setSelection(0);
        // устанавливаем обработчик нажатия
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    public void onClickCancelSettings(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    public void onClickSaveSettings(View v) {


        config.setLogin(editTextLogin.getText().toString());
        config.setPassword(editTextPassword.getText().toString());
        config.setServer(editTextHost.getText().toString());
        AgentApplication.ftpConnection.Init(this);
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
