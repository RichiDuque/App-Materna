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
import android.widget.RadioGroup;
import android.widget.Toast;

import com.venta.appmedica.utils.NetworkChangeListener;

public class RadioButtonActivityEs extends AppCompatActivity {

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    ImageButton btnRegresar;
    Button btnSiguiente;
    RadioGroup buttonA, buttonB, buttonC, buttonD, buttonE, buttonF, buttonG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radio_button_es);

        SharedPreferences preferencia = getSharedPreferences("Datos",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferencia.edit();

        btnSiguiente = findViewById(R.id.btnRadioSiguiente);
        btnRegresar = findViewById(R.id.btnRegresar);
        buttonA = findViewById(R.id.grupoA);
        buttonB = findViewById(R.id.grupoB);
        buttonC = findViewById(R.id.grupoC);
        buttonD = findViewById(R.id.grupoD);
        buttonE = findViewById(R.id.grupoE);
        buttonF = findViewById(R.id.grupoF);
        buttonG = findViewById(R.id.grupoG);

        btnRegresar.setOnClickListener(view -> {
            Intent intent = new Intent(this, DatosActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        });

        btnSiguiente.setOnClickListener(view -> {

            if(Validacion()){
                View radioButton = buttonA.findViewById(buttonA.getCheckedRadioButtonId());
                System.out.println(buttonA.indexOfChild(buttonA.findViewById(buttonA.getCheckedRadioButtonId())));
                if(buttonA.indexOfChild(buttonA.findViewById(buttonA.getCheckedRadioButtonId()))==0){
                    editor.putString("PreguntaA","Sí");
                    editor.apply();
                }else{
                    editor.putString("PreguntaA","No");
                    editor.apply();
                }
                if(buttonB.indexOfChild(buttonB.findViewById(buttonB.getCheckedRadioButtonId()))==0){
                    editor.putString("PreguntaB","Sí");
                    editor.apply();
                }else{
                    editor.putString("PreguntaB","No");
                    editor.apply();
                }
                if(buttonC.indexOfChild(buttonC.findViewById(buttonC.getCheckedRadioButtonId()))==0){
                    editor.putString("PreguntaC","Sí");
                    editor.apply();
                }else{
                    editor.putString("PreguntaC","No");
                    editor.apply();
                }
                if(buttonD.indexOfChild(buttonD.findViewById(buttonD.getCheckedRadioButtonId()))==0){
                    editor.putString("PreguntaD","Sí");
                    editor.apply();
                }else{
                    editor.putString("PreguntaD","No");
                    editor.apply();
                }
                if(buttonE.indexOfChild(buttonE.findViewById(buttonE.getCheckedRadioButtonId()))==0){
                    editor.putString("PreguntaE","Sí");
                    editor.apply();
                }else{
                    editor.putString("PreguntaE","No");
                    editor.apply();
                }
                if(buttonF.indexOfChild(buttonF.findViewById(buttonF.getCheckedRadioButtonId()))==0){
                    editor.putString("PreguntaF","Sí");
                    editor.apply();
                }else{
                    editor.putString("PreguntaF","No");
                    editor.apply();
                }
                if(buttonG.indexOfChild(buttonG.findViewById(buttonG.getCheckedRadioButtonId()))==0){
                    editor.putString("PreguntaG","Sí");
                    editor.apply();
                }else{
                    editor.putString("PreguntaG","No");
                    editor.apply();
                }

                Intent intent = new Intent(this, RadioButton2ActivityEs.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }

        });
    }
    public boolean Validacion(){
        if(buttonA.getCheckedRadioButtonId()!=-1&&buttonB.getCheckedRadioButtonId()!=-1&&
                buttonC.getCheckedRadioButtonId()!=-1&&buttonD.getCheckedRadioButtonId()!=-1&&
                buttonE.getCheckedRadioButtonId()!=-1&&buttonF.getCheckedRadioButtonId()!=-1&&buttonG.getCheckedRadioButtonId()!=-1){
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