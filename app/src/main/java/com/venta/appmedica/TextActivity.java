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

public class TextActivity extends AppCompatActivity {

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    ImageButton btnRegresarT;
    Button btnSiguienteT;
    EditText editPregunta1, editPregunta2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);

        SharedPreferences preferencia = getSharedPreferences("Datos",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferencia.edit();
        btnSiguienteT = findViewById(R.id.btnTextSiguiente);
        btnRegresarT = findViewById(R.id.btnTextRegresar);
        editPregunta1 = findViewById(R.id.editPregunta1);
        editPregunta2 = findViewById(R.id.editPregunta2);
        btnRegresarT.setOnClickListener(view -> {
            Intent intent = new Intent(this, RadioButton3Activity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        });

        btnSiguienteT.setOnClickListener(view -> {
            if(Validacion()){
                editor.putString("Otra_razon", editPregunta1.getText().toString());
                editor.putString("Actividad", editPregunta2.getText().toString());
                editor.apply();
                Intent intent = new Intent(this, IntensidadActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }

        });
    }

    public boolean Validacion(){
        if(!editPregunta1.getText().toString().isEmpty()&&!editPregunta2.getText().toString().isEmpty()){
            return true;
        }else{
            Toast.makeText(this, "Debe responder todas las preguntas", Toast.LENGTH_LONG).show();
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