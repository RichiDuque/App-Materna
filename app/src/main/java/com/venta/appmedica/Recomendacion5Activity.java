package com.venta.appmedica;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.venta.appmedica.utils.NetworkChangeListener;

public class Recomendacion5Activity extends AppCompatActivity {

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    ImageButton btnRegresar;
    Button btnSiguiente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recomendacion5);

        btnSiguiente = findViewById(R.id.btnRecomendacion5Siguiente);
        btnRegresar = findViewById(R.id.btnRecomendacion5Regresar);
        SharedPreferences preferencia = getSharedPreferences("Datos",MODE_PRIVATE);

        if(preferencia.getString("Provedor", "no").equals("si")) {
            btnSiguiente.setText("Finish");
        }else{
            if (preferencia.getString("TrabajadorSalud", "no").equals("si")) {
                btnSiguiente.setText("Next");
            } else {
                btnSiguiente.setText("Finish");
            }
        }

        btnRegresar.setOnClickListener(view -> {
            Intent intent = new Intent(this, Recomendacion4Activity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        });

        btnSiguiente.setOnClickListener(view -> {
            if(preferencia.getString("Provedor", "no").equals("si")) {
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Toast.makeText(this, "SURVEY SENT, THANK YOU FOR YOUR TIME", Toast.LENGTH_LONG).show();
                startActivity(intent);
            }else{
                if (preferencia.getString("TrabajadorSalud", "no").equals("si")) {
                    Intent intent = new Intent(this, IntroduccionActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    Toast.makeText(this, "SURVEY SENT, THANK YOU FOR YOUR TIME", Toast.LENGTH_LONG).show();
                    startActivity(intent);
                }
            }

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