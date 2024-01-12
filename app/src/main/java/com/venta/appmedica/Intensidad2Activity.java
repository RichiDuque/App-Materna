package com.venta.appmedica;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.venta.appmedica.utils.NetworkChangeListener;

public class Intensidad2Activity extends AppCompatActivity {

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    ImageButton btnRegresar;
    Button btnSiguiente;
    RadioGroup buttonFrecuencia, buttonIntensidad, buttonDuracion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intensidad2);

        SharedPreferences preferencia = getSharedPreferences("Datos",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferencia.edit();

        btnSiguiente = findViewById(R.id.btnIntensidadSiguiente_2);
        btnRegresar = findViewById(R.id.btnIntensidadRegresar_2);
        buttonFrecuencia = findViewById(R.id.radioGroup1_2);
        buttonIntensidad = findViewById(R.id.radioGroup2_2);
        buttonDuracion = findViewById(R.id.radioGroup3_2);

        btnRegresar.setOnClickListener(view -> {
            Intent intent = new Intent(this, IntensidadActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        });

        btnSiguiente.setOnClickListener(view -> {
            if(Validacion()){
                View radioButtonF = buttonFrecuencia.findViewById(buttonFrecuencia.getCheckedRadioButtonId());
                int indice = buttonFrecuencia.indexOfChild(radioButtonF);
                RadioButton rb = (RadioButton)  buttonFrecuencia.getChildAt(indice);
                System.out.println(rb.getText().toString());
                editor.putString("Frecuencia2",rb.getText().toString());
                editor.apply();

                View radioButtonI = buttonIntensidad.findViewById(buttonIntensidad.getCheckedRadioButtonId());
                int indiceI = buttonIntensidad.indexOfChild(radioButtonI);
                RadioButton rbI = (RadioButton)  buttonIntensidad.getChildAt(indiceI);
                editor.putString("Intensidad2",rbI.getText().toString());
                editor.apply();

                View radioButtonD = buttonDuracion.findViewById(buttonDuracion.getCheckedRadioButtonId());
                int indiceD = buttonDuracion.indexOfChild(radioButtonD);
                RadioButton rbD = (RadioButton)  buttonDuracion.getChildAt(indiceD);
                editor.putString("Duracion2",rbD.getText().toString());
                editor.apply();

                Intent intent = new Intent(this, Intensidad3Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }
    public boolean Validacion(){
        if(buttonFrecuencia.getCheckedRadioButtonId()!=-1&&buttonIntensidad.getCheckedRadioButtonId()!=-1&&buttonDuracion.getCheckedRadioButtonId()!=-1){
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