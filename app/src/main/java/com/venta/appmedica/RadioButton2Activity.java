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
import android.widget.RadioGroup;
import android.widget.Toast;

import com.venta.appmedica.utils.NetworkChangeListener;

public class RadioButton2Activity extends AppCompatActivity {

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    ImageButton btnRegresar;
    Button btnSiguiente;
    RadioGroup buttonH, buttonI, buttonJ, buttonK, buttonL, buttonM;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radio_button2);
        SharedPreferences preferencia = getSharedPreferences("Datos",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferencia.edit();

        btnSiguiente = findViewById(R.id.btnRadio2Siguiente);
        btnRegresar = findViewById(R.id.btn2Regresar);

        buttonH = findViewById(R.id.grupoH);
        buttonI = findViewById(R.id.grupoI);
        buttonJ = findViewById(R.id.grupoJ);
        buttonK = findViewById(R.id.grupoK);
        buttonL = findViewById(R.id.grupoL);
        buttonM = findViewById(R.id.grupoM);

        btnRegresar.setOnClickListener(view -> {
            Intent intent = new Intent(this, RadioButtonActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        });

        btnSiguiente.setOnClickListener(view -> {
            if(Validacion()){
                if(buttonH.indexOfChild(buttonH.findViewById(buttonH.getCheckedRadioButtonId()))==0){
                    editor.putString("PreguntaH","Sí");
                    editor.apply();
                }else{
                    editor.putString("PreguntaH","No");
                    editor.apply();
                }
                if(buttonI.indexOfChild(buttonI.findViewById(buttonI.getCheckedRadioButtonId()))==0){
                    editor.putString("PreguntaI","Sí");
                    editor.apply();
                }else{
                    editor.putString("PreguntaI","No");
                    editor.apply();
                }
                if(buttonJ.indexOfChild(buttonJ.findViewById(buttonJ.getCheckedRadioButtonId()))==0){
                    editor.putString("PreguntaJ","Sí");
                    editor.apply();
                }else{
                    editor.putString("PreguntaJ","No");
                    editor.apply();
                }
                if(buttonK.indexOfChild(buttonK.findViewById(buttonK.getCheckedRadioButtonId()))==0){
                    editor.putString("PreguntaK","Sí");
                    editor.apply();
                }else{
                    editor.putString("PreguntaK","No");
                    editor.apply();
                }
                if(buttonL.indexOfChild(buttonL.findViewById(buttonL.getCheckedRadioButtonId()))==0){
                    editor.putString("PreguntaL","Sí");
                    editor.apply();
                }else{
                    editor.putString("PreguntaL","No");
                    editor.apply();
                }
                if(buttonM.indexOfChild(buttonM.findViewById(buttonM.getCheckedRadioButtonId()))==0){
                    editor.putString("PreguntaM","Sí");
                    editor.apply();
                }else{
                    editor.putString("PreguntaM","No");
                    editor.apply();
                }

                Intent intent = new Intent(this, RadioButton3Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }

        });

    }
    public boolean Validacion(){
        if(buttonH.getCheckedRadioButtonId()!=-1&&buttonI.getCheckedRadioButtonId()!=-1&&
                buttonJ.getCheckedRadioButtonId()!=-1&&buttonK.getCheckedRadioButtonId()!=-1&&
                buttonL.getCheckedRadioButtonId()!=-1&&buttonM.getCheckedRadioButtonId()!=-1){
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