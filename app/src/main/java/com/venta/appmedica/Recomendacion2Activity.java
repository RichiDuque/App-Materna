package com.venta.appmedica;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import com.venta.appmedica.utils.NetworkChangeListener;

public class Recomendacion2Activity extends AppCompatActivity {

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    ImageButton btnRegresar;
    Button btnSiguiente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recomendacion2);

        btnSiguiente = findViewById(R.id.btnRecomendacion2Siguiente);
        btnRegresar = findViewById(R.id.btnRecomendacion2Regresar);

        btnRegresar.setOnClickListener(view -> {
            Intent intent = new Intent(this, Recomendacion1_1MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        });

        btnSiguiente.setOnClickListener(view -> {
            Intent intent = new Intent(this, Recomendacion3Activity.class);
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