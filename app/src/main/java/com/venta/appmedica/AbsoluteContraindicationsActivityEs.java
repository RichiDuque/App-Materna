package com.venta.appmedica;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.venta.appmedica.utils.NetworkChangeListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AbsoluteContraindicationsActivityEs extends AppCompatActivity {

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    ImageButton btnRegresar;
    Button btnSiguiente;
    CheckBox checkAbsolute1, checkAbsolute2, checkAbsolute3, checkAbsolute4, checkAbsolute5, checkAbsolute6, checkAbsolute7,
            checkAbsolute8, checkAbsolute9, checkAbsolute10, checkAbsolute11, checkAbsolute12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absolute_contraindications_es);

        SharedPreferences preferencia = getSharedPreferences("Datos",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferencia.edit();

        btnSiguiente = findViewById(R.id.btnAbsoluteSiguiente);
        btnRegresar = findViewById(R.id.btnAbsoluteRegresar);
        checkAbsolute1 = findViewById(R.id.checkBoxAbsolute1);
        checkAbsolute2 = findViewById(R.id.checkBoxAbsolute2);
        checkAbsolute3 = findViewById(R.id.checkBoxAbsolute3);
        checkAbsolute4 = findViewById(R.id.checkBoxAbsolute4);
        checkAbsolute5 = findViewById(R.id.checkBoxAbsolute5);
        checkAbsolute6 = findViewById(R.id.checkBoxAbsolute6);
        checkAbsolute7 = findViewById(R.id.checkBoxAbsolute7);
        checkAbsolute8 = findViewById(R.id.checkBoxAbsolute8);
        checkAbsolute9 = findViewById(R.id.checkBoxAbsolute9);
        checkAbsolute10 = findViewById(R.id.checkBoxAbsolute10);
        checkAbsolute11 = findViewById(R.id.checkBoxAbsolute11);
        checkAbsolute12 = findViewById(R.id.checkBoxAbsolute12);

        btnRegresar.setOnClickListener(view -> {
            Intent intent = new Intent(this, IntroduccionActivityEs.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        });

        btnSiguiente.setOnClickListener(view -> {

            if(checkAbsolute1.isChecked()){editor.putString("Profesional7","Si");}
            if(checkAbsolute2.isChecked()){editor.putString("Profesional8","Si");}
            if(checkAbsolute3.isChecked()){editor.putString("Profesional9","Si");}
            if(checkAbsolute4.isChecked()){editor.putString("Profesional10","Si");}
            if(checkAbsolute5.isChecked()){editor.putString("Profesional11","Si");}
            if(checkAbsolute6.isChecked()){editor.putString("Profesional12","Si");}
            if(checkAbsolute7.isChecked()){editor.putString("Profesional13","Si");}
            if(checkAbsolute8.isChecked()){editor.putString("Profesional14","Si");}
            if(checkAbsolute9.isChecked()){editor.putString("Profesional15","Si");}
            if(checkAbsolute10.isChecked()){editor.putString("Profesional16","Si");}
            if(checkAbsolute11.isChecked()){editor.putString("Profesional17","Si");}
            if(checkAbsolute12.isChecked()){editor.putString("Profesional18","Si");}
            editor.apply();
                Intent intent = new Intent(this, RelativeContraindicationsActivityEs.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

        });
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