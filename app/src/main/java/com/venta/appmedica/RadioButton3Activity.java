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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.venta.appmedica.utils.NetworkChangeListener;

public class RadioButton3Activity extends AppCompatActivity {

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    ImageButton btnRegresar;
    Button btnSiguiente;
    RadioButton btnCardYes, btnCardNo;
    EditText textOther;
    RadioGroup buttonN, buttonO, buttonCard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radio_button3);
        SharedPreferences preferencia = getSharedPreferences("Datos",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferencia.edit();

        btnSiguiente = findViewById(R.id.btnRadio3Siguiente);
        btnRegresar = findViewById(R.id.btn3Regresar);
        textOther = findViewById(R.id.editRadio3);
        btnCardYes = findViewById(R.id.rbtnYes3);
        btnCardNo = findViewById(R.id.rbtnNo3);

        buttonN = findViewById(R.id.grupoN);
        buttonO = findViewById(R.id.grupoO);
        buttonCard = findViewById(R.id.grupoCard);


        btnCardYes.setOnClickListener(view -> {
            textOther.setEnabled(true);
        });

        btnCardNo.setOnClickListener(view -> {
            textOther.setEnabled(false);
        });

        btnRegresar.setOnClickListener(view -> {
            Intent intent = new Intent(this, RadioButton2Activity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        });

        btnSiguiente.setOnClickListener(view -> {

            if(Validacion()){

                if(buttonN.indexOfChild(buttonN.findViewById(buttonN.getCheckedRadioButtonId()))==0){
                    editor.putString("PreguntaN","Sí");
                    editor.apply();
                }else{
                    editor.putString("PreguntaN","No");
                    editor.apply();
                }
                if(buttonO.indexOfChild(buttonO.findViewById(buttonO.getCheckedRadioButtonId()))==0){
                    editor.putString("PreguntaO","Sí");
                    editor.apply();
                }else{
                    editor.putString("PreguntaO","No");
                    editor.apply();
                }
                if(buttonCard.indexOfChild(buttonCard.findViewById(buttonCard.getCheckedRadioButtonId()))==0){
                    editor.putString("PreguntaCard","Sí");
                    editor.putString("Otra_Condicion",textOther.getText().toString());
                    editor.apply();
                }else{
                    editor.putString("PreguntaCard","No");
                    editor.apply();
                }

                Intent intent = new Intent(this, TextActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

    }
    public boolean Validacion(){
        if(buttonN.getCheckedRadioButtonId()==-1||buttonO.getCheckedRadioButtonId()==-1||buttonCard.getCheckedRadioButtonId()==-1){
            Toast.makeText(this, "Debe responder todas las preguntas", Toast.LENGTH_LONG).show();
            return false;
        }else if(buttonCard.indexOfChild(buttonCard.findViewById(buttonCard.getCheckedRadioButtonId()))==0){
            if(textOther.getText().toString().isEmpty()) {
                Toast.makeText(this, "Debe colocar la condición", Toast.LENGTH_LONG).show();
                return false;
            }
        }
        return true;
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