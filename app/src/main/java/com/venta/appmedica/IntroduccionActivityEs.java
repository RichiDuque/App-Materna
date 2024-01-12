package com.venta.appmedica;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.venta.appmedica.utils.NetworkChangeListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IntroduccionActivityEs extends AppCompatActivity {

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    ImageButton btnRegresar;
    Button btnSiguiente;
    EditText email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduccion_es);

        btnSiguiente = findViewById(R.id.btnIntroSiguienteEs);
        btnRegresar = findViewById(R.id.btnIntroRegresarEs);
        email = findViewById(R.id.editEmailTrEs);
        SharedPreferences preferencia = getSharedPreferences("Datos",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferencia.edit();

        btnRegresar.setOnClickListener(view -> {
            if(preferencia.getString("TodasPreguntas","no").equals("si")) {
                Intent intent = new Intent(this, Recomendacion5ActivityEs.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
            }else{
                Intent intent = new Intent(this, DeclarationActivityEs.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
            }
        });

        btnSiguiente.setOnClickListener(view -> {

            if(Validacion()) {
                editor.putString("EmailTr", email.getText().toString());
                editor.apply();
                Intent intent = new Intent(this, AbsoluteContraindicationsActivityEs.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    public boolean Validacion(){
        Pattern pattern = Pattern
                .compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher mather = pattern.matcher(email.getText().toString());
        if(mather.find() == true) {
            return true;
        }else{
            Toast.makeText(this, "Correo electrónico inválido",Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    @Override
    protected void onStart() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener,filter);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }
}