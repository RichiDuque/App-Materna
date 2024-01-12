package com.venta.appmedica;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.venta.appmedica.utils.NetworkChangeListener;

public class MainActivity extends AppCompatActivity {

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    Button btnSiguiente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        setTheme(com.google.android.material.R.style.Theme_Material3_Light_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(checkPermission()) {
            //Toast.makeText(this, "Permiso Aceptado", Toast.LENGTH_LONG).show();
        } else {
            requestPermissions();
        }

        SharedPreferences preferencia = getSharedPreferences("Datos",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferencia.edit();
        editor.putString("Nombre","");
        editor.putString("Fecha","");
        editor.putString("NumeroDocumento","");
        editor.putString("Ocupacion","");
        editor.putInt("Edad",0);
        editor.putInt("TipoDocumento",0);
        editor.putInt("Escolaridad",0);
        editor.putInt("Semanas",0);
        editor.putString("Email","");
        editor.putString("EmailTr","");
        editor.putString("PreguntaA","");
        editor.putString("PreguntaB","");
        editor.putString("PreguntaC","");
        editor.putString("PreguntaD","");
        editor.putString("PreguntaE","");
        editor.putString("PreguntaF","");
        editor.putString("PreguntaG","");
        editor.putString("PreguntaH","");
        editor.putString("PreguntaI","");
        editor.putString("PreguntaJ","");
        editor.putString("PreguntaK","");
        editor.putString("PreguntaL","");
        editor.putString("PreguntaM","");
        editor.putString("PreguntaO","");
        editor.putString("PreguntaCard","");
        editor.putString("Otra_Condicion","N/A");
        editor.putString("Frecuencia","");
        editor.putString("Intensidad","");
        editor.putString("Duracion","");
        editor.putString("Frecuencia2","");
        editor.putString("Intensidad2","");
        editor.putString("Duracion2","");
        editor.putString("Frecuencia3","");
        editor.putString("Intensidad3","");
        editor.putString("Duracion3","");
        editor.putString("Otra_razon","");
        editor.putString("Actividad","");
        editor.putString("ProfesionalQualified","N/A");
        editor.putString("Profesional1","N/A");
        editor.putString("Profesional2","N/A");
        editor.putString("Profesional5","N/A");
        editor.putString("Profesional6","N/A");
        editor.putString("ProfesionalComentario","N/A");
        editor.putString("Profesional7","N/A");
        editor.putString("Profesional8","N/A");
        editor.putString("Profesional9","N/A");
        editor.putString("Profesional10","N/A");
        editor.putString("Profesional11","N/A");
        editor.putString("Profesional12","N/A");
        editor.putString("Profesional13","N/A");
        editor.putString("Profesional14","N/A");
        editor.putString("Profesional15","N/A");
        editor.putString("Profesional16","N/A");
        editor.putString("Profesional17","N/A");
        editor.putString("Profesional18","N/A");
        editor.putString("Profesional19","N/A");
        editor.putString("Profesional20","N/A");
        editor.putString("Profesional21","N/A");
        editor.putString("Profesional22","N/A");
        editor.putString("Profesional23","N/A");
        editor.putString("Profesional24","N/A");
        editor.putString("Profesional25","N/A");
        editor.putString("Profesional26","N/A");
        editor.putString("Profesional27","N/A");
        editor.putString("ProfesionalAvoiding","N/A");
        editor.putString("ProfesionalIncluding","N/A");
        editor.putString("Provedor","");
        editor.apply();
        btnSiguiente = findViewById(R.id.btnSiguiente);

        btnSiguiente.setOnClickListener(view -> {
            //createFolder("Pruebas");
            Intent intent = new Intent(this, DatosActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

    }

    private boolean checkPermission() {
        int permission1 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, 200);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 200) {
            if(grantResults.length > 0) {
                boolean writeStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean readStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if(writeStorage && readStorage) {
                    Toast.makeText(this, "Permiso concedido", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Permiso denegado", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
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