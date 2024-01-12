package com.venta.appmedica;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.venta.appmedica.utils.DatePickerFragment;
import com.venta.appmedica.utils.NetworkChangeListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DatosActivity extends AppCompatActivity {

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    Spinner spinnerEdad, spinnerDocumento, spinnerEscolaridad, spinnerEmbarazo;
    EditText nombre, fecha, nDocumento, ocupacion, email;
    Button btnSiguiente, btnInfo;
    CheckBox TrabajadorSalud;
    RadioGroup idioma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos);

        SharedPreferences preferencia = getSharedPreferences("Datos",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferencia.edit();
        editor.putString("TrabajadorSalud","no");
        editor.apply();
        spinnerEdad = findViewById(R.id.spinnerEdad);
        spinnerDocumento = findViewById(R.id.spinner_cc);
        spinnerEscolaridad = findViewById(R.id.spinnerEscolar);
        spinnerEmbarazo = findViewById(R.id.spinnerEmbarazo);
        nombre = findViewById(R.id.editNombre);
        fecha = findViewById(R.id.editNacimiento);
        nDocumento = findViewById(R.id.editDocumento);
        ocupacion = findViewById(R.id.editOcupacion);
        email = findViewById(R.id.editEmail);
        idioma = findViewById(R.id.groupIdioma);
        btnSiguiente = findViewById(R.id.btnDatosSiguiente);
        btnInfo = findViewById(R.id.imageInfo);
        TrabajadorSalud = findViewById(R.id.checkBoxSalud);

        btnInfo.setOnClickListener(view -> {
            Uri uri = Uri.parse("https://drive.google.com/file/d/1ucSIKmq0gXTCfRdRPKmNxkVAydml4iN8/view?usp=share_link");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });

        if(!preferencia.getString("Nombre","").equals("")){
            nombre.setText(preferencia.getString("Nombre",""));
        }
        if(!preferencia.getString("Fecha","").equals("")){
            fecha.setText(preferencia.getString("Fecha",""));
        }
        if(!preferencia.getString("NumeroDocumento","").equals("")){
            nDocumento.setText(preferencia.getString("NumeroDocumento",""));
        }
        if(!preferencia.getString("Ocupacion","").equals("")){
            ocupacion.setText(preferencia.getString("Ocupacion",""));
        }
        if(!preferencia.getString("Email","").equals("")){
            email.setText(preferencia.getString("Email",""));
        }
        if(preferencia.getInt("Edad",0)!=0){
            spinnerEdad.setSelection(preferencia.getInt("Edad",0));
        }
        if(preferencia.getInt("TipoDocumento",0)!=0){
            spinnerDocumento.setSelection(preferencia.getInt("TipoDocumento",0));
        }
        if(preferencia.getInt("Escolaridad",0)!=0){
            spinnerEscolaridad.setSelection(preferencia.getInt("Escolaridad",0));
        }
        if(preferencia.getInt("Semanas",0)!=0){
            spinnerEmbarazo.setSelection(preferencia.getInt("Semanas",0));
        }

        TrabajadorSalud.setOnClickListener(view -> {
            if(TrabajadorSalud.isChecked()){
                editor.putString("TrabajadorSalud","si");
                editor.apply();
            }else{
                editor.putString("TrabajadorSalud","no");
                editor.apply();
            }
        });

        fecha.setOnClickListener(view -> {
            showDatePickerDialog();
        });

        btnSiguiente.setOnClickListener(view -> {
            if(Validacion()){
                    editor.putString("Nombre", nombre.getText().toString());
                    editor.putString("Fecha", fecha.getText().toString());
                    editor.putString("NumeroDocumento", nDocumento.getText().toString());
                    editor.putString("Ocupacion", ocupacion.getText().toString());
                    editor.putInt("Edad", spinnerEdad.getSelectedItemPosition());
                    editor.putInt("TipoDocumento", spinnerDocumento.getSelectedItemPosition());
                    editor.putInt("Escolaridad", spinnerEscolaridad.getSelectedItemPosition());
                    editor.putInt("Semanas", spinnerEmbarazo.getSelectedItemPosition());
                    editor.putString("Email", email.getText().toString());
                    editor.apply();
                if(idioma.indexOfChild(idioma.findViewById(idioma.getCheckedRadioButtonId()))==0) {
                    Intent intent = new Intent(this, RadioButtonActivityEs.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(this, RadioButtonActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }

        });
    }

    public boolean Validacion(){
        Pattern pattern = Pattern
                .compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher mather = pattern.matcher(email.getText().toString());
        if(!nombre.getText().toString().equals("")&&!fecha.getText().toString().equals("")&&
                !nDocumento.getText().toString().equals("")&&!ocupacion.getText().toString().equals("")&&
                !spinnerEdad.getSelectedItem().toString().equals("Edad")&&!spinnerDocumento.getSelectedItem().toString().equals("Tipo documento")&&
                !spinnerEscolaridad.getSelectedItem().toString().equals("Escolaridad")&&!email.getText().toString().equals("")&&idioma.getCheckedRadioButtonId()!=-1){
            if(mather.find() == true) {
                return true;
            }else{
                Toast.makeText(this, "Correo electronico invalido",Toast.LENGTH_SHORT).show();
                return false;
            }
        }else{
            Toast.makeText(this, "Debe rellenar todos los campos",Toast.LENGTH_SHORT).show();
            return false;
        }

    }
    private void showDatePickerDialog() {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 porque enero es 0
                final String selectedDate = day + " / " + (month+1) + " / " + year;
                fecha.setText(selectedDate);
            }
        });

        newFragment.show(this.getSupportFragmentManager(), "datePicker");
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